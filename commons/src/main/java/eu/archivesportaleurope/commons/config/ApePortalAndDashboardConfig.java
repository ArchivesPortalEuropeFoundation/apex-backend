package eu.archivesportaleurope.commons.config;


public class ApePortalAndDashboardConfig extends ApeConfig{

    private String archivalLandscapeDirPath;
    private boolean archdescSearchable = true;

	public String getArchivalLandscapeDirPath() {
		return archivalLandscapeDirPath;
	}

	public void setArchivalLandscapeDirPath(String archivalLandscapeDirPath) {
		checkConfigured();
		this.archivalLandscapeDirPath = archivalLandscapeDirPath;
	}

	public boolean isArchdescSearchable() {
		return archdescSearchable;
	}

	public void setArchdescSearchable(boolean archdescSearchable) {
		this.archdescSearchable = archdescSearchable;
	}


	
}
