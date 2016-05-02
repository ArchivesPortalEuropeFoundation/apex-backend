
ALTER TABLE archival_institution
	DROP CONSTRAINT archival_institution_parent_ai_id_fkey;

ALTER TABLE ingestionprofile
	DROP CONSTRAINT ingestionprofile_ai_id_fkey;

DROP INDEX c_level__nodes_idx;

CREATE SEQUENCE api_key_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;


CREATE TABLE api_key (
	id integer DEFAULT nextval('api_key_id_seq'::regclass) NOT NULL,
	apikey character varying(36),
	emailaddress character varying(255),
	firstname character varying(255),
	lastname character varying(255),
	url character varying(255),
	creationtime timestamp without time zone,
	status character varying(255),
	uuid character varying(36),
	version timestamp without time zone,
	liferayuserid bigint NOT NULL
);

ALTER TABLE api_key OWNER TO apenet_dashboard;

CREATE SEQUENCE pk_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pk_seq OWNER TO apenet_dashboard;

ALTER TABLE archival_institution
	ADD COLUMN opendataenabled boolean,
	ADD COLUMN totalsolrdocscount bigint,
	ADD COLUMN unprocessedsolrdocs bigint,
	ADD COLUMN totalsolrdocsforopendata bigint,
	ADD COLUMN opendataqueueid integer;

ALTER SEQUENCE api_key_id_seq
	OWNED BY api_key.id;

ALTER TABLE api_key
	ADD CONSTRAINT api_key_pkey PRIMARY KEY (id);

ALTER TABLE archival_institution
	ADD CONSTRAINT archival_institution_parent_ai_id_fkey FOREIGN KEY (parent_ai_id) REFERENCES archival_institution(id);

ALTER TABLE ingestionprofile
	ADD CONSTRAINT ingestionprofile_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);

ALTER TABLE source_guide
	ADD CONSTRAINT source_guide_sg_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);

ALTER TABLE warnings
	ADD CONSTRAINT "warnings_ec_id_fkey -> eac_cpf" FOREIGN KEY (eac_id) REFERENCES eac_cpf(id) ON UPDATE CASCADE ON DELETE CASCADE;

-- CREATE INDEX c_level__nodes_idx ON c_level USING btree (ec_id) WHERE (leaf = false);

-- CREATE INDEX c_level__unitid_ec_id_idx ON c_level USING btree (unitid, ec_id);

-- CREATE INDEX ead_content__source_guide_idx ON ead_content USING btree (sg_id);
