CREATE INDEX c_level__parent_cl_AND_order_id_idx
  ON c_level (parent_cl_id, order_id);
  
ALTER ROLE apenet_dashboard SET statement_timeout = '300000';
ALTER ROLE apenet_portal SET statement_timeout = '300000';