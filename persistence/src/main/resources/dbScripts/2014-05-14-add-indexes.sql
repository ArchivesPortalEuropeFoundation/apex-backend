CREATE INDEX c_level__unitid_ec_id_idx
  ON c_level  (unitid,ec_id);
  
  CREATE INDEX ead_content__source_guide_idx
  ON ead_content
  USING btree
  (sg_id);