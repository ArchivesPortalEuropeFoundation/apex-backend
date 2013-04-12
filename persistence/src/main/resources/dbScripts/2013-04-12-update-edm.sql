ALTER TABLE finding_aid ADD COLUMN totalnumberofcho bigint default 0;
UPDATE finding_aid SET totalnumberofcho  = 0;
UPDATE finding_aid SET totalnumberofcho  = (totalnumberofdaos + 1) WHERE totalnumberofdaos > 0;