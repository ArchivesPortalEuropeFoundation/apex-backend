ALTER TABLE finding_aid ADD COLUMN totalnumberofchos bigint default 0;
UPDATE finding_aid SET totalnumberofchos  = 0;
UPDATE finding_aid SET totalnumberofchos  = (totalnumberofdaos + 1) WHERE totalnumberofdaos > 0 and europeana > 0;