--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: ai_alternative_name; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ai_alternative_name (
    id integer NOT NULL,
    ai_a_name character varying(255) NOT NULL,
    ai_id integer NOT NULL,
    lng_id integer NOT NULL,
    primary_name boolean NOT NULL
);


ALTER TABLE public.ai_alternative_name OWNER TO postgres;

--
-- Name: ai_alternative_name_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ai_alternative_name_id_seq
    START WITH 355
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ai_alternative_name_id_seq OWNER TO postgres;

--
-- Name: ai_alternative_name_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ai_alternative_name_id_seq OWNED BY ai_alternative_name.id;


--
-- Name: api_key; Type: TABLE; Schema: public; Owner: apenet_dashboard; Tablespace: 
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


ALTER TABLE public.api_key OWNER TO apenet_dashboard;

--
-- Name: api_key_id_seq; Type: SEQUENCE; Schema: public; Owner: apenet_dashboard
--

CREATE SEQUENCE api_key_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.api_key_id_seq OWNER TO apenet_dashboard;

--
-- Name: api_key_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: apenet_dashboard
--

ALTER SEQUENCE api_key_id_seq OWNED BY api_key.id;


--
-- Name: archival_institution; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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
    feedback_email character varying(255) DEFAULT NULL::character varying,
    "openDataEnabled" boolean DEFAULT false,
    totalsolrdocsforopendata bigint,
    unprocessedsolrdocs bigint,
    opendataenabled boolean,
    totalsolrdocscount bigint,
    opendataqueueid integer
);


ALTER TABLE public.archival_institution OWNER TO postgres;

--
-- Name: archival_institution_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE archival_institution_id_seq
    START WITH 355
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.archival_institution_id_seq OWNER TO postgres;

--
-- Name: archival_institution_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE archival_institution_id_seq OWNED BY archival_institution.id;


--
-- Name: archival_institution_oai_pmh; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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
    new_harvesting timestamp without time zone DEFAULT now(),
    errors_response_path text,
    list_by_identifiers boolean DEFAULT false,
    harvesting_details text,
    harvesting_status character varying(255) DEFAULT NULL::character varying,
    locked boolean DEFAULT false
);


ALTER TABLE public.archival_institution_oai_pmh OWNER TO postgres;

--
-- Name: archival_institution_oai_pmh_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE archival_institution_oai_pmh_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.archival_institution_oai_pmh_id_seq OWNER TO postgres;

--
-- Name: archival_institution_oai_pmh_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE archival_institution_oai_pmh_id_seq OWNED BY archival_institution_oai_pmh.id;


--
-- Name: c_level; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.c_level OWNER TO postgres;

--
-- Name: c_level_cl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE c_level_cl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.c_level_cl_id_seq OWNER TO postgres;

--
-- Name: c_level_cl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE c_level_cl_id_seq OWNED BY c_level.id;


--
-- Name: c_level_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE c_level_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.c_level_id_seq OWNER TO postgres;

--
-- Name: c_level_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE c_level_id_seq OWNED BY c_level.id;


--
-- Name: collection; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.collection OWNER TO postgres;

--
-- Name: collection_content; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE collection_content (
    id bigint NOT NULL,
    id_collection bigint NOT NULL,
    id_search bigint,
    id_bookmarks bigint
);


ALTER TABLE public.collection_content OWNER TO postgres;

--
-- Name: collection_content_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE collection_content_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.collection_content_id_seq OWNER TO postgres;

--
-- Name: collection_content_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE collection_content_id_seq OWNED BY collection_content.id;


--
-- Name: collection_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE collection_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.collection_id_seq OWNER TO postgres;

--
-- Name: collection_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE collection_id_seq OWNED BY collection.id;


--
-- Name: coordinates; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE coordinates (
    id integer NOT NULL,
    ai_id integer NOT NULL,
    name_institution character varying NOT NULL,
    lat numeric,
    lon numeric,
    street character varying(255) NOT NULL,
    postalcity character varying(255) NOT NULL,
    country character varying(255) NOT NULL
);


ALTER TABLE public.coordinates OWNER TO postgres;

--
-- Name: coordinates_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE coordinates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.coordinates_id_seq OWNER TO postgres;

--
-- Name: coordinates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE coordinates_id_seq OWNED BY coordinates.id;


--
-- Name: cou_alternative_name; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cou_alternative_name (
    id integer NOT NULL,
    cou_an_name character varying(255) NOT NULL,
    cou_id integer NOT NULL,
    lng_id integer NOT NULL
);


ALTER TABLE public.cou_alternative_name OWNER TO postgres;

--
-- Name: cou_alternative_name_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE cou_alternative_name_id_seq
    START WITH 197
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.cou_alternative_name_id_seq OWNER TO postgres;

--
-- Name: cou_alternative_name_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE cou_alternative_name_id_seq OWNED BY cou_alternative_name.id;


--
-- Name: country; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE country (
    id integer NOT NULL,
    cname character varying(120) NOT NULL,
    isoname character varying(2) NOT NULL,
    al_order integer
);


ALTER TABLE public.country OWNER TO postgres;

--
-- Name: COLUMN country.isoname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN country.isoname IS 'iso3166-1';


--
-- Name: country_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE country_id_seq
    START WITH 15
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.country_id_seq OWNER TO postgres;

--
-- Name: country_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE country_id_seq OWNED BY country.id;


--
-- Name: dashboard_user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.dashboard_user OWNER TO postgres;

--
-- Name: dpt_update; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE dpt_update (
    id bigint NOT NULL,
    version character varying(255),
    new_version character varying(255)
);


ALTER TABLE public.dpt_update OWNER TO postgres;

--
-- Name: dpt_update_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE dpt_update_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.dpt_update_id_seq OWNER TO postgres;

--
-- Name: dpt_update_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE dpt_update_id_seq OWNED BY dpt_update.id;


--
-- Name: eac_cpf; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.eac_cpf OWNER TO postgres;

--
-- Name: eac_cpf_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE eac_cpf_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.eac_cpf_id_seq OWNER TO postgres;

--
-- Name: eac_cpf_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE eac_cpf_id_seq OWNED BY eac_cpf.id;


--
-- Name: ead3; Type: TABLE; Schema: public; Owner: apenet_dashboard; Tablespace: 
--

CREATE TABLE ead3 (
    id integer NOT NULL,
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
    totalnumberofunits bigint
);


ALTER TABLE public.ead3 OWNER TO apenet_dashboard;

--
-- Name: ead3_id_seq; Type: SEQUENCE; Schema: public; Owner: apenet_dashboard
--

CREATE SEQUENCE ead3_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ead3_id_seq OWNER TO apenet_dashboard;

--
-- Name: ead3_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: apenet_dashboard
--

ALTER SEQUENCE ead3_id_seq OWNED BY ead3.id;


--
-- Name: ead_content; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.ead_content OWNER TO postgres;

--
-- Name: ead_content_ec_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ead_content_ec_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ead_content_ec_id_seq OWNER TO postgres;

--
-- Name: ead_content_ec_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ead_content_ec_id_seq OWNED BY ead_content.id;


--
-- Name: ead_content_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ead_content_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ead_content_id_seq OWNER TO postgres;

--
-- Name: ead_content_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ead_content_id_seq OWNED BY ead_content.id;


--
-- Name: ead_saved_search; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ead_saved_search (
    id bigint NOT NULL,
    liferay_user_id bigint NOT NULL,
    modified_date timestamp without time zone DEFAULT now() NOT NULL,
    public_search boolean DEFAULT false NOT NULL,
    template boolean DEFAULT false NOT NULL,
    description character varying(255) DEFAULT NULL::character varying,
    search_term character varying(100) DEFAULT NULL::character varying,
    hierarchy boolean DEFAULT false NOT NULL,
    only_with_daos boolean DEFAULT false NOT NULL,
    method_optional boolean DEFAULT false NOT NULL,
    element character varying(1) DEFAULT NULL::character varying,
    typedocument character varying(2) DEFAULT NULL::character varying,
    fromdate character varying(10) DEFAULT NULL::character varying,
    todate character varying(10) DEFAULT NULL::character varying,
    exact_date_search boolean DEFAULT false NOT NULL,
    al_tree_selected_nodes text,
    refinement_country text,
    refinement_ai text,
    refinement_fond character varying(22) DEFAULT NULL::character varying,
    refinement_type character varying(9) DEFAULT NULL::character varying,
    refinement_level character varying(15) DEFAULT NULL::character varying,
    refinement_dao character varying(10) DEFAULT NULL::character varying,
    refinement_roledao character varying(40) DEFAULT NULL::character varying,
    refinement_date_type character varying(25) DEFAULT NULL::character varying,
    refinement_startdate character varying(12) DEFAULT NULL::character varying,
    refinement_enddate character varying(12) DEFAULT NULL::character varying,
    refinement_facet_settings character varying(255) DEFAULT NULL::character varying,
    refinement_topic text,
    topic text
);


