CREATE TABLE ai_alternative_name
(
  ai_an_id integer NOT NULL,
  ai_a_name character varying(255) NOT NULL,
  ai_id integer NOT NULL,
  lng_id integer NOT NULL,
  primary_name boolean NOT NULL
);

CREATE TABLE archival_institution (
    ai_id integer NOT NULL,
    ainame character varying(255) NOT NULL,
    registration_date date,
    eag_path character varying(255),
    user_id integer default NULL,
    country_id integer NOT NULL,
    repositorycode character varying(20),
    autform character varying(255),
    isgroup boolean,
  	parent_ai_id integer,
  	alorder integer NOT NULL,
  	internal_al_id character varying(100) NOT NULL
);

CREATE TABLE archival_institution_oai_pmh (
    aio_id integer NOT NULL,
    oai_pmh_id integer NOT NULL,
    oai_pmh_url character varying(100) NOT NULL,
    last_harvesting date,
    ai_id integer NOT NULL
);

CREATE TABLE c_level (
    cl_id bigint NOT NULL,
  	ec_id bigint,
  	parent_cl_id bigint,
  	unittitle text,
  	unitid character varying(255),
  	level character varying(50),
  	order_id integer,
  	leaf boolean,
  	xml text,
  	href_eadid character varying(255)
);

CREATE TABLE cou_alternative_name (
  cou_an_id integer NOT NULL,
  cou_an_name character varying(255) NOT NULL,
  cou_id integer NOT NULL,
  lng_id integer NOT NULL
);

CREATE TABLE country (
    cou_id integer NOT NULL,
    cname character varying(120) NOT NULL,
    isoname character varying(2) NOT NULL,
    al_order integer
);

COMMENT ON COLUMN country.isoname IS 'iso3166-1';

CREATE TABLE cpf_content (
    id bigint NOT NULL,
  	ai_id bigint,
  	cpf_id character varying(255),
  	xml text
);

CREATE TABLE dpt_update (
    id bigint NOT NULL,
  	version character varying(255)
);

CREATE TABLE ead_content (
    ec_id bigint NOT NULL,
    fa_id bigint,
    hg_id bigint,
    sg_id bigint,
    xml text,
    titleproper text,
    eadid character varying(255),
    unittitle text,
	display_intro boolean  NOT NULL DEFAULT TRUE,
	display_did boolean  NOT NULL  DEFAULT TRUE,
    visible boolean
);

CREATE TABLE ese (
    ese_id integer NOT NULL,
    creation_date timestamp with time zone NOT NULL,
    modification_date timestamp with time zone,
    eset text NOT NULL,
    path character varying(255),
  	es_id integer NOT NULL,
  	mf_id integer NOT NULL,
  	fa_id integer,
    path_html character varying(200),
    number_of_records integer DEFAULT 0,
    oai_identifier character varying(255)
);

CREATE TABLE ese_state (
    es_id integer NOT NULL,
    state character varying(20) NOT NULL
);

CREATE TABLE file_state (
    fs_id integer NOT NULL,
    state character varying(35) NOT NULL
);

CREATE TABLE file_type (
    ft_id integer NOT NULL,
    ftype character varying(20) NOT NULL
);

CREATE TABLE finding_aid (
    fa_id integer NOT NULL,
    fa_title text NOT NULL,
    upload_date timestamp without time zone NOT NULL,
    path_apenetead character varying(255) NOT NULL,
    fs_id integer NOT NULL,
    um_id integer NOT NULL,
    fa_eadid character varying(255) NOT NULL,
    ai_id integer NOT NULL,
    totalnumberofdaos bigint,
    totalnumberofunits bigint,
    totalnumberofunitswithdao bigint
);

CREATE TABLE holdings_guide (
    hg_id integer NOT NULL,
    hg_tittle text NOT NULL,
    upload_date timestamp without time zone NOT NULL,
    path_apenetead character varying(255) NOT NULL,
    ai_id integer NOT NULL,
    fs_id integer NOT NULL,
    um_id integer NOT NULL,
    hg_eadid character varying(255),
    path_html character varying(100),
    totalnumberofdaos bigint,
    totalnumberofunits bigint,
    totalnumberofunitswithdao bigint
);


