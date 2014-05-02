ALTER TABLE coordinates ADD CONSTRAINT coordinates_pkey PRIMARY KEY (id);
ALTER INDEX partner_pkey RENAME TO dashboard_user_pkey;
ALTER INDEX index_queue_pkey RENAME TO queue_pkey;
ALTER TABLE queue ADD CONSTRAINT queue_pkey PRIMARY KEY (id);