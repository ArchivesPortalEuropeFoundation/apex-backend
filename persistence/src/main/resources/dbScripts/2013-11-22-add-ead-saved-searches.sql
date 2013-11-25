CREATE TABLE ead_saved_search
(
  id bigserial NOT NULL,
  liferay_user_id bigint NOT NULL,
  modified_date timestamp without time zone DEFAULT now(),
  public_search boolean DEFAULT false,
  label character varying(255) default NULL,
  term character varying(255) default NULL,
  CONSTRAINT ead_saved_search_pkey PRIMARY KEY (id)
);
ALTER TABLE ead_saved_search OWNER TO postgres;
REVOKE ALL ON TABLE ead_saved_search FROM PUBLIC;
GRANT ALL ON TABLE ead_saved_search TO postgres;
GRANT ALL ON TABLE ead_saved_search TO admin;
GRANT ALL ON TABLE ead_saved_search TO apenet_dashboard;
GRANT ALL ON TABLE ead_saved_search TO apenet_portal;

CREATE INDEX ead_saved_search__liferay_user_idx
  ON ead_saved_search
  USING btree
  (liferay_user_id);