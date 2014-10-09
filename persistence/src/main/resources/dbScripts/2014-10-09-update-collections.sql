ALTER TABLE collection_content DROP CONSTRAINT collection_content_fkey_saved_bookmarks;
ALTER TABLE collection_content ADD CONSTRAINT collection_content_fkey_saved_bookmarks 
	  FOREIGN KEY (id_bookmarks)
      REFERENCES saved_bookmarks (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE collection_content DROP CONSTRAINT collection_content_fkey_search;
ALTER TABLE collection_content ADD CONSTRAINT collection_content_fkey_search 
	  FOREIGN KEY (id_search)
      REFERENCES ead_saved_search (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;