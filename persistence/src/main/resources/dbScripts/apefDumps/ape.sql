--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.9
-- Dumped by pg_dump version 9.3.9
-- Started on 2015-12-02 15:32:49 CET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 241 (class 3079 OID 12694)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 3396 (class 0 OID 0)
-- Dependencies: 241
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 170 (class 1259 OID 21093)
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
-- TOC entry 171 (class 1259 OID 21096)
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
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 171
-- Name: ai_alternative_name_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ai_alternative_name_id_seq OWNED BY ai_alternative_name.id;


--
-- TOC entry 172 (class 1259 OID 21098)
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
    feedback_email character varying(255) DEFAULT NULL::character varying
);


ALTER TABLE public.archival_institution OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 21107)
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
-- TOC entry 3401 (class 0 OID 0)
-- Dependencies: 173
-- Name: archival_institution_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE archival_institution_id_seq OWNED BY archival_institution.id;


--
-- TOC entry 174 (class 1259 OID 21109)
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
    list_by_identifiers boolean DEFAULT false,
    harvesting_details text,
    harvesting_status character varying(255) DEFAULT NULL::character varying,
    errors_response_path text,
    locked boolean DEFAULT false
);


ALTER TABLE public.archival_institution_oai_pmh OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 21120)
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
-- TOC entry 3404 (class 0 OID 0)
-- Dependencies: 175
-- Name: archival_institution_oai_pmh_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE archival_institution_oai_pmh_id_seq OWNED BY archival_institution_oai_pmh.id;


--
-- TOC entry 176 (class 1259 OID 21122)
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
-- TOC entry 177 (class 1259 OID 21129)
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
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 177
-- Name: c_level_cl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE c_level_cl_id_seq OWNED BY c_level.id;


--
-- TOC entry 178 (class 1259 OID 21131)
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
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 178
-- Name: c_level_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE c_level_id_seq OWNED BY c_level.id;


--
-- TOC entry 179 (class 1259 OID 21133)
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
-- TOC entry 180 (class 1259 OID 21140)
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
-- TOC entry 181 (class 1259 OID 21143)
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
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 181
-- Name: collection_content_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE collection_content_id_seq OWNED BY collection_content.id;


--
-- TOC entry 182 (class 1259 OID 21145)
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
-- TOC entry 3415 (class 0 OID 0)
-- Dependencies: 182
-- Name: collection_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE collection_id_seq OWNED BY collection.id;


--
-- TOC entry 183 (class 1259 OID 21147)
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
-- TOC entry 184 (class 1259 OID 21153)
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
-- TOC entry 3418 (class 0 OID 0)
-- Dependencies: 184
-- Name: coordinates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE coordinates_id_seq OWNED BY coordinates.id;


--
-- TOC entry 185 (class 1259 OID 21155)
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
-- TOC entry 186 (class 1259 OID 21158)
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
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 186
-- Name: cou_alternative_name_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE cou_alternative_name_id_seq OWNED BY cou_alternative_name.id;


--
-- TOC entry 187 (class 1259 OID 21160)
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
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 187
-- Name: COLUMN country.isoname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN country.isoname IS 'iso3166-1';


--
-- TOC entry 188 (class 1259 OID 21163)
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
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 188
-- Name: country_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE country_id_seq OWNED BY country.id;


--
-- TOC entry 189 (class 1259 OID 21165)
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
-- TOC entry 190 (class 1259 OID 21172)
-- Name: dpt_update; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE dpt_update (
    id bigint NOT NULL,
    version character varying(255)
);


ALTER TABLE public.dpt_update OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 21175)
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
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 191
-- Name: dpt_update_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE dpt_update_id_seq OWNED BY dpt_update.id;


--
-- TOC entry 192 (class 1259 OID 21177)
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
-- TOC entry 193 (class 1259 OID 21191)
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
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 193
-- Name: eac_cpf_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE eac_cpf_id_seq OWNED BY eac_cpf.id;


--
-- TOC entry 194 (class 1259 OID 21193)
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
-- TOC entry 195 (class 1259 OID 21201)
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
-- TOC entry 3435 (class 0 OID 0)
-- Dependencies: 195
-- Name: ead_content_ec_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ead_content_ec_id_seq OWNED BY ead_content.id;


--
-- TOC entry 196 (class 1259 OID 21203)
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
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 196
-- Name: ead_content_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ead_content_id_seq OWNED BY ead_content.id;


--
-- TOC entry 197 (class 1259 OID 21205)
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
-- TOC entry 198 (class 1259 OID 21233)
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
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 198
-- Name: ead_saved_search_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ead_saved_search_id_seq OWNED BY ead_saved_search.id;


--
-- TOC entry 199 (class 1259 OID 21235)
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
-- TOC entry 200 (class 1259 OID 21244)
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
-- TOC entry 3443 (class 0 OID 0)
-- Dependencies: 200
-- Name: ese_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ese_id_seq OWNED BY ese.id;


--
-- TOC entry 201 (class 1259 OID 21246)
-- Name: ese_state; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ese_state (
    id integer NOT NULL,
    state character varying(20) NOT NULL
);


ALTER TABLE public.ese_state OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 21249)
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
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 202
-- Name: ese_state_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ese_state_id_seq OWNED BY ese_state.id;


--
-- TOC entry 203 (class 1259 OID 21251)
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
-- TOC entry 204 (class 1259 OID 21265)
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
-- TOC entry 3449 (class 0 OID 0)
-- Dependencies: 204
-- Name: finding_aid_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE finding_aid_id_seq OWNED BY finding_aid.id;


--
-- TOC entry 205 (class 1259 OID 21267)
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
-- TOC entry 206 (class 1259 OID 21269)
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
-- TOC entry 207 (class 1259 OID 21273)
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
-- TOC entry 208 (class 1259 OID 21276)
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
-- TOC entry 3454 (class 0 OID 0)
-- Dependencies: 208
-- Name: hg_sg_fa_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hg_sg_fa_relation_id_seq OWNED BY hg_sg_fa_relation.id;


--
-- TOC entry 209 (class 1259 OID 21278)
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
-- TOC entry 210 (class 1259 OID 21289)
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
-- TOC entry 3457 (class 0 OID 0)
-- Dependencies: 210
-- Name: holdings_guide_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE holdings_guide_id_seq OWNED BY holdings_guide.id;


--
-- TOC entry 211 (class 1259 OID 21291)
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
    ai_id integer NOT NULL
);


ALTER TABLE public.queue OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 21297)
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
-- TOC entry 3460 (class 0 OID 0)
-- Dependencies: 212
-- Name: index_queue_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE index_queue_id_seq OWNED BY queue.id;


--
-- TOC entry 213 (class 1259 OID 21299)
-- Name: ingestionprofile; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.ingestionprofile OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 21323)
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
-- TOC entry 3463 (class 0 OID 0)
-- Dependencies: 214
-- Name: COLUMN lang.isoname; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN lang.isoname IS 'iso639-2';


--
-- TOC entry 215 (class 1259 OID 21326)
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
-- TOC entry 3465 (class 0 OID 0)
-- Dependencies: 215
-- Name: lang_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE lang_id_seq OWNED BY lang.id;


--
-- TOC entry 216 (class 1259 OID 21328)
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
-- TOC entry 217 (class 1259 OID 21332)
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
-- TOC entry 3468 (class 0 OID 0)
-- Dependencies: 217
-- Name: resumption_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE resumption_token_id_seq OWNED BY resumption_token.id;


--
-- TOC entry 218 (class 1259 OID 21334)
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
-- TOC entry 219 (class 1259 OID 21345)
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
-- TOC entry 3471 (class 0 OID 0)
-- Dependencies: 219
-- Name: saved_bookmarks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE saved_bookmarks_id_seq OWNED BY saved_bookmarks.id;


--
-- TOC entry 220 (class 1259 OID 21347)
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
-- TOC entry 221 (class 1259 OID 21350)
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
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 221
-- Name: sent_mail_register_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sent_mail_register_id_seq OWNED BY sent_mail_register.id;


--
-- TOC entry 222 (class 1259 OID 21352)
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
-- TOC entry 223 (class 1259 OID 21363)
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
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 223
-- Name: source_guide_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE source_guide_id_seq OWNED BY source_guide.id;


--
-- TOC entry 224 (class 1259 OID 21365)
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
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 224
-- Name: source_guide_sg_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE source_guide_sg_id_seq OWNED BY source_guide.id;


--
-- TOC entry 225 (class 1259 OID 21367)
-- Name: topic; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE topic (
    id bigint NOT NULL,
    property_key character varying(40) NOT NULL,
    description character varying(100) NOT NULL
);


ALTER TABLE public.topic OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 21370)
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
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 226
-- Name: topic_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE topic_id_seq OWNED BY topic.id;


