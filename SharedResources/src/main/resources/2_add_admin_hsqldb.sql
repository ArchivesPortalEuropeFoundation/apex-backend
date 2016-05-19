INSERT INTO dashboard_user (id, email_address, password, secret_question, secret_answer, user_role_id, first_name, last_name, is_active) 
    VALUES (1, 'apeAdmin@gmail.com','5JgP8LV83cuhtvwYmd6Z9hOVLA4=','What is your surname?','APE',3,'Admin','APE', true);
INSERT INTO api_key(id, apikey, emailaddress, firstname, lastname, url, creationtime, status, uuid, version, liferayuserid)
    VALUES (1, 'myApiKeyXXXX123456789', 'apeAdmin@gmail.com', 'Admin','APE', '', '2016-03-04 10:42:49.729', 
            'created', '91657144-ffee-4031-8f56-8bb787aca64d', '2016-03-04 10:42:49.729', '96401');
