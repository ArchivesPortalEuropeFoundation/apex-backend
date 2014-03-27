ALTER TABLE warnings RENAME ec_id  TO eac_id;

ALTER TABLE warnings
  RENAME CONSTRAINT warnings_ec_id_fkey TO warnings_eac_id_fkey;
