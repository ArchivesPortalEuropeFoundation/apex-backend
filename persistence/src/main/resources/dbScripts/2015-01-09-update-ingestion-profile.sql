alter table ingestionprofile ADD COLUMN xsl_upload_id integer;
alter table ingestionprofile ADD CONSTRAINT xsl_upload_id_fkey FOREIGN KEY (xsl_upload_id) REFERENCES xsl_upload(id)
MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE xsl_upload OWNER TO postgres;
REVOKE ALL ON TABLE xsl_upload FROM PUBLIC;
REVOKE ALL ON TABLE xsl_upload FROM admin;
GRANT ALL ON TABLE xsl_upload TO admin;
GRANT ALL ON TABLE xsl_upload TO apenet_dashboard;
REVOKE ALL ON SEQUENCE xsl_upload_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE xsl_upload_id_seq FROM admin;
GRANT ALL ON SEQUENCE xsl_upload_id_seq TO admin;
GRANT ALL ON SEQUENCE xsl_upload_id_seq TO apenet_dashboard;
