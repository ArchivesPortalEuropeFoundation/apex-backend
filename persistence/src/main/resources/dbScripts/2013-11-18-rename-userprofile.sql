ALTER TABLE userprofile
  RENAME TO ingestionprofile;
  RENAME CONSTRAINT userprofile_pkey TO ingestionprofile_pkey;
  RENAME CONSTRAINT userprofile_ai_id_fkey TO ingestionprofile_ai_id_fkey;
ALTER INDEX public.fki_userprofile_fkey_ai_id
  RENAME TO fki_ingestionprofile_fkey_ai_id;