CREATE INDEX finding_aid_path
  ON finding_aid
  USING btree (path_apenetead)

CREATE INDEX holdings_guide_path
  ON holdings_guide
  USING btree (path_apenetead)

CREATE INDEX source_guide_path
  ON source_guide
  USING btree (path_apenetead)