CREATE TABLE item (
    it_id integer NOT NULL,
    u_id bigint NOT NULL,
    label character varying(40) NOT NULL,
    lt_id integer NOT NULL
);

CREATE TABLE index_queue
(
  iq_id integer NOT NULL,
  fa_id integer,
  hg_id integer,
  sg_id integer,
  position integer,
  queue_date timestamp without time zone NOT NULL,
  fs_id integer NOT NULL,
  errors text
);

CREATE TABLE lang (
    lng_id integer NOT NULL,
    lname character varying(20) NOT NULL,
    isoname character varying(3) NOT NULL,
    iso2name character varying(2) NOT NULL,
    lnativename character varying(20) NOT NULL
);

COMMENT ON COLUMN lang.isoname IS 'iso639-2';

CREATE TABLE level_type (
    lt_id integer NOT NULL,
    ltype character varying(40) NOT NULL
);

CREATE TABLE metadata_format (
    mf_id integer NOT NULL,
    format character varying(20) NOT NULL
);

CREATE TABLE normal_user (
    u_id bigint NOT NULL,
    us_id bigint NOT NULL,
    nick character varying(60) NOT NULL,
    email_address character varying(100) NOT NULL,
    pwd character varying(60) NOT NULL,
    secret_question character varying(200),
    secret_answer character varying(200)
);

CREATE TABLE "user" (
    id integer NOT NULL,
    user_state_id integer NOT NULL,
    email_address character varying(256) UNIQUE NOT NULL,
    password character varying(60) NOT NULL,
    country_id integer default NULL,
    secret_question character varying(200),
    secret_answer character varying(200),
    user_role_id integer NOT NULL,
    first_name character varying(60) NOT NULL,
    last_name character varying(100) NOT NULL
);


CREATE TABLE resumption_token
(
  rt_id integer NOT NULL,
  expiration_date timestamp with time zone NOT NULL,
  from_date timestamp with time zone NOT NULL,
  until_date timestamp with time zone NOT NULL,
  "set" character varying(100),
  last_record_harvested character varying(100) NOT NULL,
  mf_id integer
 );
 
CREATE TABLE user_role
(
  id integer NOT NULL,
  role character varying(40) NOT NULL
);

CREATE TABLE searches (
    sea_id bigint NOT NULL,
    search_string character varying(100) NOT NULL,
    label character varying(40),
    u_id bigint NOT NULL,
    date timestamp with time zone NOT NULL,
    view character varying(10) NOT NULL,
    navtreelang character varying(3),
  	resultsperpage integer,
  	searchbutton character varying(20),
  	element character varying(5),
  	fromdate character varying(30),
  	todate character varying(30),
  	typedocument character varying,
  	dao character varying(30),
  	method character varying(30),
  	navigationtreenodesselected text,
  	expandednodes text,
  	resultsorder character varying(128),
  	startdate character varying(128),
  	enddate character varying(128),
  	ai character varying(30),
  	type character varying(5),
  	datetype character varying(30),
  	country character varying(30),
  	pagenumber character varying(30)
);


CREATE TABLE sent_mail_register (
    smr_id integer NOT NULL,
    validation_link character varying(100) NOT NULL,
    date date NOT NULL,
    email_address character varying(100) NOT NULL,
    user_id integer,
    ai_id integer
);

CREATE TABLE source_guide (
    sg_id int4 not null,
    fs_id int4 not null,
    ai_id int4 not null,
    um_id int4 not null,
    title text not null,
    upload_date timestamp not null,
    path_apenetead varchar(255) not null,
    eadid varchar(255),
    totalnumberofdaos int8,
    totalnumberofunits int8,
    totalnumberofunitswithdao int8
);



CREATE TABLE up_file (
    uf_id integer NOT NULL,
    path character varying(255) NOT NULL,
    um_id integer NOT NULL,
    ufs_id integer NOT NULL,
    ai_id integer NOT NULL,
    ft_id integer NOT NULL,
    fname character varying(255) NOT NULL
);

