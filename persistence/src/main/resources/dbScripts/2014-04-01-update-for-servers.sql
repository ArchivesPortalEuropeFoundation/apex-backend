DROP TABLE cpf_content;

CREATE TABLE eac_cpf (
    id serial NOT NULL,
    title text,
    upload_date timestamp without time zone NOT NULL,
    path character varying(255) NOT NULL,
    um_id integer NOT NULL,
    ai_id integer NOT NULL,
    published boolean DEFAULT false,
    converted boolean DEFAULT false,
    validated smallint DEFAULT 1,
    cpfrelations bigint default 0,
    resourcerelations bigint default 0,
    functionrelations bigint default 0,
    europeana smallint DEFAULT 0,
    queuing smallint DEFAULT 0,
    publish_date timestamp without time zone,
    identifier character varying(255) NOT NULL,
    CONSTRAINT eac_cpf_pkey PRIMARY KEY (id),
    CONSTRAINT eac_cpf_ai_id_fkey FOREIGN KEY (ai_id)
        REFERENCES archival_institution (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT eac_cpf_um_id_fkey FOREIGN KEY (um_id)
        REFERENCES upload_method (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

REVOKE ALL ON TABLE eac_cpf FROM PUBLIC;
REVOKE ALL ON TABLE eac_cpf FROM postgres;
GRANT ALL ON TABLE eac_cpf TO postgres;
GRANT ALL ON TABLE eac_cpf TO admin;
GRANT ALL ON TABLE eac_cpf TO apenet_dashboard;
GRANT SELECT ON TABLE eac_cpf TO apenet_portal;


ALTER TABLE queue ADD COLUMN eac_cpf_id integer;
ALTER TABLE queue
  ADD CONSTRAINT index_queue_eac_id_fkey FOREIGN KEY (eac_cpf_id)
      REFERENCES eac_cpf (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
GRANT ALL ON TABLE eac_cpf_id_seq TO postgres;
GRANT ALL ON TABLE eac_cpf_id_seq TO admin;
GRANT ALL ON TABLE eac_cpf_id_seq TO apenet_dashboard;


ALTER TABLE warnings ADD COLUMN eac_id integer;

ALTER TABLE warnings
  ADD CONSTRAINT warnings_eac_id_fkey FOREIGN KEY (eac_id)
      REFERENCES eac_cpf (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;


CREATE INDEX eac_cpf__archival_institution_idx
  ON eac_cpf
  USING btree
  (ai_id);