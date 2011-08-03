/*
 * IDHtmlTreeNode.java, created on 2005-11-23 by chen.liang@trs.com.cn
 */

package com.guzzservices.util.tree;

import java.util.List;

public interface IDHtmlTreeNode
{
    public String getName() ;
    
    public String getLink() ;
    
    public String getTarget() ;
    
    public List   getSons() ;
}
