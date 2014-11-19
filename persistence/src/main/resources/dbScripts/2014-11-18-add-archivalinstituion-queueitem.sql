alter table queue add column ai_id integer;

update queue set ai_id = finding_aid.ai_id FROM queue queue1, finding_aid WHERE queue1.fa_id = finding_aid.id;
update queue set ai_id = holdings_guide.ai_id FROM queue queue1, holdings_guide WHERE queue1.hg_id = holdings_guide.id;
update queue set ai_id = source_guide.ai_id FROM queue queue1, source_guide WHERE queue1.sg_id = source_guide.id;
update queue set ai_id = eac_cpf.ai_id FROM queue queue1, eac_cpf WHERE queue1.eac_cpf_id = eac_cpf.id;
update queue set ai_id = up_file.ai_id FROM queue queue1, up_file WHERE queue1.uf_id = up_file.id;

alter table queue alter column ai_id set not null;