CREATE TABLE up_file_state (
    ufs_id integer NOT NULL,
    state character varying(20) NOT NULL
);

CREATE TABLE upload_method (
    um_id integer NOT NULL,
    method character varying(20) NOT NULL
);

CREATE TABLE user_state (
    id integer NOT NULL,
    state character varying(20)
);

CREATE TABLE warnings (
    war_id integer NOT NULL,
    abstract text NOT NULL,
    hg_id integer,
    fa_id integer,
    sg_id integer,
    iswarning boolean
);

CREATE SEQUENCE user_id_seq
    START WITH 100
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

ALTER SEQUENCE user_id_seq OWNED BY "user".id;
ALTER TABLE "user"
    ALTER COLUMN id
        SET DEFAULT NEXTVAL('user_id_seq');

CREATE SEQUENCE c_level_cl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

ALTER SEQUENCE c_level_cl_id_seq OWNED BY c_level.cl_id;

CREATE SEQUENCE cpf_content_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

ALTER SEQUENCE cpf_content_id_seq OWNED BY cpf_content.id;

CREATE SEQUENCE dpt_update_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

ALTER SEQUENCE dpt_update_id_seq OWNED BY dpt_update.id;

CREATE SEQUENCE ead_content_ec_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

ALTER SEQUENCE ead_content_ec_id_seq OWNED BY ead_content.ec_id;

CREATE SEQUENCE normal_user_u_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

ALTER SEQUENCE normal_user_u_id_seq OWNED BY normal_user.u_id;


CREATE SEQUENCE searches_sea_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
  CACHE 1;

ALTER SEQUENCE searches_sea_id_seq OWNED BY searches.sea_id;
    

CREATE SEQUENCE source_guide_sg_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

ALTER SEQUENCE source_guide_sg_id_seq OWNED BY source_guide.sg_id;

ALTER TABLE ONLY ai_alternative_name 
    ADD CONSTRAINT alternative_name_pkey PRIMARY KEY (ai_an_id);

ALTER TABLE ONLY archival_institution_oai_pmh
    ADD CONSTRAINT archival_institution_oai_pmh_pkey PRIMARY KEY (aio_id);

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_pkey PRIMARY KEY (ai_id);

ALTER TABLE ONLY c_level
    ADD CONSTRAINT c_level_pkey PRIMARY KEY (cl_id);

ALTER TABLE ONLY country
    ADD CONSTRAINT country_pkey PRIMARY KEY (cou_id);

ALTER TABLE ONLY cou_alternative_name
    ADD CONSTRAINT cou_alternative_name_pkey PRIMARY KEY (cou_an_id);

ALTER TABLE ONLY cpf_content
    ADD CONSTRAINT cpf_content_pkey PRIMARY KEY (id);

ALTER TABLE ONLY dpt_update
    ADD CONSTRAINT dpt_update_pkey PRIMARY KEY (id);

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_pkey PRIMARY KEY (ec_id);

ALTER TABLE ONLY ese
    ADD CONSTRAINT ese_pkey PRIMARY KEY (ese_id);

ALTER TABLE ONLY ese_state
    ADD CONSTRAINT ese_state_pkey PRIMARY KEY (es_id);

ALTER TABLE ONLY file_state
    ADD CONSTRAINT file_state_pkey PRIMARY KEY (fs_id);

ALTER TABLE ONLY file_type
    ADD CONSTRAINT file_type_pkey PRIMARY KEY (ft_id);

ALTER TABLE ONLY finding_aid
    ADD CONSTRAINT finding_aid_pkey PRIMARY KEY (fa_id);

ALTER TABLE ONLY holdings_guide
    ADD CONSTRAINT holdings_guide_pkey PRIMARY KEY (hg_id);

ALTER TABLE ONLY item
    ADD CONSTRAINT item_pkey PRIMARY KEY (it_id, u_id);
    
ALTER TABLE ONLY index_queue
  	ADD CONSTRAINT index_queue_pkey PRIMARY KEY (iq_id);

ALTER TABLE ONLY lang
    ADD CONSTRAINT lang_pkey PRIMARY KEY (lng_id);

