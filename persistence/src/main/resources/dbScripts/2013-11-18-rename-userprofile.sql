ALTER TABLE userprofile RENAME TO ingestionprofile;
ALTER TABLE ingestionprofile   RENAME CONSTRAINT userprofile_pkey TO ingestionprofile_pkey;
ALTER TABLE ingestionprofile   RENAME CONSTRAINT userprofile_ai_id_fkey TO ingestionprofile_ai_id_fkey;
