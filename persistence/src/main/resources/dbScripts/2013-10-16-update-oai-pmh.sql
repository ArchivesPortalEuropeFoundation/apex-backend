ALTER TABLE archival_institution_oai_pmh ALTER COLUMN id TYPE bigint;
ALTER TABLE archival_institution_oai_pmh ALTER COLUMN oai_pmh_url TYPE character varying(255);
ALTER TABLE archival_institution_oai_pmh ALTER COLUMN oai_pmh_id TYPE character varying(255);
ALTER TABLE archival_institution_oai_pmh RENAME COLUMN oai_pmh_id TO oai_pmh_set;
ALTER TABLE archival_institution_oai_pmh ALTER COLUMN oai_pmh_set DROP NOT NULL;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN oai_pmh_metadata character varying(255) NOT NULL;