package eu.archivesportaleurope.harvester.oaipmh;

import java.util.Date;

import eu.archivesportaleurope.harvester.oaipmh.parser.record.OaiPmhRecord;

public class HarvestResult {
    private DateHarvestModel newestFileHarvested;
    private DateHarvestModel oldestFileHarvested;
    private String errors;
    private int numberOfRecords;
    
    
    public String getErrors() {
		return errors;
	}


	public int getNumberOfRecords() {
		return numberOfRecords;
	}


	public void setNumberOfRecords(int numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}


	public void setErrors(String errors) {
		this.errors = errors;
	}
	public void addErrors(String errors) {
		if (this.errors == null){
			this.errors = errors + "\n";
		}else {
			this.errors += errors + "\n";
		}
	}

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

	public static class DateHarvestModel {
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
