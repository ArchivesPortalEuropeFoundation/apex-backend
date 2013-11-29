CREATE TABLE coordinates (
    id serial NOT NULL,
    ai_id integer NOT NULL,
    name_institution character varying NOT NULL,
    address character varying NOT NULL,
    lat numeric,
    lon numeric,
      CONSTRAINT coordinates_archival_institution_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) ON UPDATE NO ACTION ON DELETE CASCADE
);

REVOKE ALL ON TABLE coordinates FROM PUBLIC;
REVOKE ALL ON TABLE coordinates FROM postgres;
GRANT ALL ON TABLE coordinates TO postgres;
GRANT ALL ON TABLE coordinates TO admin;
GRANT ALL ON TABLE coordinates TO apenet_dashboard;
GRANT SELECT ON TABLE coordinates TO apenet_portal;

REVOKE ALL ON SEQUENCE coordinates_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE coordinates_id_seq FROM postgres;
GRANT ALL ON SEQUENCE coordinates_id_seq TO postgres;
GRANT ALL ON SEQUENCE coordinates_id_seq TO apenet_dashboard;
GRANT SELECT ON SEQUENCE coordinates_id_seq TO apenet_portal;
GRANT ALL ON SEQUENCE coordinates_id_seq TO admin;