ALTER TABLE public.ead_saved_search OWNER TO postgres;

--
-- Name: ead_saved_search_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ead_saved_search_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ead_saved_search_id_seq OWNER TO postgres;

--
-- Name: ead_saved_search_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ead_saved_search_id_seq OWNED BY ead_saved_search.id;


--
-- Name: ese; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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
    metadataformat character varying(20) DEFAULT NULL::character varying NOT NULL,
    number_of_web_resource integer DEFAULT 0
);


ALTER TABLE public.ese OWNER TO postgres;

--
-- Name: ese_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ese_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ese_id_seq OWNER TO postgres;

--
-- Name: ese_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ese_id_seq OWNED BY ese.id;


--
-- Name: ese_state; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ese_state (
    id integer NOT NULL,
    state character varying(20) NOT NULL
);


ALTER TABLE public.ese_state OWNER TO postgres;

--
-- Name: ese_state_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ese_state_id_seq
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ese_state_id_seq OWNER TO postgres;

--
-- Name: ese_state_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ese_state_id_seq OWNED BY ese_state.id;


--
-- Name: finding_aid; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.finding_aid OWNER TO postgres;

--
-- Name: finding_aid_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE finding_aid_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.finding_aid_id_seq OWNER TO postgres;

--
-- Name: finding_aid_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE finding_aid_id_seq OWNED BY finding_aid.id;


--
-- Name: ftp_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ftp_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ftp_id_seq OWNER TO postgres;

--
-- Name: ftp; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ftp (
    id integer DEFAULT nextval('ftp_id_seq'::regclass) NOT NULL,
    url character varying(256) NOT NULL,
    port integer,
    username character varying(60),
    ai_id integer NOT NULL
);


ALTER TABLE public.ftp OWNER TO postgres;

--
-- Name: hg_sg_fa_relation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hg_sg_fa_relation (
    id bigint NOT NULL,
    fa_id bigint NOT NULL,
    hg_id bigint,
    sg_id bigint,
    ai_id bigint,
    hg_sg_clevel_id bigint NOT NULL
);


ALTER TABLE public.hg_sg_fa_relation OWNER TO postgres;

--
-- Name: hg_sg_fa_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hg_sg_fa_relation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hg_sg_fa_relation_id_seq OWNER TO postgres;

--
-- Name: hg_sg_fa_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hg_sg_fa_relation_id_seq OWNED BY hg_sg_fa_relation.id;


--
-- Name: holdings_guide; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.holdings_guide OWNER TO postgres;

--
-- Name: holdings_guide_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE holdings_guide_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.holdings_guide_id_seq OWNER TO postgres;

--
-- Name: holdings_guide_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE holdings_guide_id_seq OWNED BY holdings_guide.id;


--
-- Name: queue; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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
    ai_id integer NOT NULL,
    ead3_id integer
);


ALTER TABLE public.queue OWNER TO postgres;

--
-- Name: index_queue_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE index_queue_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.index_queue_id_seq OWNER TO postgres;

--
-- Name: index_queue_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE index_queue_id_seq OWNED BY queue.id;


