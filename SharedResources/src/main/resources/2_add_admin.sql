INSERT INTO dashboard_user (id, email_address, password, secret_question, secret_answer, user_role_id, first_name, last_name, is_active) VALUES (1, 'apeAdmin@gmail.com','5JgP8LV83cuhtvwYmd6Z9hOVLA4=','What is your surname?','APE',3,'Admin','APE', true);
SELECT pg_catalog.setval('user_id_seq', 2, true);

INSERT INTO api_key VALUES (1, 'myApiKeyXXXX123456789', 'apeAdmin@gmail.com', 'APE', 'Admin', '', '2016-03-04 10:42:49.729', 'created', '91657144-ffee-4031-8f56-8bb787aca64d', '2016-03-04 10:42:49.729', 96401);

INSERT INTO archival_institution(id,ainame,isgroup,alorder,internal_al_id,country_id,contain_searchable_items,using_mets,opendataenabled)
    VALUES(361,'Nationaal Archief',false,0,'A1449139721542-879333.75',7,true,true,true);

INSERT INTO archival_institution(id,ainame,isgroup,alorder,internal_al_id,country_id,contain_searchable_items,using_mets,opendataenabled)
    VALUES(391,'testAiinUnit',false,0,'A1454502006742-943391.44',7,true,false,true);

INSERT INTO archival_institution(id,ainame,isgroup,alorder,internal_al_id,country_id,contain_searchable_items,using_mets,opendataenabled)
    VALUES(657,'geek',false,1,'A1461062306986-432864.4',7,false,false,true);