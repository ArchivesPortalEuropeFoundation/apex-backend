alter table queue add column ai_id integer;

update queue set queue.ai_id = finding_aid.ai_id FROM queue, finding_aid WHERE queue.fa_id = finding_aid.id;
update queue set queue.ai_id = holdings_guide.ai_id FROM queue, holdings_guide WHERE queue.hg_id = holdings_guide.id;
update queue set queue.ai_id = source_guide.ai_id FROM queue, source_guide WHERE queue.sg_id = source_guide.id;
update queue set queue.ai_id = eac_cpf.ai_id FROM queue, eac_cpf WHERE queue.eac_cpf_id = eac_cpf.id;
update queue set queue.ai_id = up_file.ai_id FROM queue, up_file WHERE queue.uf_id = up_file.id;

alter table queue alter column ai_id integer not null;