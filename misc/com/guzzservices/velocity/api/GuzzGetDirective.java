/**
 * 
 */
package com.guzzservices.velocity.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.guzz.orm.Business;
import org.guzz.orm.se.SearchExpression;
import org.guzz.taglib.db.GhostGetTag;
import org.guzz.transaction.ReadonlyTranSession;
import org.guzz.util.StringUtil;

/**
 * 
 * See {@link GhostGetTag}.
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class GuzzGetDirective extends SummonDirective {

	protected Object summonGhosts(Business business, Object tableCondition, List conditions, Map params) throws IOException {
		String orderBy = (String) params.get("orderBy") ;
		
		SearchExpression se = SearchExpression.forBusiness(business.getName()) ;
		se.setTableCondition(tableCondition) ;
		se.and(conditions) ;
		if(StringUtil.notEmpty(orderBy)){
			se.setOrderBy(orderBy) ;
		}
		
		ReadonlyTranSession tran = guzzContext.getTransactionManager().openDelayReadTran() ;
		
		try{
			return tran.findObject(se) ;
		}finally{
			tran.close() ;
		}
	}

	public String getName() {
		return "guzzGet" ;
	}
	
}
