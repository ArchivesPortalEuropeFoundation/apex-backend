ALTER TABLE userprofile ADD COLUMN dao_type_from_file boolean;
ALTER TABLE userprofile ALTER COLUMN dao_type_from_file SET NOT NULL;
ALTER TABLE userprofile ALTER COLUMN dao_type_from_file SET DEFAULT true;