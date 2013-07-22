CREATE INDEX source_guide__archival_institution_idx ON source_guide USING btree (ai_id );
CREATE INDEX holdings_guide_searchable ON holdings_guide (searchable );
CREATE INDEX source_guide_searchable ON source_guide (searchable );
CREATE INDEX finding_aid_searchable ON finding_aid (searchable );
CREATE INDEX finding_aid_eadid ON finding_aid (eadid );