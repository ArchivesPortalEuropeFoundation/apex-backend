DELETE FROM file_state;

INSERT INTO file_state (fs_id, state) VALUES (1, 'New');
INSERT INTO file_state (fs_id, state) VALUES (4, 'Validated_Converted');
INSERT INTO file_state (fs_id, state) VALUES (2, 'Not_Validated_Not_Converted');
INSERT INTO file_state (fs_id, state) VALUES (3, 'Not_Validated_Converted');
INSERT INTO file_state (fs_id, state) VALUES (5, 'Validated_Not_Converted');
INSERT INTO file_state (fs_id, state) VALUES (6, 'Validated_Final_Error');
INSERT INTO file_state (fs_id, state) VALUES (7, 'Indexing');
INSERT INTO file_state (fs_id, state) VALUES (8, 'Indexed_Not converted to ESE/EDM');
INSERT INTO file_state (fs_id, state) VALUES (9, 'Indexed_Converted to ESE/EDM');
INSERT INTO file_state (fs_id, state) VALUES (10, 'Indexed_Delivered to Europeana');
INSERT INTO file_state (fs_id, state) VALUES (11, 'Indexed_Harvested to Europeana');
INSERT INTO file_state (fs_id, state) VALUES (12, 'Indexed_No html');
INSERT INTO file_state (fs_id, state) VALUES (13, 'Indexed_Not linked');
INSERT INTO file_state (fs_id, state) VALUES (14, 'Indexed_Linked');
INSERT INTO file_state (fs_id, state) VALUES (15, 'Ready_to_index');
