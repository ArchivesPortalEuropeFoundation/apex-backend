ALTER TABLE topic
  OWNER TO postgres;
REVOKE ALL ON TABLE topic FROM PUBLIC;
GRANT ALL ON TABLE topic TO postgres;
GRANT ALL ON TABLE topic TO admin;
GRANT ALL ON TABLE topic TO apenet_dashboard;
GRANT ALL ON TABLE topic TO apenet_portal;


ALTER TABLE topic_mapping
  OWNER TO postgres;
REVOKE ALL ON TABLE topic_mapping FROM PUBLIC;
GRANT ALL ON TABLE topic_mapping TO postgres;
GRANT ALL ON TABLE topic_mapping TO admin;
GRANT ALL ON TABLE topic_mapping TO apenet_dashboard;
GRANT ALL ON TABLE topic_mapping TO apenet_portal;

ALTER TABLE topic_mapping_id_seq
  OWNER TO postgres;
REVOKE ALL ON SEQUENCE topic_mapping_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE topic_mapping_id_seq FROM admin;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO admin;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE topic_mapping_id_seq TO apenet_portal;

ALTER TABLE topic_id_seq
  OWNER TO postgres;
REVOKE ALL ON SEQUENCE topic_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE topic_id_seq FROM admin;
GRANT ALL ON SEQUENCE topic_id_seq TO admin;
GRANT ALL ON SEQUENCE topic_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE topic_id_seq TO apenet_portal;