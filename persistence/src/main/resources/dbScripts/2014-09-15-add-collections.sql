-- Create table for Collections;

CREATE TABLE collection
(
  id bigserial NOT NULL,
  title character varying(200),
  liferay_user_id bigint NOT NULL,
  public boolean NOT NULL,
  edit boolean NOT NULL,
  description text,
  CONSTRAINT collection_pkey PRIMARY KEY (id)
);
ALTER TABLE collection
  OWNER TO postgres;
REVOKE ALL ON TABLE collection FROM PUBLIC;
GRANT ALL ON TABLE collection TO postgres;
GRANT ALL ON TABLE collection TO admin;
GRANT ALL ON TABLE collection TO apenet_dashboard;
GRANT ALL ON TABLE collection TO apenet_portal;

CREATE INDEX collection__liferay_user_idx
  ON collection
  USING btree
  (liferay_user_id);

-- Fix sequence permissions for collection id.
ALTER TABLE collection_id_seq
  OWNER TO postgres;
REVOKE ALL ON SEQUENCE collection_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE collection_id_seq FROM admin;
GRANT ALL ON SEQUENCE collection_id_seq TO admin;
GRANT ALL ON SEQUENCE collection_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE collection_id_seq TO apenet_portal;

ALTER TABLE collection
   ALTER COLUMN id SET DEFAULT nextval('collection_id_seq'::regclass);

-- Create table for cSollection content;
CREATE TABLE collection_content
(
  id bigserial NOT NULL,
  id_collection bigint NOT NULL,
  id_search bigint,
  id_bookmarks bigint,
  CONSTRAINT collection_content_pkey PRIMARY KEY (id),
  CONSTRAINT collection_content_fkey_collection FOREIGN KEY (id_collection)
      REFERENCES collection (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT collection_content_fkey_saved_bookmarks FOREIGN KEY (id_bookmarks)
      REFERENCES saved_bookmarks (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT collection_content_fkey_search FOREIGN KEY (id_search)
      REFERENCES ead_saved_search (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE collection_content
  OWNER TO postgres;
REVOKE ALL ON TABLE collection_content FROM PUBLIC;
GRANT ALL ON TABLE collection_content TO postgres;
GRANT ALL ON TABLE collection_content TO admin;
GRANT ALL ON TABLE collection_content TO apenet_dashboard;
GRANT ALL ON TABLE collection_content TO apenet_portal;

-- Fix sequence permissions for collection content id.
ALTER TABLE collection_content_id_seq
  OWNER TO postgres;
REVOKE ALL ON SEQUENCE collection_content_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE collection_content_id_seq FROM admin;
GRANT ALL ON SEQUENCE collection_content_id_seq TO admin;
GRANT ALL ON SEQUENCE collection_content_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE collection_content_id_seq TO apenet_portal;

ALTER TABLE collection_content
   ALTER COLUMN id SET DEFAULT nextval('collection_content_id_seq'::regclass);

