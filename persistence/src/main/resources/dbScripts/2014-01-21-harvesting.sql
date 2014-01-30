ALTER TABLE archival_institution_oai_pmh DROP COLUMN errors;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN errors text DEFAULT null;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN errors_response_path character varying(255) DEFAULT null;