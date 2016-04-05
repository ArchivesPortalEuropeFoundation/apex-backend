ALTER TABLE archival_institution ADD COLUMN "openDataEnabled" boolean DEFAULT false;
ALTER TABLE archival_institution ADD COLUMN totalsolrdocsforopendata bigint;
ALTER TABLE archival_institution ADD COLUMN unprocessedsolrdocs bigint;

