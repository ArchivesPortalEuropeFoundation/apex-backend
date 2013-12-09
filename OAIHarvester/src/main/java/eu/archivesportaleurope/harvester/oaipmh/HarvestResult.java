package eu.archivesportaleurope.harvester.oaipmh;

import java.util.Date;

import org.oclc.oai.harvester.parser.record.OaiPmhRecord;

public class HarvestResult {
    private DateHarvestModel newestFileHarvested;
    private DateHarvestModel oldestFileHarvested;
    
    
    public DateHarvestModel getNewestFileHarvested() {
		return newestFileHarvested;
	}


	public void setNewestFileHarvested(DateHarvestModel newestFileHarvested) {
		this.newestFileHarvested = newestFileHarvested;
	}


	public DateHarvestModel getOldestFileHarvested() {
		return oldestFileHarvested;
	}


	public void setOldestFileHarvested(DateHarvestModel oldestFileHarvested) {
		this.oldestFileHarvested = oldestFileHarvested;
	}
	public void add(OaiPmhRecord oaiPmhRecord){
        if(oldestFileHarvested == null && newestFileHarvested == null) {
            oldestFileHarvested = new DateHarvestModel(oaiPmhRecord.getTimestamp(), oaiPmhRecord.getIdentifier());
            newestFileHarvested = new DateHarvestModel(oaiPmhRecord.getTimestamp(), oaiPmhRecord.getIdentifier());
        } else {
            if(!newestFileHarvested.isCurrentOlderThanNew(oaiPmhRecord.getTimestamp())) {
                newestFileHarvested = new DateHarvestModel(oaiPmhRecord.getTimestamp(), oaiPmhRecord.getIdentifier());
            } else if(oldestFileHarvested.isCurrentOlderThanNew(oaiPmhRecord.getTimestamp())) {
                oldestFileHarvested = new DateHarvestModel(oaiPmhRecord.getTimestamp(), oaiPmhRecord.getIdentifier());
            }
        }
	}

	class DateHarvestModel {
        private Date datestamp;
        private String identifier;

        public DateHarvestModel(Date datestamp, String identifier) {
            this.datestamp = datestamp;
            this.identifier = identifier;
        }

        @Override
        public String toString() {
            return "datestamp: '" + datestamp + "' - identifier: '" + identifier + "'";
        }

        public boolean isCurrentOlderThanNew(Date newDate) {
            return datestamp.after(newDate);
        }
    }

}
