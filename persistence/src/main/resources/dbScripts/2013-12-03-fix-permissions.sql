REVOKE ALL ON SEQUENCE ead_saved_search_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE ead_saved_search_id_seq FROM admin;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO admin;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO apenet_dashboard;
GRANT ALL ON SEQUENCE ead_saved_search_id_seq TO apenet_portal;