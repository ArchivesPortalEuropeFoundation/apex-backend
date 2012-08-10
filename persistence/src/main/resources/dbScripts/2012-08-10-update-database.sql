ALTER TABLE "user" RENAME TO dashboard_user;
ALTER TABLE finding_aid RENAME COLUMN fa_title TO title;
ALTER TABLE finding_aid RENAME COLUMN fa_eadid TO eadid;
ALTER TABLE holdings_guide RENAME COLUMN hg_tittle TO title;
ALTER TABLE holdings_guide RENAME COLUMN hg_eadid TO eadid;