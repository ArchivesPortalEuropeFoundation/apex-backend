ALTER TABLE finding_aid ADD COLUMN publish_date timestamp without time zone NULL;
ALTER TABLE source_guide ADD COLUMN publish_date timestamp without time zone NULL;
ALTER TABLE holdings_guide ADD COLUMN publish_date timestamp without time zone NULL;
UPDATE finding_aid SET  publish_date=NOW() WHERE published = true;
UPDATE source_guide SET  publish_date=NOW() WHERE published = true;
UPDATE holdings_guide SET  publish_date=NOW() WHERE published = true;