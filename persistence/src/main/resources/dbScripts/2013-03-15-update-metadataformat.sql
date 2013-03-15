ALTER TABLE ese ADD COLUMN metadataformat character varying(20) default NULL;
UPDATE ese SET metadataformat  = 'EDM' WHERE mf_id =1;
ALTER TABLE ese ALTER COLUMN metadataformat SET NOT NULL;
ALTER TABLE ese DROP COLUMN mf_id;
CREATE INDEX ese_metadataformat ON ese (metadataformat );


ALTER TABLE resumption_token ADD COLUMN metadataformat character varying(20) default NULL;
UPDATE resumption_token SET metadataformat  = 'EDM' WHERE mf_id =1;
ALTER TABLE resumption_token ALTER COLUMN metadataformat SET NOT NULL;
ALTER TABLE resumption_token DROP COLUMN mf_id;
DROP TABLE IF EXISTS metadata_format;
CREATE INDEX resumption_token_metadataformat ON ese (metadataformat );