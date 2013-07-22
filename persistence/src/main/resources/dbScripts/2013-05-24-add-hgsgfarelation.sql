CREATE TABLE hg_sg_fa_relation
(
  id bigserial NOT NULL,
  fa_id bigint NOT NULL,
  hg_id bigint,
  sg_id bigint,
  ai_id bigint,
  hg_sg_clevel_id bigint NOT NULL,
  CONSTRAINT hg_sg_fa_relation_pkey PRIMARY KEY (id),
  CONSTRAINT hg_sg_fa_relation_fa_id_fkey FOREIGN KEY (fa_id)
      REFERENCES finding_aid (id) ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT hg_sg_fa_relation_hg_id_fkey FOREIGN KEY (hg_id)
      REFERENCES holdings_guide (id) ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT hg_sg_fa_relation_sg_id_fkey FOREIGN KEY (sg_id)
      REFERENCES source_guide (id) ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT hg_sg_fa_relation_ai_id_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT hg_sg_fa_relation_hg_sg_clevel_id_fkey FOREIGN KEY (hg_sg_clevel_id)
      REFERENCES c_level (id) ON UPDATE NO ACTION ON DELETE CASCADE
);
ALTER TABLE hg_sg_fa_relation OWNER TO postgres;
REVOKE ALL ON TABLE hg_sg_fa_relation_id_seq FROM PUBLIC;
GRANT ALL ON TABLE hg_sg_fa_relation TO postgres;
GRANT ALL ON TABLE hg_sg_fa_relation TO admin;
GRANT ALL ON TABLE hg_sg_fa_relation TO apenet_dashboard;
GRANT SELECT ON TABLE hg_sg_fa_relation TO apenet_portal;

CREATE INDEX hg_sg_fa_relation__archival_institution_idx
  ON hg_sg_fa_relation
  USING btree
  (ai_id);
  
CREATE INDEX hg_sg_fa_relation__finding_aid_idx
  ON hg_sg_fa_relation
  USING btree
  (fa_id);

CREATE INDEX hg_sg_fa_relation__holdings_guide_idx
  ON hg_sg_fa_relation
  USING btree
  (hg_id);
  
  CREATE INDEX hg_sg_fa_relation__source_guide_idx
  ON hg_sg_fa_relation
  USING btree
  (sg_id);
  
    CREATE INDEX hg_sg_fa_relation__c_level_idx
  ON hg_sg_fa_relation
  USING btree
  (hg_sg_clevel_id);