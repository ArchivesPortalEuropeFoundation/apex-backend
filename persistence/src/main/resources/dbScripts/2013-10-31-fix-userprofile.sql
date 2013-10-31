ALTER TABLE userprofile DROP COLUMN europeana_license;
ALTER TABLE userprofile DROP COLUMN europeana_rights;
ALTER TABLE userprofile ADD COLUMN europeana_license character varying(60);
ALTER TABLE userprofile ADD COLUMN europeana_license_details character varying(60);
ALTER TABLE userprofile ADD COLUMN europeana_dao_type integer;
ALTER TABLE userprofile ADD COLUMN europeana_dao_type_from_file boolean DEFAULT true;