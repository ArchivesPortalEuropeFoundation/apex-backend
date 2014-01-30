ALTER TABLE archival_institution_oai_pmh DROP COLUMN errors;
ALTER TABLE archival_institution_oai_pmh DROP COLUMN errors_response_path;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN errors_response_path text DEFAULT null;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN list_by_identifiers boolean DEFAULT false;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN harvesting_details text DEFAULT null;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN harvesting_status  character varying(255) DEFAULT null;