ALTER TABLE ONLY level_type
    ADD CONSTRAINT level_type_pkey PRIMARY KEY (lt_id);

ALTER TABLE ONLY metadata_format
    ADD CONSTRAINT metadata_format_pkey PRIMARY KEY (mf_id);

ALTER TABLE ONLY normal_user
    ADD CONSTRAINT normal_user_pkey PRIMARY KEY (u_id);

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);

    
ALTER TABLE ONLY resumption_token
    ADD CONSTRAINT resumption_token_pkey PRIMARY KEY (rt_id);

ALTER TABLE ONLY user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (id);

ALTER TABLE ONLY searches
    ADD CONSTRAINT searches_pkey PRIMARY KEY (sea_id);


ALTER TABLE ONLY sent_mail_register
    ADD CONSTRAINT sent_mail_register_pkey PRIMARY KEY (smr_id);

ALTER TABLE ONLY source_guide
    ADD CONSTRAINT source_guide_pkey PRIMARY KEY (sg_id);



ALTER TABLE ONLY up_file
    ADD CONSTRAINT up_file_pkey PRIMARY KEY (uf_id);

ALTER TABLE ONLY up_file_state
    ADD CONSTRAINT up_file_state_pkey PRIMARY KEY (ufs_id);

ALTER TABLE ONLY upload_method
    ADD CONSTRAINT upload_method_pkey PRIMARY KEY (um_id);

ALTER TABLE ONLY user_state
    ADD CONSTRAINT user_state_pkey PRIMARY KEY (id);

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_pkey PRIMARY KEY (war_id);

CREATE INDEX c_level__ead_content_idx ON c_level USING btree (ec_id);

CREATE INDEX c_level__order_idx ON c_level USING btree (order_id);

CREATE INDEX c_level__parent_cl_idx ON c_level USING btree (parent_cl_id);

CREATE INDEX c_level__top_c_levels_idx ON c_level USING btree (ec_id) WHERE parent_cl_id IS NULL;

CREATE INDEX c_level__href_eadid_idx ON c_level USING btree (href_eadid);

CREATE INDEX ead_content__finding_aid_idx ON ead_content USING btree (fa_id);

CREATE INDEX ead_content__holdings_guide_idx ON ead_content USING btree (hg_id);

CREATE INDEX finding_aid__archival_institution_idx ON finding_aid USING btree (ai_id);

CREATE INDEX holdings_guide__archival_institution_idx ON holdings_guide USING btree (ai_id);

CREATE INDEX cpf_content__cpf_id_idx ON cpf_content USING btree (cpf_id);

CREATE INDEX searches_u_id_idx ON searches USING btree (u_id);

ALTER TABLE ONLY ai_alternative_name
    ADD CONSTRAINT archival_institution_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(ai_id);

ALTER TABLE ONLY ai_alternative_name
    ADD CONSTRAINT lang_lng_id_fkey FOREIGN KEY (lng_id) REFERENCES lang(lng_id);

ALTER TABLE ONLY archival_institution_oai_pmh
    ADD CONSTRAINT archival_institution_oai_pmh_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(ai_id);

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_parent_ai_id_fkey FOREIGN KEY (parent_ai_id) REFERENCES archival_institution (ai_id);

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_cou_id_fkey FOREIGN KEY (country_id) REFERENCES country(cou_id);
    
ALTER TABLE ONLY c_level
    ADD CONSTRAINT c_level_ec_id_fkey FOREIGN KEY (ec_id) REFERENCES ead_content(ec_id) ON DELETE CASCADE;

ALTER TABLE ONLY c_level
    ADD CONSTRAINT c_level_parent_cl_id_fkey FOREIGN KEY (parent_cl_id) REFERENCES c_level(cl_id) ON DELETE CASCADE;

ALTER TABLE ONLY cou_alternative_name
    ADD CONSTRAINT country_cou_id_fkey FOREIGN KEY (cou_id) REFERENCES country(cou_id) ON DELETE CASCADE;

ALTER TABLE ONLY cou_alternative_name
    ADD CONSTRAINT lng_id FOREIGN KEY (lng_id) REFERENCES lang(lng_id);

