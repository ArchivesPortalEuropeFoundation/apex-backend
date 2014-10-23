-- Delete preferences from queue table.
alter table queue drop column preferences;

-- Delete preferences to up_file table.
alter table up_file add column preferences text;