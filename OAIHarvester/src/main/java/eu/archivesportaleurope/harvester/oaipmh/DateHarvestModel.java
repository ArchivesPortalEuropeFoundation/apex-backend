package eu.archivesportaleurope.harvester.oaipmh;

import java.util.Date;

public class DateHarvestModel {

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
