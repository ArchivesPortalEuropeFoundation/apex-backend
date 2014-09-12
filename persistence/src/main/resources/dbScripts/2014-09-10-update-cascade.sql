ALTER TABLE queue DROP CONSTRAINT queue_uf_id_fkey;
ALTER TABLE queue ADD CONSTRAINT queue_uf_id_fkey FOREIGN KEY (uf_id)
      REFERENCES up_file (id) ON DELETE CASCADE;
