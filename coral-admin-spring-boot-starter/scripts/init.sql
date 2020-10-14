INSERT INTO sc_account(`username`,`category`,`status`,`creation_user`,`last_modified_user`) VALUES ('admin',1,0,'系统初始化','系统初始化');
set @accountId:=LAST_INSERT_ID();
INSERT INTO sc_account_role(`account_id`,`role_code`,`creation_user`,`last_modified_user`) VALUES (@accountId,'admin','系统初始化','系统初始化');