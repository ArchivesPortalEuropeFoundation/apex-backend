ALTER TABLE finding_aid DROP CONSTRAINT finding_aid_thu_s_id_fkey;
ALTER TABLE finding_aid DROP COLUMN iscaching;
ALTER TABLE finding_aid DROP COLUMN path_html;
ALTER TABLE finding_aid DROP COLUMN thu_s_id;
ALTER TABLE finding_aid DROP COLUMN number_cached_thumbnails;
ALTER TABLE finding_aid DROP COLUMN number_clicks;
DROP TABLE IF EXISTS nu_operation;
DROP TABLE IF EXISTS p_operation;
DROP TABLE IF EXISTS operation_type;
DROP TABLE IF EXISTS section;
DROP TABLE IF EXISTS recommended;
DROP TABLE IF EXISTS thumbnails;
DROP TABLE IF EXISTS thumbnails_state;

ALTER TABLE archival_institution ADD COLUMN country_id integer DEFAULT NULL;
UPDATE archival_institution SET country_id = partner.cou_id FROM partner WHERE archival_institution.p_id = partner.p_id;

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_cou_id_fkey FOREIGN KEY (country_id) REFERENCES country(cou_id);
ALTER TABLE archival_institution ALTER COLUMN country_id SET NOT NULL;    

ALTER TABLE partner ALTER COLUMN cou_id DROP  NOT NULL;
ALTER TABLE partner ALTER COLUMN cou_id SET DEFAULT NULL;

CREATE SEQUENCE partner_p_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

ALTER SEQUENCE partner_p_id_seq OWNED BY partner.p_id;
ALTER TABLE partner 
    ALTER COLUMN p_id 
        SET DEFAULT NEXTVAL('partner_p_id_seq');
        
REVOKE ALL ON SEQUENCE partner_p_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE partner_p_id_seq FROM admin;
GRANT ALL ON SEQUENCE partner_p_id_seq TO admin;
GRANT ALL ON SEQUENCE partner_p_id_seq TO apenet_dashboard;


ALTER TABLE archival_institution ALTER COLUMN p_id DROP NOT NULL;
ALTER TABLE archival_institution ALTER COLUMN p_id SET DEFAULT NULL;

UPDATE archival_institution SET p_id = NULL FROM partner WHERE archival_institution.p_id = partner.p_id AND partner.role_id = 1;

CREATE TABLE dpt_update (id bigint NOT NULL, version character varying(255));
ALTER TABLE ONLY dpt_update ADD CONSTRAINT dpt_update_pkey PRIMARY KEY (id);
CREATE SEQUENCE dpt_update_id_seq START WITH 1 INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1;
ALTER SEQUENCE dpt_update_id_seq OWNED BY dpt_update.id;
REVOKE ALL ON TABLE dpt_update FROM PUBLIC;
REVOKE ALL ON TABLE dpt_update FROM admin;
GRANT ALL ON TABLE dpt_update TO admin;
GRANT ALL ON TABLE dpt_update TO apenet_dashboard;
GRANT SELECT ON TABLE dpt_update TO apenet_portal;
REVOKE ALL ON SEQUENCE dpt_update_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE dpt_update_id_seq FROM admin;
GRANT ALL ON SEQUENCE dpt_update_id_seq TO admin;
GRANT ALL ON SEQUENCE dpt_update_id_seq TO apenet_dashboard;

ALTER TABLE sent_mail_register DROP COLUMN u_id;
ALTER TABLE sent_mail_register DROP COLUMN tur_id;
DROP TABLE IF EXISTS temporal_user_register;
ALTER TABLE partner DROP COLUMN nick;
ALTER TABLE partner RENAME COLUMN pwd TO password;
ALTER TABLE partner RENAME COLUMN p_name TO first_name;
ALTER TABLE partner RENAME COLUMN p_surname TO last_name;
ALTER TABLE partner RENAME COLUMN p_id TO id;
ALTER TABLE partner RENAME COLUMN cou_id TO country_id;
ALTER TABLE partner RENAME COLUMN us_id TO user_state_id;
ALTER TABLE partner ADD CONSTRAINT partner_email_address_unique UNIQUE (email_address);
ALTER TABLE partner RENAME TO "user";
ALTER TABLE role_type RENAME TO user_role;
ALTER TABLE user_role RENAME COLUMN role_id TO id;
ALTER TABLE user_role RENAME COLUMN roletype TO role;
ALTER TABLE "user" ALTER COLUMN email_address TYPE character varying(256);
ALTER SEQUENCE partner_p_id_seq RENAME TO user_id_seq;
ALTER TABLE archival_institution RENAME COLUMN p_id TO user_id;
ALTER TABLE sent_mail_register RENAME COLUMN p_id TO user_id;
ALTER TABLE ONLY sent_mail_register DROP CONSTRAINT sent_mail_register_p_id_fkey;
ALTER TABLE ONLY sent_mail_register
    ADD CONSTRAINT sent_mail_register_p_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id)  ON DELETE CASCADE;
ALTER TABLE "user" RENAME COLUMN role_id TO user_role_id;

ALTER TABLE ONLY cou_alternative_name DROP CONSTRAINT country_cou_id_fkey;
ALTER TABLE ONLY cou_alternative_name
    ADD CONSTRAINT country_cou_id_fkey FOREIGN KEY (cou_id) REFERENCES country(cou_id) ON DELETE CASCADE;
