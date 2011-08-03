/**
 * 
 */
package com.guzzservices.velocity.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.guzz.dao.PageFlip;
import org.guzz.orm.Business;
import org.guzz.orm.se.SearchExpression;
import org.guzz.taglib.db.GhostPageTag;
import org.guzz.transaction.ReadonlyTranSession;
import org.guzz.util.StringUtil;

/**
 * 
 * See {@link GhostPageTag}.
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class GuzzPageDirective extends SummonDirective {

	protected Object summonGhosts(Business business, Object tableCondition, List conditions, Map params) throws IOException {
		int skipCount = LangUtil.getIntParam(params, "skipCount", 0) ;
		int pageNo = LangUtil.getIntParam(params, "pageNo", 1) ;
		int pageSize = LangUtil.getIntParam(params, "pageSize", 20) ;
		String orderBy = (String) params.get("orderBy") ;
		
		int pageSpan = LangUtil.getIntParam(params, "pageSpan", 10) ;
		int pageBeforeSpan = LangUtil.getIntParam(params, "pageBeforeSpan", -1) ;
		int pageAfterSpan = LangUtil.getIntParam(params, "pageAfterSpan", -1) ;
		
		SearchExpression se = SearchExpression.forBusiness(business.getName(), pageNo, pageSize) ;
		se.setTableCondition(tableCondition) ;
		se.setSkipCount(skipCount) ;
		se.and(conditions) ;
		if(StringUtil.notEmpty(orderBy)){
			se.setOrderBy(orderBy) ;
		}
		
		ReadonlyTranSession tran = guzzContext.getTransactionManager().openDelayReadTran() ;
		
		PageFlip page ;
		try{
			page = tran.page(se) ;
		}finally{
			tran.close() ;
		}

		page.setPagesShow(pageSpan) ;
		
		if(pageBeforeSpan > 0){
			page.setPageBeforeSpan(pageBeforeSpan) ;
		}
		
		if(pageAfterSpan > 0){
			page.setPageAfterSpan(pageAfterSpan) ;
		}
		
		return page ;
	}

	public String getName() {
		return "guzzPage" ;
	}
	
}
