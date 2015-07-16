CREATE SEQUENCE ftp_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  CACHE 1;
ALTER TABLE ftp_id_seq
  OWNER TO postgres;
GRANT ALL ON SEQUENCE ftp_id_seq TO postgres;
GRANT ALL ON SEQUENCE ftp_id_seq TO admin;
GRANT ALL ON SEQUENCE ftp_id_seq TO apenet_dashboard;

CREATE TABLE ftp
(
  id integer NOT NULL DEFAULT nextval('ftp_id_seq'::regclass),
  url character varying(256) NOT NULL,
  port integer,
  username character varying(60),
  ai_id integer NOT NULL,
  CONSTRAINT ftp_pkey PRIMARY KEY (id),
  CONSTRAINT ai_id_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ftp
  OWNER TO postgres;
GRANT ALL ON TABLE ftp TO postgres;
GRANT ALL ON TABLE ftp TO admin;
GRANT ALL ON TABLE ftp TO apenet_dashboard;
