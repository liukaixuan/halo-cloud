/**
 * 
 */
package com.guzzservices.velocity.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.guzz.orm.Business;
import org.guzz.orm.se.SearchExpression;
import org.guzz.taglib.db.GhostListTag;
import org.guzz.transaction.ReadonlyTranSession;
import org.guzz.util.StringUtil;

/**
 * 
 * See {@link GhostListTag}.
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class GuzzListDirective extends SummonDirective {

	protected Object summonGhosts(Business business, Object tableCondition, List conditions, Map params) throws IOException {
		int skipCount = LangUtil.getIntParam(params, "skipCount", 0) ;
		int pageNo = LangUtil.getIntParam(params, "pageNo", 1) ;
		int pageSize = LangUtil.getIntParam(params, "pageSize", 20) ;
		String orderBy = (String) params.get("orderBy") ;
		
		SearchExpression se = SearchExpression.forBusiness(business.getName(), pageNo, pageSize) ;
		se.setTableCondition(tableCondition) ;
		se.setSkipCount(skipCount) ;
		se.and(conditions) ;
		if(StringUtil.notEmpty(orderBy)){
			se.setOrderBy(orderBy) ;
		}
		
		ReadonlyTranSession tran = guzzContext.getTransactionManager().openDelayReadTran() ;
		
		try{
			return tran.list(se) ;
		}finally{
			tran.close() ;
		}
	}

	public String getName() {
		return "guzzList" ;
	}
	
}
