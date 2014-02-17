CREATE TABLE ingestionprofile
(
	id bigserial NOT NULL,
	name_profile character varying(50) NOT NULL,
	file_type integer NOT NULL DEFAULT 1,
	upload_action integer NOT NULL DEFAULT 1,
	exist_action integer NOT NULL DEFAULT 1,
	dao_type integer NOT NULL DEFAULT 6,
	dao_type_from_file boolean NOT NULL DEFAULT true,
	noeadid_action integer NOT NULL DEFAULT 1,
	ai_id integer NOT NULL,
	europeana_dp character varying(100),
	europeana_dp_from_file boolean DEFAULT true,
	europeana_languages character varying(60),
	europeana_languages_from_file boolean DEFAULT true,
	europeana_license character varying(60),
	europeana_license_details character varying(60),
	europeana_dao_type integer,
	europeana_dao_type_from_file boolean DEFAULT true,
	europeana_add_rights character varying(200),
	europeana_hp character varying(20) DEFAULT 'Context information',
	europeana_inh_elements boolean DEFAULT false,
	europeana_inh_origin boolean DEFAULT false,
	CONSTRAINT ingestionprofile_pkey PRIMARY KEY (id),
	CONSTRAINT ingestionprofile_ai_id_fkey FOREIGN KEY (ai_id)
	      REFERENCES archival_institution (id) MATCH SIMPLE
	      ON UPDATE CASCADE ON DELETE SET NULL

);
ALTER TABLE ingestionprofile OWNER TO postgres;
GRANT ALL ON TABLE ingestionprofile TO postgres;
GRANT ALL ON TABLE ingestionprofile TO admin;
GRANT ALL ON TABLE ingestionprofile TO apenet_dashboard;

REVOKE ALL ON SEQUENCE ingestionprofile_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ingestionprofile_id_seq FROM admin;
GRANT ALL ON SEQUENCE ingestionprofile_id_seq TO admin;
GRANT ALL ON SEQUENCE ingestionprofile_id_seq TO apenet_dashboard;

ALTER TABLE archival_institution_oai_pmh ALTER COLUMN id TYPE bigint;
ALTER TABLE archival_institution_oai_pmh ALTER COLUMN oai_pmh_url TYPE character varying(255);
ALTER TABLE archival_institution_oai_pmh ALTER COLUMN oai_pmh_id TYPE character varying(255);
ALTER TABLE archival_institution_oai_pmh RENAME COLUMN oai_pmh_id TO oai_pmh_set;
ALTER TABLE archival_institution_oai_pmh ALTER COLUMN oai_pmh_set DROP NOT NULL;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN oai_pmh_metadata character varying(255) NOT NULL;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN profile_id bigint NOT NULL;
ALTER TABLE archival_institution_oai_pmh ADD CONSTRAINT archival_institutition_oai_pmh_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES ingestionprofile (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;



ALTER TABLE archival_institution_oai_pmh ALTER COLUMN last_harvesting TYPE timestamp without time zone;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN interval_harvesting bigint NOT NULL;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN enabled boolean;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN harvest_only_weekend boolean NOT NULL DEFAULT false;

ALTER TABLE finding_aid ADD COLUMN publish_date timestamp without time zone NULL;
ALTER TABLE source_guide ADD COLUMN publish_date timestamp without time zone NULL;
ALTER TABLE holdings_guide ADD COLUMN publish_date timestamp without time zone NULL;
ALTER TABLE archival_institution ADD COLUMN content_lastmodified_date timestamp without time zone NULL;
UPDATE finding_aid SET  publish_date=NOW() WHERE published = true;
UPDATE source_guide SET  publish_date=NOW() WHERE published = true;
UPDATE holdings_guide SET  publish_date=NOW() WHERE published = true;
UPDATE archival_institution SET  content_lastmodified_date=NOW() WHERE contain_searchable_items = true and isgroup = false ;
ALTER TABLE queue DROP CONSTRAINT index_queue_sg_id_fkey, ADD CONSTRAINT index_queue_sg_id_fkey FOREIGN KEY (sg_id) REFERENCES source_guide(id) ON DELETE CASCADE;

CREATE TABLE coordinates (
    id serial NOT NULL,
    ai_id integer NOT NULL,
    name_institution character varying NOT NULL,
    address character varying NOT NULL,
    lat numeric,
    lon numeric,
      CONSTRAINT coordinates_archival_institution_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) ON UPDATE NO ACTION ON DELETE CASCADE
);

