-- Create table for Saved Bookmarks.

CREATE TABLE saved_bookmarks
(
  id bigserial NOT NULL,
  liferay_user_id bigint NOT NULL,
  modified_date timestamp without time zone NOT NULL DEFAULT now(),
  bookmark_name character varying(255) DEFAULT NULL,
  description character varying(255) DEFAULT NULL,
  persistent_link character varying(255) DEFAULT NULL,
  typedocument character varying(20) DEFAULT NULL,
  CONSTRAINT saved_bookmarks_pkey PRIMARY KEY (id)
);
ALTER TABLE saved_bookmarks
  OWNER TO postgres;
REVOKE ALL ON TABLE saved_bookmarks FROM PUBLIC;
GRANT ALL ON TABLE saved_bookmarks TO postgres;
GRANT ALL ON TABLE saved_bookmarks TO admin;
GRANT ALL ON TABLE saved_bookmarks TO apenet_dashboard;
GRANT ALL ON TABLE saved_bookmarks TO apenet_portal;

CREATE INDEX saved_bookmarks__liferay_user_idx
  ON saved_bookmarks
  USING btree
  (liferay_user_id);

-- Fix sequence permissions for Saved Bookmarks id.
ALTER TABLE saved_bookmarks_id_seq
  OWNER TO postgres;
REVOKE ALL ON SEQUENCE saved_bookmarks_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE saved_bookmarks_id_seq FROM admin;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO admin;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO apenet_portal;

ALTER TABLE saved_bookmarks
   ALTER COLUMN id SET DEFAULT nextval('saved_bookmarks_id_seq'::regclass);