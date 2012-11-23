
UPDATE archival_institution SET contain_searchable_items=true WHERE id IN (SELECT DISTINCT(ai_id) FROM finding_aid WHERE published=true);
UPDATE archival_institution SET contain_searchable_items=true WHERE id IN (SELECT DISTINCT(ai_id) FROM holdings_guide WHERE published=true);
UPDATE archival_institution SET contain_searchable_items=true WHERE id IN (SELECT DISTINCT(ai_id) FROM source_guide WHERE published=true);

UPDATE archival_institution SET contain_searchable_items=true WHERE isgroup = true AND contain_searchable_items = false AND id IN (SELECT parent_ai_id FROM archival_institution WHERE  contain_searchable_items = true)
UPDATE archival_institution SET contain_searchable_items=true WHERE isgroup = true AND contain_searchable_items = false AND id IN (SELECT parent_ai_id FROM archival_institution WHERE  contain_searchable_items = true)
UPDATE archival_institution SET contain_searchable_items=true WHERE isgroup = true AND contain_searchable_items = false AND id IN (SELECT parent_ai_id FROM archival_institution WHERE  contain_searchable_items = true)