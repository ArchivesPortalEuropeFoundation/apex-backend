ALTER TABLE userprofile DROP COLUMN user_id;
ALTER TABLE userprofile DROP CONSTRAINT userprofile_user_id_fkey;

ALTER TABLE userprofile ADD COLUMN ai_id integer;
ALTER TABLE userprofile ALTER COLUMN ai_id SET NOT NULL;

ALTER TABLE userprofile
  ADD CONSTRAINT userprofile_ai_id_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL;