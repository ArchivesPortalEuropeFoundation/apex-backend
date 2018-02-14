drop schema public cascade;

CREATE TABLE ai_alternative_name (
    id integer NOT NULL,
    ai_a_name character varying(255) NOT NULL,
    ai_id integer NOT NULL,
    lng_id integer NOT NULL,
    primary_name boolean NOT NULL
);

--
-- TOC entry 181 (class 1259 OID 37492)
-- Name: ai_alternative_name_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE ai_alternative_name_id_seq
    START WITH 355
    INCREMENT BY 1;




--
-- TOC entry 2677 (class 0 OID 0)
-- Dependencies: 181
-- Name: ai_alternative_name_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sa
--



--
-- TOC entry 183 (class 1259 OID 16391)
-- Name: api_key; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE api_key (
    id integer NOT NULL,
    apikey character varying(36),
    emailaddress character varying(255),
    firstname character varying(255),
    lastname character varying(255),
    url character varying(255),
    creationtime timestamp without time zone,
    status character varying(255),
    uuid character varying(36),
    version timestamp without time zone,
    liferayuserid bigint NOT NULL
);




--
-- TOC entry 184 (class 1259 OID 16397)
-- Name: api_key_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE api_key_id_seq
    START WITH 1
    INCREMENT BY 1;




--
-- TOC entry 2729 (class 0 OID 0)
-- Dependencies: 184
-- Name: api_key_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sa
--



--
-- TOC entry 182 (class 1259 OID 37494)
-- Name: archival_institution; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE archival_institution (
    id integer NOT NULL,
    ainame character varying(255) NOT NULL,
    registration_date date,
    eag_path character varying(255),
    user_id integer,
    repositorycode character varying(20),
    autform character varying(255),
    isgroup boolean,
    parent_ai_id integer,
    alorder integer NOT NULL,
    internal_al_id character varying(100) NOT NULL,
    country_id integer NOT NULL,
    contain_searchable_items boolean DEFAULT false,
    content_lastmodified_date timestamp without time zone,
    using_mets boolean DEFAULT false,
    feedback_email character varying(255) DEFAULT NULL,
    opendataenabled boolean,
    opendataqueueid integer,
    totalsolrdocscount bigint,
    unprocessedsolrdocs bigint,
    totalsolrdocsforopendata bigint
);




--
-- TOC entry 183 (class 1259 OID 37503)
-- Name: archival_institution_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE archival_institution_id_seq
    START WITH 355
    INCREMENT BY 1;




--
-- TOC entry 2678 (class 0 OID 0)
-- Dependencies: 183
-- Name: archival_institution_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sa
--




--
-- TOC entry 184 (class 1259 OID 37505)
-- Name: archival_institution_oai_pmh; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE archival_institution_oai_pmh (
    id bigint NOT NULL,
    oai_pmh_set character varying(255),
    oai_pmh_url character varying(255) NOT NULL,
    last_harvesting timestamp without time zone,
    ai_id integer NOT NULL,
    oai_pmh_metadata character varying(255) NOT NULL,
    profile_id bigint NOT NULL,
    interval_harvesting bigint NOT NULL,
    enabled boolean,
    harvest_only_weekend boolean DEFAULT false NOT NULL,
    from_date character varying(10),
    new_harvesting timestamp without time zone,
    list_by_identifiers boolean DEFAULT false,
    harvesting_details text,
    harvesting_status character varying(255) DEFAULT NULL,
    errors_response_path text,
    locked boolean DEFAULT false
);




--
-- TOC entry 185 (class 1259 OID 37516)
-- Name: archival_institution_oai_pmh_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE archival_institution_oai_pmh_id_seq
    START WITH 1
    INCREMENT BY 1;




--
-- TOC entry 2679 (class 0 OID 0)
-- Dependencies: 185
-- Name: archival_institution_oai_pmh_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sa
--




--
-- TOC entry 186 (class 1259 OID 37518)
-- Name: c_level; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE c_level (
    id bigint NOT NULL,
    ec_id bigint,
    parent_cl_id bigint,
    unittitle text,
    unitid text,
    order_id integer,
    leaf boolean,
    xml text,
    href_eadid character varying(255),
    duplicate_unitid boolean DEFAULT false,
    cid text
);




--
-- TOC entry 187 (class 1259 OID 37525)
-- Name: c_level_cl_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE c_level_cl_id_seq
    START WITH 1
    INCREMENT BY 1;

--
-- TOC entry 188 (class 1259 OID 37527)
-- Name: c_level_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE c_level_id_seq
    START WITH 1
    INCREMENT BY 1;




--
-- TOC entry 2681 (class 0 OID 0)
-- Dependencies: 188
-- Name: c_level_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sa
--




--
-- TOC entry 189 (class 1259 OID 37529)
-- Name: collection; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE collection (
    id bigint NOT NULL,
    title character varying(200),
    liferay_user_id bigint NOT NULL,
    public_collection boolean NOT NULL,
    edit boolean NOT NULL,
    description text,
    modified_date timestamp without time zone DEFAULT now() NOT NULL
);




--
-- TOC entry 190 (class 1259 OID 37536)
-- Name: collection_content; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE collection_content (
    id bigint NOT NULL,
    id_collection bigint NOT NULL,
    id_search bigint,
    id_bookmarks bigint
);




--
-- TOC entry 191 (class 1259 OID 37539)
-- Name: collection_content_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE collection_content_id_seq
    START WITH 1
    INCREMENT BY 1;




--
-- TOC entry 2682 (class 0 OID 0)
-- Dependencies: 191
-- Name: collection_content_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sa
--




--
-- TOC entry 192 (class 1259 OID 37541)
-- Name: collection_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE collection_id_seq
    START WITH 1
    INCREMENT BY 1;




--
-- TOC entry 2683 (class 0 OID 0)
-- Dependencies: 192
-- Name: collection_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sa
--




--
-- TOC entry 193 (class 1259 OID 37543)
-- Name: coordinates; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE coordinates (
    id integer NOT NULL,
    ai_id integer NOT NULL,
    name_institution character varying(255) NOT NULL,
    lat numeric,
    lon numeric,
    street character varying(255) NOT NULL,
    postalcity character varying(255) NOT NULL,
    country character varying(255) NOT NULL
);




--
-- TOC entry 194 (class 1259 OID 37549)
-- Name: coordinates_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE coordinates_id_seq
    START WITH 1
    INCREMENT BY 1;




--
-- TOC entry 2684 (class 0 OID 0)
-- Dependencies: 194
-- Name: coordinates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sa
--




--
-- TOC entry 195 (class 1259 OID 37551)
-- Name: cou_alternative_name; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE cou_alternative_name (
    id integer NOT NULL,
    cou_an_name character varying(255) NOT NULL,
    cou_id integer NOT NULL,
    lng_id integer NOT NULL
);




--
-- TOC entry 196 (class 1259 OID 37554)
-- Name: cou_alternative_name_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE cou_alternative_name_id_seq
    START WITH 197
    INCREMENT BY 1;


--
-- TOC entry 197 (class 1259 OID 37556)
-- Name: country; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE country (
    id integer NOT NULL,
    cname character varying(120) NOT NULL,
    isoname character varying(2) NOT NULL,
    al_order integer
);




--
-- TOC entry 2686 (class 0 OID 0)
-- Dependencies: 197
-- Name: COLUMN country.isoname; Type: COMMENT; Schema: public; Owner: sa
--

COMMENT ON COLUMN country.isoname IS 'iso3166-1';


--
-- TOC entry 198 (class 1259 OID 37559)
-- Name: country_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE country_id_seq
    START WITH 15
    INCREMENT BY 1;


--
-- TOC entry 199 (class 1259 OID 37561)
-- Name: dashboard_user; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE dashboard_user (
    id integer NOT NULL,
    email_address character varying(256) NOT NULL,
    password character varying(60) NOT NULL,
    country_id integer,
    secret_question character varying(200),
    secret_answer character varying(200),
    user_role_id integer NOT NULL,
    first_name character varying(60) NOT NULL,
    last_name character varying(100) NOT NULL,
    is_active boolean DEFAULT true NOT NULL
);




--
-- TOC entry 200 (class 1259 OID 37568)
-- Name: dpt_update; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE dpt_update (
    id bigint NOT NULL,
    version character varying(255),
    new_version character varying(255)
);




--
-- TOC entry 201 (class 1259 OID 37574)
-- Name: dpt_update_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE dpt_update_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 202 (class 1259 OID 37576)
-- Name: eac_cpf; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE eac_cpf (
    id integer NOT NULL,
    title text,
    upload_date timestamp without time zone NOT NULL,
    path character varying(255) NOT NULL,
    um_id integer NOT NULL,
    ai_id integer NOT NULL,
    published boolean DEFAULT false,
    converted boolean DEFAULT false,
    validated smallint DEFAULT 1,
    cpfrelations bigint DEFAULT 0,
    resourcerelations bigint DEFAULT 0,
    functionrelations bigint DEFAULT 0,
    europeana smallint DEFAULT 0,
    queuing smallint DEFAULT 0,
    publish_date timestamp without time zone,
    identifier character varying(255) NOT NULL
);




--
-- TOC entry 203 (class 1259 OID 37590)
-- Name: eac_cpf_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE eac_cpf_id_seq
    START WITH 1
    INCREMENT BY 1;



-- Table: ead3

-- DROP TABLE ead3;