ALTER TABLE ONLY cpf_content
    ADD CONSTRAINT cpf_content_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(ai_id);

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(fa_id) ON DELETE CASCADE;

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(hg_id) ON DELETE CASCADE;

ALTER TABLE ead_content
    ADD CONSTRAINT ead_content_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(sg_id) ON DELETE CASCADE;

ALTER TABLE ONLY ese
    ADD CONSTRAINT ese_es_id_fkey FOREIGN KEY (es_id) REFERENCES ese_state(es_id);

ALTER TABLE ONLY ese
    ADD CONSTRAINT ese_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(fa_id);

ALTER TABLE ONLY ese
    ADD CONSTRAINT ese_mf_id_fkey FOREIGN KEY (mf_id) REFERENCES metadata_format(mf_id);

ALTER TABLE ONLY up_file
    ADD CONSTRAINT file_type_ft_id_fkey FOREIGN KEY (ft_id) REFERENCES file_type(ft_id);

ALTER TABLE ONLY finding_aid
    ADD CONSTRAINT finding_aid_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(ai_id);

ALTER TABLE ONLY finding_aid
    ADD CONSTRAINT finding_aid_fs_id_fkey FOREIGN KEY (fs_id) REFERENCES file_state(fs_id);


ALTER TABLE ONLY finding_aid
    ADD CONSTRAINT finding_aid_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(um_id);

ALTER TABLE ONLY holdings_guide
    ADD CONSTRAINT holdings_guide_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(ai_id);

ALTER TABLE ONLY holdings_guide
    ADD CONSTRAINT holdings_guide_fs_id_fkey FOREIGN KEY (fs_id) REFERENCES file_state(fs_id);

ALTER TABLE ONLY holdings_guide
    ADD CONSTRAINT holdings_guide_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(um_id);

ALTER TABLE ONLY item
    ADD CONSTRAINT item_lt_id_fkey FOREIGN KEY (lt_id) REFERENCES level_type(lt_id);

ALTER TABLE ONLY item
    ADD CONSTRAINT item_u_id_fkey FOREIGN KEY (u_id) REFERENCES normal_user(u_id);
    
ALTER TABLE ONLY index_queue
  	ADD CONSTRAINT index_queue_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid (fa_id) ON DELETE CASCADE;
  	
ALTER TABLE ONLY index_queue
  	ADD CONSTRAINT index_queue_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide (hg_id) ON DELETE CASCADE;
  	
ALTER TABLE ONLY index_queue
  	ADD CONSTRAINT index_queue_fs_id_fkey FOREIGN KEY (fs_id) REFERENCES file_state (fs_id);

ALTER TABLE index_queue
    ADD CONSTRAINT index_queue_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(sg_id);

ALTER TABLE ONLY normal_user
    ADD CONSTRAINT normal_user_us_id_fkey FOREIGN KEY (us_id) REFERENCES user_state(id);



ALTER TABLE ONLY resumption_token
	ADD CONSTRAINT resumption_token_mf_id_fkey FOREIGN KEY (mf_id) REFERENCES metadata_format (mf_id);
    
ALTER TABLE ONLY searches
    ADD CONSTRAINT searches_u_id_fkey FOREIGN KEY (u_id) REFERENCES normal_user(u_id);

ALTER TABLE ONLY sent_mail_register
    ADD CONSTRAINT sent_mail_register_p_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id)  ON DELETE CASCADE;

ALTER TABLE ONLY sent_mail_register
    ADD CONSTRAINT sent_mail_register_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution (ai_id);

ALTER TABLE source_guide
    ADD CONSTRAINT source_guide_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(ai_id);

ALTER TABLE source_guide
    ADD CONSTRAINT source_guide_fs_id_fkey FOREIGN KEY (fs_id) REFERENCES file_state(fs_id);

ALTER TABLE source_guide
    ADD CONSTRAINT source_guide_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(um_id);

ALTER TABLE ONLY up_file
    ADD CONSTRAINT up_file_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(ai_id);

ALTER TABLE ONLY up_file
    ADD CONSTRAINT up_file_ufs_id_fkey FOREIGN KEY (ufs_id) REFERENCES up_file_state(ufs_id);