REVOKE ALL ON TABLE coordinates FROM PUBLIC;
REVOKE ALL ON TABLE coordinates FROM postgres;
GRANT ALL ON TABLE coordinates TO postgres;
GRANT ALL ON TABLE coordinates TO admin;
GRANT ALL ON TABLE coordinates TO apenet_dashboard;
GRANT SELECT ON TABLE coordinates TO apenet_portal;

REVOKE ALL ON SEQUENCE coordinates_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE coordinates_id_seq FROM postgres;
GRANT ALL ON SEQUENCE coordinates_id_seq TO postgres;
GRANT ALL ON SEQUENCE coordinates_id_seq TO apenet_dashboard;
GRANT SELECT ON SEQUENCE coordinates_id_seq TO apenet_portal;
GRANT ALL ON SEQUENCE coordinates_id_seq TO admin;

CREATE TABLE ead_saved_search
(
  id bigserial NOT NULL,
  liferay_user_id bigint NOT NULL,
  modified_date timestamp without time zone NOT NULL DEFAULT now(),
  public_search boolean NOT NULL DEFAULT false,
  template boolean NOT NULL DEFAULT false,
  description character varying(255) DEFAULT NULL,
  
  search_term character varying(100) DEFAULT NULL,
  hierarchy boolean NOT NULL DEFAULT false,
  only_with_daos boolean NOT NULL DEFAULT false,
  method_optional boolean NOT NULL DEFAULT false,
  
  
  element character varying(1) DEFAULT NULL,
  typedocument character varying(2) DEFAULT NULL,
  fromdate character varying(10) DEFAULT NULL,
  todate character varying(10) DEFAULT NULL,
  exact_date_search boolean NOT NULL DEFAULT false,
  
  al_tree_selected_nodes text DEFAULT NULL,

  results_per_page smallint DEFAULT NULL,
  pagenumber integer DEFAULT NULL,
  sorting character varying(128),
  sorting_asc boolean DEFAULT NULL,
  
  refinement_country text DEFAULT NULL,
  refinement_ai text DEFAULT NULL,
  refinement_fond character varying(22) DEFAULT NULL,
  refinement_type character varying(5) DEFAULT NULL,
  refinement_level character varying(15) DEFAULT NULL,  
  refinement_dao character varying(10) DEFAULT NULL,
  refinement_roledao character varying(20) DEFAULT NULL,
  refinement_date_type character varying(25) DEFAULT NULL,
  refinement_startdate character varying(12) DEFAULT NULL,
  refinement_enddate character varying(12) DEFAULT NULL,
  refinement_facet_settings character varying(255) DEFAULT NULL,
  CONSTRAINT ead_saved_search_pkey PRIMARY KEY (id)
);
ALTER TABLE ead_saved_search OWNER TO postgres;
REVOKE ALL ON TABLE ead_saved_search FROM PUBLIC;
GRANT ALL ON TABLE ead_saved_search TO postgres;
GRANT ALL ON TABLE ead_saved_search TO admin;
GRANT ALL ON TABLE ead_saved_search TO apenet_dashboard;
GRANT ALL ON TABLE ead_saved_search TO apenet_portal;

CREATE INDEX ead_saved_search__liferay_user_idx
  ON ead_saved_search
  USING btree
  (liferay_user_id);

