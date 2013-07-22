ALTER TABLE finding_aid ADD COLUMN converted boolean DEFAULT false;
ALTER TABLE finding_aid ADD COLUMN validated smallint DEFAULT 1;
ALTER TABLE finding_aid ADD COLUMN europeana smallint DEFAULT 0;


ALTER TABLE holdings_guide ADD COLUMN converted boolean DEFAULT false;
ALTER TABLE holdings_guide ADD COLUMN validated smallint DEFAULT 1;

ALTER TABLE source_guide ADD COLUMN converted boolean DEFAULT false;
ALTER TABLE source_guide ADD COLUMN validated smallint DEFAULT 1;


UPDATE finding_aid SET converted = true FROM file_state WHERE finding_aid.fs_id = file_state.id AND (file_state.id = 3 or file_state.id = 4 or file_state.id >= 7);
UPDATE holdings_guide SET converted = true FROM file_state WHERE holdings_guide.fs_id = file_state.id AND (file_state.id = 3 or file_state.id = 4 or file_state.id >= 7);
UPDATE source_guide SET converted = true FROM file_state WHERE source_guide.fs_id = file_state.id AND (file_state.id = 3 or file_state.id = 4 or file_state.id >= 7);

UPDATE finding_aid SET validated = 2 FROM file_state WHERE finding_aid.fs_id = file_state.id AND (file_state.id = 4 or file_state.id = 5 or file_state.id >= 7);
UPDATE holdings_guide SET validated = 2 FROM file_state WHERE holdings_guide.fs_id = file_state.id AND (file_state.id = 4 or file_state.id = 5 or file_state.id >= 7);
UPDATE source_guide SET validated = 2 FROM file_state WHERE source_guide.fs_id = file_state.id AND (file_state.id = 4 or file_state.id = 5 or file_state.id >= 7);

UPDATE finding_aid SET validated = 0 FROM file_state WHERE finding_aid.fs_id = file_state.id AND (file_state.id = 6);
UPDATE holdings_guide SET validated = 0 FROM file_state WHERE holdings_guide.fs_id = file_state.id AND (file_state.id = 6);
UPDATE source_guide SET validated = 0 FROM file_state WHERE source_guide.fs_id = file_state.id AND (file_state.id = 6);

UPDATE finding_aid SET europeana = 1 FROM file_state WHERE finding_aid.fs_id = file_state.id AND (file_state.id = 9);
UPDATE finding_aid SET europeana = 2 FROM file_state WHERE finding_aid.fs_id = file_state.id AND (file_state.id = 10);
UPDATE finding_aid SET europeana = 3 FROM file_state WHERE finding_aid.fs_id = file_state.id AND (file_state.id = 11);

ALTER TABLE finding_aid ADD COLUMN queuing smallint DEFAULT 0;
ALTER TABLE holdings_guide ADD COLUMN queuing smallint DEFAULT 0;
ALTER TABLE source_guide ADD COLUMN queuing smallint DEFAULT 0;

ALTER TABLE index_queue RENAME TO queue;
ALTER TABLE queue ADD COLUMN priority smallint NOT NULL;
ALTER TABLE queue ADD COLUMN action character varying(255) NOT NULL;
ALTER TABLE queue DROP COLUMN position;
ALTER TABLE queue DROP COLUMN fs_id;