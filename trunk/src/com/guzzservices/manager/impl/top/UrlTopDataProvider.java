/**
 * 
 */
package com.guzzservices.manager.impl.top;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.guzzservices.business.StatItem;
import com.guzzservices.business.TopRecord;
import com.guzzservices.manager.IStatItemManager;
import com.guzzservices.manager.ITopRecordManager;
import com.guzzservices.rpc.util.JsonUtil;
import com.guzzservices.util.HttpClientUtils;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class UrlTopDataProvider implements TopDataProvider {

	public List<TopRecord> readNewData(ITopRecordManager topRecordManager, IStatItemManager statItemManager, StatItem item) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>() ;
		params.put("authKey", item.getAuthKey()) ;
		params.put("statId", String.valueOf(item.getId())) ;
		params.put("time", String.valueOf(item.getStatBeforeMinutes())) ;
		params.put("pageSize", String.valueOf(item.getFetchSize())) ;
		params.put("programId", item.getProgramId()) ;
		
		String json = HttpClientUtils.get(item.getDataProviderUrl(), params, item.getEncoding()) ;
		
		if(json != null){
			json = json.trim() ;
		}
		
		LinkedList<Map> fromJson = JsonUtil.fromJson(json, LinkedList.class);
		List<TopRecord> records = new LinkedList<TopRecord>() ;
		
		for(Map m : fromJson){
			TopRecord r = new TopRecord() ;
			r.setExtra1(String.valueOf(m.get("extra1"))) ;
			r.setExtra2(String.valueOf( m.get("extra2"))) ;
			r.setExtra3(String.valueOf(m.get("extra3"))) ;
			r.setObjectCreatedTime(String.valueOf( m.get("objectCreatedTime"))) ;
			r.setObjectId(String.valueOf(m.get("objectId"))) ;
			r.setObjectTitle(String.valueOf( m.get("objectTitle"))) ;
			r.setObjectURL(String.valueOf( m.get("objectURL"))) ;
			r.setOpTimes(((Number) m.get("opTimes")).intValue()) ;
			
			records.add(r) ;
		}
		
		return records ;
	}

}