REVOKE ALL ON SEQUENCE ead_saved_search_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ead_saved_search_id_seq FROM admin;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO admin;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO apenet_portal;

ALTER TABLE dpt_update ADD COLUMN new_version character varying(255);

REVOKE ALL ON SEQUENCE ead_saved_search_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ead_saved_search_id_seq FROM admin;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO admin;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO apenet_portal;

ALTER TABLE ingestionprofile ALTER COLUMN name_profile TYPE character varying(100);
ALTER TABLE ingestionprofile ALTER COLUMN europeana_languages TYPE character varying(100);
ALTER TABLE ingestionprofile ALTER COLUMN europeana_hp TYPE character varying(100);
ALTER TABLE ingestionprofile ALTER COLUMN europeana_license TYPE character varying(100);
ALTER TABLE ingestionprofile ALTER COLUMN europeana_license_details TYPE character varying(100);

alter table ead_saved_search alter column refinement_roledao TYPE character varying(40);

ALTER TABLE ingestionprofile
   ADD COLUMN europeana_conversion_type boolean DEFAULT true;
ALTER TABLE archival_institution_oai_pmh DROP CONSTRAINT archival_institutition_oai_pmh_profile_id_fkey;
ALTER TABLE archival_institution_oai_pmh DROP CONSTRAINT archival_institution_oai_pmh_ai_id_fkey;

ALTER TABLE archival_institution_oai_pmh ADD CONSTRAINT archival_institution_oai_pmh_ai_id_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) ON DELETE CASCADE;
      
ALTER TABLE archival_institution_oai_pmh ADD CONSTRAINT archival_institutition_oai_pmh_profile_id_fkey FOREIGN KEY (profile_id)
      REFERENCES ingestionprofile (id);

ALTER TABLE ingestionprofile DROP CONSTRAINT ingestionprofile_ai_id_fkey;      
ALTER TABLE ingestionprofile ADD CONSTRAINT ingestionprofile_ai_id_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) ON DELETE CASCADE;
      
ALTER TABLE ai_alternative_name DROP CONSTRAINT archival_institution_ai_id_fkey;    
ALTER TABLE ai_alternative_name ADD CONSTRAINT archival_institution_ai_id_fkey FOREIGN KEY (ai_id)
      REFERENCES archival_institution (id) ON DELETE CASCADE;
      
ALTER TABLE archival_institution DROP CONSTRAINT archival_institution_parent_ai_id_fkey;         
ALTER TABLE archival_institution ADD CONSTRAINT archival_institution_parent_ai_id_fkey FOREIGN KEY (parent_ai_id)
      REFERENCES archival_institution (id) ON DELETE CASCADE;
alter table ead_saved_search alter column refinement_type TYPE character varying(9);

ALTER TABLE ingestionprofile ADD COLUMN europeana_hp_check boolean DEFAULT true;
ALTER TABLE ingestionprofile ADD COLUMN europeana_inh_elements_check boolean DEFAULT true;
ALTER TABLE ingestionprofile ADD COLUMN europeana_inh_origin_check boolean DEFAULT true;

ALTER TABLE archival_institution_oai_pmh ADD COLUMN errors boolean DEFAULT false;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN from_date character varying(10);
ALTER TABLE archival_institution_oai_pmh ADD COLUMN new_harvesting timestamp without time zone DEFAULT NOW();
ALTER TABLE archival_institution_oai_pmh DROP COLUMN errors;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN errors text DEFAULT null;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN errors_response_path character varying(255) DEFAULT null;
ALTER TABLE archival_institution_oai_pmh DROP COLUMN errors;
ALTER TABLE archival_institution_oai_pmh DROP COLUMN errors_response_path;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN errors_response_path text DEFAULT null;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN list_by_identifiers boolean DEFAULT false;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN harvesting_details text DEFAULT null;
ALTER TABLE archival_institution_oai_pmh ADD COLUMN harvesting_status  character varying(255) DEFAULT null;

DROP TABLE item;