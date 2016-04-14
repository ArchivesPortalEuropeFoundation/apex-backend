ALTER TABLE archival_institution ADD COLUMN "openDataEnabled" boolean DEFAULT false;
ALTER TABLE archival_institution ADD COLUMN totalsolrdocsforopendata bigint;
ALTER TABLE archival_institution ADD COLUMN unprocessedsolrdocs bigint;

CREATE SEQUENCE pk_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE pk_seq
  OWNER TO apenet_dashboard;
