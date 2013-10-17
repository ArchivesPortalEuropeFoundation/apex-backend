ALTER TABLE archival_institution_oai_pmh ALTER COLUMN last_harvesting TYPE timestamp without time zone;
ALTER TABLE archival_institution_oai_pmh DROP COLUMN next_harvesting;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN interval_harvesting bigint NOT NULL;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN enabled boolean;