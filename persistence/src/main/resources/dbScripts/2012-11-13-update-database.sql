UPDATE finding_aid SET totalnumberofdaos = 0 WHERE totalnumberofdaos is null;
UPDATE finding_aid SET totalnumberofunits = 0 WHERE totalnumberofunits is null;
UPDATE finding_aid SET totalnumberofunitswithdao = 0 WHERE totalnumberofunitswithdao is null;
ALTER TABLE finding_aid ALTER COLUMN totalnumberofdaos SET NOT NULL;    
ALTER TABLE finding_aid ALTER COLUMN totalnumberofunits SET NOT NULL;    
ALTER TABLE finding_aid ALTER COLUMN totalnumberofunitswithdao SET NOT NULL;  

UPDATE holdings_guide SET totalnumberofdaos = 0 WHERE totalnumberofdaos is null;
UPDATE holdings_guide SET totalnumberofunits = 0 WHERE totalnumberofunits is null;
UPDATE holdings_guide SET totalnumberofunitswithdao = 0 WHERE totalnumberofunitswithdao is null;
ALTER TABLE holdings_guide ALTER COLUMN totalnumberofdaos SET NOT NULL;    
ALTER TABLE holdings_guide ALTER COLUMN totalnumberofunits SET NOT NULL;    
ALTER TABLE holdings_guide ALTER COLUMN totalnumberofunitswithdao SET NOT NULL;  

UPDATE source_guide SET totalnumberofdaos = 0 WHERE totalnumberofdaos is null;
UPDATE source_guide SET totalnumberofunits = 0 WHERE totalnumberofunits is null;
UPDATE source_guide SET totalnumberofunitswithdao = 0 WHERE totalnumberofunitswithdao is null;
ALTER TABLE source_guide ALTER COLUMN totalnumberofdaos SET NOT NULL;    
ALTER TABLE source_guide ALTER COLUMN totalnumberofunits SET NOT NULL;    
ALTER TABLE source_guide ALTER COLUMN totalnumberofunitswithdao SET NOT NULL;  