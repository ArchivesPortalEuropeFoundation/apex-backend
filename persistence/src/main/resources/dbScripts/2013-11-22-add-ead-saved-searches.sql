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