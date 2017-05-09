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
-- Add new column for EAD3 to queue
--

ALTER TABLE queue
   ADD COLUMN ead3_id integer;


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
-- Add new column for EAD3 to warnings
--

ALTER TABLE warnings
   ADD COLUMN ead3_id integer;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: apenet_dashboard
--

ALTER TABLE ONLY ead3 ALTER COLUMN id SET DEFAULT nextval('ead3_id_seq'::regclass);

--
-- Name: id; Type: DEFAULT; Schema: public; Owner: apenet_dashboard
--

ALTER TABLE ONLY reindex_doc ALTER COLUMN id SET DEFAULT nextval('reindex_doc_id_seq'::regclass);


--
-- Name: ead3_pkey; Type: CONSTRAINT; Schema: public; Owner: apenet_dashboard; Tablespace: 
--

ALTER TABLE ONLY ead3
    ADD CONSTRAINT ead3_pkey PRIMARY KEY (id);


--
-- Name: reindex_doc_pkey; Type: CONSTRAINT; Schema: public; Owner: apenet_dashboard; Tablespace: 
--

ALTER TABLE ONLY reindex_doc
    ADD CONSTRAINT reindex_doc_pkey PRIMARY KEY (id);


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


