alter table ead_saved_search alter column refinement_topic text DEFAULT NULL;
alter table ead_saved_search drop column pagenumber;
alter table ead_saved_search drop column sorting;
alter table ead_saved_search drop column results_per_page;
alter table ead_saved_search drop column sorting_asc;
