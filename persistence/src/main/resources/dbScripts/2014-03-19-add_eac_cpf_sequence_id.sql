-- Sequence: eac_cpf_id_seq

-- DROP SEQUENCE eac_cpf_id_seq;

CREATE SEQUENCE eac_cpf_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE eac_cpf_id_seq
  OWNER TO postgres;
GRANT ALL ON TABLE eac_cpf_id_seq TO postgres;
GRANT ALL ON TABLE eac_cpf_id_seq TO admin;
GRANT ALL ON TABLE eac_cpf_id_seq TO apenet_dashboard;

ALTER TABLE eac_cpf
   ALTER COLUMN id SET DEFAULT nextval('eac_cpf_id_seq'::regclass);