--
-- TOC entry 227 (class 1259 OID 21372)
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
-- TOC entry 228 (class 1259 OID 21379)
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
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 228
-- Name: topic_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE topic_mapping_id_seq OWNED BY topic_mapping.id;


--
-- TOC entry 229 (class 1259 OID 21381)
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
-- TOC entry 230 (class 1259 OID 21388)
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
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 230
-- Name: up_file_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE up_file_id_seq OWNED BY up_file.id;


--
-- TOC entry 231 (class 1259 OID 21390)
-- Name: upload_method; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE upload_method (
    id integer NOT NULL,
    method character varying(20) NOT NULL
);


ALTER TABLE public.upload_method OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 21393)
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
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 232
-- Name: upload_method_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE upload_method_id_seq OWNED BY upload_method.id;


--
-- TOC entry 233 (class 1259 OID 21395)
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
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 233
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE user_id_seq OWNED BY dashboard_user.id;


--
-- TOC entry 234 (class 1259 OID 21397)
-- Name: user_role; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE user_role (
    id integer NOT NULL,
    role character varying(40) NOT NULL
);


ALTER TABLE public.user_role OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 21400)
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
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 235
-- Name: user_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE user_role_id_seq OWNED BY user_role.id;


--
-- TOC entry 236 (class 1259 OID 21402)
-- Name: userprofile_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE userprofile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.userprofile_id_seq OWNER TO postgres;

--
-- TOC entry 3497 (class 0 OID 0)
-- Dependencies: 236
-- Name: userprofile_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE userprofile_id_seq OWNED BY ingestionprofile.id;


--
-- TOC entry 237 (class 1259 OID 21404)
-- Name: warnings; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.warnings OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 21410)
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
-- TOC entry 3500 (class 0 OID 0)
-- Dependencies: 238
-- Name: warnings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE warnings_id_seq OWNED BY warnings.id;


--
-- TOC entry 239 (class 1259 OID 21412)
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
-- TOC entry 240 (class 1259 OID 21418)
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
-- TOC entry 3503 (class 0 OID 0)
-- Dependencies: 240
-- Name: xsl_upload_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE xsl_upload_id_seq OWNED BY xsl_upload.id;


--
-- TOC entry 2990 (class 2604 OID 21420)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ai_alternative_name ALTER COLUMN id SET DEFAULT nextval('ai_alternative_name_id_seq'::regclass);


--
-- TOC entry 2994 (class 2604 OID 21421)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution ALTER COLUMN id SET DEFAULT nextval('archival_institution_id_seq'::regclass);


--
-- TOC entry 3000 (class 2604 OID 21422)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution_oai_pmh ALTER COLUMN id SET DEFAULT nextval('archival_institution_oai_pmh_id_seq'::regclass);


--
-- TOC entry 3002 (class 2604 OID 21423)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY c_level ALTER COLUMN id SET DEFAULT nextval('c_level_id_seq'::regclass);


--
-- TOC entry 3004 (class 2604 OID 21424)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY collection ALTER COLUMN id SET DEFAULT nextval('collection_id_seq'::regclass);


--
-- TOC entry 3005 (class 2604 OID 21425)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY collection_content ALTER COLUMN id SET DEFAULT nextval('collection_content_id_seq'::regclass);


--
-- TOC entry 3006 (class 2604 OID 21426)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY coordinates ALTER COLUMN id SET DEFAULT nextval('coordinates_id_seq'::regclass);


--
-- TOC entry 3007 (class 2604 OID 21427)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cou_alternative_name ALTER COLUMN id SET DEFAULT nextval('cou_alternative_name_id_seq'::regclass);


--
-- TOC entry 3008 (class 2604 OID 21428)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY country ALTER COLUMN id SET DEFAULT nextval('country_id_seq'::regclass);


--
-- TOC entry 3010 (class 2604 OID 21429)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dashboard_user ALTER COLUMN id SET DEFAULT nextval('user_id_seq'::regclass);


--
-- TOC entry 3019 (class 2604 OID 21430)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY eac_cpf ALTER COLUMN id SET DEFAULT nextval('eac_cpf_id_seq'::regclass);


--
-- TOC entry 3022 (class 2604 OID 21431)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ead_content ALTER COLUMN id SET DEFAULT nextval('ead_content_id_seq'::regclass);


--
-- TOC entry 3045 (class 2604 OID 21432)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ead_saved_search ALTER COLUMN id SET DEFAULT nextval('ead_saved_search_id_seq'::regclass);


--
-- TOC entry 3049 (class 2604 OID 21433)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ese ALTER COLUMN id SET DEFAULT nextval('ese_id_seq'::regclass);


--
-- TOC entry 3050 (class 2604 OID 21434)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ese_state ALTER COLUMN id SET DEFAULT nextval('ese_state_id_seq'::regclass);


--
-- TOC entry 3059 (class 2604 OID 21435)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY finding_aid ALTER COLUMN id SET DEFAULT nextval('finding_aid_id_seq'::regclass);


--
-- TOC entry 3061 (class 2604 OID 21436)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation ALTER COLUMN id SET DEFAULT nextval('hg_sg_fa_relation_id_seq'::regclass);


--
-- TOC entry 3067 (class 2604 OID 21437)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY holdings_guide ALTER COLUMN id SET DEFAULT nextval('holdings_guide_id_seq'::regclass);


--
-- TOC entry 3087 (class 2604 OID 21438)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ingestionprofile ALTER COLUMN id SET DEFAULT nextval('userprofile_id_seq'::regclass);


--
-- TOC entry 3088 (class 2604 OID 21439)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY lang ALTER COLUMN id SET DEFAULT nextval('lang_id_seq'::regclass);


--
-- TOC entry 3068 (class 2604 OID 21440)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue ALTER COLUMN id SET DEFAULT nextval('index_queue_id_seq'::regclass);


--
-- TOC entry 3090 (class 2604 OID 21441)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY resumption_token ALTER COLUMN id SET DEFAULT nextval('resumption_token_id_seq'::regclass);


--
-- TOC entry 3096 (class 2604 OID 21442)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY saved_bookmarks ALTER COLUMN id SET DEFAULT nextval('saved_bookmarks_id_seq'::regclass);


--
-- TOC entry 3097 (class 2604 OID 21443)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sent_mail_register ALTER COLUMN id SET DEFAULT nextval('sent_mail_register_id_seq'::regclass);


--
-- TOC entry 3103 (class 2604 OID 21444)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY source_guide ALTER COLUMN id SET DEFAULT nextval('source_guide_id_seq'::regclass);


--
-- TOC entry 3104 (class 2604 OID 21445)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic ALTER COLUMN id SET DEFAULT nextval('topic_id_seq'::regclass);


--
-- TOC entry 3106 (class 2604 OID 21446)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic_mapping ALTER COLUMN id SET DEFAULT nextval('topic_mapping_id_seq'::regclass);


--
-- TOC entry 3108 (class 2604 OID 21447)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY up_file ALTER COLUMN id SET DEFAULT nextval('up_file_id_seq'::regclass);


--
-- TOC entry 3109 (class 2604 OID 21448)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY upload_method ALTER COLUMN id SET DEFAULT nextval('upload_method_id_seq'::regclass);


--
-- TOC entry 3110 (class 2604 OID 21449)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_role ALTER COLUMN id SET DEFAULT nextval('user_role_id_seq'::regclass);


--
-- TOC entry 3111 (class 2604 OID 21450)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings ALTER COLUMN id SET DEFAULT nextval('warnings_id_seq'::regclass);


--
-- TOC entry 3112 (class 2604 OID 21451)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY xsl_upload ALTER COLUMN id SET DEFAULT nextval('xsl_upload_id_seq'::regclass);


--
-- TOC entry 3114 (class 2606 OID 21461)
-- Name: alternative_name_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ai_alternative_name
    ADD CONSTRAINT alternative_name_pkey PRIMARY KEY (id);


--
-- TOC entry 3119 (class 2606 OID 21463)
-- Name: archival_institution_oai_pmh_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY archival_institution_oai_pmh
    ADD CONSTRAINT archival_institution_oai_pmh_pkey PRIMARY KEY (id);


--
-- TOC entry 3116 (class 2606 OID 21465)
-- Name: archival_institution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_pkey PRIMARY KEY (id);


--
-- TOC entry 3128 (class 2606 OID 21467)
-- Name: c_level_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY c_level
    ADD CONSTRAINT c_level_pkey PRIMARY KEY (id);


--
-- TOC entry 3133 (class 2606 OID 21469)
-- Name: collection_content_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY collection_content
    ADD CONSTRAINT collection_content_pkey PRIMARY KEY (id);