CREATE TABLE ead3
(
  id serial NOT NULL,
  ai_id integer,
  converted boolean NOT NULL,
  europeana integer,
  identifier character varying(255) NOT NULL,
  path character varying(255),
  publish_date date,
  published boolean NOT NULL,
  queuing integer,
  title character varying(255),
  upload_date timestamp without time zone,
  validated integer,
  um_id integer,
  totalnumberofdaos bigint,
  totalnumberofunits bigint,
  eadcontent_id bigint
--   CONSTRAINT ead3_pkey PRIMARY KEY (id),
--   CONSTRAINT ead3_ai_id_fkey FOREIGN KEY (ai_id)
--       REFERENCES archival_institution (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT ead3_um_id_fkey FOREIGN KEY (um_id)
--       REFERENCES upload_method (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION
);

--
-- TOC entry 204 (class 1259 OID 37592)
-- Name: ead_content; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE ead_content (
    id bigint NOT NULL,
    fa_id bigint,
    hg_id bigint,
    sg_id bigint,
    xml text,
    titleproper text,
    eadid character varying(255),
    unittitle text,
    display_intro boolean DEFAULT true NOT NULL,
    display_did boolean DEFAULT true NOT NULL,
    visible boolean
);




--
-- TOC entry 205 (class 1259 OID 37600)
-- Name: ead_content_ec_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE ead_content_ec_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 206 (class 1259 OID 37602)
-- Name: ead_content_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE ead_content_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 207 (class 1259 OID 37604)
-- Name: ead_saved_search; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE ead_saved_search (
    id bigint NOT NULL,
    liferay_user_id bigint NOT NULL,
    modified_date timestamp without time zone DEFAULT now() NOT NULL,
    public_search boolean DEFAULT false NOT NULL,
    template boolean DEFAULT false NOT NULL,
    description character varying(255) DEFAULT NULL,
    search_term character varying(100) DEFAULT NULL,
    hierarchy boolean DEFAULT false NOT NULL,
    only_with_daos boolean DEFAULT false NOT NULL,
    method_optional boolean DEFAULT false NOT NULL,
    element character varying(1) DEFAULT NULL,
    typedocument character varying(2) DEFAULT NULL,
    fromdate character varying(10) DEFAULT NULL,
    todate character varying(10) DEFAULT NULL,
    exact_date_search boolean DEFAULT false NOT NULL,
    al_tree_selected_nodes text,
    refinement_country text,
    refinement_ai text,
    refinement_fond character varying(22) DEFAULT NULL,
    refinement_type character varying(9) DEFAULT NULL,
    refinement_level character varying(15) DEFAULT NULL,
    refinement_dao character varying(10) DEFAULT NULL,
    refinement_roledao character varying(40) DEFAULT NULL,
    refinement_date_type character varying(25) DEFAULT NULL,
    refinement_startdate character varying(12) DEFAULT NULL,
    refinement_enddate character varying(12) DEFAULT NULL,
    refinement_facet_settings character varying(255) DEFAULT NULL,
    refinement_topic text,
    topic text
);




--
-- TOC entry 208 (class 1259 OID 37632)
-- Name: ead_saved_search_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE ead_saved_search_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 209 (class 1259 OID 37634)
-- Name: ese; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE ese (
    id integer NOT NULL,
    creation_date timestamp with time zone NOT NULL,
    modification_date timestamp with time zone,
    eset text NOT NULL,
    path character varying(255),
    es_id integer NOT NULL,
    fa_id integer,
    path_html character varying(200),
    number_of_records integer DEFAULT 0,
    oai_identifier character varying(255),
    metadataformat character varying(20) DEFAULT NULL NOT NULL,
    number_of_web_resource integer DEFAULT 0
);




--
-- TOC entry 210 (class 1259 OID 37643)
-- Name: ese_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE ese_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 211 (class 1259 OID 37645)
-- Name: ese_state; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE ese_state (
    id integer NOT NULL,
    state character varying(20) NOT NULL
);




--
-- TOC entry 212 (class 1259 OID 37648)
-- Name: ese_state_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE ese_state_id_seq
    START WITH 4
    INCREMENT BY 1;



--
-- TOC entry 213 (class 1259 OID 37650)
-- Name: finding_aid; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE finding_aid (
    id integer NOT NULL,
    title text NOT NULL,
    upload_date timestamp without time zone NOT NULL,
    path_apenetead character varying(255) NOT NULL,
    um_id integer NOT NULL,
    eadid character varying(255) NOT NULL,
    ai_id integer NOT NULL,
    totalnumberofdaos bigint NOT NULL,
    totalnumberofunits bigint NOT NULL,
    totalnumberofunitswithdao bigint NOT NULL,
    published boolean DEFAULT false,
    converted boolean DEFAULT false,
    validated smallint DEFAULT 1,
    europeana smallint DEFAULT 0,
    queuing smallint DEFAULT 0,
    totalnumberofchos bigint DEFAULT 0,
    dynamic boolean DEFAULT false,
    publish_date timestamp without time zone,
    total_number_of_web_resource_edm integer DEFAULT 0
);




--
-- TOC entry 214 (class 1259 OID 37664)
-- Name: finding_aid_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE finding_aid_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 215 (class 1259 OID 37666)
-- Name: ftp_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE ftp_id_seq
    START WITH 1
    INCREMENT BY 1;




--
-- TOC entry 216 (class 1259 OID 37668)
-- Name: ftp; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE ftp (
    id integer DEFAULT nextval('ftp_id_seq') NOT NULL,
    url character varying(256) NOT NULL,
    port integer,
    username character varying(60),
    ai_id integer NOT NULL
);




--
-- TOC entry 217 (class 1259 OID 37672)
-- Name: hg_sg_fa_relation; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE hg_sg_fa_relation (
    id bigint NOT NULL,
    fa_id bigint NOT NULL,
    hg_id bigint,
    sg_id bigint,
    ai_id bigint,
    hg_sg_clevel_id bigint NOT NULL
);




--
-- TOC entry 218 (class 1259 OID 37675)
-- Name: hg_sg_fa_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE hg_sg_fa_relation_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 219 (class 1259 OID 37677)
-- Name: holdings_guide; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE holdings_guide (
    id integer NOT NULL,
    title text NOT NULL,
    upload_date timestamp without time zone NOT NULL,
    path_apenetead character varying(255) NOT NULL,
    ai_id integer NOT NULL,
    um_id integer NOT NULL,
    eadid character varying(255),
    path_html character varying(100),
    totalnumberofdaos bigint NOT NULL,
    totalnumberofunits bigint NOT NULL,
    totalnumberofunitswithdao bigint NOT NULL,
    published boolean DEFAULT false,
    converted boolean DEFAULT false,
    validated smallint DEFAULT 1,
    queuing smallint DEFAULT 0,
    dynamic boolean DEFAULT false,
    publish_date timestamp without time zone
);




--
-- TOC entry 220 (class 1259 OID 37688)
-- Name: holdings_guide_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE holdings_guide_id_seq
    START WITH 1
    INCREMENT BY 1;



--
-- TOC entry 221 (class 1259 OID 37690)
-- Name: queue; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE queue (
    id integer NOT NULL,
    fa_id integer,
    hg_id integer,
    sg_id integer,
    queue_date timestamp without time zone,
    errors text,
    priority smallint NOT NULL,
    action character varying(255) NOT NULL,
    preferences text,
    uf_id integer,
    eac_cpf_id integer,
    ai_id integer NOT NULL
);




--
-- TOC entry 222 (class 1259 OID 37696)
-- Name: index_queue_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE index_queue_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 223 (class 1259 OID 37698)
-- Name: ingestionprofile; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE ingestionprofile (
    id bigint NOT NULL,
    name_profile character varying(100) NOT NULL,
    file_type integer DEFAULT 1 NOT NULL,
    upload_action integer DEFAULT 1 NOT NULL,
    exist_action integer DEFAULT 1 NOT NULL,
    dao_type integer DEFAULT 6 NOT NULL,
    europeana_dp character varying(100),
    europeana_dp_from_file boolean DEFAULT true,
    europeana_languages character varying(100),
    europeana_languages_from_file boolean DEFAULT true,
    europeana_add_rights character varying(200),
    europeana_inh_elements boolean DEFAULT false,
    europeana_inh_origin boolean DEFAULT false,
    noeadid_action integer DEFAULT 1 NOT NULL,
    europeana_license character varying(100),
    europeana_license_details character varying(100),
    europeana_dao_type integer,
    europeana_dao_type_from_file boolean DEFAULT true,
    dao_type_from_file boolean DEFAULT true NOT NULL,
    europeana_conversion_type boolean DEFAULT true,
    europeana_inh_elements_check boolean DEFAULT true,
    europeana_inh_origin_check boolean DEFAULT true,
    ai_id integer NOT NULL,
    source_of_identifiers character varying(100),
    rights_of_digital_objects character varying(100),
    rights_of_digital_description text,
    rights_of_digital_holder text,
    rights_of_ead_data character varying(100),
    rights_of_ead_description text,
    rights_of_ead_holder text,
    rights_of_digital_objects_text character varying(200),
    rights_of_ead_data_text character varying(200),
    europeana_license_from_file boolean DEFAULT true,
    xsl_upload_id integer,
    europeana_inh_unittitle_check boolean DEFAULT true,
    europeana_inh_unittitle boolean DEFAULT false,
    use_archdesc_unittitle boolean DEFAULT true
);




--
-- TOC entry 224 (class 1259 OID 37722)
-- Name: lang; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE lang (
    id integer NOT NULL,
    lname character varying(20) NOT NULL,
    isoname character varying(3) NOT NULL,
    iso2name character varying(2) NOT NULL,
    lnativename character varying(20) NOT NULL
);




--
-- TOC entry 2699 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN lang.isoname; Type: COMMENT; Schema: public; Owner: sa
--

COMMENT ON COLUMN lang.isoname IS 'iso639-2';


--
-- TOC entry 225 (class 1259 OID 37725)
-- Name: lang_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE lang_id_seq
    START WITH 15
    INCREMENT BY 1;



--
-- TOC entry 228 (class 1259 OID 37733)
-- Name: saved_bookmarks; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE saved_bookmarks (
    id bigint NOT NULL,
    liferay_user_id bigint NOT NULL,
    modified_date timestamp without time zone DEFAULT now() NOT NULL,
    bookmark_name text DEFAULT NULL,
    description text DEFAULT NULL,
    persistent_link text DEFAULT NULL,
    typedocument character varying(20) DEFAULT NULL
);




--
-- TOC entry 229 (class 1259 OID 37744)
-- Name: saved_bookmarks_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE saved_bookmarks_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 230 (class 1259 OID 37746)
-- Name: sent_mail_register; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE sent_mail_register (
    id integer NOT NULL,
    validation_link character varying(100) NOT NULL,
    date date NOT NULL,
    email_address character varying(100) NOT NULL,
    user_id integer,
    ai_id integer
);




--
-- TOC entry 231 (class 1259 OID 37749)
-- Name: sent_mail_register_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE sent_mail_register_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 232 (class 1259 OID 37751)
-- Name: source_guide; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE source_guide (
    id integer NOT NULL,
    ai_id integer NOT NULL,
    um_id integer NOT NULL,
    title text NOT NULL,
    upload_date timestamp without time zone NOT NULL,
    path_apenetead character varying(255) NOT NULL,
    eadid character varying(255),
    totalnumberofdaos bigint NOT NULL,
    totalnumberofunits bigint NOT NULL,
    totalnumberofunitswithdao bigint NOT NULL,
    published boolean DEFAULT false,
    converted boolean DEFAULT false,
    validated smallint DEFAULT 1,
    queuing smallint DEFAULT 0,
    dynamic boolean DEFAULT false,
    publish_date timestamp without time zone
);




--
-- TOC entry 233 (class 1259 OID 37762)
-- Name: source_guide_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE source_guide_id_seq
    START WITH 1
    INCREMENT BY 1;



--
-- TOC entry 234 (class 1259 OID 37764)
-- Name: source_guide_sg_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE source_guide_sg_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 235 (class 1259 OID 37766)
-- Name: topic; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE topic (
    id bigint NOT NULL,
    property_key character varying(40) NOT NULL,
    description character varying(100) NOT NULL
);




--
-- TOC entry 236 (class 1259 OID 37769)
-- Name: topic_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE topic_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 237 (class 1259 OID 37771)
-- Name: topic_mapping; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE topic_mapping (
    id bigint NOT NULL,
    ai_id integer,
    topic_id bigint NOT NULL,
    controlaccess_keyword text DEFAULT NULL,
    sg_id bigint,
    country_id integer
);




--
-- TOC entry 238 (class 1259 OID 37778)
-- Name: topic_mapping_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE topic_mapping_id_seq
    START WITH 1
    INCREMENT BY 1;



--
-- TOC entry 239 (class 1259 OID 37780)
-- Name: up_file; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE up_file (
    id integer NOT NULL,
    path character varying(255) NOT NULL,
    um_id integer NOT NULL,
    ai_id integer NOT NULL,
    filename character varying(255) NOT NULL,
    file_type character varying(3) DEFAULT NULL NOT NULL
);




--
-- TOC entry 240 (class 1259 OID 37787)
-- Name: up_file_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE up_file_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 241 (class 1259 OID 37789)
-- Name: upload_method; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE upload_method (
    id integer NOT NULL,
    method character varying(20) NOT NULL
);




--
-- TOC entry 242 (class 1259 OID 37792)
-- Name: upload_method_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE upload_method_id_seq
    START WITH 4
    INCREMENT BY 1;


--
-- TOC entry 243 (class 1259 OID 37794)
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 244 (class 1259 OID 37796)
-- Name: user_role; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE user_role (
    id integer NOT NULL,
    role character varying(40) NOT NULL
);




--
-- TOC entry 245 (class 1259 OID 37799)
-- Name: user_role_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE user_role_id_seq
    START WITH 4
    INCREMENT BY 1;



--
-- TOC entry 246 (class 1259 OID 37801)
-- Name: userprofile_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE userprofile_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 247 (class 1259 OID 37803)
-- Name: warnings; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE warnings (
    id integer NOT NULL,
    abstract text NOT NULL,
    hg_id integer,
    fa_id integer,
    sg_id integer,
    iswarning boolean,
    eac_id integer
);




--
-- TOC entry 248 (class 1259 OID 37809)
-- Name: warnings_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE warnings_id_seq
    START WITH 1
    INCREMENT BY 1;



--
-- TOC entry 249 (class 1259 OID 37811)
-- Name: xsl_upload; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE xsl_upload (
    id integer NOT NULL,
    readable_name character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    archival_institution_id integer NOT NULL
);




--
-- TOC entry 250 (class 1259 OID 37817)
-- Name: xsl_upload_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

CREATE SEQUENCE xsl_upload_id_seq
    START WITH 1
    INCREMENT BY 1;


--
-- TOC entry 2330 (class 2606 OID 37852)
-- Name: alternative_name_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ai_alternative_name
    ADD CONSTRAINT alternative_name_pkey PRIMARY KEY (id);


--
-- TOC entry 2380 (class 2606 OID 16799)
-- Name: api_key_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE api_key
    ADD CONSTRAINT api_key_pkey PRIMARY KEY (id);

--
-- TOC entry 2335 (class 2606 OID 37854)
-- Name: archival_institution_oai_pmh_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE archival_institution_oai_pmh
    ADD CONSTRAINT archival_institution_oai_pmh_pkey PRIMARY KEY (id);


--
-- TOC entry 2332 (class 2606 OID 37856)
-- Name: archival_institution_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE archival_institution
    ADD CONSTRAINT archival_institution_pkey PRIMARY KEY (id);


--
-- TOC entry 2344 (class 2606 OID 37858)
-- Name: c_level_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE c_level
    ADD CONSTRAINT c_level_pkey PRIMARY KEY (id);


--
-- TOC entry 2349 (class 2606 OID 37860)
-- Name: collection_content_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE collection_content
    ADD CONSTRAINT collection_content_pkey PRIMARY KEY (id);


--
-- TOC entry 2347 (class 2606 OID 37862)
-- Name: collection_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE collection
    ADD CONSTRAINT collection_pkey PRIMARY KEY (id);


--
-- TOC entry 2351 (class 2606 OID 37864)
-- Name: coordinates_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE coordinates
    ADD CONSTRAINT coordinates_pkey PRIMARY KEY (id);


--
-- TOC entry 2353 (class 2606 OID 37866)
-- Name: cou_alternative_name_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE cou_alternative_name
    ADD CONSTRAINT cou_alternative_name_pkey PRIMARY KEY (id);


--
-- TOC entry 2355 (class 2606 OID 37868)
-- Name: country_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);


--
-- TOC entry 2357 (class 2606 OID 37870)
-- Name: dashboard_user_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE dashboard_user
    ADD CONSTRAINT dashboard_user_pkey PRIMARY KEY (id);


--
-- TOC entry 2361 (class 2606 OID 37872)
-- Name: dpt_update_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE dpt_update
    ADD CONSTRAINT dpt_update_pkey PRIMARY KEY (id);


--
-- TOC entry 2364 (class 2606 OID 37874)
-- Name: eac_cpf_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE eac_cpf
    ADD CONSTRAINT eac_cpf_pkey PRIMARY KEY (id);


--
-- TOC entry 2369 (class 2606 OID 37876)
-- Name: ead_content_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ead_content
    ADD CONSTRAINT ead_content_pkey PRIMARY KEY (id);


--
-- TOC entry 2372 (class 2606 OID 37878)
-- Name: ead_saved_search_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ead_saved_search
    ADD CONSTRAINT ead_saved_search_pkey PRIMARY KEY (id);


--
-- TOC entry 2375 (class 2606 OID 37880)
-- Name: ese_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ese
    ADD CONSTRAINT ese_pkey PRIMARY KEY (id);


--
-- TOC entry 2378 (class 2606 OID 37882)
-- Name: ese_state_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ese_state
    ADD CONSTRAINT ese_state_pkey PRIMARY KEY (id);


--
-- TOC entry 2384 (class 2606 OID 37884)
-- Name: finding_aid_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE finding_aid
    ADD CONSTRAINT finding_aid_pkey PRIMARY KEY (id);


--
-- TOC entry 2388 (class 2606 OID 37886)
-- Name: ftp_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ftp
    ADD CONSTRAINT ftp_pkey PRIMARY KEY (id);


--
-- TOC entry 2395 (class 2606 OID 37888)
-- Name: hg_sg_fa_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_pkey PRIMARY KEY (id);


--
-- TOC entry 2400 (class 2606 OID 37890)
-- Name: holdings_guide_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE holdings_guide
    ADD CONSTRAINT holdings_guide_pkey PRIMARY KEY (id);


--
-- TOC entry 2406 (class 2606 OID 37892)
-- Name: ingestionprofile_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ingestionprofile
    ADD CONSTRAINT ingestionprofile_pkey PRIMARY KEY (id);


--
-- TOC entry 2408 (class 2606 OID 37894)
-- Name: lang_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE lang
    ADD CONSTRAINT lang_pkey PRIMARY KEY (id);


--
-- TOC entry 2359 (class 2606 OID 37896)
-- Name: partner_email_address_unique; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE dashboard_user
    ADD CONSTRAINT partner_email_address_unique UNIQUE (email_address);


--
-- TOC entry 2403 (class 2606 OID 37898)
-- Name: queue_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE queue
    ADD CONSTRAINT queue_pkey PRIMARY KEY (id);


--
-- TOC entry 2437 (class 2606 OID 37902)
-- Name: role_type_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE user_role
    ADD CONSTRAINT role_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2413 (class 2606 OID 37904)
-- Name: saved_bookmarks_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE saved_bookmarks
    ADD CONSTRAINT saved_bookmarks_pkey PRIMARY KEY (id);


--
-- TOC entry 2415 (class 2606 OID 37906)
-- Name: sent_mail_register_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE sent_mail_register
    ADD CONSTRAINT sent_mail_register_pkey PRIMARY KEY (id);


--
-- TOC entry 2420 (class 2606 OID 37908)
-- Name: source_guide_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE source_guide
    ADD CONSTRAINT source_guide_pkey PRIMARY KEY (id);


--
-- TOC entry 2429 (class 2606 OID 37910)
-- Name: topic_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE topic_mapping
    ADD CONSTRAINT topic_mapping_pkey PRIMARY KEY (id);


--
-- TOC entry 2425 (class 2606 OID 37912)
-- Name: topic_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE topic
    ADD CONSTRAINT topic_pkey PRIMARY KEY (id);


--
-- TOC entry 2433 (class 2606 OID 37914)
-- Name: up_file_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE up_file
    ADD CONSTRAINT up_file_pkey PRIMARY KEY (id);


--
-- TOC entry 2435 (class 2606 OID 37916)
-- Name: upload_method_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE upload_method
    ADD CONSTRAINT upload_method_pkey PRIMARY KEY (id);


--
-- TOC entry 2439 (class 2606 OID 37918)
-- Name: warnings_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE warnings
    ADD CONSTRAINT warnings_pkey PRIMARY KEY (id);


--
-- TOC entry 2441 (class 2606 OID 37920)
-- Name: xsl_upload_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE xsl_upload
    ADD CONSTRAINT xsl_upload_pkey PRIMARY KEY (id);


--
-- TOC entry 2333 (class 1259 OID 37921)
-- Name: archival_institution_repositorycode; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX archival_institution_repositorycode ON archival_institution (repositorycode);


--
-- TOC entry 2336 (class 1259 OID 37922)
-- Name: c_level__cid_ec_id_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX c_level__cid_ec_id_idx ON c_level (cid, ec_id);


--
-- TOC entry 2337 (class 1259 OID 37923)
-- Name: c_level__eadid_ref_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX c_level__eadid_ref_idx ON c_level (ec_id, href_eadid);


--
-- TOC entry 2338 (class 1259 OID 37924)
-- Name: c_level__nodes_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX c_level__nodes_idx ON c_level (ec_id);


--
-- TOC entry 2339 (class 1259 OID 37925)
-- Name: c_level__parent_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX c_level__parent_idx ON c_level (parent_cl_id, order_id);


--
-- TOC entry 2340 (class 1259 OID 37926)
-- Name: c_level__persistent_link_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX c_level__persistent_link_idx ON c_level (unitid, ec_id);


--
-- TOC entry 2341 (class 1259 OID 37927)
-- Name: c_level__top_levels_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX c_level__top_levels_idx ON c_level (order_id, ec_id);


--
-- TOC entry 2342 (class 1259 OID 37928)
-- Name: c_level__unitid_ec_id_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX c_level__unitid_ec_id_idx ON c_level (unitid, ec_id);


--
-- TOC entry 2345 (class 1259 OID 37929)
-- Name: collection__liferay_user_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX collection__liferay_user_idx ON collection (liferay_user_id);


--
-- TOC entry 2362 (class 1259 OID 37930)
-- Name: eac_cpf__archival_institution_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX eac_cpf__archival_institution_idx ON eac_cpf (ai_id);


--
-- TOC entry 2365 (class 1259 OID 37931)
-- Name: ead_content__finding_aid_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX ead_content__finding_aid_idx ON ead_content (fa_id);


--
-- TOC entry 2366 (class 1259 OID 37932)
-- Name: ead_content__holdings_guide_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX ead_content__holdings_guide_idx ON ead_content (hg_id);


--
-- TOC entry 2367 (class 1259 OID 37933)
-- Name: ead_content__source_guide_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX ead_content__source_guide_idx ON ead_content (sg_id);


--
-- TOC entry 2370 (class 1259 OID 37934)
-- Name: ead_saved_search__liferay_user_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX ead_saved_search__liferay_user_idx ON ead_saved_search (liferay_user_id);


--
-- TOC entry 2373 (class 1259 OID 37935)
-- Name: ese_metadataformat; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX ese_metadataformat ON ese (metadataformat);


--
-- TOC entry 2379 (class 1259 OID 37936)
-- Name: finding_aid__archival_institution_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX finding_aid__archival_institution_idx ON finding_aid (ai_id);


--
-- TOC entry 2380 (class 1259 OID 37937)
-- Name: finding_aid_dynamic; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX finding_aid_dynamic ON finding_aid (dynamic);


--
-- TOC entry 2381 (class 1259 OID 37938)
-- Name: finding_aid_eadid; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX finding_aid_eadid ON finding_aid (eadid);


--
-- TOC entry 2382 (class 1259 OID 37939)
-- Name: finding_aid_path; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX finding_aid_path ON finding_aid (path_apenetead);


--
-- TOC entry 2385 (class 1259 OID 37940)
-- Name: finding_aid_searchable; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX finding_aid_searchable ON finding_aid (published);


--
-- TOC entry 2386 (class 1259 OID 37941)
-- Name: finding_aid_title; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX finding_aid_title ON finding_aid (title);


--
-- TOC entry 2389 (class 1259 OID 37942)
-- Name: hg_sg_fa_relation__archival_institution_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX hg_sg_fa_relation__archival_institution_idx ON hg_sg_fa_relation (ai_id);


--
-- TOC entry 2390 (class 1259 OID 37943)
-- Name: hg_sg_fa_relation__c_level_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX hg_sg_fa_relation__c_level_idx ON hg_sg_fa_relation (hg_sg_clevel_id);


--
-- TOC entry 2391 (class 1259 OID 37944)
-- Name: hg_sg_fa_relation__finding_aid_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX hg_sg_fa_relation__finding_aid_idx ON hg_sg_fa_relation (fa_id);


--
-- TOC entry 2392 (class 1259 OID 37945)
-- Name: hg_sg_fa_relation__holdings_guide_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX hg_sg_fa_relation__holdings_guide_idx ON hg_sg_fa_relation (hg_id);


--
-- TOC entry 2393 (class 1259 OID 37946)
-- Name: hg_sg_fa_relation__source_guide_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX hg_sg_fa_relation__source_guide_idx ON hg_sg_fa_relation (sg_id);


--
-- TOC entry 2396 (class 1259 OID 37947)
-- Name: holdings_guide__archival_institution_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX holdings_guide__archival_institution_idx ON holdings_guide (ai_id);


--
-- TOC entry 2397 (class 1259 OID 37948)
-- Name: holdings_guide_dynamic; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX holdings_guide_dynamic ON holdings_guide (dynamic);


--
-- TOC entry 2398 (class 1259 OID 37949)
-- Name: holdings_guide_path; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX holdings_guide_path ON holdings_guide (path_apenetead);


--
-- TOC entry 2401 (class 1259 OID 37950)
-- Name: holdings_guide_searchable; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX holdings_guide_searchable ON holdings_guide (published);


--
-- TOC entry 2404 (class 1259 OID 37951)
-- Name: queue_uf_id; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX queue_uf_id ON queue (uf_id);


--
-- TOC entry 2376 (class 1259 OID 37952)
-- Name: resumption_token_metadataformat; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX resumption_token_metadataformat ON ese (metadataformat);


--
-- TOC entry 2411 (class 1259 OID 37953)
-- Name: saved_bookmarks__liferay_user_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX saved_bookmarks__liferay_user_idx ON saved_bookmarks (liferay_user_id);


--
-- TOC entry 2416 (class 1259 OID 37954)
-- Name: source_guide__archival_institution_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX source_guide__archival_institution_idx ON source_guide (ai_id);


--
-- TOC entry 2417 (class 1259 OID 37955)
-- Name: source_guide_dynamic; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX source_guide_dynamic ON source_guide (dynamic);


--
-- TOC entry 2418 (class 1259 OID 37956)
-- Name: source_guide_path; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX source_guide_path ON source_guide (path_apenetead);


--
-- TOC entry 2421 (class 1259 OID 37957)
-- Name: source_guide_searchable; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX source_guide_searchable ON source_guide (published);


--
-- TOC entry 2422 (class 1259 OID 37958)
-- Name: topic__description_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE UNIQUE INDEX topic__description_idx ON topic (description);


--
-- TOC entry 2423 (class 1259 OID 37959)
-- Name: topic__property_key_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE UNIQUE INDEX topic__property_key_idx ON topic (property_key);


--
-- TOC entry 2426 (class 1259 OID 37960)
-- Name: topic_mapping__archival_institution_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX topic_mapping__archival_institution_idx ON topic_mapping (ai_id);


--
-- TOC entry 2427 (class 1259 OID 37961)
-- Name: topic_mapping__source_guide_idx; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX topic_mapping__source_guide_idx ON topic_mapping (sg_id);


--
-- TOC entry 2430 (class 1259 OID 37962)
-- Name: up_file_ai_id; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX up_file_ai_id ON up_file (ai_id);


--
-- TOC entry 2431 (class 1259 OID 37963)
-- Name: up_file_file_type; Type: INDEX; Schema: public; Owner: sa
--

CREATE INDEX up_file_file_type ON up_file (file_type);


--
-- TOC entry 2494 (class 2606 OID 37964)
-- Name: ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ftp
    ADD CONSTRAINT ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 2442 (class 2606 OID 37969)
-- Name: archival_institution_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ai_alternative_name
    ADD CONSTRAINT archival_institution_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 2446 (class 2606 OID 37974)
-- Name: archival_institution_cou_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE archival_institution
    ADD CONSTRAINT archival_institution_cou_id_fkey FOREIGN KEY (country_id) REFERENCES country(id);


--
-- TOC entry 2554 (class 2606 OID 37979)
-- Name: archival_institution_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE xsl_upload
    ADD CONSTRAINT archival_institution_id_fkey FOREIGN KEY (archival_institution_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 2452 (class 2606 OID 37984)
-- Name: archival_institution_oai_pmh_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE archival_institution_oai_pmh
    ADD CONSTRAINT archival_institution_oai_pmh_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 2447 (class 2606 OID 37989)
-- Name: archival_institution_p_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE archival_institution
    ADD CONSTRAINT archival_institution_p_id_fkey FOREIGN KEY (user_id) REFERENCES dashboard_user(id);


--
-- TOC entry 2448 (class 2606 OID 37994)
-- Name: archival_institution_parent_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE archival_institution
    ADD CONSTRAINT archival_institution_parent_ai_id_fkey FOREIGN KEY (parent_ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 2453 (class 2606 OID 37999)
-- Name: archival_institutition_oai_pmh_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE archival_institution_oai_pmh
    ADD CONSTRAINT archival_institutition_oai_pmh_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES ingestionprofile(id);


--
-- TOC entry 2456 (class 2606 OID 38004)
-- Name: c_level_ec_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE c_level
    ADD CONSTRAINT c_level_ec_id_fkey FOREIGN KEY (ec_id) REFERENCES ead_content(id) ON DELETE CASCADE;


--
-- TOC entry 2457 (class 2606 OID 38009)
-- Name: c_level_parent_cl_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE c_level
    ADD CONSTRAINT c_level_parent_cl_id_fkey FOREIGN KEY (parent_cl_id) REFERENCES c_level(id) ON DELETE CASCADE;


--
-- TOC entry 2460 (class 2606 OID 38014)
-- Name: collection_content_fkey_collection; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE collection_content
    ADD CONSTRAINT collection_content_fkey_collection FOREIGN KEY (id_collection) REFERENCES collection(id) ON DELETE CASCADE;


--
-- TOC entry 2461 (class 2606 OID 38019)
-- Name: collection_content_fkey_saved_bookmarks; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE collection_content
    ADD CONSTRAINT collection_content_fkey_saved_bookmarks FOREIGN KEY (id_bookmarks) REFERENCES saved_bookmarks(id) ON DELETE CASCADE;


--
-- TOC entry 2462 (class 2606 OID 38024)
-- Name: collection_content_fkey_search; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE collection_content
    ADD CONSTRAINT collection_content_fkey_search FOREIGN KEY (id_search) REFERENCES ead_saved_search(id) ON DELETE CASCADE;


--
-- TOC entry 2466 (class 2606 OID 38029)
-- Name: coordinates_archival_institution_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE coordinates
    ADD CONSTRAINT coordinates_archival_institution_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 2468 (class 2606 OID 38034)
-- Name: country_cou_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE cou_alternative_name
    ADD CONSTRAINT country_cou_id_fkey FOREIGN KEY (cou_id) REFERENCES country(id);


--
-- TOC entry 2533 (class 2606 OID 38039)
-- Name: country_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE topic_mapping
    ADD CONSTRAINT country_id_fkey FOREIGN KEY (country_id) REFERENCES country(id) ON DELETE CASCADE;


--
-- TOC entry 2476 (class 2606 OID 38044)
-- Name: eac_cpf_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE eac_cpf
    ADD CONSTRAINT eac_cpf_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 2477 (class 2606 OID 38049)
-- Name: eac_cpf_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE eac_cpf
    ADD CONSTRAINT eac_cpf_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- TOC entry 2480 (class 2606 OID 38054)
-- Name: ead_content_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ead_content
    ADD CONSTRAINT ead_content_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- TOC entry 2481 (class 2606 OID 38059)
-- Name: ead_content_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ead_content
    ADD CONSTRAINT ead_content_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- TOC entry 2482 (class 2606 OID 38064)
-- Name: ead_content_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ead_content
    ADD CONSTRAINT ead_content_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- TOC entry 2486 (class 2606 OID 38069)
-- Name: ese_es_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ese
    ADD CONSTRAINT ese_es_id_fkey FOREIGN KEY (es_id) REFERENCES ese_state(id);


--
-- TOC entry 2487 (class 2606 OID 38074)
-- Name: ese_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ese
    ADD CONSTRAINT ese_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id);


--
-- TOC entry 2490 (class 2606 OID 38079)
-- Name: finding_aid_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE finding_aid
    ADD CONSTRAINT finding_aid_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 2491 (class 2606 OID 38084)
-- Name: finding_aid_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE finding_aid
    ADD CONSTRAINT finding_aid_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- TOC entry 2500 (class 2606 OID 38369)
-- Name: hg_sg_fa_relation_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 2501 (class 2606 OID 38374)
-- Name: hg_sg_fa_relation_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- TOC entry 2502 (class 2606 OID 38379)
-- Name: hg_sg_fa_relation_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- TOC entry 2503 (class 2606 OID 38384)
-- Name: hg_sg_fa_relation_hg_sg_clevel_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_hg_sg_clevel_id_fkey FOREIGN KEY (hg_sg_clevel_id) REFERENCES c_level(id) ON DELETE CASCADE;


--
-- TOC entry 2504 (class 2606 OID 38389)
-- Name: hg_sg_fa_relation_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- TOC entry 2507 (class 2606 OID 38394)
-- Name: holdings_guide_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE holdings_guide
    ADD CONSTRAINT holdings_guide_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 2508 (class 2606 OID 38399)
-- Name: holdings_guide_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE holdings_guide
    ADD CONSTRAINT holdings_guide_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- TOC entry 2515 (class 2606 OID 38404)
-- Name: index_queue_eac_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE queue
    ADD CONSTRAINT index_queue_eac_id_fkey FOREIGN KEY (eac_cpf_id) REFERENCES eac_cpf(id) ON DELETE CASCADE;


--
-- TOC entry 2516 (class 2606 OID 38409)
-- Name: index_queue_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE queue
    ADD CONSTRAINT index_queue_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- TOC entry 2517 (class 2606 OID 38414)
-- Name: index_queue_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE queue
    ADD CONSTRAINT index_queue_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- TOC entry 2518 (class 2606 OID 38419)
-- Name: index_queue_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE queue
    ADD CONSTRAINT index_queue_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- TOC entry 2523 (class 2606 OID 38424)
-- Name: ingestionprofile_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ingestionprofile
    ADD CONSTRAINT ingestionprofile_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 2445 (class 2606 OID 38429)
-- Name: lang_lng_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ai_alternative_name
    ADD CONSTRAINT lang_lng_id_fkey FOREIGN KEY (lng_id) REFERENCES lang(id);


--
-- TOC entry 2471 (class 2606 OID 38434)
-- Name: lng_id; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE cou_alternative_name
    ADD CONSTRAINT lng_id FOREIGN KEY (lng_id) REFERENCES lang(id);


--
-- TOC entry 2474 (class 2606 OID 38439)
-- Name: partner_cou_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE dashboard_user
    ADD CONSTRAINT partner_cou_id_fkey FOREIGN KEY (country_id) REFERENCES country(id);


--
-- TOC entry 2475 (class 2606 OID 38444)
-- Name: partner_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE dashboard_user
    ADD CONSTRAINT partner_role_id_fkey FOREIGN KEY (user_role_id) REFERENCES user_role(id);


--
-- TOC entry 2519 (class 2606 OID 38449)
-- Name: queue_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE queue
    ADD CONSTRAINT queue_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 2520 (class 2606 OID 38454)
-- Name: queue_uf_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE queue
    ADD CONSTRAINT queue_uf_id_fkey FOREIGN KEY (uf_id) REFERENCES up_file(id) ON DELETE CASCADE;


--
-- TOC entry 2527 (class 2606 OID 38459)
-- Name: sent_mail_register_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE sent_mail_register
    ADD CONSTRAINT sent_mail_register_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 2528 (class 2606 OID 38464)
-- Name: sent_mail_register_p_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE sent_mail_register
    ADD CONSTRAINT sent_mail_register_p_id_fkey FOREIGN KEY (user_id) REFERENCES dashboard_user(id) ON DELETE CASCADE;


--
-- TOC entry 2531 (class 2606 OID 38469)
-- Name: source_guide_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE source_guide
    ADD CONSTRAINT source_guide_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 2532 (class 2606 OID 38474)
-- Name: source_guide_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE source_guide
    ADD CONSTRAINT source_guide_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- TOC entry 2538 (class 2606 OID 38479)
-- Name: topic_mapping_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE topic_mapping
    ADD CONSTRAINT topic_mapping_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 2539 (class 2606 OID 38484)
-- Name: topic_mapping_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE topic_mapping
    ADD CONSTRAINT topic_mapping_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- TOC entry 2540 (class 2606 OID 38489)
-- Name: topic_mapping_topic_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE topic_mapping
    ADD CONSTRAINT topic_mapping_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES topic(id);


--
-- TOC entry 2543 (class 2606 OID 38494)
-- Name: up_file_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE up_file
    ADD CONSTRAINT up_file_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 2544 (class 2606 OID 38499)
-- Name: up_file_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE up_file
    ADD CONSTRAINT up_file_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- TOC entry 2549 (class 2606 OID 38504)
-- Name: warnings_eac_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE warnings
    ADD CONSTRAINT warnings_eac_id_fkey FOREIGN KEY (eac_id) REFERENCES eac_cpf(id) ON DELETE CASCADE;


--
-- TOC entry 2551 (class 2606 OID 38514)
-- Name: warnings_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE warnings
    ADD CONSTRAINT warnings_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- TOC entry 2552 (class 2606 OID 38519)
-- Name: warnings_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE warnings
    ADD CONSTRAINT warnings_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- TOC entry 2553 (class 2606 OID 38524)
-- Name: warnings_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE warnings
    ADD CONSTRAINT warnings_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- TOC entry 2524 (class 2606 OID 38529)
-- Name: xsl_upload_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ingestionprofile
    ADD CONSTRAINT xsl_upload_id_fkey FOREIGN KEY (xsl_upload_id) REFERENCES xsl_upload(id);


--
-- TOC entry 2675 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: sa
--

--REVOKE ALL ON SCHEMA public FROM PUBLIC;
--REVOKE ALL ON SCHEMA public FROM sa;
--GRANT ALL ON SCHEMA public TO sa;
--GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-02-03 16:36:29

--
-- PostgreSQL database dump complete
--

