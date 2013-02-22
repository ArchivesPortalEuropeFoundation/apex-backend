ALTER TABLE ONLY warnings DROP CONSTRAINT warnings_fa_id_fkey;
ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id)  ON DELETE CASCADE;

ALTER TABLE ONLY warnings DROP CONSTRAINT warnings_hg_id_fkey;
ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id)  ON DELETE CASCADE;

ALTER TABLE ONLY warnings DROP CONSTRAINT warnings_sg_id_fkey;
ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id)  ON DELETE CASCADE;