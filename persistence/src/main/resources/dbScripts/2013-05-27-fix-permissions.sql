REVOKE ALL ON SEQUENCE hg_sg_fa_relation_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE hg_sg_fa_relation_id_seq FROM admin;
GRANT ALL ON SEQUENCE hg_sg_fa_relation_id_seq TO admin;
GRANT ALL ON SEQUENCE hg_sg_fa_relation_id_seq TO apenet_dashboard;