CREATE TABLE coordinates (
    id integer NOT NULL,
    id_institution integer NOT NULL,
    name_institution character varying NOT NULL,
    address character varying NOT NULL,
    lat numeric,
    lon numeric
);


ALTER TABLE public.coordinates OWNER TO postgres;
CREATE SEQUENCE coordinates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.coordinates_id_seq OWNER TO postgres;
ALTER SEQUENCE coordinates_id_seq OWNED BY coordinates.id;
ALTER TABLE ONLY coordinates ALTER COLUMN id SET DEFAULT nextval('coordinates_id_seq'::regclass);
SELECT pg_catalog.setval('coordinates_id_seq', 1, false);

ALTER TABLE ONLY coordinates
    ADD CONSTRAINT id PRIMARY KEY (id);
ALTER TABLE ONLY coordinates
    ADD CONSTRAINT coordinates_archival_institution_fk FOREIGN KEY (id_institution) REFERENCES archival_institution(id);


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