--
-- TOC entry 3131 (class 2606 OID 21471)
-- Name: collection_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY collection
    ADD CONSTRAINT collection_pkey PRIMARY KEY (id);


--
-- TOC entry 3135 (class 2606 OID 21473)
-- Name: coordinates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY coordinates
    ADD CONSTRAINT coordinates_pkey PRIMARY KEY (id);


--
-- TOC entry 3137 (class 2606 OID 21475)
-- Name: cou_alternative_name_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cou_alternative_name
    ADD CONSTRAINT cou_alternative_name_pkey PRIMARY KEY (id);


--
-- TOC entry 3139 (class 2606 OID 21477)
-- Name: country_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);


--
-- TOC entry 3141 (class 2606 OID 21479)
-- Name: dashboard_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY dashboard_user
    ADD CONSTRAINT dashboard_user_pkey PRIMARY KEY (id);


--
-- TOC entry 3145 (class 2606 OID 21481)
-- Name: dpt_update_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY dpt_update
    ADD CONSTRAINT dpt_update_pkey PRIMARY KEY (id);


--
-- TOC entry 3148 (class 2606 OID 21483)
-- Name: eac_cpf_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eac_cpf
    ADD CONSTRAINT eac_cpf_pkey PRIMARY KEY (id);


--
-- TOC entry 3153 (class 2606 OID 21485)
-- Name: ead_content_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_pkey PRIMARY KEY (id);


--
-- TOC entry 3156 (class 2606 OID 21487)
-- Name: ead_saved_search_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ead_saved_search
    ADD CONSTRAINT ead_saved_search_pkey PRIMARY KEY (id);


--
-- TOC entry 3159 (class 2606 OID 21489)
-- Name: ese_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ese
    ADD CONSTRAINT ese_pkey PRIMARY KEY (id);


--
-- TOC entry 3162 (class 2606 OID 21491)
-- Name: ese_state_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ese_state
    ADD CONSTRAINT ese_state_pkey PRIMARY KEY (id);


--
-- TOC entry 3168 (class 2606 OID 21493)
-- Name: finding_aid_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY finding_aid
    ADD CONSTRAINT finding_aid_pkey PRIMARY KEY (id);


--
-- TOC entry 3172 (class 2606 OID 21495)
-- Name: ftp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ftp
    ADD CONSTRAINT ftp_pkey PRIMARY KEY (id);


--
-- TOC entry 3179 (class 2606 OID 21497)
-- Name: hg_sg_fa_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_pkey PRIMARY KEY (id);


--
-- TOC entry 3184 (class 2606 OID 21499)
-- Name: holdings_guide_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY holdings_guide
    ADD CONSTRAINT holdings_guide_pkey PRIMARY KEY (id);


--
-- TOC entry 3190 (class 2606 OID 21501)
-- Name: ingestionprofile_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ingestionprofile
    ADD CONSTRAINT ingestionprofile_pkey PRIMARY KEY (id);


--
-- TOC entry 3192 (class 2606 OID 21503)
-- Name: lang_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY lang
    ADD CONSTRAINT lang_pkey PRIMARY KEY (id);


--
-- TOC entry 3143 (class 2606 OID 21505)
-- Name: partner_email_address_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY dashboard_user
    ADD CONSTRAINT partner_email_address_unique UNIQUE (email_address);


--
-- TOC entry 3187 (class 2606 OID 21507)
-- Name: queue_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT queue_pkey PRIMARY KEY (id);


--
-- TOC entry 3194 (class 2606 OID 21509)
-- Name: resumption_token_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY resumption_token
    ADD CONSTRAINT resumption_token_pkey PRIMARY KEY (id);


--
-- TOC entry 3221 (class 2606 OID 21511)
-- Name: role_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT role_type_pkey PRIMARY KEY (id);


--
-- TOC entry 3197 (class 2606 OID 21513)
-- Name: saved_bookmarks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY saved_bookmarks
    ADD CONSTRAINT saved_bookmarks_pkey PRIMARY KEY (id);


--
-- TOC entry 3199 (class 2606 OID 21515)
-- Name: sent_mail_register_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sent_mail_register
    ADD CONSTRAINT sent_mail_register_pkey PRIMARY KEY (id);


--
-- TOC entry 3204 (class 2606 OID 21517)
-- Name: source_guide_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY source_guide
    ADD CONSTRAINT source_guide_pkey PRIMARY KEY (id);


--
-- TOC entry 3213 (class 2606 OID 21519)
-- Name: topic_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY topic_mapping
    ADD CONSTRAINT topic_mapping_pkey PRIMARY KEY (id);


--
-- TOC entry 3209 (class 2606 OID 21521)
-- Name: topic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY topic
    ADD CONSTRAINT topic_pkey PRIMARY KEY (id);


--
-- TOC entry 3217 (class 2606 OID 21523)
-- Name: up_file_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY up_file
    ADD CONSTRAINT up_file_pkey PRIMARY KEY (id);


--
-- TOC entry 3219 (class 2606 OID 21525)
-- Name: upload_method_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY upload_method
    ADD CONSTRAINT upload_method_pkey PRIMARY KEY (id);


--
-- TOC entry 3223 (class 2606 OID 21527)
-- Name: warnings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_pkey PRIMARY KEY (id);


--
-- TOC entry 3225 (class 2606 OID 21529)
-- Name: xsl_upload_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY xsl_upload
    ADD CONSTRAINT xsl_upload_pkey PRIMARY KEY (id);


--
-- TOC entry 3117 (class 1259 OID 21530)
-- Name: archival_institution_repositorycode; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX archival_institution_repositorycode ON archival_institution USING btree (repositorycode);


--
-- TOC entry 3120 (class 1259 OID 21531)
-- Name: c_level__cid_ec_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__cid_ec_id_idx ON c_level USING btree (cid, ec_id);


--
-- TOC entry 3121 (class 1259 OID 21532)
-- Name: c_level__eadid_ref_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__eadid_ref_idx ON c_level USING btree (ec_id, href_eadid) WHERE (href_eadid IS NOT NULL);


--
-- TOC entry 3122 (class 1259 OID 21533)
-- Name: c_level__nodes_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__nodes_idx ON c_level USING btree (ec_id) WHERE (leaf = false);


--
-- TOC entry 3123 (class 1259 OID 21534)
-- Name: c_level__parent_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__parent_idx ON c_level USING btree (parent_cl_id, order_id) WHERE (parent_cl_id IS NOT NULL);


--
-- TOC entry 3124 (class 1259 OID 21535)
-- Name: c_level__persistent_link_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__persistent_link_idx ON c_level USING btree (unitid, ec_id) WHERE (duplicate_unitid = false);


--
-- TOC entry 3125 (class 1259 OID 21536)
-- Name: c_level__top_levels_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__top_levels_idx ON c_level USING btree (order_id, ec_id) WHERE (parent_cl_id IS NULL);


--
-- TOC entry 3126 (class 1259 OID 21537)
-- Name: c_level__unitid_ec_id_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX c_level__unitid_ec_id_idx ON c_level USING btree (unitid, ec_id);


--
-- TOC entry 3129 (class 1259 OID 21538)
-- Name: collection__liferay_user_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX collection__liferay_user_idx ON collection USING btree (liferay_user_id);


--
-- TOC entry 3146 (class 1259 OID 21539)
-- Name: eac_cpf__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eac_cpf__archival_institution_idx ON eac_cpf USING btree (ai_id);


--
-- TOC entry 3149 (class 1259 OID 21540)
-- Name: ead_content__finding_aid_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ead_content__finding_aid_idx ON ead_content USING btree (fa_id);


--
-- TOC entry 3150 (class 1259 OID 21541)
-- Name: ead_content__holdings_guide_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ead_content__holdings_guide_idx ON ead_content USING btree (hg_id);


--
-- TOC entry 3151 (class 1259 OID 21542)
-- Name: ead_content__source_guide_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ead_content__source_guide_idx ON ead_content USING btree (sg_id);


--
-- TOC entry 3154 (class 1259 OID 21543)
-- Name: ead_saved_search__liferay_user_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ead_saved_search__liferay_user_idx ON ead_saved_search USING btree (liferay_user_id);


--
-- TOC entry 3157 (class 1259 OID 21544)
-- Name: ese_metadataformat; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ese_metadataformat ON ese USING btree (metadataformat);


--
-- TOC entry 3163 (class 1259 OID 21545)
-- Name: finding_aid__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid__archival_institution_idx ON finding_aid USING btree (ai_id);


--
-- TOC entry 3164 (class 1259 OID 21546)
-- Name: finding_aid_dynamic; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid_dynamic ON finding_aid USING btree (dynamic);