ALTER TABLE ONLY up_file
    ADD CONSTRAINT up_file_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(um_id);

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_country_id_fkey FOREIGN KEY (country_id) REFERENCES country(cou_id);

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_user_state_id_fkey FOREIGN KEY (user_state_id) REFERENCES user_state(id);

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_user_role_id_fkey FOREIGN KEY (user_role_id) REFERENCES user_role (id);
    
    
ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(fa_id);

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(hg_id);

ALTER TABLE warnings
    ADD CONSTRAINT warnings_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(sg_id);

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;

REVOKE ALL ON TABLE ai_alternative_name FROM PUBLIC;
REVOKE ALL ON TABLE ai_alternative_name FROM admin;
GRANT ALL ON TABLE ai_alternative_name TO admin;
GRANT ALL ON TABLE ai_alternative_name TO apenet_dashboard;
GRANT SELECT ON TABLE ai_alternative_name TO apenet_portal;

REVOKE ALL ON TABLE archival_institution FROM PUBLIC;
REVOKE ALL ON TABLE archival_institution FROM admin;
GRANT ALL ON TABLE archival_institution TO admin;
GRANT ALL ON TABLE archival_institution TO apenet_dashboard;
GRANT SELECT ON TABLE archival_institution TO apenet_portal;

REVOKE ALL ON TABLE archival_institution_oai_pmh FROM PUBLIC;
REVOKE ALL ON TABLE archival_institution_oai_pmh FROM admin;
GRANT ALL ON TABLE archival_institution_oai_pmh TO admin;
GRANT ALL ON TABLE archival_institution_oai_pmh TO apenet_dashboard;

REVOKE ALL ON TABLE c_level FROM PUBLIC;
REVOKE ALL ON TABLE c_level FROM admin;
GRANT ALL ON TABLE c_level TO admin;
GRANT ALL ON TABLE c_level TO apenet_dashboard;
GRANT SELECT ON TABLE c_level TO apenet_portal;

REVOKE ALL ON SEQUENCE c_level_cl_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE c_level_cl_id_seq FROM admin;
GRANT ALL ON SEQUENCE c_level_cl_id_seq TO admin;
GRANT ALL ON SEQUENCE c_level_cl_id_seq TO apenet_dashboard;

REVOKE ALL ON TABLE cou_alternative_name FROM PUBLIC;
REVOKE ALL ON TABLE cou_alternative_name FROM admin;
GRANT ALL ON TABLE cou_alternative_name TO admin;
GRANT ALL ON TABLE cou_alternative_name TO apenet_dashboard;
GRANT SELECT ON TABLE cou_alternative_name TO apenet_portal;

REVOKE ALL ON TABLE country FROM PUBLIC;
REVOKE ALL ON TABLE country FROM admin;
GRANT ALL ON TABLE country TO admin;
GRANT ALL ON TABLE country TO apenet_dashboard;
GRANT SELECT ON TABLE country TO apenet_portal;

REVOKE ALL ON TABLE cpf_content FROM PUBLIC;
REVOKE ALL ON TABLE cpf_content FROM admin;
GRANT ALL ON TABLE cpf_content TO admin;
GRANT ALL ON TABLE cpf_content TO apenet_dashboard;
GRANT SELECT ON TABLE cpf_content TO apenet_portal;

REVOKE ALL ON SEQUENCE cpf_content_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE cpf_content_id_seq FROM admin;
GRANT ALL ON SEQUENCE cpf_content_id_seq TO admin;
GRANT ALL ON SEQUENCE cpf_content_id_seq TO apenet_dashboard;

REVOKE ALL ON TABLE dpt_update FROM PUBLIC;
REVOKE ALL ON TABLE dpt_update FROM admin;
GRANT ALL ON TABLE dpt_update TO admin;
GRANT ALL ON TABLE dpt_update TO apenet_dashboard;
GRANT SELECT ON TABLE dpt_update TO apenet_portal;

REVOKE ALL ON SEQUENCE dpt_update_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE dpt_update_id_seq FROM admin;
GRANT ALL ON SEQUENCE dpt_update_id_seq TO admin;
GRANT ALL ON SEQUENCE dpt_update_id_seq TO apenet_dashboard;

