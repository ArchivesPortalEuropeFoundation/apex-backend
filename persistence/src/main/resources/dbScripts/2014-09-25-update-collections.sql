ALTER TABLE collection_content DROP CONSTRAINT collection_content_fkey_collection;
ALTER TABLE collection_content ADD CONSTRAINT collection_content_fkey_collection 
	  FOREIGN KEY (id_collection)
      REFERENCES collection (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;