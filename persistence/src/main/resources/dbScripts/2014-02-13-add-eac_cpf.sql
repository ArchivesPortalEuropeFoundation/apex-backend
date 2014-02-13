-- EAC-CPF table.

CREATE TABLE eac_cpf (
    id integer NOT NULL,
    name_entry text,
    upload_date timestamp without time zone NOT NULL,
    path_cpf character varying(255) NOT NULL,
    um_id integer NOT NULL,
    ai_id integer NOT NULL,
    published boolean DEFAULT false,
    converted boolean DEFAULT false,
    validated smallint DEFAULT 1,
    europeana smallint DEFAULT 0,
    queuing smallint DEFAULT 0,
    publish_date timestamp without time zone,
    cpf_id character varying(255) NOT NULL,
    CONSTRAINT eac_cpf_pkey PRIMARY KEY (id),
    CONSTRAINT "eac_cpf_ai_id_fKey" FOREIGN KEY (ai_id)
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

-- CPF_content table.
DROP TABLE cpf_content;

CREATE TABLE cpf_content (
    id integer NOT NULL,
    cpf_id character varying(255) NOT NULL,
    xml text,
    eac_cpf_id bigint,
    visible boolean,
    entity_id text,
    display_intro boolean DEFAULT true NOT NULL,
    CONSTRAINT cpf_content_pkey PRIMARY KEY (id),
    CONSTRAINT cpf_content_eac_cpf_id_fkey FOREIGN KEY (eac_cpf_id)
        REFERENCES eac_cpf (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

REVOKE ALL ON TABLE cpf_content FROM PUBLIC;
REVOKE ALL ON TABLE cpf_content FROM postgres;
GRANT ALL ON TABLE cpf_content TO postgres;
GRANT ALL ON TABLE cpf_content TO admin;
GRANT ALL ON TABLE cpf_content TO apenet_dashboard;
GRANT SELECT ON TABLE cpf_content TO apenet_portal;

-- Queue table.
ALTER TABLE queue ADD COLUMN eac_id integer;
ALTER TABLE queue
  ADD CONSTRAINT index_queue_eac_id_fkey FOREIGN KEY (eac_id)
      REFERENCES eac_cpf (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
