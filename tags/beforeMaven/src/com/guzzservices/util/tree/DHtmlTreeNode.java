/*
 * DHtmlTreeNode.java, created on 2005-11-23 by chen.liang@trs.com.cn
 */

package com.guzzservices.util.tree;

import java.util.LinkedList;
import java.util.List;

public class DHtmlTreeNode implements IDHtmlTreeNode
{
    private String name;
    
    private String link;

    private String target;
    
    private List sons;
    
    /**
     * @return link to open when mouse click on the name.
     */
    public String getLink()
    {
        return this.link;
    }
    
    /**
     * @param link the link to set
     */
    public void setLink(String link)
    {
        this.link = link;
    }
    
    /**
     * @return the name to display in a DHTML tree.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the target to display target.
     */
    public String getTarget()
    {
        return this.target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(String target)
    {
        this.target = target;
    }

    /**
     * @return the children of this node
     */
    public List getSons()
    {
        return this.sons;
    }
    
    /**
     * @param p_son the child to add
     */
    public void addSon(IDHtmlTreeNode p_son)
    {
        if (this.sons == null)
        {
            this.sons = new LinkedList();
        }
        
        this.sons.add(p_son);
    }

    /**
     * @param p_sons The children to set.
     */
    public void setSons(List p_sons)
    {
        this.sons = p_sons;
    }
}