--
-- TOC entry 3165 (class 1259 OID 21547)
-- Name: finding_aid_eadid; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid_eadid ON finding_aid USING btree (eadid);


--
-- TOC entry 3166 (class 1259 OID 21548)
-- Name: finding_aid_path; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid_path ON finding_aid USING btree (path_apenetead);


--
-- TOC entry 3169 (class 1259 OID 21549)
-- Name: finding_aid_searchable; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid_searchable ON finding_aid USING btree (published);


--
-- TOC entry 3170 (class 1259 OID 21550)
-- Name: finding_aid_title; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX finding_aid_title ON finding_aid USING btree (title);


--
-- TOC entry 3173 (class 1259 OID 21551)
-- Name: hg_sg_fa_relation__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hg_sg_fa_relation__archival_institution_idx ON hg_sg_fa_relation USING btree (ai_id);


--
-- TOC entry 3174 (class 1259 OID 21552)
-- Name: hg_sg_fa_relation__c_level_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hg_sg_fa_relation__c_level_idx ON hg_sg_fa_relation USING btree (hg_sg_clevel_id);


--
-- TOC entry 3175 (class 1259 OID 21553)
-- Name: hg_sg_fa_relation__finding_aid_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hg_sg_fa_relation__finding_aid_idx ON hg_sg_fa_relation USING btree (fa_id);


--
-- TOC entry 3176 (class 1259 OID 21554)
-- Name: hg_sg_fa_relation__holdings_guide_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hg_sg_fa_relation__holdings_guide_idx ON hg_sg_fa_relation USING btree (hg_id);


--
-- TOC entry 3177 (class 1259 OID 21555)
-- Name: hg_sg_fa_relation__source_guide_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hg_sg_fa_relation__source_guide_idx ON hg_sg_fa_relation USING btree (sg_id);


--
-- TOC entry 3180 (class 1259 OID 21556)
-- Name: holdings_guide__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX holdings_guide__archival_institution_idx ON holdings_guide USING btree (ai_id);


--
-- TOC entry 3181 (class 1259 OID 21557)
-- Name: holdings_guide_dynamic; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX holdings_guide_dynamic ON holdings_guide USING btree (dynamic);


--
-- TOC entry 3182 (class 1259 OID 21558)
-- Name: holdings_guide_path; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX holdings_guide_path ON holdings_guide USING btree (path_apenetead);


--
-- TOC entry 3185 (class 1259 OID 21559)
-- Name: holdings_guide_searchable; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX holdings_guide_searchable ON holdings_guide USING btree (published);


--
-- TOC entry 3188 (class 1259 OID 21560)
-- Name: queue_uf_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX queue_uf_id ON queue USING btree (uf_id);


--
-- TOC entry 3160 (class 1259 OID 21561)
-- Name: resumption_token_metadataformat; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX resumption_token_metadataformat ON ese USING btree (metadataformat);


--
-- TOC entry 3195 (class 1259 OID 21562)
-- Name: saved_bookmarks__liferay_user_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX saved_bookmarks__liferay_user_idx ON saved_bookmarks USING btree (liferay_user_id);


--
-- TOC entry 3200 (class 1259 OID 21563)
-- Name: source_guide__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX source_guide__archival_institution_idx ON source_guide USING btree (ai_id);


--
-- TOC entry 3201 (class 1259 OID 21564)
-- Name: source_guide_dynamic; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX source_guide_dynamic ON source_guide USING btree (dynamic);


--
-- TOC entry 3202 (class 1259 OID 21565)
-- Name: source_guide_path; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX source_guide_path ON source_guide USING btree (path_apenetead);


--
-- TOC entry 3205 (class 1259 OID 21566)
-- Name: source_guide_searchable; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX source_guide_searchable ON source_guide USING btree (published);


--
-- TOC entry 3206 (class 1259 OID 21567)
-- Name: topic__description_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX topic__description_idx ON topic USING btree (description);


--
-- TOC entry 3207 (class 1259 OID 21568)
-- Name: topic__property_key_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX topic__property_key_idx ON topic USING btree (property_key);


--
-- TOC entry 3210 (class 1259 OID 21569)
-- Name: topic_mapping__archival_institution_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX topic_mapping__archival_institution_idx ON topic_mapping USING btree (ai_id);


--
-- TOC entry 3211 (class 1259 OID 21570)
-- Name: topic_mapping__source_guide_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX topic_mapping__source_guide_idx ON topic_mapping USING btree (sg_id);


--
-- TOC entry 3214 (class 1259 OID 21571)
-- Name: up_file_ai_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX up_file_ai_id ON up_file USING btree (ai_id);


--
-- TOC entry 3215 (class 1259 OID 21572)
-- Name: up_file_file_type; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX up_file_file_type ON up_file USING btree (file_type);


--
-- TOC entry 3252 (class 2606 OID 21573)
-- Name: ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ftp
    ADD CONSTRAINT ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 3226 (class 2606 OID 21578)
-- Name: archival_institution_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ai_alternative_name
    ADD CONSTRAINT archival_institution_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 3228 (class 2606 OID 21583)
-- Name: archival_institution_cou_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_cou_id_fkey FOREIGN KEY (country_id) REFERENCES country(id);


