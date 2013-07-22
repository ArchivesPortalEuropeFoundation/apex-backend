UPDATE finding_aid SET published = FALSE, totalnumberofunits=0, totalnumberofunitswithdao=0;
UPDATE holdings_guide SET published = FALSE, totalnumberofunits=0, totalnumberofunitswithdao=0;
UPDATE source_guide SET published = FALSE, totalnumberofunits=0, totalnumberofunitswithdao=0;
UPDATE archival_institution SET contain_searchable_items = FALSE;