--
-- Name: ingestionprofile; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ingestionprofile (
    id bigint NOT NULL,
    name_profile character varying(100) NOT NULL,
    file_type integer DEFAULT 1 NOT NULL,
    upload_action integer DEFAULT 1 NOT NULL,
    exist_action integer DEFAULT 1 NOT NULL,
    dao_type integer DEFAULT 6 NOT NULL,
    dao_type_from_file boolean DEFAULT true NOT NULL,
    noeadid_action integer DEFAULT 1 NOT NULL,
    ai_id integer NOT NULL,
    europeana_dp character varying(100),
    europeana_dp_from_file boolean DEFAULT true,
    europeana_languages character varying(100),
    europeana_languages_from_file boolean DEFAULT true,
    europeana_license character varying(100),
    europeana_license_details character varying(100),
    europeana_dao_type integer,
    europeana_dao_type_from_file boolean DEFAULT true,
    europeana_add_rights character varying(200),
    europeana_inh_elements boolean DEFAULT false,
    europeana_inh_origin boolean DEFAULT false,
    europeana_conversion_type boolean DEFAULT true,
    europeana_inh_elements_check boolean DEFAULT true,
    europeana_inh_origin_check boolean DEFAULT true,
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


ALTER TABLE public.ingestionprofile OWNER TO postgres;

--
-- Name: ingestionprofile_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ingestionprofile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ingestionprofile_id_seq OWNER TO postgres;

--
-- Name: ingestionprofile_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ingestionprofile_id_seq OWNED BY ingestionprofile.id;


--
-- Name: lang; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE lang (
    id integer NOT NULL,
    lname character varying(20) NOT NULL,
    isoname character varying(3) NOT NULL,
    iso2name character varying(2) NOT NULL,
    lnativename character varying(20) NOT NULL
);


ALTER TABLE public.lang OWNER TO postgres;

--
-- Name: COLUMN lang.isoname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN lang.isoname IS 'iso639-2';


--
-- Name: lang_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE lang_id_seq
    START WITH 15
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lang_id_seq OWNER TO postgres;

--
-- Name: lang_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE lang_id_seq OWNED BY lang.id;


--
-- Name: pk_seq; Type: SEQUENCE; Schema: public; Owner: apenet_dashboard
--

CREATE SEQUENCE pk_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.pk_seq OWNER TO apenet_dashboard;

--
-- Name: reindex_doc; Type: TABLE; Schema: public; Owner: apenet_dashboard; Tablespace: 
--

CREATE TABLE reindex_doc (
    id integer NOT NULL,
    ai_id integer,
    archivalinstitution bytea,
    holdingsguide bytea,
    eac_cpf_id integer,
    ead3_id integer,
    fa_id integer,
    sg_id integer
);


ALTER TABLE public.reindex_doc OWNER TO apenet_dashboard;

--
-- Name: reindex_doc_id_seq; Type: SEQUENCE; Schema: public; Owner: apenet_dashboard
--

CREATE SEQUENCE reindex_doc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reindex_doc_id_seq OWNER TO apenet_dashboard;

--
-- Name: reindex_doc_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: apenet_dashboard
--

ALTER SEQUENCE reindex_doc_id_seq OWNED BY reindex_doc.id;


--
-- Name: resumption_token; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE resumption_token (
    id integer NOT NULL,
    expiration_date timestamp with time zone NOT NULL,
    from_date timestamp with time zone NOT NULL,
    until_date timestamp with time zone NOT NULL,
    set character varying(100),
    last_record_harvested character varying(100) NOT NULL,
    metadataformat character varying(20) DEFAULT NULL::character varying NOT NULL
);


ALTER TABLE public.resumption_token OWNER TO postgres;

--
-- Name: resumption_token_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE resumption_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.resumption_token_id_seq OWNER TO postgres;

--
-- Name: resumption_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE resumption_token_id_seq OWNED BY resumption_token.id;


--
-- Name: saved_bookmarks; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE saved_bookmarks (
    id bigint NOT NULL,
    liferay_user_id bigint NOT NULL,
    modified_date timestamp without time zone DEFAULT now() NOT NULL,
    bookmark_name text DEFAULT NULL::character varying,
    description text DEFAULT NULL::character varying,
    persistent_link text DEFAULT NULL::character varying,
    typedocument character varying(20) DEFAULT NULL::character varying
);


ALTER TABLE public.saved_bookmarks OWNER TO postgres;

--
-- Name: saved_bookmarks_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE saved_bookmarks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.saved_bookmarks_id_seq OWNER TO postgres;

--
-- Name: saved_bookmarks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE saved_bookmarks_id_seq OWNED BY saved_bookmarks.id;


--
-- Name: sent_mail_register; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sent_mail_register (
    id integer NOT NULL,
    validation_link character varying(100) NOT NULL,
    date date NOT NULL,
    email_address character varying(100) NOT NULL,
    user_id integer,
    ai_id integer
);


ALTER TABLE public.sent_mail_register OWNER TO postgres;

--
-- Name: sent_mail_register_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sent_mail_register_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sent_mail_register_id_seq OWNER TO postgres;

--
-- Name: sent_mail_register_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sent_mail_register_id_seq OWNED BY sent_mail_register.id;


--
-- Name: source_guide; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.source_guide OWNER TO postgres;

--
-- Name: source_guide_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE source_guide_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.source_guide_id_seq OWNER TO postgres;

--
-- Name: source_guide_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE source_guide_id_seq OWNED BY source_guide.id;


--
-- Name: source_guide_sg_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE source_guide_sg_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.source_guide_sg_id_seq OWNER TO postgres;

--
-- Name: source_guide_sg_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE source_guide_sg_id_seq OWNED BY source_guide.id;


--
-- Name: topic; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE topic (
    id bigint NOT NULL,
    property_key character varying(40) NOT NULL,
    description character varying(100) NOT NULL
);


ALTER TABLE public.topic OWNER TO postgres;

--
-- Name: topic_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE topic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.topic_id_seq OWNER TO postgres;

--
-- Name: topic_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE topic_id_seq OWNED BY topic.id;


--
-- Name: topic_mapping; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE topic_mapping (
    id bigint NOT NULL,
    ai_id integer,
    topic_id bigint NOT NULL,
    controlaccess_keyword text DEFAULT NULL::character varying,
    sg_id bigint,
    country_id integer
);


ALTER TABLE public.topic_mapping OWNER TO postgres;

--
-- Name: topic_mapping_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE topic_mapping_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.topic_mapping_id_seq OWNER TO postgres;

--
-- Name: topic_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE topic_mapping_id_seq OWNED BY topic_mapping.id;


--
-- Name: up_file; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE up_file (
    id integer NOT NULL,
    path character varying(255) NOT NULL,
    um_id integer NOT NULL,
    ai_id integer NOT NULL,
    filename character varying(255) NOT NULL,
    file_type character varying(3) DEFAULT NULL::character varying NOT NULL
);


ALTER TABLE public.up_file OWNER TO postgres;

--
-- Name: up_file_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE up_file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.up_file_id_seq OWNER TO postgres;

--
-- Name: up_file_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE up_file_id_seq OWNED BY up_file.id;


--
-- Name: upload_method; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE upload_method (
    id integer NOT NULL,
    method character varying(20) NOT NULL
);


ALTER TABLE public.upload_method OWNER TO postgres;

--
-- Name: upload_method_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE upload_method_id_seq
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.upload_method_id_seq OWNER TO postgres;

--
-- Name: upload_method_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE upload_method_id_seq OWNED BY upload_method.id;


--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_id_seq OWNER TO postgres;

--
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE user_id_seq OWNED BY dashboard_user.id;


--
-- Name: user_role; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE user_role (
    id integer NOT NULL,
    role character varying(40) NOT NULL
);


ALTER TABLE public.user_role OWNER TO postgres;

--
-- Name: user_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE user_role_id_seq
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_role_id_seq OWNER TO postgres;

--
-- Name: user_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE user_role_id_seq OWNED BY user_role.id;


--
-- Name: warnings; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE warnings (
    id integer NOT NULL,
    abstract text NOT NULL,
    hg_id integer,
    fa_id integer,
    sg_id integer,
    iswarning boolean,
    eac_id integer,
    ead3_id integer
);


ALTER TABLE public.warnings OWNER TO postgres;

--
-- Name: warnings_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE warnings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.warnings_id_seq OWNER TO postgres;

--
-- Name: warnings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE warnings_id_seq OWNED BY warnings.id;


--
-- Name: xsl_upload; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE xsl_upload (
    id integer NOT NULL,
    readable_name character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    archival_institution_id integer NOT NULL
);


ALTER TABLE public.xsl_upload OWNER TO postgres;

--
-- Name: xsl_upload_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE xsl_upload_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.xsl_upload_id_seq OWNER TO postgres;

--
-- Name: xsl_upload_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE xsl_upload_id_seq OWNED BY xsl_upload.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ai_alternative_name ALTER COLUMN id SET DEFAULT nextval('ai_alternative_name_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: apenet_dashboard
--

ALTER TABLE ONLY api_key ALTER COLUMN id SET DEFAULT nextval('api_key_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution ALTER COLUMN id SET DEFAULT nextval('archival_institution_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution_oai_pmh ALTER COLUMN id SET DEFAULT nextval('archival_institution_oai_pmh_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY c_level ALTER COLUMN id SET DEFAULT nextval('c_level_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY collection ALTER COLUMN id SET DEFAULT nextval('collection_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY collection_content ALTER COLUMN id SET DEFAULT nextval('collection_content_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY coordinates ALTER COLUMN id SET DEFAULT nextval('coordinates_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cou_alternative_name ALTER COLUMN id SET DEFAULT nextval('cou_alternative_name_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY country ALTER COLUMN id SET DEFAULT nextval('country_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dashboard_user ALTER COLUMN id SET DEFAULT nextval('user_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dpt_update ALTER COLUMN id SET DEFAULT nextval('dpt_update_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY eac_cpf ALTER COLUMN id SET DEFAULT nextval('eac_cpf_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: apenet_dashboard
--

ALTER TABLE ONLY ead3 ALTER COLUMN id SET DEFAULT nextval('ead3_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ead_content ALTER COLUMN id SET DEFAULT nextval('ead_content_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ead_saved_search ALTER COLUMN id SET DEFAULT nextval('ead_saved_search_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ese ALTER COLUMN id SET DEFAULT nextval('ese_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ese_state ALTER COLUMN id SET DEFAULT nextval('ese_state_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY finding_aid ALTER COLUMN id SET DEFAULT nextval('finding_aid_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation ALTER COLUMN id SET DEFAULT nextval('hg_sg_fa_relation_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY holdings_guide ALTER COLUMN id SET DEFAULT nextval('holdings_guide_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ingestionprofile ALTER COLUMN id SET DEFAULT nextval('ingestionprofile_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lang ALTER COLUMN id SET DEFAULT nextval('lang_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue ALTER COLUMN id SET DEFAULT nextval('index_queue_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: apenet_dashboard
--

ALTER TABLE ONLY reindex_doc ALTER COLUMN id SET DEFAULT nextval('reindex_doc_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY resumption_token ALTER COLUMN id SET DEFAULT nextval('resumption_token_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY saved_bookmarks ALTER COLUMN id SET DEFAULT nextval('saved_bookmarks_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sent_mail_register ALTER COLUMN id SET DEFAULT nextval('sent_mail_register_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY source_guide ALTER COLUMN id SET DEFAULT nextval('source_guide_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic ALTER COLUMN id SET DEFAULT nextval('topic_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic_mapping ALTER COLUMN id SET DEFAULT nextval('topic_mapping_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY up_file ALTER COLUMN id SET DEFAULT nextval('up_file_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY upload_method ALTER COLUMN id SET DEFAULT nextval('upload_method_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_role ALTER COLUMN id SET DEFAULT nextval('user_role_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings ALTER COLUMN id SET DEFAULT nextval('warnings_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY xsl_upload ALTER COLUMN id SET DEFAULT nextval('xsl_upload_id_seq'::regclass);


--
-- Name: alternative_name_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ai_alternative_name
    ADD CONSTRAINT alternative_name_pkey PRIMARY KEY (id);


--
-- Name: api_key_pkey; Type: CONSTRAINT; Schema: public; Owner: apenet_dashboard; Tablespace: 
--

ALTER TABLE ONLY api_key
    ADD CONSTRAINT api_key_pkey PRIMARY KEY (id);


--
-- Name: archival_institution_oai_pmh_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY archival_institution_oai_pmh
    ADD CONSTRAINT archival_institution_oai_pmh_pkey PRIMARY KEY (id);


--
-- Name: archival_institution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_pkey PRIMARY KEY (id);


--
-- Name: c_level_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY c_level
    ADD CONSTRAINT c_level_pkey PRIMARY KEY (id);


--
-- Name: collection_content_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY collection_content
    ADD CONSTRAINT collection_content_pkey PRIMARY KEY (id);


--
-- Name: collection_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY collection
    ADD CONSTRAINT collection_pkey PRIMARY KEY (id);


--
-- Name: coordinates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY coordinates
    ADD CONSTRAINT coordinates_pkey PRIMARY KEY (id);


--
-- Name: cou_alternative_name_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cou_alternative_name
    ADD CONSTRAINT cou_alternative_name_pkey PRIMARY KEY (id);


--
-- Name: country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);


--
-- Name: dashboard_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY dashboard_user
    ADD CONSTRAINT dashboard_user_pkey PRIMARY KEY (id);


--
-- Name: dpt_update_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY dpt_update
    ADD CONSTRAINT dpt_update_pkey PRIMARY KEY (id);


--
-- Name: eac_cpf_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eac_cpf
    ADD CONSTRAINT eac_cpf_pkey PRIMARY KEY (id);


--
-- Name: ead3_pkey; Type: CONSTRAINT; Schema: public; Owner: apenet_dashboard; Tablespace: 
--

ALTER TABLE ONLY ead3
    ADD CONSTRAINT ead3_pkey PRIMARY KEY (id);


--
-- Name: ead_content_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_pkey PRIMARY KEY (id);


--
-- Name: ead_saved_search_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ead_saved_search
    ADD CONSTRAINT ead_saved_search_pkey PRIMARY KEY (id);


--
-- Name: ese_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ese
    ADD CONSTRAINT ese_pkey PRIMARY KEY (id);


--
-- Name: ese_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ese_state
    ADD CONSTRAINT ese_state_pkey PRIMARY KEY (id);


--
-- Name: finding_aid_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY finding_aid
    ADD CONSTRAINT finding_aid_pkey PRIMARY KEY (id);


--
-- Name: ftp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ftp
    ADD CONSTRAINT ftp_pkey PRIMARY KEY (id);


--
-- Name: hg_sg_fa_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_pkey PRIMARY KEY (id);


--
-- Name: holdings_guide_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY holdings_guide
    ADD CONSTRAINT holdings_guide_pkey PRIMARY KEY (id);


--
-- Name: ingestionprofile_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ingestionprofile
    ADD CONSTRAINT ingestionprofile_pkey PRIMARY KEY (id);


--
-- Name: lang_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY lang
    ADD CONSTRAINT lang_pkey PRIMARY KEY (id);


--
-- Name: partner_email_address_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY dashboard_user
    ADD CONSTRAINT partner_email_address_unique UNIQUE (email_address);


--
-- Name: queue_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT queue_pkey PRIMARY KEY (id);


--
-- Name: reindex_doc_pkey; Type: CONSTRAINT; Schema: public; Owner: apenet_dashboard; Tablespace: 
--

ALTER TABLE ONLY reindex_doc
    ADD CONSTRAINT reindex_doc_pkey PRIMARY KEY (id);


--
-- Name: resumption_token_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY resumption_token
    ADD CONSTRAINT resumption_token_pkey PRIMARY KEY (id);


--
-- Name: role_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT role_type_pkey PRIMARY KEY (id);


--
-- Name: saved_bookmarks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY saved_bookmarks
    ADD CONSTRAINT saved_bookmarks_pkey PRIMARY KEY (id);


--
-- Name: sent_mail_register_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sent_mail_register
    ADD CONSTRAINT sent_mail_register_pkey PRIMARY KEY (id);


--
-- Name: source_guide_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY source_guide
    ADD CONSTRAINT source_guide_pkey PRIMARY KEY (id);


--
-- Name: topic_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY topic_mapping
    ADD CONSTRAINT topic_mapping_pkey PRIMARY KEY (id);


--
-- Name: topic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY topic
    ADD CONSTRAINT topic_pkey PRIMARY KEY (id);


--
-- Name: up_file_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY up_file
    ADD CONSTRAINT up_file_pkey PRIMARY KEY (id);


--
-- Name: upload_method_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY upload_method
    ADD CONSTRAINT upload_method_pkey PRIMARY KEY (id);


--
-- Name: warnings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_pkey PRIMARY KEY (id);


--
-- Name: xsl_upload_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY xsl_upload
    ADD CONSTRAINT xsl_upload_pkey PRIMARY KEY (id);


--
-- Name: archival_institution_repositorycode; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX archival_institution_repositorycode ON archival_institution USING btree (repositorycode);


--
-- Name: c_level__cid_ec_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__cid_ec_id_idx ON c_level USING btree (cid, ec_id);


--
-- Name: c_level__eadid_ref_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__eadid_ref_idx ON c_level USING btree (ec_id, href_eadid) WHERE (href_eadid IS NOT NULL);


--
-- Name: c_level__parent_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__parent_idx ON c_level USING btree (parent_cl_id, order_id) WHERE (parent_cl_id IS NOT NULL);


--
-- Name: c_level__persistent_link_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__persistent_link_idx ON c_level USING btree (unitid, ec_id) WHERE (duplicate_unitid = false);


--
-- Name: c_level__top_levels_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__top_levels_idx ON c_level USING btree (order_id, ec_id) WHERE (parent_cl_id IS NULL);


--
-- Name: collection__liferay_user_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX collection__liferay_user_idx ON collection USING btree (liferay_user_id);


--
-- Name: eac_cpf__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eac_cpf__archival_institution_idx ON eac_cpf USING btree (ai_id);


--
-- Name: ead_content__finding_aid_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ead_content__finding_aid_idx ON ead_content USING btree (fa_id);


--
-- Name: ead_content__holdings_guide_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ead_content__holdings_guide_idx ON ead_content USING btree (hg_id);


--
-- Name: ead_saved_search__liferay_user_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ead_saved_search__liferay_user_idx ON ead_saved_search USING btree (liferay_user_id);


--
-- Name: ese_metadataformat; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ese_metadataformat ON ese USING btree (metadataformat);


--
-- Name: finding_aid__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid__archival_institution_idx ON finding_aid USING btree (ai_id);


--
-- Name: finding_aid_dynamic; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid_dynamic ON finding_aid USING btree (dynamic);


--
-- Name: finding_aid_eadid; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid_eadid ON finding_aid USING btree (eadid);


--
-- Name: finding_aid_path; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid_path ON finding_aid USING btree (path_apenetead);


--
-- Name: finding_aid_searchable; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid_searchable ON finding_aid USING btree (published);


--
-- Name: finding_aid_title; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid_title ON finding_aid USING btree (title);


--
-- Name: hg_sg_fa_relation__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hg_sg_fa_relation__archival_institution_idx ON hg_sg_fa_relation USING btree (ai_id);


--
-- Name: hg_sg_fa_relation__c_level_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hg_sg_fa_relation__c_level_idx ON hg_sg_fa_relation USING btree (hg_sg_clevel_id);


--
-- Name: hg_sg_fa_relation__finding_aid_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hg_sg_fa_relation__finding_aid_idx ON hg_sg_fa_relation USING btree (fa_id);


--
-- Name: hg_sg_fa_relation__holdings_guide_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hg_sg_fa_relation__holdings_guide_idx ON hg_sg_fa_relation USING btree (hg_id);


--
-- Name: hg_sg_fa_relation__source_guide_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hg_sg_fa_relation__source_guide_idx ON hg_sg_fa_relation USING btree (sg_id);


--
-- Name: holdings_guide__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX holdings_guide__archival_institution_idx ON holdings_guide USING btree (ai_id);


--
-- Name: holdings_guide_dynamic; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX holdings_guide_dynamic ON holdings_guide USING btree (dynamic);


--
-- Name: holdings_guide_path; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX holdings_guide_path ON holdings_guide USING btree (path_apenetead);


--
-- Name: holdings_guide_searchable; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX holdings_guide_searchable ON holdings_guide USING btree (published);


--
-- Name: queue_uf_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX queue_uf_id ON queue USING btree (uf_id);


--
-- Name: resumption_token_metadataformat; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX resumption_token_metadataformat ON ese USING btree (metadataformat);


--
-- Name: saved_bookmarks__liferay_user_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX saved_bookmarks__liferay_user_idx ON saved_bookmarks USING btree (liferay_user_id);


--
-- Name: source_guide__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX source_guide__archival_institution_idx ON source_guide USING btree (ai_id);


--
-- Name: source_guide_dynamic; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX source_guide_dynamic ON source_guide USING btree (dynamic);


--
-- Name: source_guide_path; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX source_guide_path ON source_guide USING btree (path_apenetead);


--
-- Name: source_guide_searchable; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX source_guide_searchable ON source_guide USING btree (published);


--
-- Name: topic__description_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX topic__description_idx ON topic USING btree (description);


--
-- Name: topic__property_key_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX topic__property_key_idx ON topic USING btree (property_key);


--
-- Name: topic_mapping__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX topic_mapping__archival_institution_idx ON topic_mapping USING btree (ai_id);


--
-- Name: topic_mapping__source_guide_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX topic_mapping__source_guide_idx ON topic_mapping USING btree (sg_id);


--
-- Name: up_file_ai_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX up_file_ai_id ON up_file USING btree (ai_id);


--
-- Name: up_file_file_type; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX up_file_file_type ON up_file USING btree (file_type);


--
-- Name: ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ftp
    ADD CONSTRAINT ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- Name: archival_institution_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ai_alternative_name
    ADD CONSTRAINT archival_institution_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- Name: archival_institution_cou_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_cou_id_fkey FOREIGN KEY (country_id) REFERENCES country(id);


--
-- Name: archival_institution_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY xsl_upload
    ADD CONSTRAINT archival_institution_id_fkey FOREIGN KEY (archival_institution_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- Name: archival_institution_oai_pmh_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution_oai_pmh
    ADD CONSTRAINT archival_institution_oai_pmh_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- Name: archival_institution_p_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_p_id_fkey FOREIGN KEY (user_id) REFERENCES dashboard_user(id);


--
-- Name: archival_institution_parent_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_parent_ai_id_fkey FOREIGN KEY (parent_ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- Name: archival_institutition_oai_pmh_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution_oai_pmh
    ADD CONSTRAINT archival_institutition_oai_pmh_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES ingestionprofile(id);


--
-- Name: c_level_ec_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY c_level
    ADD CONSTRAINT c_level_ec_id_fkey FOREIGN KEY (ec_id) REFERENCES ead_content(id) ON DELETE CASCADE;


--
-- Name: c_level_parent_cl_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY c_level
    ADD CONSTRAINT c_level_parent_cl_id_fkey FOREIGN KEY (parent_cl_id) REFERENCES c_level(id) ON DELETE CASCADE;


--
-- Name: collection_content_fkey_collection; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY collection_content
    ADD CONSTRAINT collection_content_fkey_collection FOREIGN KEY (id_collection) REFERENCES collection(id) ON DELETE CASCADE;


--
-- Name: collection_content_fkey_saved_bookmarks; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY collection_content
    ADD CONSTRAINT collection_content_fkey_saved_bookmarks FOREIGN KEY (id_bookmarks) REFERENCES saved_bookmarks(id) ON DELETE CASCADE;


--
-- Name: collection_content_fkey_search; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY collection_content
    ADD CONSTRAINT collection_content_fkey_search FOREIGN KEY (id_search) REFERENCES ead_saved_search(id) ON DELETE CASCADE;


--
-- Name: coordinates_archival_institution_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY coordinates
    ADD CONSTRAINT coordinates_archival_institution_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- Name: country_cou_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cou_alternative_name
    ADD CONSTRAINT country_cou_id_fkey FOREIGN KEY (cou_id) REFERENCES country(id);


--
-- Name: country_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic_mapping
    ADD CONSTRAINT country_id_fkey FOREIGN KEY (country_id) REFERENCES country(id) ON DELETE CASCADE;


--
-- Name: eac_cpf_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY eac_cpf
    ADD CONSTRAINT eac_cpf_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- Name: eac_cpf_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY eac_cpf
    ADD CONSTRAINT eac_cpf_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- Name: ead3_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: apenet_dashboard
--

ALTER TABLE ONLY ead3
    ADD CONSTRAINT ead3_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- Name: ead3_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: apenet_dashboard
--

ALTER TABLE ONLY ead3
    ADD CONSTRAINT ead3_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- Name: ead_content_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- Name: ead_content_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- Name: ead_content_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- Name: ese_es_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ese
    ADD CONSTRAINT ese_es_id_fkey FOREIGN KEY (es_id) REFERENCES ese_state(id);


--
-- Name: ese_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ese
    ADD CONSTRAINT ese_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id);


--
-- Name: finding_aid_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY finding_aid
    ADD CONSTRAINT finding_aid_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- Name: finding_aid_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY finding_aid
    ADD CONSTRAINT finding_aid_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- Name: hg_sg_fa_relation_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- Name: hg_sg_fa_relation_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- Name: hg_sg_fa_relation_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- Name: hg_sg_fa_relation_hg_sg_clevel_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_hg_sg_clevel_id_fkey FOREIGN KEY (hg_sg_clevel_id) REFERENCES c_level(id) ON DELETE CASCADE;


--
-- Name: hg_sg_fa_relation_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- Name: holdings_guide_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY holdings_guide
    ADD CONSTRAINT holdings_guide_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- Name: holdings_guide_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY holdings_guide
    ADD CONSTRAINT holdings_guide_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- Name: index_queue_eac_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT index_queue_eac_id_fkey FOREIGN KEY (eac_cpf_id) REFERENCES eac_cpf(id) ON DELETE CASCADE;


--
-- Name: index_queue_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT index_queue_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- Name: index_queue_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT index_queue_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- Name: index_queue_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT index_queue_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- Name: ingestionprofile_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ingestionprofile
    ADD CONSTRAINT ingestionprofile_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- Name: lang_lng_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ai_alternative_name
    ADD CONSTRAINT lang_lng_id_fkey FOREIGN KEY (lng_id) REFERENCES lang(id);


--
-- Name: lng_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cou_alternative_name
    ADD CONSTRAINT lng_id FOREIGN KEY (lng_id) REFERENCES lang(id);


--
-- Name: partner_cou_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dashboard_user
    ADD CONSTRAINT partner_cou_id_fkey FOREIGN KEY (country_id) REFERENCES country(id);


--
-- Name: partner_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dashboard_user
    ADD CONSTRAINT partner_role_id_fkey FOREIGN KEY (user_role_id) REFERENCES user_role(id);


--
-- Name: queue_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT queue_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- Name: queue_uf_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT queue_uf_id_fkey FOREIGN KEY (uf_id) REFERENCES up_file(id) ON DELETE CASCADE;


--
-- Name: sent_mail_register_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sent_mail_register
    ADD CONSTRAINT sent_mail_register_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- Name: sent_mail_register_p_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sent_mail_register
    ADD CONSTRAINT sent_mail_register_p_id_fkey FOREIGN KEY (user_id) REFERENCES dashboard_user(id) ON DELETE CASCADE;


--
-- Name: source_guide_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY source_guide
    ADD CONSTRAINT source_guide_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- Name: source_guide_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY source_guide
    ADD CONSTRAINT source_guide_sg_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- Name: source_guide_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY source_guide
    ADD CONSTRAINT source_guide_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- Name: topic_mapping_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic_mapping
    ADD CONSTRAINT topic_mapping_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- Name: topic_mapping_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic_mapping
    ADD CONSTRAINT topic_mapping_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- Name: topic_mapping_topic_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic_mapping
    ADD CONSTRAINT topic_mapping_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES topic(id);


--
-- Name: up_file_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY up_file
    ADD CONSTRAINT up_file_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- Name: up_file_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY up_file
    ADD CONSTRAINT up_file_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- Name: warnings_eac_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_eac_id_fkey FOREIGN KEY (eac_id) REFERENCES eac_cpf(id) ON DELETE CASCADE;


--
-- Name: warnings_ec_id_fkey -> eac_cpf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT "warnings_ec_id_fkey -> eac_cpf" FOREIGN KEY (eac_id) REFERENCES eac_cpf(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: warnings_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- Name: warnings_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- Name: warnings_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- Name: xsl_upload_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ingestionprofile
    ADD CONSTRAINT xsl_upload_id_fkey FOREIGN KEY (xsl_upload_id) REFERENCES xsl_upload(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: ai_alternative_name; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ai_alternative_name FROM PUBLIC;
REVOKE ALL ON TABLE ai_alternative_name FROM postgres;
GRANT ALL ON TABLE ai_alternative_name TO postgres;
GRANT ALL ON TABLE ai_alternative_name TO admin;
GRANT ALL ON TABLE ai_alternative_name TO apenet_dashboard;
GRANT SELECT ON TABLE ai_alternative_name TO apenet_portal;


--
-- Name: ai_alternative_name_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ai_alternative_name_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ai_alternative_name_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ai_alternative_name_id_seq TO postgres;
GRANT ALL ON SEQUENCE ai_alternative_name_id_seq TO admin;
GRANT ALL ON SEQUENCE ai_alternative_name_id_seq TO apenet_dashboard;


--
-- Name: archival_institution; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE archival_institution FROM PUBLIC;
REVOKE ALL ON TABLE archival_institution FROM postgres;
GRANT ALL ON TABLE archival_institution TO postgres;
GRANT ALL ON TABLE archival_institution TO admin;
GRANT ALL ON TABLE archival_institution TO apenet_dashboard;
GRANT SELECT ON TABLE archival_institution TO apenet_portal;


--
-- Name: archival_institution_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE archival_institution_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE archival_institution_id_seq FROM postgres;
GRANT ALL ON SEQUENCE archival_institution_id_seq TO postgres;
GRANT ALL ON SEQUENCE archival_institution_id_seq TO admin;
GRANT ALL ON SEQUENCE archival_institution_id_seq TO apenet_dashboard;


--
-- Name: archival_institution_oai_pmh; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE archival_institution_oai_pmh FROM PUBLIC;
REVOKE ALL ON TABLE archival_institution_oai_pmh FROM postgres;
GRANT ALL ON TABLE archival_institution_oai_pmh TO postgres;
GRANT ALL ON TABLE archival_institution_oai_pmh TO admin;
GRANT ALL ON TABLE archival_institution_oai_pmh TO apenet_dashboard;


--
-- Name: archival_institution_oai_pmh_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE archival_institution_oai_pmh_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE archival_institution_oai_pmh_id_seq FROM postgres;
GRANT ALL ON SEQUENCE archival_institution_oai_pmh_id_seq TO postgres;
GRANT ALL ON SEQUENCE archival_institution_oai_pmh_id_seq TO admin;
GRANT ALL ON SEQUENCE archival_institution_oai_pmh_id_seq TO apenet_dashboard;


--
-- Name: c_level; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE c_level FROM PUBLIC;
REVOKE ALL ON TABLE c_level FROM postgres;
GRANT ALL ON TABLE c_level TO postgres;
GRANT ALL ON TABLE c_level TO admin;
GRANT ALL ON TABLE c_level TO apenet_dashboard;
GRANT SELECT ON TABLE c_level TO apenet_portal;


--
-- Name: c_level_cl_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE c_level_cl_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE c_level_cl_id_seq FROM postgres;
GRANT ALL ON SEQUENCE c_level_cl_id_seq TO postgres;
GRANT ALL ON SEQUENCE c_level_cl_id_seq TO admin;
GRANT ALL ON SEQUENCE c_level_cl_id_seq TO apenet_dashboard;


--
-- Name: c_level_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE c_level_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE c_level_id_seq FROM postgres;
GRANT ALL ON SEQUENCE c_level_id_seq TO postgres;
GRANT ALL ON SEQUENCE c_level_id_seq TO admin;
GRANT ALL ON SEQUENCE c_level_id_seq TO apenet_dashboard;


--
-- Name: collection; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE collection FROM PUBLIC;
REVOKE ALL ON TABLE collection FROM postgres;
GRANT ALL ON TABLE collection TO postgres;
GRANT ALL ON TABLE collection TO admin;
GRANT ALL ON TABLE collection TO apenet_dashboard;
GRANT ALL ON TABLE collection TO apenet_portal;


--
-- Name: collection_content; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE collection_content FROM PUBLIC;
REVOKE ALL ON TABLE collection_content FROM postgres;
GRANT ALL ON TABLE collection_content TO postgres;
GRANT ALL ON TABLE collection_content TO admin;
GRANT ALL ON TABLE collection_content TO apenet_dashboard;
GRANT ALL ON TABLE collection_content TO apenet_portal;


--
-- Name: collection_content_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE collection_content_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE collection_content_id_seq FROM postgres;
GRANT ALL ON SEQUENCE collection_content_id_seq TO postgres;
GRANT ALL ON SEQUENCE collection_content_id_seq TO admin;
GRANT ALL ON SEQUENCE collection_content_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE collection_content_id_seq TO apenet_portal;


--
-- Name: collection_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE collection_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE collection_id_seq FROM postgres;
GRANT ALL ON SEQUENCE collection_id_seq TO postgres;
GRANT ALL ON SEQUENCE collection_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE collection_id_seq TO apenet_portal;
GRANT ALL ON SEQUENCE collection_id_seq TO admin;


--
-- Name: coordinates; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE coordinates FROM PUBLIC;
REVOKE ALL ON TABLE coordinates FROM postgres;
GRANT ALL ON TABLE coordinates TO postgres;
GRANT ALL ON TABLE coordinates TO admin;
GRANT ALL ON TABLE coordinates TO apenet_dashboard;
GRANT SELECT ON TABLE coordinates TO apenet_portal;


--
-- Name: coordinates_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE coordinates_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE coordinates_id_seq FROM postgres;
GRANT ALL ON SEQUENCE coordinates_id_seq TO postgres;
GRANT ALL ON SEQUENCE coordinates_id_seq TO apenet_dashboard;
GRANT SELECT ON SEQUENCE coordinates_id_seq TO apenet_portal;
GRANT ALL ON SEQUENCE coordinates_id_seq TO admin;


--
-- Name: cou_alternative_name; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cou_alternative_name FROM PUBLIC;
REVOKE ALL ON TABLE cou_alternative_name FROM postgres;
GRANT ALL ON TABLE cou_alternative_name TO postgres;
GRANT ALL ON TABLE cou_alternative_name TO admin;
GRANT ALL ON TABLE cou_alternative_name TO apenet_dashboard;
GRANT SELECT ON TABLE cou_alternative_name TO apenet_portal;


--
-- Name: cou_alternative_name_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE cou_alternative_name_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE cou_alternative_name_id_seq FROM postgres;
GRANT ALL ON SEQUENCE cou_alternative_name_id_seq TO postgres;
GRANT ALL ON SEQUENCE cou_alternative_name_id_seq TO admin;
GRANT ALL ON SEQUENCE cou_alternative_name_id_seq TO apenet_dashboard;


--
-- Name: country; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE country FROM PUBLIC;
REVOKE ALL ON TABLE country FROM postgres;
GRANT ALL ON TABLE country TO postgres;
GRANT ALL ON TABLE country TO admin;
GRANT ALL ON TABLE country TO apenet_dashboard;
GRANT SELECT ON TABLE country TO apenet_portal;


--
-- Name: country_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE country_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE country_id_seq FROM postgres;
GRANT ALL ON SEQUENCE country_id_seq TO postgres;
GRANT ALL ON SEQUENCE country_id_seq TO admin;
GRANT ALL ON SEQUENCE country_id_seq TO apenet_dashboard;


--
-- Name: dashboard_user; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE dashboard_user FROM PUBLIC;
REVOKE ALL ON TABLE dashboard_user FROM postgres;
GRANT ALL ON TABLE dashboard_user TO postgres;
GRANT ALL ON TABLE dashboard_user TO admin;
GRANT ALL ON TABLE dashboard_user TO apenet_dashboard;
GRANT SELECT ON TABLE dashboard_user TO apenet_portal;


--
-- Name: dpt_update; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE dpt_update FROM PUBLIC;
REVOKE ALL ON TABLE dpt_update FROM postgres;
GRANT ALL ON TABLE dpt_update TO postgres;
GRANT ALL ON TABLE dpt_update TO admin;
GRANT ALL ON TABLE dpt_update TO apenet_dashboard;
GRANT SELECT ON TABLE dpt_update TO apenet_portal;


--
-- Name: dpt_update_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE dpt_update_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE dpt_update_id_seq FROM postgres;
GRANT ALL ON SEQUENCE dpt_update_id_seq TO postgres;
GRANT ALL ON SEQUENCE dpt_update_id_seq TO admin;
GRANT ALL ON SEQUENCE dpt_update_id_seq TO apenet_dashboard;


--
-- Name: eac_cpf; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE eac_cpf FROM PUBLIC;
REVOKE ALL ON TABLE eac_cpf FROM postgres;
GRANT ALL ON TABLE eac_cpf TO postgres;
GRANT ALL ON TABLE eac_cpf TO admin;
GRANT ALL ON TABLE eac_cpf TO apenet_dashboard;
GRANT SELECT ON TABLE eac_cpf TO apenet_portal;


--
-- Name: eac_cpf_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE eac_cpf_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE eac_cpf_id_seq FROM postgres;
GRANT ALL ON SEQUENCE eac_cpf_id_seq TO postgres;
GRANT ALL ON SEQUENCE eac_cpf_id_seq TO admin;
GRANT ALL ON SEQUENCE eac_cpf_id_seq TO apenet_dashboard;


--
-- Name: ead_content; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ead_content FROM PUBLIC;
REVOKE ALL ON TABLE ead_content FROM postgres;
GRANT ALL ON TABLE ead_content TO postgres;
GRANT ALL ON TABLE ead_content TO admin;
GRANT ALL ON TABLE ead_content TO apenet_dashboard;
GRANT SELECT ON TABLE ead_content TO apenet_portal;


--
-- Name: ead_content_ec_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ead_content_ec_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ead_content_ec_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ead_content_ec_id_seq TO postgres;
GRANT ALL ON SEQUENCE ead_content_ec_id_seq TO admin;
GRANT ALL ON SEQUENCE ead_content_ec_id_seq TO apenet_dashboard;


--
-- Name: ead_content_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ead_content_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ead_content_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ead_content_id_seq TO postgres;
GRANT ALL ON SEQUENCE ead_content_id_seq TO admin;
GRANT ALL ON SEQUENCE ead_content_id_seq TO apenet_dashboard;


--
-- Name: ead_saved_search; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ead_saved_search FROM PUBLIC;
REVOKE ALL ON TABLE ead_saved_search FROM postgres;
GRANT ALL ON TABLE ead_saved_search TO postgres;
GRANT ALL ON TABLE ead_saved_search TO admin;
GRANT ALL ON TABLE ead_saved_search TO apenet_dashboard;
GRANT ALL ON TABLE ead_saved_search TO apenet_portal;


--
-- Name: ead_saved_search_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ead_saved_search_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ead_saved_search_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO postgres;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO apenet_portal;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO admin;


--
-- Name: ese; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ese FROM PUBLIC;
REVOKE ALL ON TABLE ese FROM postgres;
GRANT ALL ON TABLE ese TO postgres;
GRANT ALL ON TABLE ese TO admin;
GRANT ALL ON TABLE ese TO apenet_dashboard;


--
-- Name: ese_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ese_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ese_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ese_id_seq TO postgres;
GRANT ALL ON SEQUENCE ese_id_seq TO admin;
GRANT ALL ON SEQUENCE ese_id_seq TO apenet_dashboard;


--
-- Name: ese_state; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ese_state FROM PUBLIC;
REVOKE ALL ON TABLE ese_state FROM postgres;
GRANT ALL ON TABLE ese_state TO postgres;
GRANT ALL ON TABLE ese_state TO admin;
GRANT ALL ON TABLE ese_state TO apenet_dashboard;


--
-- Name: ese_state_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ese_state_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ese_state_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ese_state_id_seq TO postgres;
GRANT ALL ON SEQUENCE ese_state_id_seq TO admin;
GRANT ALL ON SEQUENCE ese_state_id_seq TO apenet_dashboard;


--
-- Name: finding_aid; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE finding_aid FROM PUBLIC;
REVOKE ALL ON TABLE finding_aid FROM postgres;
GRANT ALL ON TABLE finding_aid TO postgres;
GRANT ALL ON TABLE finding_aid TO admin;
GRANT ALL ON TABLE finding_aid TO apenet_dashboard;
GRANT SELECT ON TABLE finding_aid TO apenet_portal;


--
-- Name: finding_aid_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE finding_aid_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE finding_aid_id_seq FROM postgres;
GRANT ALL ON SEQUENCE finding_aid_id_seq TO postgres;
GRANT ALL ON SEQUENCE finding_aid_id_seq TO admin;
GRANT ALL ON SEQUENCE finding_aid_id_seq TO apenet_dashboard;


--
-- Name: ftp_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ftp_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ftp_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ftp_id_seq TO postgres;
GRANT ALL ON SEQUENCE ftp_id_seq TO admin;
GRANT ALL ON SEQUENCE ftp_id_seq TO apenet_dashboard;


--
-- Name: ftp; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ftp FROM PUBLIC;
REVOKE ALL ON TABLE ftp FROM postgres;
GRANT ALL ON TABLE ftp TO postgres;
GRANT ALL ON TABLE ftp TO admin;
GRANT ALL ON TABLE ftp TO apenet_dashboard;


--
-- Name: hg_sg_fa_relation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hg_sg_fa_relation FROM PUBLIC;
REVOKE ALL ON TABLE hg_sg_fa_relation FROM postgres;
GRANT ALL ON TABLE hg_sg_fa_relation TO postgres;
GRANT ALL ON TABLE hg_sg_fa_relation TO admin;
GRANT ALL ON TABLE hg_sg_fa_relation TO apenet_dashboard;
GRANT SELECT ON TABLE hg_sg_fa_relation TO apenet_portal;


--
-- Name: hg_sg_fa_relation_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE hg_sg_fa_relation_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE hg_sg_fa_relation_id_seq FROM postgres;
GRANT ALL ON SEQUENCE hg_sg_fa_relation_id_seq TO postgres;
GRANT ALL ON SEQUENCE hg_sg_fa_relation_id_seq TO admin;
GRANT ALL ON SEQUENCE hg_sg_fa_relation_id_seq TO apenet_dashboard;


--
-- Name: holdings_guide; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE holdings_guide FROM PUBLIC;
REVOKE ALL ON TABLE holdings_guide FROM postgres;
GRANT ALL ON TABLE holdings_guide TO postgres;
GRANT ALL ON TABLE holdings_guide TO admin;
GRANT ALL ON TABLE holdings_guide TO apenet_dashboard;
GRANT SELECT ON TABLE holdings_guide TO apenet_portal;


--
-- Name: holdings_guide_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE holdings_guide_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE holdings_guide_id_seq FROM postgres;
GRANT ALL ON SEQUENCE holdings_guide_id_seq TO postgres;
GRANT ALL ON SEQUENCE holdings_guide_id_seq TO admin;
GRANT ALL ON SEQUENCE holdings_guide_id_seq TO apenet_dashboard;


--
-- Name: queue; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE queue FROM PUBLIC;
REVOKE ALL ON TABLE queue FROM postgres;
GRANT ALL ON TABLE queue TO postgres;
GRANT ALL ON TABLE queue TO admin;
GRANT ALL ON TABLE queue TO apenet_dashboard;


--
-- Name: index_queue_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE index_queue_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE index_queue_id_seq FROM postgres;
GRANT ALL ON SEQUENCE index_queue_id_seq TO postgres;
GRANT ALL ON SEQUENCE index_queue_id_seq TO admin;
GRANT ALL ON SEQUENCE index_queue_id_seq TO apenet_dashboard;


--
-- Name: ingestionprofile; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ingestionprofile FROM PUBLIC;
REVOKE ALL ON TABLE ingestionprofile FROM postgres;
GRANT ALL ON TABLE ingestionprofile TO postgres;
GRANT ALL ON TABLE ingestionprofile TO admin;
GRANT ALL ON TABLE ingestionprofile TO apenet_dashboard;


--
-- Name: ingestionprofile_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ingestionprofile_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ingestionprofile_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ingestionprofile_id_seq TO postgres;
GRANT ALL ON SEQUENCE ingestionprofile_id_seq TO admin;
GRANT ALL ON SEQUENCE ingestionprofile_id_seq TO apenet_dashboard;


--
-- Name: lang; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE lang FROM PUBLIC;
REVOKE ALL ON TABLE lang FROM postgres;
GRANT ALL ON TABLE lang TO postgres;
GRANT ALL ON TABLE lang TO admin;
GRANT ALL ON TABLE lang TO apenet_dashboard;
GRANT SELECT ON TABLE lang TO apenet_portal;


--
-- Name: lang_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE lang_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE lang_id_seq FROM postgres;
GRANT ALL ON SEQUENCE lang_id_seq TO postgres;
GRANT ALL ON SEQUENCE lang_id_seq TO admin;
GRANT ALL ON SEQUENCE lang_id_seq TO apenet_dashboard;


--
-- Name: resumption_token; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE resumption_token FROM PUBLIC;
REVOKE ALL ON TABLE resumption_token FROM postgres;
GRANT ALL ON TABLE resumption_token TO postgres;
GRANT ALL ON TABLE resumption_token TO admin;
GRANT ALL ON TABLE resumption_token TO apenet_dashboard;


--
-- Name: resumption_token_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE resumption_token_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE resumption_token_id_seq FROM postgres;
GRANT ALL ON SEQUENCE resumption_token_id_seq TO postgres;
GRANT ALL ON SEQUENCE resumption_token_id_seq TO admin;
GRANT ALL ON SEQUENCE resumption_token_id_seq TO apenet_dashboard;


--
-- Name: saved_bookmarks; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE saved_bookmarks FROM PUBLIC;
REVOKE ALL ON TABLE saved_bookmarks FROM postgres;
GRANT ALL ON TABLE saved_bookmarks TO postgres;
GRANT ALL ON TABLE saved_bookmarks TO admin;
GRANT ALL ON TABLE saved_bookmarks TO apenet_dashboard;
GRANT ALL ON TABLE saved_bookmarks TO apenet_portal;


--
-- Name: saved_bookmarks_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE saved_bookmarks_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE saved_bookmarks_id_seq FROM postgres;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO postgres;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO apenet_portal;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO admin;


--
-- Name: sent_mail_register; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sent_mail_register FROM PUBLIC;
REVOKE ALL ON TABLE sent_mail_register FROM postgres;
GRANT ALL ON TABLE sent_mail_register TO postgres;
GRANT ALL ON TABLE sent_mail_register TO admin;
GRANT ALL ON TABLE sent_mail_register TO apenet_portal;
GRANT ALL ON TABLE sent_mail_register TO apenet_dashboard;


--
-- Name: sent_mail_register_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE sent_mail_register_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE sent_mail_register_id_seq FROM postgres;
GRANT ALL ON SEQUENCE sent_mail_register_id_seq TO postgres;
GRANT ALL ON SEQUENCE sent_mail_register_id_seq TO admin;
GRANT ALL ON SEQUENCE sent_mail_register_id_seq TO apenet_dashboard;


--
-- Name: source_guide; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE source_guide FROM PUBLIC;
REVOKE ALL ON TABLE source_guide FROM postgres;
GRANT ALL ON TABLE source_guide TO postgres;
GRANT ALL ON TABLE source_guide TO admin;
GRANT ALL ON TABLE source_guide TO apenet_dashboard;
GRANT SELECT ON TABLE source_guide TO apenet_portal;


--
-- Name: source_guide_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE source_guide_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE source_guide_id_seq FROM postgres;
GRANT ALL ON SEQUENCE source_guide_id_seq TO postgres;
GRANT ALL ON SEQUENCE source_guide_id_seq TO admin;
GRANT ALL ON SEQUENCE source_guide_id_seq TO apenet_dashboard;


--
-- Name: topic; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE topic FROM PUBLIC;
REVOKE ALL ON TABLE topic FROM postgres;
GRANT ALL ON TABLE topic TO postgres;
GRANT ALL ON TABLE topic TO admin;
GRANT ALL ON TABLE topic TO apenet_dashboard;
GRANT ALL ON TABLE topic TO apenet_portal;


--
-- Name: topic_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE topic_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE topic_id_seq FROM postgres;
GRANT ALL ON SEQUENCE topic_id_seq TO postgres;
GRANT ALL ON SEQUENCE topic_id_seq TO admin;
GRANT ALL ON SEQUENCE topic_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE topic_id_seq TO apenet_portal;


--
-- Name: topic_mapping; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE topic_mapping FROM PUBLIC;
REVOKE ALL ON TABLE topic_mapping FROM postgres;
GRANT ALL ON TABLE topic_mapping TO postgres;
GRANT ALL ON TABLE topic_mapping TO admin;
GRANT ALL ON TABLE topic_mapping TO apenet_dashboard;
GRANT ALL ON TABLE topic_mapping TO apenet_portal;


--
-- Name: topic_mapping_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE topic_mapping_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE topic_mapping_id_seq FROM postgres;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO postgres;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO admin;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO apenet_portal;


--
-- Name: up_file; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE up_file FROM PUBLIC;
REVOKE ALL ON TABLE up_file FROM postgres;
GRANT ALL ON TABLE up_file TO postgres;
GRANT ALL ON TABLE up_file TO admin;
GRANT ALL ON TABLE up_file TO apenet_dashboard;
GRANT SELECT ON TABLE up_file TO apenet_portal;


--
-- Name: up_file_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE up_file_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE up_file_id_seq FROM postgres;
GRANT ALL ON SEQUENCE up_file_id_seq TO postgres;
GRANT ALL ON SEQUENCE up_file_id_seq TO admin;
GRANT ALL ON SEQUENCE up_file_id_seq TO apenet_dashboard;


--
-- Name: upload_method; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE upload_method FROM PUBLIC;
REVOKE ALL ON TABLE upload_method FROM postgres;
GRANT ALL ON TABLE upload_method TO postgres;
GRANT ALL ON TABLE upload_method TO admin;
GRANT ALL ON TABLE upload_method TO apenet_dashboard;
GRANT SELECT ON TABLE upload_method TO apenet_portal;


--
-- Name: upload_method_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE upload_method_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE upload_method_id_seq FROM postgres;
GRANT ALL ON SEQUENCE upload_method_id_seq TO postgres;
GRANT ALL ON SEQUENCE upload_method_id_seq TO admin;
GRANT ALL ON SEQUENCE upload_method_id_seq TO apenet_dashboard;


--
-- Name: user_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE user_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE user_id_seq FROM postgres;
GRANT ALL ON SEQUENCE user_id_seq TO postgres;
GRANT ALL ON SEQUENCE user_id_seq TO admin;
GRANT ALL ON SEQUENCE user_id_seq TO apenet_dashboard;


--
-- Name: user_role; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE user_role FROM PUBLIC;
REVOKE ALL ON TABLE user_role FROM postgres;
GRANT ALL ON TABLE user_role TO postgres;
GRANT ALL ON TABLE user_role TO admin;
GRANT ALL ON TABLE user_role TO apenet_dashboard;


--
-- Name: user_role_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE user_role_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE user_role_id_seq FROM postgres;
GRANT ALL ON SEQUENCE user_role_id_seq TO postgres;
GRANT ALL ON SEQUENCE user_role_id_seq TO admin;
GRANT ALL ON SEQUENCE user_role_id_seq TO apenet_dashboard;


--
-- Name: warnings; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE warnings FROM PUBLIC;
REVOKE ALL ON TABLE warnings FROM postgres;
GRANT ALL ON TABLE warnings TO postgres;
GRANT ALL ON TABLE warnings TO admin;
GRANT ALL ON TABLE warnings TO apenet_dashboard;


--
-- Name: warnings_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE warnings_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE warnings_id_seq FROM postgres;
GRANT ALL ON SEQUENCE warnings_id_seq TO postgres;
GRANT ALL ON SEQUENCE warnings_id_seq TO admin;
GRANT ALL ON SEQUENCE warnings_id_seq TO apenet_dashboard;


--
-- Name: xsl_upload; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE xsl_upload FROM PUBLIC;
REVOKE ALL ON TABLE xsl_upload FROM postgres;
GRANT ALL ON TABLE xsl_upload TO postgres;
GRANT ALL ON TABLE xsl_upload TO apenet_dashboard;
GRANT ALL ON TABLE xsl_upload TO admin;


--
-- Name: xsl_upload_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE xsl_upload_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE xsl_upload_id_seq FROM postgres;
GRANT ALL ON SEQUENCE xsl_upload_id_seq TO postgres;
GRANT ALL ON SEQUENCE xsl_upload_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE xsl_upload_id_seq TO admin;


--
-- PostgreSQL database dump complete
--

