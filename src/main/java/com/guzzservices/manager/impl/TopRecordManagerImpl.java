package com.guzzservices.manager.impl;

import java.util.Date;
import java.util.List;

import org.guzz.orm.se.SearchExpression;
import org.guzz.orm.se.Terms;
import org.guzz.orm.sql.CompiledSQL;
import org.guzz.transaction.WriteTranSession;

import com.guzzservices.business.BannedTopRecord;
import com.guzzservices.business.StatItem;
import com.guzzservices.business.TopRecord;
import com.guzzservices.manager.ITopRecordManager;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class TopRecordManagerImpl extends AbstractBaseManagerImpl<TopRecord> implements ITopRecordManager {
		
	public BannedTopRecord getBannedTopRecord(String objectId, int groupId){
		SearchExpression se = SearchExpression.forClass(BannedTopRecord.class) ;
		se.and(Terms.eq("objectId", objectId)) ;
		se.and(Terms.eq("groupId", groupId)) ; 
		
		return (BannedTopRecord) super.findObject(se) ;
	}
	
	public void hideRecord(TopRecord record){
		record.setBanned(true) ;
		BannedTopRecord btr = getBannedTopRecord(record.getObjectId(), record.getGroupId()) ;
		
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		
		try{
			if(btr == null){
				btr = new BannedTopRecord() ;
				btr.setCreatedTime(new Date()) ;
				btr.setGroupId(record.getGroupId()) ;
				btr.setLastHitTime(new Date()) ;
				btr.setObjectId(record.getObjectId()) ;
				btr.setObjectTitle(record.getObjectTitle()) ;
				btr.setObjectURL(record.getObjectURL()) ;
				
				write.insert(btr) ;
				write.update(record) ;
			}else{
				btr.setLastHitTime(new Date()) ;
				
				write.update(record) ;
				write.update(btr) ;
			}
			
			write.commit() ;
		}catch(RuntimeException e){
			write.rollback() ;
			
			throw e ;
		}finally{
			write.close() ;
		}
	}
	
	public void showRecord(TopRecord record){
		BannedTopRecord btr = getBannedTopRecord(record.getObjectId(), record.getGroupId()) ;
		
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		
		try{
			if(btr != null){
				write.delete(btr) ;
			}

			record.setBanned(false) ;
			write.update(record) ;
			
			write.commit() ;
		}catch(RuntimeException e){
			write.rollback() ;
			
			throw e ;
		}finally{
			write.close() ;
		}
	}

	public void sortRecords(StatItem item, boolean isAsending) {
		//按照opTimes提取到记录，然后写回去。
		SearchExpression se = SearchExpression.forClass(TopRecord.class, 1, 200) ;
		se.and(Terms.eq("statId", item.getId())) ;
		
		if(isAsending){
			se.setOrderBy("opTimes asc") ;
		}else{
			se.setOrderBy("opTimes desc") ;
		}
		
		List records = super.list(se) ;
		
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		int total = records.size() ;
		
		try{
			//一个个的读取，然后重新设置顺序。
			for(int i = 0 ; i < total ; i++){
				TopRecord record = (TopRecord) records.get(i) ;
				record.setObjectOrder(i + 1) ;
				
				write.update(record) ;
			}
			
			write.commit() ;
		}catch(RuntimeException e){
			write.rollback() ;
		}finally{
			write.close() ;
		}
	}
	
	public List<TopRecord> listCleanRecords(StatItem item, int maxSize){
		SearchExpression se = SearchExpression.forClass(TopRecord.class, 1, maxSize) ;
		se.and(Terms.eq("statId", item.getId())) ;
		se.and(Terms.eq("banned", false)) ;
		se.setOrderBy("objectOrder asc") ;
		
		return super.list(se) ;
	}
	
	public void cleanUpOldData(WriteTranSession write, StatItem item) {
		if(cleanUpOldDataCS == null){
			cleanUpOldDataCS = this.getTransactionManager().getCompiledSQLBuilder().buildCompiledSQL(TopRecord.class, 
					"delete from @@" + TopRecord.class.getName() + " where @statId = :statId") ;
			cleanUpOldDataCS.addParamPropMapping("statId", "statId") ;
		}
		
		write.executeUpdate(cleanUpOldDataCS.bind("statId", item.getId())) ;
	}
	
	private CompiledSQL cleanUpOldDataCS ;

}
