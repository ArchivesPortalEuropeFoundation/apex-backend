DROP TABLE level_type;
DROP TABLE item;
DROP TABLE searches;
DROP TABLE normal_user;

ALTER TABLE dashboard_user DROP CONSTRAINT partner_us_id_fkey;
ALTER TABLE dashboard_user DROP COLUMN user_state_id;
ALTER TABLE dashboard_user ADD COLUMN is_active boolean DEFAULT true;

DROP TABLE user_state;