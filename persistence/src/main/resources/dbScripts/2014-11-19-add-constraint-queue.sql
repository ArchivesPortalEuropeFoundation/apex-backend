ALTER TABLE queue ADD CONSTRAINT queue_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;