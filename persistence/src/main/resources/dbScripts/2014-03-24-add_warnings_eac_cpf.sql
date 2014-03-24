-- Column: ec_id

-- ALTER TABLE warnings DROP COLUMN ec_id;

ALTER TABLE warnings ADD COLUMN ec_id integer;

ALTER TABLE warnings
  ADD CONSTRAINT "warnings_ec_id_fkey -> eac_cpf" FOREIGN KEY (ec_id) REFERENCES eac_cpf (id)
   ON UPDATE CASCADE ON DELETE CASCADE;

-- Foreign Key: warnings_ec_id_fkey

-- ALTER TABLE warnings DROP CONSTRAINT warnings_ec_id_fkey;

ALTER TABLE warnings
  ADD CONSTRAINT warnings_ec_id_fkey FOREIGN KEY (ec_id)
      REFERENCES eac_cpf (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

-- Index: eac_cpf__archival_institution_idx

-- DROP INDEX eac_cpf__archival_institution_idx;

CREATE INDEX eac_cpf__archival_institution_idx
  ON eac_cpf
  USING btree
  (ai_id);