REVOKE ALL ON TABLE ead_content FROM PUBLIC;
REVOKE ALL ON TABLE ead_content FROM admin;
GRANT ALL ON TABLE ead_content TO admin;
GRANT ALL ON TABLE ead_content TO apenet_dashboard;
GRANT SELECT ON TABLE ead_content TO apenet_portal;

REVOKE ALL ON SEQUENCE ead_content_ec_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ead_content_ec_id_seq FROM admin;
GRANT ALL ON SEQUENCE ead_content_ec_id_seq TO admin;
GRANT ALL ON SEQUENCE ead_content_ec_id_seq TO apenet_dashboard;

REVOKE ALL ON SEQUENCE normal_user_u_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE normal_user_u_id_seq FROM admin;
GRANT ALL ON TABLE normal_user_u_id_seq TO admin;
GRANT ALL ON TABLE normal_user_u_id_seq TO apenet_portal;

REVOKE ALL ON SEQUENCE searches_sea_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE searches_sea_id_seq FROM admin;
GRANT ALL ON TABLE searches_sea_id_seq TO admin;
GRANT ALL ON TABLE searches_sea_id_seq TO apenet_portal;


REVOKE ALL ON TABLE ese FROM PUBLIC;
REVOKE ALL ON TABLE ese FROM admin;
GRANT ALL ON TABLE ese TO admin;
GRANT ALL ON TABLE ese TO apenet_dashboard;

REVOKE ALL ON TABLE ese_state FROM PUBLIC;
REVOKE ALL ON TABLE ese_state FROM admin;
GRANT ALL ON TABLE ese_state TO admin;
GRANT ALL ON TABLE ese_state TO apenet_dashboard;

REVOKE ALL ON TABLE file_state FROM PUBLIC;
REVOKE ALL ON TABLE file_state FROM admin;
GRANT ALL ON TABLE file_state TO admin;
GRANT ALL ON TABLE file_state TO apenet_dashboard;
GRANT SELECT ON TABLE file_state TO apenet_portal;

REVOKE ALL ON TABLE file_type FROM PUBLIC;
REVOKE ALL ON TABLE file_type FROM admin;
GRANT ALL ON TABLE file_type TO admin;
GRANT ALL ON TABLE file_type TO apenet_dashboard;

REVOKE ALL ON TABLE finding_aid FROM PUBLIC;
REVOKE ALL ON TABLE finding_aid FROM admin;
GRANT ALL ON TABLE finding_aid TO admin;
GRANT ALL ON TABLE finding_aid TO apenet_dashboard;
GRANT SELECT ON TABLE finding_aid TO apenet_portal;

REVOKE ALL ON TABLE holdings_guide FROM PUBLIC;
REVOKE ALL ON TABLE holdings_guide FROM admin;
GRANT ALL ON TABLE holdings_guide TO admin;
GRANT ALL ON TABLE holdings_guide TO apenet_dashboard;
GRANT SELECT ON TABLE holdings_guide TO apenet_portal;

REVOKE ALL ON TABLE item FROM PUBLIC;
REVOKE ALL ON TABLE item FROM admin;
GRANT ALL ON TABLE item TO admin;
GRANT ALL ON TABLE item TO apenet_portal;

REVOKE ALL ON TABLE index_queue FROM PUBLIC;
REVOKE ALL ON TABLE index_queue FROM admin;
GRANT ALL ON TABLE index_queue TO admin;
GRANT ALL ON TABLE index_queue TO apenet_dashboard;

REVOKE ALL ON TABLE lang FROM PUBLIC;
REVOKE ALL ON TABLE lang FROM admin;
GRANT ALL ON TABLE lang TO admin;
GRANT ALL ON TABLE lang TO apenet_dashboard;
GRANT SELECT ON TABLE lang TO apenet_portal;

REVOKE ALL ON TABLE level_type FROM PUBLIC;
REVOKE ALL ON TABLE level_type FROM admin;
GRANT ALL ON TABLE level_type TO admin;
GRANT ALL ON TABLE level_type TO apenet_portal;

