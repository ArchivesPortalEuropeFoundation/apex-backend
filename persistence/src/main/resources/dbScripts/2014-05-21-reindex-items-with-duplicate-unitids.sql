INSERT INTO queue(fa_id, queue_date, priority, action)
SELECT fa_id, NOW(), 1000, 'REPUBLISH' from ead_content where fa_id is not NULL AND id IN (
SELECT DISTINCT (c_level.ec_id)
FROM 
  c_level, 
  ead_content,
  finding_aid
WHERE 
  c_level.ec_id = ead_content.id AND c_level.unitid is not null AND c_level.duplicate_unitid = false AND c_level.unitid != '' AND ead_content.fa_id = finding_aid.id  AND finding_aid.published=true 
GROUP BY c_level.unitid,c_level.ec_id
HAVING COUNT(*) > 1)

INSERT INTO queue(hg_id, queue_date, priority, action)
SELECT hg_id, NOW(), 1000, 'REPUBLISH' from ead_content where hg_id is not NULL AND id IN (
SELECT DISTINCT (c_level.ec_id)
FROM 
  c_level, 
  ead_content,
  holdings_guide
WHERE 
  c_level.ec_id = ead_content.id AND c_level.unitid is not null AND c_level.duplicate_unitid = false AND c_level.unitid != '' AND ead_content.hg_id = holdings_guide.id  AND holdings_guide.published=true 
GROUP BY c_level.unitid,c_level.ec_id
HAVING COUNT(*) > 1)

INSERT INTO queue(sg_id, queue_date, priority, action)
SELECT sg_id, NOW(), 1000, 'REPUBLISH' from ead_content where sg_id is not NULL AND id IN (
SELECT DISTINCT (c_level.ec_id)
FROM 
  c_level, 
  ead_content,
  source_guide
WHERE 
  c_level.ec_id = ead_content.id AND c_level.unitid is not null AND c_level.duplicate_unitid = false AND c_level.unitid != '' AND ead_content.sg_id = source_guide.id  AND source_guide.published=true 
GROUP BY c_level.unitid,c_level.ec_id
HAVING COUNT(*) > 1)