--
-- TOC entry 3283 (class 2606 OID 21588)
-- Name: archival_institution_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY xsl_upload
    ADD CONSTRAINT archival_institution_id_fkey FOREIGN KEY (archival_institution_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 3231 (class 2606 OID 21593)
-- Name: archival_institution_oai_pmh_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution_oai_pmh
    ADD CONSTRAINT archival_institution_oai_pmh_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 3229 (class 2606 OID 21598)
-- Name: archival_institution_p_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_p_id_fkey FOREIGN KEY (user_id) REFERENCES dashboard_user(id);


--
-- TOC entry 3230 (class 2606 OID 21603)
-- Name: archival_institution_parent_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution
    ADD CONSTRAINT archival_institution_parent_ai_id_fkey FOREIGN KEY (parent_ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 3232 (class 2606 OID 21608)
-- Name: archival_institutition_oai_pmh_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY archival_institution_oai_pmh
    ADD CONSTRAINT archival_institutition_oai_pmh_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES ingestionprofile(id);


--
-- TOC entry 3233 (class 2606 OID 21613)
-- Name: c_level_ec_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY c_level
    ADD CONSTRAINT c_level_ec_id_fkey FOREIGN KEY (ec_id) REFERENCES ead_content(id) ON DELETE CASCADE;


--
-- TOC entry 3234 (class 2606 OID 21618)
-- Name: c_level_parent_cl_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY c_level
    ADD CONSTRAINT c_level_parent_cl_id_fkey FOREIGN KEY (parent_cl_id) REFERENCES c_level(id) ON DELETE CASCADE;


--
-- TOC entry 3235 (class 2606 OID 21623)
-- Name: collection_content_fkey_collection; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY collection_content
    ADD CONSTRAINT collection_content_fkey_collection FOREIGN KEY (id_collection) REFERENCES collection(id) ON DELETE CASCADE;


--
-- TOC entry 3236 (class 2606 OID 21628)
-- Name: collection_content_fkey_saved_bookmarks; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY collection_content
    ADD CONSTRAINT collection_content_fkey_saved_bookmarks FOREIGN KEY (id_bookmarks) REFERENCES saved_bookmarks(id) ON DELETE CASCADE;


--
-- TOC entry 3237 (class 2606 OID 21633)
-- Name: collection_content_fkey_search; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY collection_content
    ADD CONSTRAINT collection_content_fkey_search FOREIGN KEY (id_search) REFERENCES ead_saved_search(id) ON DELETE CASCADE;


--
-- TOC entry 3238 (class 2606 OID 21638)
-- Name: coordinates_archival_institution_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY coordinates
    ADD CONSTRAINT coordinates_archival_institution_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 3239 (class 2606 OID 21643)
-- Name: country_cou_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cou_alternative_name
    ADD CONSTRAINT country_cou_id_fkey FOREIGN KEY (cou_id) REFERENCES country(id);


--
-- TOC entry 3272 (class 2606 OID 21648)
-- Name: country_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic_mapping
    ADD CONSTRAINT country_id_fkey FOREIGN KEY (country_id) REFERENCES country(id) ON DELETE CASCADE;


--
-- TOC entry 3243 (class 2606 OID 21653)
-- Name: eac_cpf_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY eac_cpf
    ADD CONSTRAINT eac_cpf_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 3244 (class 2606 OID 21658)
-- Name: eac_cpf_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY eac_cpf
    ADD CONSTRAINT eac_cpf_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- TOC entry 3245 (class 2606 OID 21663)
-- Name: ead_content_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- TOC entry 3246 (class 2606 OID 21668)
-- Name: ead_content_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- TOC entry 3247 (class 2606 OID 21673)
-- Name: ead_content_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ead_content
    ADD CONSTRAINT ead_content_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- TOC entry 3248 (class 2606 OID 21678)
-- Name: ese_es_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ese
    ADD CONSTRAINT ese_es_id_fkey FOREIGN KEY (es_id) REFERENCES ese_state(id);


--
-- TOC entry 3249 (class 2606 OID 21683)
-- Name: ese_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ese
    ADD CONSTRAINT ese_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id);


--
-- TOC entry 3250 (class 2606 OID 21688)
-- Name: finding_aid_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY finding_aid
    ADD CONSTRAINT finding_aid_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 3251 (class 2606 OID 21693)
-- Name: finding_aid_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY finding_aid
    ADD CONSTRAINT finding_aid_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- TOC entry 3253 (class 2606 OID 21698)
-- Name: hg_sg_fa_relation_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 3254 (class 2606 OID 21703)
-- Name: hg_sg_fa_relation_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- TOC entry 3255 (class 2606 OID 21708)
-- Name: hg_sg_fa_relation_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- TOC entry 3256 (class 2606 OID 21713)
-- Name: hg_sg_fa_relation_hg_sg_clevel_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_hg_sg_clevel_id_fkey FOREIGN KEY (hg_sg_clevel_id) REFERENCES c_level(id) ON DELETE CASCADE;


--
-- TOC entry 3257 (class 2606 OID 21718)
-- Name: hg_sg_fa_relation_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hg_sg_fa_relation
    ADD CONSTRAINT hg_sg_fa_relation_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- TOC entry 3258 (class 2606 OID 21723)
-- Name: holdings_guide_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY holdings_guide
    ADD CONSTRAINT holdings_guide_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 3259 (class 2606 OID 21728)
-- Name: holdings_guide_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY holdings_guide
    ADD CONSTRAINT holdings_guide_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- TOC entry 3260 (class 2606 OID 21733)
-- Name: index_queue_eac_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT index_queue_eac_id_fkey FOREIGN KEY (eac_cpf_id) REFERENCES eac_cpf(id) ON DELETE CASCADE;


--
-- TOC entry 3261 (class 2606 OID 21738)
-- Name: index_queue_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT index_queue_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- TOC entry 3262 (class 2606 OID 21743)
-- Name: index_queue_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT index_queue_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- TOC entry 3263 (class 2606 OID 21748)
-- Name: index_queue_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT index_queue_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- TOC entry 3266 (class 2606 OID 21753)
-- Name: ingestionprofile_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ingestionprofile
    ADD CONSTRAINT ingestionprofile_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 3227 (class 2606 OID 21758)
-- Name: lang_lng_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ai_alternative_name
    ADD CONSTRAINT lang_lng_id_fkey FOREIGN KEY (lng_id) REFERENCES lang(id);


--
-- TOC entry 3240 (class 2606 OID 21763)
-- Name: lng_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cou_alternative_name
    ADD CONSTRAINT lng_id FOREIGN KEY (lng_id) REFERENCES lang(id);


--
-- TOC entry 3241 (class 2606 OID 21768)
-- Name: partner_cou_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dashboard_user
    ADD CONSTRAINT partner_cou_id_fkey FOREIGN KEY (country_id) REFERENCES country(id);


--
-- TOC entry 3242 (class 2606 OID 21773)
-- Name: partner_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dashboard_user
    ADD CONSTRAINT partner_role_id_fkey FOREIGN KEY (user_role_id) REFERENCES user_role(id);


--
-- TOC entry 3264 (class 2606 OID 21778)
-- Name: queue_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT queue_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 3265 (class 2606 OID 21783)
-- Name: queue_uf_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT queue_uf_id_fkey FOREIGN KEY (uf_id) REFERENCES up_file(id) ON DELETE CASCADE;


--
-- TOC entry 3268 (class 2606 OID 21788)
-- Name: sent_mail_register_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sent_mail_register
    ADD CONSTRAINT sent_mail_register_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 3269 (class 2606 OID 21793)
-- Name: sent_mail_register_p_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sent_mail_register
    ADD CONSTRAINT sent_mail_register_p_id_fkey FOREIGN KEY (user_id) REFERENCES dashboard_user(id) ON DELETE CASCADE;


--
-- TOC entry 3270 (class 2606 OID 21798)
-- Name: source_guide_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY source_guide
    ADD CONSTRAINT source_guide_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 3271 (class 2606 OID 21803)
-- Name: source_guide_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY source_guide
    ADD CONSTRAINT source_guide_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- TOC entry 3273 (class 2606 OID 21808)
-- Name: topic_mapping_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic_mapping
    ADD CONSTRAINT topic_mapping_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id) ON DELETE CASCADE;


--
-- TOC entry 3274 (class 2606 OID 21813)
-- Name: topic_mapping_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic_mapping
    ADD CONSTRAINT topic_mapping_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- TOC entry 3275 (class 2606 OID 21818)
-- Name: topic_mapping_topic_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topic_mapping
    ADD CONSTRAINT topic_mapping_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES topic(id);


--
-- TOC entry 3276 (class 2606 OID 21823)
-- Name: up_file_ai_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY up_file
    ADD CONSTRAINT up_file_ai_id_fkey FOREIGN KEY (ai_id) REFERENCES archival_institution(id);


--
-- TOC entry 3277 (class 2606 OID 21828)
-- Name: up_file_um_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY up_file
    ADD CONSTRAINT up_file_um_id_fkey FOREIGN KEY (um_id) REFERENCES upload_method(id);


--
-- TOC entry 3278 (class 2606 OID 21833)
-- Name: warnings_eac_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_eac_id_fkey FOREIGN KEY (eac_id) REFERENCES eac_cpf(id) ON DELETE CASCADE;


