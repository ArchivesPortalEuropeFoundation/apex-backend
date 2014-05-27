INSERT INTO queue(fa_id, queue_date, priority, action)
SELECT fa_id, NOW(), 1000, 'REPUBLISH' from ead_content where fa_id is not NULL  AND id IN (
SELECT ec_id
FROM c_level  WHERE unitid is not null AND unitid != '' 
GROUP BY unitid,ec_id
HAVING COUNT(*) > 1)  

INSERT INTO queue(hg_id, queue_date, priority, action)
SELECT hg_id, NOW(), 1000, 'REPUBLISH' from ead_content where hg_id is not NULL  AND id IN (
SELECT ec_id
FROM c_level  WHERE unitid is not null AND unitid != '' 
GROUP BY unitid,ec_id
HAVING COUNT(*) > 1)

INSERT INTO queue(sg_id, queue_date, priority, action)
SELECT sg_id, NOW(), 1000, 'REPUBLISH' from ead_content where sg_id is not NULL  AND id IN (
SELECT ec_id
FROM c_level  WHERE unitid is not null AND unitid != '' 
GROUP BY unitid,ec_id
HAVING COUNT(*) > 1)