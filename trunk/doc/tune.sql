
权限表错误后的恢复sql，创建者取回权限：

select f.id, u.email  from gs_filter_word_group as f left join gs_user as u on f.userId = u.id;

insert into gs_authed_service(serviceName, owner, serviceKey, email) select 'gs_fw', 1, f.id, u.email  from gs_filter_word_group as f left join gs_user as u on f.userId = u.id;

insert into gs_authed_service(serviceName, owner, serviceKey, email) select 'gs_config', 1, f.id, u.email  from gs_configuration_group as f left join gs_user as u on f.userId = u.id;

insert into gs_authed_service(serviceName, owner, serviceKey, email) select 'gs_si', 1, f.id, u.email  from gs_stat_item_group as f left join gs_user as u on f.userId = u.id;




update QRTZ_JOB_DETAILS set JOB_CLASS_NAME = "com.guzzservices.manager.impl.ExecuteTaskJob" WHERE JOB_CLASS_NAME = "com.guzzservices.manager.impl.StatItemManagerImpl$TopRankJob" and JOB_GROUP LIKE "task%" ;
update QRTZ_TRIGGERS set TRIGGER_STATE = "WAITING" WHERE TRIGGER_STATE="ERROR" ;


alter table gs_stat_item modify column autoPublish bit(1) default 0 ;

