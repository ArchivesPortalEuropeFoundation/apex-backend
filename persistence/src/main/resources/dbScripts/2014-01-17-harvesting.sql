ALTER TABLE archival_institution_oai_pmh ADD COLUMN errors boolean DEFAULT false;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN from_date character varying(10);