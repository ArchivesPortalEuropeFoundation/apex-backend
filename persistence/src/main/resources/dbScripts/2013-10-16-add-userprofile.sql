CREATE TABLE userprofile
(
	id bigserial NOT NULL,
	user_id integer NOT NULL,
	name_profile character varying(50) NOT NULL,
	file_type integer NOT NULL DEFAULT 1,
	upload_action integer NOT NULL DEFAULT 1,
	exist_action integer NOT NULL DEFAULT 1,
	dao_type integer NOT NULL DEFAULT 6,
	europeana_dp character varying(100),
	europeana_dp_from_file boolean DEFAULT true,
	europeana_languages character varying(60),
	europeana_languages_from_file boolean DEFAULT true,
	europeana_license integer DEFAULT 3,
	europeana_rights integer,
	europeana_add_rights character varying(200),
	europeana_hp character varying(20) DEFAULT 'Context information',
	europeana_inh_elements boolean DEFAULT false,
	europeana_inh_origin boolean DEFAULT false,
	CONSTRAINT userprofile_pkey PRIMARY KEY (id),
	CONSTRAINT userprofile_user_id_fkey FOREIGN KEY (user_id)
      REFERENCES dashboard_user (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE
);
ALTER TABLE userprofile OWNER TO postgres;
GRANT ALL ON TABLE userprofile TO postgres;
GRANT ALL ON TABLE userprofile TO admin;
GRANT ALL ON TABLE userprofile TO apenet_dashboard;