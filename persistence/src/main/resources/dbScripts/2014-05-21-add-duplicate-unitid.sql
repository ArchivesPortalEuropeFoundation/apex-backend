ALTER TABLE c_level
   ADD COLUMN duplicate_unitid boolean DEFAULT false;
   
DROP INDEX c_level__top_c_levels_idx;
CREATE INDEX c_level__top_c_levels_idx
  ON c_level
  USING btree
  (order_id, ec_id)
  WHERE parent_cl_id IS NULL;