--
-- TOC entry 3279 (class 2606 OID 21838)
-- Name: warnings_ec_id_fkey -> eac_cpf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT "warnings_ec_id_fkey -> eac_cpf" FOREIGN KEY (eac_id) REFERENCES eac_cpf(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3280 (class 2606 OID 21843)
-- Name: warnings_fa_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_fa_id_fkey FOREIGN KEY (fa_id) REFERENCES finding_aid(id) ON DELETE CASCADE;


--
-- TOC entry 3281 (class 2606 OID 21848)
-- Name: warnings_hg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_hg_id_fkey FOREIGN KEY (hg_id) REFERENCES holdings_guide(id) ON DELETE CASCADE;


--
-- TOC entry 3282 (class 2606 OID 21853)
-- Name: warnings_sg_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY warnings
    ADD CONSTRAINT warnings_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;


--
-- TOC entry 3267 (class 2606 OID 21858)
-- Name: xsl_upload_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ingestionprofile
    ADD CONSTRAINT xsl_upload_id_fkey FOREIGN KEY (xsl_upload_id) REFERENCES xsl_upload(id);


--
-- TOC entry 3397 (class 0 OID 0)
-- Dependencies: 170
-- Name: ai_alternative_name; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ai_alternative_name FROM PUBLIC;
REVOKE ALL ON TABLE ai_alternative_name FROM postgres;
GRANT ALL ON TABLE ai_alternative_name TO postgres;
GRANT ALL ON TABLE ai_alternative_name TO admin;
GRANT ALL ON TABLE ai_alternative_name TO apenet_dashboard;
GRANT SELECT ON TABLE ai_alternative_name TO apenet_portal;


--
-- TOC entry 3399 (class 0 OID 0)
-- Dependencies: 171
-- Name: ai_alternative_name_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ai_alternative_name_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ai_alternative_name_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ai_alternative_name_id_seq TO postgres;
GRANT ALL ON SEQUENCE ai_alternative_name_id_seq TO admin;
GRANT ALL ON SEQUENCE ai_alternative_name_id_seq TO apenet_dashboard;


--
-- TOC entry 3400 (class 0 OID 0)
-- Dependencies: 172
-- Name: archival_institution; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE archival_institution FROM PUBLIC;
REVOKE ALL ON TABLE archival_institution FROM postgres;
GRANT ALL ON TABLE archival_institution TO postgres;
GRANT ALL ON TABLE archival_institution TO admin;
GRANT ALL ON TABLE archival_institution TO apenet_dashboard;
GRANT SELECT ON TABLE archival_institution TO apenet_portal;


--
-- TOC entry 3402 (class 0 OID 0)
-- Dependencies: 173
-- Name: archival_institution_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE archival_institution_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE archival_institution_id_seq FROM postgres;
GRANT ALL ON SEQUENCE archival_institution_id_seq TO postgres;
GRANT ALL ON SEQUENCE archival_institution_id_seq TO admin;
GRANT ALL ON SEQUENCE archival_institution_id_seq TO apenet_dashboard;


--
-- TOC entry 3403 (class 0 OID 0)
-- Dependencies: 174
-- Name: archival_institution_oai_pmh; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE archival_institution_oai_pmh FROM PUBLIC;
REVOKE ALL ON TABLE archival_institution_oai_pmh FROM postgres;
GRANT ALL ON TABLE archival_institution_oai_pmh TO postgres;
GRANT ALL ON TABLE archival_institution_oai_pmh TO admin;
GRANT ALL ON TABLE archival_institution_oai_pmh TO apenet_dashboard;


--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 175
-- Name: archival_institution_oai_pmh_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE archival_institution_oai_pmh_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE archival_institution_oai_pmh_id_seq FROM postgres;
GRANT ALL ON SEQUENCE archival_institution_oai_pmh_id_seq TO postgres;
GRANT ALL ON SEQUENCE archival_institution_oai_pmh_id_seq TO admin;
GRANT ALL ON SEQUENCE archival_institution_oai_pmh_id_seq TO apenet_dashboard;


--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 176
-- Name: c_level; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE c_level FROM PUBLIC;
REVOKE ALL ON TABLE c_level FROM postgres;
GRANT ALL ON TABLE c_level TO postgres;
GRANT ALL ON TABLE c_level TO admin;
GRANT ALL ON TABLE c_level TO apenet_dashboard;
GRANT SELECT ON TABLE c_level TO apenet_portal;


--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 177
-- Name: c_level_cl_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE c_level_cl_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE c_level_cl_id_seq FROM postgres;
GRANT ALL ON SEQUENCE c_level_cl_id_seq TO postgres;
GRANT ALL ON SEQUENCE c_level_cl_id_seq TO admin;
GRANT ALL ON SEQUENCE c_level_cl_id_seq TO apenet_dashboard;


--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 178
-- Name: c_level_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE c_level_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE c_level_id_seq FROM postgres;
GRANT ALL ON SEQUENCE c_level_id_seq TO postgres;
GRANT ALL ON SEQUENCE c_level_id_seq TO admin;
GRANT ALL ON SEQUENCE c_level_id_seq TO apenet_dashboard;


--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 179
-- Name: collection; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE collection FROM PUBLIC;
REVOKE ALL ON TABLE collection FROM postgres;
GRANT ALL ON TABLE collection TO postgres;
GRANT ALL ON TABLE collection TO admin;
GRANT ALL ON TABLE collection TO apenet_dashboard;
GRANT ALL ON TABLE collection TO apenet_portal;


--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 180
-- Name: collection_content; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE collection_content FROM PUBLIC;
REVOKE ALL ON TABLE collection_content FROM postgres;
GRANT ALL ON TABLE collection_content TO postgres;
GRANT ALL ON TABLE collection_content TO admin;
GRANT ALL ON TABLE collection_content TO apenet_dashboard;
GRANT ALL ON TABLE collection_content TO apenet_portal;


--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 181
-- Name: collection_content_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE collection_content_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE collection_content_id_seq FROM postgres;
GRANT ALL ON SEQUENCE collection_content_id_seq TO postgres;
GRANT ALL ON SEQUENCE collection_content_id_seq TO admin;
GRANT ALL ON SEQUENCE collection_content_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE collection_content_id_seq TO apenet_portal;


--
-- TOC entry 3416 (class 0 OID 0)
-- Dependencies: 182
-- Name: collection_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE collection_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE collection_id_seq FROM postgres;
GRANT ALL ON SEQUENCE collection_id_seq TO postgres;
GRANT ALL ON SEQUENCE collection_id_seq TO admin;
GRANT ALL ON SEQUENCE collection_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE collection_id_seq TO apenet_portal;


--
-- TOC entry 3417 (class 0 OID 0)
-- Dependencies: 183
-- Name: coordinates; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE coordinates FROM PUBLIC;
REVOKE ALL ON TABLE coordinates FROM postgres;
GRANT ALL ON TABLE coordinates TO postgres;
GRANT ALL ON TABLE coordinates TO admin;
GRANT ALL ON TABLE coordinates TO apenet_dashboard;
GRANT SELECT ON TABLE coordinates TO apenet_portal;


--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 184
-- Name: coordinates_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE coordinates_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE coordinates_id_seq FROM postgres;
GRANT ALL ON SEQUENCE coordinates_id_seq TO postgres;
GRANT ALL ON SEQUENCE coordinates_id_seq TO apenet_dashboard;
GRANT SELECT ON SEQUENCE coordinates_id_seq TO apenet_portal;
GRANT ALL ON SEQUENCE coordinates_id_seq TO admin;


--
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 185
-- Name: cou_alternative_name; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cou_alternative_name FROM PUBLIC;
REVOKE ALL ON TABLE cou_alternative_name FROM postgres;
GRANT ALL ON TABLE cou_alternative_name TO postgres;
GRANT ALL ON TABLE cou_alternative_name TO admin;
GRANT ALL ON TABLE cou_alternative_name TO apenet_dashboard;
GRANT SELECT ON TABLE cou_alternative_name TO apenet_portal;


--
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 186
-- Name: cou_alternative_name_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE cou_alternative_name_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE cou_alternative_name_id_seq FROM postgres;
GRANT ALL ON SEQUENCE cou_alternative_name_id_seq TO postgres;
GRANT ALL ON SEQUENCE cou_alternative_name_id_seq TO admin;
GRANT ALL ON SEQUENCE cou_alternative_name_id_seq TO apenet_dashboard;


--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 187
-- Name: country; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE country FROM PUBLIC;
REVOKE ALL ON TABLE country FROM postgres;
GRANT ALL ON TABLE country TO postgres;
GRANT ALL ON TABLE country TO admin;
GRANT ALL ON TABLE country TO apenet_dashboard;
GRANT SELECT ON TABLE country TO apenet_portal;


--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 188
-- Name: country_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE country_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE country_id_seq FROM postgres;
GRANT ALL ON SEQUENCE country_id_seq TO postgres;
GRANT ALL ON SEQUENCE country_id_seq TO admin;
GRANT ALL ON SEQUENCE country_id_seq TO apenet_dashboard;


--
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 189
-- Name: dashboard_user; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE dashboard_user FROM PUBLIC;
REVOKE ALL ON TABLE dashboard_user FROM postgres;
GRANT ALL ON TABLE dashboard_user TO postgres;
GRANT ALL ON TABLE dashboard_user TO admin;
GRANT ALL ON TABLE dashboard_user TO apenet_dashboard;
GRANT SELECT ON TABLE dashboard_user TO apenet_portal;


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 190
-- Name: dpt_update; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE dpt_update FROM PUBLIC;
REVOKE ALL ON TABLE dpt_update FROM postgres;
GRANT ALL ON TABLE dpt_update TO postgres;
GRANT ALL ON TABLE dpt_update TO admin;
GRANT ALL ON TABLE dpt_update TO apenet_dashboard;
GRANT SELECT ON TABLE dpt_update TO apenet_portal;


--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 191
-- Name: dpt_update_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE dpt_update_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE dpt_update_id_seq FROM postgres;
GRANT ALL ON SEQUENCE dpt_update_id_seq TO postgres;
GRANT ALL ON SEQUENCE dpt_update_id_seq TO admin;
GRANT ALL ON SEQUENCE dpt_update_id_seq TO apenet_dashboard;


--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 192
-- Name: eac_cpf; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE eac_cpf FROM PUBLIC;
REVOKE ALL ON TABLE eac_cpf FROM postgres;
GRANT ALL ON TABLE eac_cpf TO postgres;
GRANT ALL ON TABLE eac_cpf TO admin;
GRANT ALL ON TABLE eac_cpf TO apenet_dashboard;
GRANT SELECT ON TABLE eac_cpf TO apenet_portal;


--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 193
-- Name: eac_cpf_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE eac_cpf_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE eac_cpf_id_seq FROM postgres;
GRANT ALL ON SEQUENCE eac_cpf_id_seq TO postgres;
GRANT ALL ON SEQUENCE eac_cpf_id_seq TO admin;
GRANT ALL ON SEQUENCE eac_cpf_id_seq TO apenet_dashboard;


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 194
-- Name: ead_content; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ead_content FROM PUBLIC;
REVOKE ALL ON TABLE ead_content FROM postgres;
GRANT ALL ON TABLE ead_content TO postgres;
GRANT ALL ON TABLE ead_content TO admin;
GRANT ALL ON TABLE ead_content TO apenet_dashboard;
GRANT SELECT ON TABLE ead_content TO apenet_portal;


--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 195
-- Name: ead_content_ec_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ead_content_ec_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ead_content_ec_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ead_content_ec_id_seq TO postgres;
GRANT ALL ON SEQUENCE ead_content_ec_id_seq TO admin;
GRANT ALL ON SEQUENCE ead_content_ec_id_seq TO apenet_dashboard;


--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 196
-- Name: ead_content_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ead_content_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ead_content_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ead_content_id_seq TO postgres;
GRANT ALL ON SEQUENCE ead_content_id_seq TO admin;
GRANT ALL ON SEQUENCE ead_content_id_seq TO apenet_dashboard;


--
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 197
-- Name: ead_saved_search; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ead_saved_search FROM PUBLIC;
REVOKE ALL ON TABLE ead_saved_search FROM postgres;
GRANT ALL ON TABLE ead_saved_search TO postgres;
GRANT ALL ON TABLE ead_saved_search TO admin;
GRANT ALL ON TABLE ead_saved_search TO apenet_dashboard;
GRANT ALL ON TABLE ead_saved_search TO apenet_portal;


--
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 198
-- Name: ead_saved_search_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ead_saved_search_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ead_saved_search_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO postgres;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO apenet_portal;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO admin;


--
-- TOC entry 3442 (class 0 OID 0)
-- Dependencies: 199
-- Name: ese; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ese FROM PUBLIC;
REVOKE ALL ON TABLE ese FROM postgres;
GRANT ALL ON TABLE ese TO postgres;
GRANT ALL ON TABLE ese TO admin;
GRANT ALL ON TABLE ese TO apenet_dashboard;


--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 200
-- Name: ese_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ese_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ese_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ese_id_seq TO postgres;
GRANT ALL ON SEQUENCE ese_id_seq TO admin;
GRANT ALL ON SEQUENCE ese_id_seq TO apenet_dashboard;


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 201
-- Name: ese_state; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ese_state FROM PUBLIC;
REVOKE ALL ON TABLE ese_state FROM postgres;
GRANT ALL ON TABLE ese_state TO postgres;
GRANT ALL ON TABLE ese_state TO admin;
GRANT ALL ON TABLE ese_state TO apenet_dashboard;


--
-- TOC entry 3447 (class 0 OID 0)
-- Dependencies: 202
-- Name: ese_state_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ese_state_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ese_state_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ese_state_id_seq TO postgres;
GRANT ALL ON SEQUENCE ese_state_id_seq TO admin;
GRANT ALL ON SEQUENCE ese_state_id_seq TO apenet_dashboard;


--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 203
-- Name: finding_aid; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE finding_aid FROM PUBLIC;
REVOKE ALL ON TABLE finding_aid FROM postgres;
GRANT ALL ON TABLE finding_aid TO postgres;
GRANT ALL ON TABLE finding_aid TO admin;
GRANT ALL ON TABLE finding_aid TO apenet_dashboard;
GRANT SELECT ON TABLE finding_aid TO apenet_portal;


--
-- TOC entry 3450 (class 0 OID 0)
-- Dependencies: 204
-- Name: finding_aid_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE finding_aid_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE finding_aid_id_seq FROM postgres;
GRANT ALL ON SEQUENCE finding_aid_id_seq TO postgres;
GRANT ALL ON SEQUENCE finding_aid_id_seq TO admin;
GRANT ALL ON SEQUENCE finding_aid_id_seq TO apenet_dashboard;


--
-- TOC entry 3451 (class 0 OID 0)
-- Dependencies: 205
-- Name: ftp_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE ftp_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ftp_id_seq FROM postgres;
GRANT ALL ON SEQUENCE ftp_id_seq TO postgres;
GRANT ALL ON SEQUENCE ftp_id_seq TO admin;
GRANT ALL ON SEQUENCE ftp_id_seq TO apenet_dashboard;


--
-- TOC entry 3452 (class 0 OID 0)
-- Dependencies: 206
-- Name: ftp; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ftp FROM PUBLIC;
REVOKE ALL ON TABLE ftp FROM postgres;
GRANT ALL ON TABLE ftp TO postgres;
GRANT ALL ON TABLE ftp TO admin;
GRANT ALL ON TABLE ftp TO apenet_dashboard;


--
-- TOC entry 3453 (class 0 OID 0)
-- Dependencies: 207
-- Name: hg_sg_fa_relation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hg_sg_fa_relation FROM PUBLIC;
REVOKE ALL ON TABLE hg_sg_fa_relation FROM postgres;
GRANT ALL ON TABLE hg_sg_fa_relation TO postgres;
GRANT ALL ON TABLE hg_sg_fa_relation TO admin;
GRANT ALL ON TABLE hg_sg_fa_relation TO apenet_dashboard;
GRANT SELECT ON TABLE hg_sg_fa_relation TO apenet_portal;


--
-- TOC entry 3455 (class 0 OID 0)
-- Dependencies: 208
-- Name: hg_sg_fa_relation_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE hg_sg_fa_relation_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE hg_sg_fa_relation_id_seq FROM postgres;
GRANT ALL ON SEQUENCE hg_sg_fa_relation_id_seq TO postgres;
GRANT ALL ON SEQUENCE hg_sg_fa_relation_id_seq TO admin;
GRANT ALL ON SEQUENCE hg_sg_fa_relation_id_seq TO apenet_dashboard;


--
-- TOC entry 3456 (class 0 OID 0)
-- Dependencies: 209
-- Name: holdings_guide; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE holdings_guide FROM PUBLIC;
REVOKE ALL ON TABLE holdings_guide FROM postgres;
GRANT ALL ON TABLE holdings_guide TO postgres;
GRANT ALL ON TABLE holdings_guide TO admin;
GRANT ALL ON TABLE holdings_guide TO apenet_dashboard;
GRANT SELECT ON TABLE holdings_guide TO apenet_portal;


--
-- TOC entry 3458 (class 0 OID 0)
-- Dependencies: 210
-- Name: holdings_guide_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE holdings_guide_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE holdings_guide_id_seq FROM postgres;
GRANT ALL ON SEQUENCE holdings_guide_id_seq TO postgres;
GRANT ALL ON SEQUENCE holdings_guide_id_seq TO admin;
GRANT ALL ON SEQUENCE holdings_guide_id_seq TO apenet_dashboard;


--
-- TOC entry 3459 (class 0 OID 0)
-- Dependencies: 211
-- Name: queue; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE queue FROM PUBLIC;
REVOKE ALL ON TABLE queue FROM postgres;
GRANT ALL ON TABLE queue TO postgres;
GRANT ALL ON TABLE queue TO admin;
GRANT ALL ON TABLE queue TO apenet_dashboard;


--
-- TOC entry 3461 (class 0 OID 0)
-- Dependencies: 212
-- Name: index_queue_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE index_queue_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE index_queue_id_seq FROM postgres;
GRANT ALL ON SEQUENCE index_queue_id_seq TO postgres;
GRANT ALL ON SEQUENCE index_queue_id_seq TO admin;
GRANT ALL ON SEQUENCE index_queue_id_seq TO apenet_dashboard;


--
-- TOC entry 3462 (class 0 OID 0)
-- Dependencies: 213
-- Name: ingestionprofile; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ingestionprofile FROM PUBLIC;
REVOKE ALL ON TABLE ingestionprofile FROM postgres;
GRANT ALL ON TABLE ingestionprofile TO postgres;
GRANT ALL ON TABLE ingestionprofile TO admin;
GRANT ALL ON TABLE ingestionprofile TO apenet_dashboard;


--
-- TOC entry 3464 (class 0 OID 0)
-- Dependencies: 214
-- Name: lang; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE lang FROM PUBLIC;
REVOKE ALL ON TABLE lang FROM postgres;
GRANT ALL ON TABLE lang TO postgres;
GRANT ALL ON TABLE lang TO admin;
GRANT ALL ON TABLE lang TO apenet_dashboard;
GRANT SELECT ON TABLE lang TO apenet_portal;


--
-- TOC entry 3466 (class 0 OID 0)
-- Dependencies: 215
-- Name: lang_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE lang_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE lang_id_seq FROM postgres;
GRANT ALL ON SEQUENCE lang_id_seq TO postgres;
GRANT ALL ON SEQUENCE lang_id_seq TO admin;
GRANT ALL ON SEQUENCE lang_id_seq TO apenet_dashboard;


--
-- TOC entry 3467 (class 0 OID 0)
-- Dependencies: 216
-- Name: resumption_token; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE resumption_token FROM PUBLIC;
REVOKE ALL ON TABLE resumption_token FROM postgres;
GRANT ALL ON TABLE resumption_token TO postgres;
GRANT ALL ON TABLE resumption_token TO admin;
GRANT ALL ON TABLE resumption_token TO apenet_dashboard;


--
-- TOC entry 3469 (class 0 OID 0)
-- Dependencies: 217
-- Name: resumption_token_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE resumption_token_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE resumption_token_id_seq FROM postgres;
GRANT ALL ON SEQUENCE resumption_token_id_seq TO postgres;
GRANT ALL ON SEQUENCE resumption_token_id_seq TO admin;
GRANT ALL ON SEQUENCE resumption_token_id_seq TO apenet_dashboard;


--
-- TOC entry 3470 (class 0 OID 0)
-- Dependencies: 218
-- Name: saved_bookmarks; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE saved_bookmarks FROM PUBLIC;
REVOKE ALL ON TABLE saved_bookmarks FROM postgres;
GRANT ALL ON TABLE saved_bookmarks TO postgres;
GRANT ALL ON TABLE saved_bookmarks TO admin;
GRANT ALL ON TABLE saved_bookmarks TO apenet_dashboard;
GRANT ALL ON TABLE saved_bookmarks TO apenet_portal;


--
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 219
-- Name: saved_bookmarks_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE saved_bookmarks_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE saved_bookmarks_id_seq FROM postgres;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO postgres;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO admin;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE saved_bookmarks_id_seq TO apenet_portal;


--
-- TOC entry 3473 (class 0 OID 0)
-- Dependencies: 220
-- Name: sent_mail_register; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sent_mail_register FROM PUBLIC;
REVOKE ALL ON TABLE sent_mail_register FROM postgres;
GRANT ALL ON TABLE sent_mail_register TO postgres;
GRANT ALL ON TABLE sent_mail_register TO admin;
GRANT ALL ON TABLE sent_mail_register TO apenet_portal;
GRANT ALL ON TABLE sent_mail_register TO apenet_dashboard;


--
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 221
-- Name: sent_mail_register_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE sent_mail_register_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE sent_mail_register_id_seq FROM postgres;
GRANT ALL ON SEQUENCE sent_mail_register_id_seq TO postgres;
GRANT ALL ON SEQUENCE sent_mail_register_id_seq TO admin;
GRANT ALL ON SEQUENCE sent_mail_register_id_seq TO apenet_dashboard;


--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 222
-- Name: source_guide; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE source_guide FROM PUBLIC;
REVOKE ALL ON TABLE source_guide FROM postgres;
GRANT ALL ON TABLE source_guide TO postgres;
GRANT ALL ON TABLE source_guide TO admin;
GRANT ALL ON TABLE source_guide TO apenet_dashboard;
GRANT SELECT ON TABLE source_guide TO apenet_portal;


--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 223
-- Name: source_guide_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE source_guide_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE source_guide_id_seq FROM postgres;
GRANT ALL ON SEQUENCE source_guide_id_seq TO postgres;
GRANT ALL ON SEQUENCE source_guide_id_seq TO admin;
GRANT ALL ON SEQUENCE source_guide_id_seq TO apenet_dashboard;


--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 225
-- Name: topic; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE topic FROM PUBLIC;
REVOKE ALL ON TABLE topic FROM postgres;
GRANT ALL ON TABLE topic TO postgres;
GRANT ALL ON TABLE topic TO admin;
GRANT ALL ON TABLE topic TO apenet_dashboard;
GRANT ALL ON TABLE topic TO apenet_portal;


--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 226
-- Name: topic_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE topic_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE topic_id_seq FROM postgres;
GRANT ALL ON SEQUENCE topic_id_seq TO postgres;
GRANT ALL ON SEQUENCE topic_id_seq TO admin;
GRANT ALL ON SEQUENCE topic_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE topic_id_seq TO apenet_portal;


--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 227
-- Name: topic_mapping; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE topic_mapping FROM PUBLIC;
REVOKE ALL ON TABLE topic_mapping FROM postgres;
GRANT ALL ON TABLE topic_mapping TO postgres;
GRANT ALL ON TABLE topic_mapping TO admin;
GRANT ALL ON TABLE topic_mapping TO apenet_dashboard;
GRANT ALL ON TABLE topic_mapping TO apenet_portal;


--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 228
-- Name: topic_mapping_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE topic_mapping_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE topic_mapping_id_seq FROM postgres;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO postgres;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO admin;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO apenet_portal;


--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 229
-- Name: up_file; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE up_file FROM PUBLIC;
REVOKE ALL ON TABLE up_file FROM postgres;
GRANT ALL ON TABLE up_file TO postgres;
GRANT ALL ON TABLE up_file TO admin;
GRANT ALL ON TABLE up_file TO apenet_dashboard;
GRANT SELECT ON TABLE up_file TO apenet_portal;


--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 230
-- Name: up_file_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE up_file_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE up_file_id_seq FROM postgres;
GRANT ALL ON SEQUENCE up_file_id_seq TO postgres;
GRANT ALL ON SEQUENCE up_file_id_seq TO admin;
GRANT ALL ON SEQUENCE up_file_id_seq TO apenet_dashboard;


--
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 231
-- Name: upload_method; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE upload_method FROM PUBLIC;
REVOKE ALL ON TABLE upload_method FROM postgres;
GRANT ALL ON TABLE upload_method TO postgres;
GRANT ALL ON TABLE upload_method TO admin;
GRANT ALL ON TABLE upload_method TO apenet_dashboard;
GRANT SELECT ON TABLE upload_method TO apenet_portal;


--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 232
-- Name: upload_method_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE upload_method_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE upload_method_id_seq FROM postgres;
GRANT ALL ON SEQUENCE upload_method_id_seq TO postgres;
GRANT ALL ON SEQUENCE upload_method_id_seq TO admin;
GRANT ALL ON SEQUENCE upload_method_id_seq TO apenet_dashboard;


--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 233
-- Name: user_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE user_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE user_id_seq FROM postgres;
GRANT ALL ON SEQUENCE user_id_seq TO postgres;
GRANT ALL ON SEQUENCE user_id_seq TO admin;
GRANT ALL ON SEQUENCE user_id_seq TO apenet_dashboard;


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 234
-- Name: user_role; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE user_role FROM PUBLIC;
REVOKE ALL ON TABLE user_role FROM postgres;
GRANT ALL ON TABLE user_role TO postgres;
GRANT ALL ON TABLE user_role TO admin;
GRANT ALL ON TABLE user_role TO apenet_dashboard;


--
-- TOC entry 3496 (class 0 OID 0)
-- Dependencies: 235
-- Name: user_role_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE user_role_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE user_role_id_seq FROM postgres;
GRANT ALL ON SEQUENCE user_role_id_seq TO postgres;
GRANT ALL ON SEQUENCE user_role_id_seq TO admin;
GRANT ALL ON SEQUENCE user_role_id_seq TO apenet_dashboard;


--
-- TOC entry 3498 (class 0 OID 0)
-- Dependencies: 236
-- Name: userprofile_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE userprofile_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE userprofile_id_seq FROM postgres;
GRANT ALL ON SEQUENCE userprofile_id_seq TO postgres;
GRANT ALL ON SEQUENCE userprofile_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE userprofile_id_seq TO admin;


--
-- TOC entry 3499 (class 0 OID 0)
-- Dependencies: 237
-- Name: warnings; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE warnings FROM PUBLIC;
REVOKE ALL ON TABLE warnings FROM postgres;
GRANT ALL ON TABLE warnings TO postgres;
GRANT ALL ON TABLE warnings TO admin;
GRANT ALL ON TABLE warnings TO apenet_dashboard;


--
-- TOC entry 3501 (class 0 OID 0)
-- Dependencies: 238
-- Name: warnings_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE warnings_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE warnings_id_seq FROM postgres;
GRANT ALL ON SEQUENCE warnings_id_seq TO postgres;
GRANT ALL ON SEQUENCE warnings_id_seq TO admin;
GRANT ALL ON SEQUENCE warnings_id_seq TO apenet_dashboard;


--
-- TOC entry 3502 (class 0 OID 0)
-- Dependencies: 239
-- Name: xsl_upload; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE xsl_upload FROM PUBLIC;
REVOKE ALL ON TABLE xsl_upload FROM postgres;
GRANT ALL ON TABLE xsl_upload TO postgres;
GRANT ALL ON TABLE xsl_upload TO apenet_dashboard;
GRANT ALL ON TABLE xsl_upload TO admin;


--
-- TOC entry 3504 (class 0 OID 0)
-- Dependencies: 240
-- Name: xsl_upload_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE xsl_upload_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE xsl_upload_id_seq FROM postgres;
GRANT ALL ON SEQUENCE xsl_upload_id_seq TO postgres;
GRANT ALL ON SEQUENCE xsl_upload_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE xsl_upload_id_seq TO admin;


-- Completed on 2015-12-02 15:32:49 CET

--
-- PostgreSQL database dump complete
--

