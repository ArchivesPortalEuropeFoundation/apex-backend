ALTER TABLE finding_aid ADD COLUMN dynamic boolean DEFAULT false;
ALTER TABLE source_guide ADD COLUMN dynamic boolean DEFAULT false;
ALTER TABLE holdings_guide ADD COLUMN dynamic boolean DEFAULT false;
CREATE INDEX holdings_guide_dynamic ON holdings_guide (dynamic );
CREATE INDEX source_guide_dynamic ON source_guide (dynamic );
CREATE INDEX finding_aid_dynamic ON finding_aid (dynamic );