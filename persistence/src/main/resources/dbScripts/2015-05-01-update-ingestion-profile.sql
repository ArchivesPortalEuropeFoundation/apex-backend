ALTER TABLE ingestionprofile
   ADD COLUMN europeana_inh_unittitle_check boolean DEFAULT true;
ALTER TABLE ingestionprofile
   ADD COLUMN europeana_inh_unittitle boolean DEFAULT false;
