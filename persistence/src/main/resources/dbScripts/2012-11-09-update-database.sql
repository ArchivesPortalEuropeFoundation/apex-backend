ALTER TABLE finding_aid DROP COLUMN fs_id;
ALTER TABLE holdings_guide DROP COLUMN fs_id;
ALTER TABLE source_guide DROP COLUMN fs_id;

DROP TABLE IF EXISTS file_state;
ALTER TABLE queue ADD COLUMN uf_id integer default NULL;