REVOKE ALL ON TABLE metadata_format FROM PUBLIC;
REVOKE ALL ON TABLE metadata_format FROM admin;
GRANT ALL ON TABLE metadata_format TO admin;
GRANT ALL ON TABLE metadata_format TO apenet_dashboard;

REVOKE ALL ON TABLE normal_user FROM PUBLIC;
REVOKE ALL ON TABLE normal_user FROM admin;
GRANT ALL ON TABLE normal_user TO admin;
GRANT ALL ON TABLE normal_user TO apenet_portal;

REVOKE ALL ON TABLE "user" FROM PUBLIC;
REVOKE ALL ON TABLE "user" FROM admin;
GRANT ALL ON TABLE "user" TO admin;
GRANT ALL ON TABLE "user" TO apenet_dashboard;
GRANT SELECT ON TABLE "user" TO apenet_portal;

REVOKE ALL ON SEQUENCE user_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE user_id_seq FROM admin;
GRANT ALL ON SEQUENCE user_id_seq TO admin;
GRANT ALL ON SEQUENCE user_id_seq TO apenet_dashboard;

REVOKE ALL ON TABLE resumption_token FROM PUBLIC;
REVOKE ALL ON TABLE resumption_token FROM admin;
GRANT ALL ON TABLE resumption_token TO admin;
GRANT ALL ON TABLE resumption_token TO apenet_dashboard;

REVOKE ALL ON TABLE user_role FROM PUBLIC;
REVOKE ALL ON TABLE user_role FROM admin;
GRANT ALL ON TABLE user_role TO admin;
GRANT ALL ON TABLE user_role TO apenet_dashboard;

REVOKE ALL ON TABLE searches FROM PUBLIC;
REVOKE ALL ON TABLE searches FROM admin;
GRANT ALL ON TABLE searches TO admin;
GRANT ALL ON TABLE searches TO apenet_portal;

REVOKE ALL ON TABLE sent_mail_register FROM PUBLIC;
REVOKE ALL ON TABLE sent_mail_register FROM admin;
GRANT ALL ON TABLE sent_mail_register TO admin;
GRANT ALL ON TABLE sent_mail_register TO apenet_portal;
GRANT ALL ON TABLE sent_mail_register TO apenet_dashboard;

REVOKE ALL ON TABLE source_guide FROM PUBLIC;
REVOKE ALL ON TABLE source_guide FROM admin;
GRANT ALL ON TABLE source_guide TO admin;
GRANT ALL ON TABLE source_guide TO apenet_dashboard;
GRANT SELECT ON TABLE source_guide TO apenet_portal;


REVOKE ALL ON TABLE up_file FROM PUBLIC;
REVOKE ALL ON TABLE up_file FROM admin;
GRANT ALL ON TABLE up_file TO admin;
GRANT ALL ON TABLE up_file TO apenet_dashboard;
GRANT SELECT ON TABLE up_file TO apenet_portal;

REVOKE ALL ON TABLE up_file_state FROM PUBLIC;
REVOKE ALL ON TABLE up_file_state FROM admin;
GRANT ALL ON TABLE up_file_state TO admin;
GRANT ALL ON TABLE up_file_state TO apenet_dashboard;

REVOKE ALL ON TABLE upload_method FROM PUBLIC;
REVOKE ALL ON TABLE upload_method FROM admin;
GRANT ALL ON TABLE upload_method TO admin;
GRANT ALL ON TABLE upload_method TO apenet_dashboard;
GRANT SELECT ON TABLE upload_method TO apenet_portal;

REVOKE ALL ON TABLE user_state FROM PUBLIC;
REVOKE ALL ON TABLE user_state FROM admin;
GRANT ALL ON TABLE user_state TO admin;
GRANT ALL ON TABLE user_state TO apenet_dashboard;
GRANT ALL ON TABLE user_state TO apenet_portal;

REVOKE ALL ON TABLE warnings FROM PUBLIC;
REVOKE ALL ON TABLE warnings FROM admin;
GRANT ALL ON TABLE warnings TO admin;
GRANT ALL ON TABLE warnings TO apenet_dashboard;

