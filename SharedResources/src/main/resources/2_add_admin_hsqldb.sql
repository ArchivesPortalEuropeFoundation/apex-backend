INSERT INTO dashboard_user (id, email_address, password, secret_question, secret_answer, user_role_id, first_name, last_name, is_active) 
    VALUES (1, 'apeAdmin@gmail.com','5JgP8LV83cuhtvwYmd6Z9hOVLA4=','What is your surname?','APE',3,'Admin','APE', true);
INSERT INTO api_key(id, apikey, emailaddress, firstname, lastname, url, creationtime, status, uuid, version, liferayuserid)
    VALUES (1, 'myApiKeyXXXX123456789', 'apeAdmin@gmail.com', 'Admin','APE', '', '2016-03-04 10:42:49.729', 
            'created', '91657144-ffee-4031-8f56-8bb787aca64d', '2016-03-04 10:42:49.729', '96401');
INSERT INTO archival_institution(id,ainame,user_id,isgroup,alorder,internal_al_id,country_id,contain_searchable_items,using_mets,opendataenabled,parent_ai_id,OPENDATAQUEUEID)
    VALUES(361,'Nationaal Archief',1,false,0,'A1449139721542-879333.75',7,true,true,true,null, null);

INSERT INTO archival_institution(id,ainame,user_id,isgroup,alorder,internal_al_id,country_id,contain_searchable_items,using_mets,opendataenabled,parent_ai_id)
    VALUES(391,'testAiinUnit',1,false,0,'A1454502006742-943391.44',7,true,false,true,null);

INSERT INTO archival_institution(id,ainame,user_id,isgroup,alorder,internal_al_id,country_id,contain_searchable_items,using_mets,opendataenabled,parent_ai_id)
    VALUES(657,'geek',1,false,1,'A1461062306986-432864.4',7,false,false,true,null);