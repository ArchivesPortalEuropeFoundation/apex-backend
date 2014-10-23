alter table collection rename column public to public_collection;
alter table collection add column modified_date timestamp without time zone NOT NULL DEFAULT now();