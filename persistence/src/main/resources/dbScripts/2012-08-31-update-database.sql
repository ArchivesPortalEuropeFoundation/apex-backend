ALTER TABLE finding_aid ADD COLUMN searchable boolean DEFAULT false;
ALTER TABLE holdings_guide ADD COLUMN searchable boolean DEFAULT false;
ALTER TABLE source_guide ADD COLUMN searchable boolean DEFAULT false;
UPDATE finding_aid SET searchable = true FROM file_state WHERE finding_aid.fs_id = file_state.id AND file_state.id >= 8 and file_state.id <= 14;
UPDATE holdings_guide SET searchable = true FROM file_state WHERE holdings_guide.fs_id = file_state.id AND file_state.id >= 8 and file_state.id <= 14;
UPDATE source_guide SET searchable = true FROM file_state WHERE source_guide.fs_id = file_state.id AND file_state.id >= 8 and file_state.id <= 14;
ALTER TABLE archival_institution ADD COLUMN contain_searchable_items boolean DEFAULT false;