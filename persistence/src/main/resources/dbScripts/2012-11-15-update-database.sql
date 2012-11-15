ALTER TABLE up_file ADD COLUMN file_type character varying(3) default NULL;
UPDATE up_file SET file_type  = 'XML' WHERE ft_id =1;
UPDATE up_file SET file_type = 'ZIP' WHERE ft_id =0;
UPDATE up_file SET file_type = 'XSL' WHERE ft_id =2;
ALTER TABLE up_file ALTER COLUMN file_type SET NOT NULL;
ALTER TABLE up_file DROP COLUMN ft_id;
DROP TABLE IF EXISTS file_type;
ALTER TABLE up_file RENAME COLUMN fname TO filename;
ALTER TABLE ONLY queue
    ADD CONSTRAINT queue_uf_id_fkey FOREIGN KEY (uf_id) REFERENCES up_file(id);