/**
 * 
 */
package com.guzzservices.velocity.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.guzz.orm.Business;
import org.guzz.orm.se.SearchExpression;
import org.guzz.taglib.db.GhostCountTag;
import org.guzz.transaction.ReadonlyTranSession;

/**
 * 
 * See {@link GhostCountTag}.
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class GuzzCountDirective extends SummonDirective {

	protected Object summonGhosts(Business business, Object tableCondition, List conditions, Map params) throws IOException {
		String selectPhrase = (String) params.get("selectPhrase") ;
		
		SearchExpression se = SearchExpression.forBusiness(business.getName()) ;
		se.setTableCondition(tableCondition) ;
		
		if(selectPhrase != null){
			se.setCountSelectPhrase(selectPhrase) ;
		}
		
		se.and(conditions) ;
		
		ReadonlyTranSession tran = guzzContext.getTransactionManager().openDelayReadTran() ;
		
		try{
			return Long.valueOf(tran.count(se)) ;
		}finally{
			tran.close() ;
		}
	}

	public String getName() {
		return "guzzCount" ;
	}
	
}
