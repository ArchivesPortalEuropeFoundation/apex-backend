ALTER TABLE topic_mapping ADD  CONSTRAINT topic_mapping_topic_id_fkey FOREIGN KEY (topic_id)
      REFERENCES topic (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION        