CREATE TABLE topic
(
	id bigserial NOT NULL,
	property_key character varying(100) NOT NULL,
	description character varying(100) NOT NULL,
  CONSTRAINT topic_pkey PRIMARY KEY (id)
);
ALTER TABLE topic OWNER TO postgres;
GRANT ALL ON TABLE topic TO postgres;
GRANT ALL ON TABLE topic TO admin;
GRANT ALL ON TABLE topic TO apenet_dashboard;

CREATE UNIQUE INDEX topic__property_key_idx
  ON topic
  USING btree
  (property_key);
  
  CREATE UNIQUE INDEX topic__description_idx
  ON topic
  USING btree
  (description);

CREATE TABLE topic_mapping
(
	id bigserial NOT NULL,
	ai_id integer NOT NULL,
	topic_id bigint NOT NULL,
	controlaccess_keyword character varying(255) default NULL,
	sg_id bigint default NULL,
  CONSTRAINT topic_mapping_pkey PRIMARY KEY (id),
  CONSTRAINT topic_mapping_sg_id_fkey FOREIGN KEY (sg_id)
      REFERENCES source_guide (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT topic_mapping_ai_id_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE      
);
ALTER TABLE topic OWNER TO postgres;
GRANT ALL ON TABLE topic TO postgres;
GRANT ALL ON TABLE topic TO admin;
GRANT ALL ON TABLE topic TO apenet_dashboard;

CREATE INDEX topic_mapping__archival_institution_idx
  ON topic_mapping
  USING btree
  (ai_id);
  
  CREATE INDEX topic_mapping__source_guide_idx
  ON topic_mapping
  USING btree
  (sg_id);
