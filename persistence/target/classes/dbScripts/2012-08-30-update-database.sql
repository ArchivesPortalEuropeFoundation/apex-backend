DROP TABLE item CASCADE;
DROP TABLE level_type CASCADE;
DROP TABLE searches CASCADE;
DROP TABLE normal_user CASCADE;

ALTER TABLE dashboard_user DROP CONSTRAINT partner_us_id_fkey;
ALTER TABLE dashboard_user DROP COLUMN user_state_id;
ALTER TABLE dashboard_user ADD COLUMN is_active boolean DEFAULT true;
ALTER TABLE dashboard_user ALTER COLUMN is_active SET NOT NULL;

DROP TABLE user_state CASCADE;

ALTER TABLE up_file DROP COLUMN ufs_id;
DROP TABLE up_file_state CASCADE;