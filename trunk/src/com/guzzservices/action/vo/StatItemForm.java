package com.guzzservices.action.vo;

import com.guzzservices.business.StatItem;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class StatItemForm {

	private StatItem statItem ;	
	private boolean newStatItem ;
	
	private static String defaultTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n \n<rank>\n    <name><![CDATA[${statItem.name}]]></name>\n    #foreach($m_record in $records)\n    <item> \n        <order>$m_record.objectOrder</order> \n        <id>$m_record.objectId</id> \n        <opTimes>$m_record.opTimes</opTimes> \n        <createdTime>$m_record.objectCreatedTime</createdTime> \n        <title><![CDATA[$m_record.objectTitle]]></title> \n        <link><![CDATA[$m_record.objectURL]]></link>\n        <extra1><![CDATA[$m_record.extra1]]></extra1>\n        <extra2><![CDATA[$m_record.extra2]]></extra2>\n        <extra3><![CDATA[$m_record.extra3]]></extra3> \n    </item>\n    #end\n</rank>" ;
		
	public StatItemForm(StatItem statItem){
		newStatItem = false ;
		this.statItem = statItem ;
	}
	
	public StatItemForm(int groupId, int userId){
		newStatItem = true ;
		this.statItem = new StatItem() ;
		this.statItem.setGroupId(groupId) ;
		this.statItem.setUserId(userId) ;
		this.statItem.setTemplateContent(defaultTemplate) ;
	}

	public boolean isNewStatItem() {
		return newStatItem;
	}

	public void setNewStatItem(boolean newStatItem) {
		this.newStatItem = newStatItem;
	}

	public StatItem getStatItem() {
		return statItem;
	}

	public void setStatItem(StatItem statItem) {
		this.statItem = statItem;
	}
	
}
