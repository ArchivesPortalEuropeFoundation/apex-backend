ALTER TABLE c_level
   ADD COLUMN duplicate_unitid boolean DEFAULT false;
DROP INDEX c_level__ead_content_idx;
DROP INDEX c_level__href_eadid_idx;
DROP INDEX c_level__order_idx; 	
DROP INDEX c_level__parent_cl_idx; 	
DROP INDEX c_level__top_c_levels_idx;
DROP INDEX c_level__parent_cl_and_order_id_idx;   
CREATE INDEX c_level__eadid_ref_idx
  ON c_level
  USING btree
  (ec_id, href_eadid)
  WHERE href_eadid IS NOT NULL;


CREATE INDEX c_level__nodes_idx
  ON c_level
  USING btree
  (ec_id)
  WHERE leaf = false;



CREATE INDEX c_level__parent_idx
  ON c_level
  USING btree
  (parent_cl_id, order_id)
  WHERE parent_cl_id IS NOT NULL;


CREATE INDEX c_level__persistent_link_idx
  ON c_level
  USING btree
  (unitid, ec_id)
  WHERE duplicate_unitid = false;


CREATE INDEX c_level__top_levels_idx
  ON c_level
  USING btree
  (order_id, ec_id)
  WHERE parent_cl_id IS NULL;