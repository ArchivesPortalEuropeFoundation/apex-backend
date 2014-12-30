CREATE TABLE xsl_upload
(
  id serial NOT NULL,
  readable_name character varying(255) NOT NULL,
  name character varying(255) NOT NULL,
  archival_institution_id integer NOT NULL,
  CONSTRAINT xsl_upload_pkey PRIMARY KEY (id),
  CONSTRAINT archival_institution_id_fkey FOREIGN KEY (archival_institution_id)
      REFERENCES archival_institution(id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE xsl_upload OWNER TO apenet_dashboard;
GRANT ALL ON TABLE xsl_upload TO apenet_dashboard;
GRANT ALL ON TABLE xsl_upload TO admin;