/*
 * DHtmlTree.java, created on 2005-11-23 by chen.liang@trs.com.cn
 */

package com.guzzservices.util.tree;

import java.util.LinkedList;
import java.util.List;

public class DHtmlTree implements IDHtmlTreeNode
{
    private List   sons;
    private String name;
    private String link;
    private String target;
    private String items;
    private String file = "../js/dhtmlTree.js";
    private String show = "<script language=\"JavaScript\">\n<!--\n new tree (TREE_ITEMS, tree_tpl);\n//-->\n</script>";
    
    public DHtmlTree(){
    	
    }
    
    public boolean hasSon(){
    	if(sons == null) return false ;
    	return !sons.isEmpty() ;
    }
    
    public DHtmlTree(String name){
    	this.name = name ;
    	this.link = "" ;
    	this.target = "" ;
    }
    
    public DHtmlTree(String name, String link){
    	this.name = name ;
    	this.link = link ;
    	this.target = "" ;
    }
    
    public DHtmlTree(String name, String link, String target){
    	this.name = name ;
    	this.link = link ;
    	this.target = target ;
    }
    
    public void update()
    {
        StringBuffer buff = new StringBuffer(4096);
        buff.append("<script language=\"JavaScript\">\n<!--\n var TREE_ITEMS = [");
        this.buildItems(this, buff);
        buff.append("];\n");
        buff.append(" var TREE_TARGET = new Array();\n");
        this.buildTarget(this, buff);
        buff.append("\n//-->\n</script>");
        
        this.setItems(buff.toString());
    }
    
    private void buildTarget(IDHtmlTreeNode p_node, StringBuffer p_buff){
        if (p_node == null || p_buff == null)
        {
            return;
        }
        
        p_buff.append("TREE_TARGET['" + p_node.getName() + "'] = '" + p_node.getTarget() + "';\n");
        
        List sons = p_node.getSons();
        
        if (sons != null && sons.size() > 0)
        {
            for (int i=0; i<sons.size(); i++)
            {
                this.buildTarget((IDHtmlTreeNode)sons.get(i), p_buff);
            }
        }
    }
    
    private void buildItems(IDHtmlTreeNode p_node, StringBuffer p_buff)
    {
        if (p_node == null || p_buff == null)
        {
            return;
        }
        
        p_buff.append("['");
        p_buff.append(p_node.getName());
        p_buff.append("','");
        p_buff.append(p_node.getLink());
        p_buff.append("'");
        
        List sons = p_node.getSons();
        
        if (sons != null && sons.size() > 0)
        {
            for (int i=0; i<sons.size(); i++)
            {
                p_buff.append(",");
                this.buildItems((IDHtmlTreeNode)sons.get(i), p_buff);
            }
        }
        
        p_buff.append("]");
    }

    public String getShow()
    {
        return this.show;
    }
    
    public String getFile()
    {
        return this.file;
    }

    public void setFile(String coreJsUri)
    {
        this.file = coreJsUri;
    }

    public String getItems()
    {
        if (this.items == null)
        {
            this.update();
        }
        
        return this.items;
    }

    public void setItems(String p_item)
    {
        this.items = p_item;
    }
    
    public void addSon(IDHtmlTreeNode p_son)
    {
        if (this.sons == null)
        {
            this.sons = new LinkedList();
        }
        
        this.sons.add(p_son);
    }
    
    public String getName()
    {
        return this.name;
    }

    public String getLink()
    {
        return this.link;
    }

    public String getTarget()
    {
        return this.target;
    }

    public List getSons()
    {
        return this.sons;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public void setSons(List sons)
    {
        this.sons = sons;
    }
}
