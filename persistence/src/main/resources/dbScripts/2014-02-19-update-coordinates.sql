ALTER TABLE coordinates DROP COLUMN address;
ALTER TABLE coordinates ADD COLUMN street character varying(255) NOT NULL;
ALTER TABLE coordinates ADD COLUMN postalcity character varying(255) NOT NULL;
ALTER TABLE coordinates ADD COLUMN country character varying(255) NOT NULL;
