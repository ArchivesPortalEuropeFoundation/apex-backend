DROP INDEX c_level__nodes_idx;
CREATE INDEX c_level__nodes_idx
  ON c_level
  USING btree
  (ec_id, leaf)
