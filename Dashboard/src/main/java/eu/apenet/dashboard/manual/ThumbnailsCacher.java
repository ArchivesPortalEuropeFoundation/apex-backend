package eu.apenet.dashboard.manual;

/**This class is in charge of caching and creating all the 
 * thumbnails embedded within a Finding Aid. Some of its 
 * methods should execute in background.*/
public class ThumbnailsCacher {
	
	public ThumbnailsCacher() {
		
	}

	public Integer checkEADState(/*APEnetEAD ead*/) {
		return null;		
	}

	/** This method obtains all the images related to the 
	 * file and stores them in the Data Base 
	 * (Thumbnails table) */
	protected static void getImagesFromFA () {
		
	}

	/** For every new entry inserted into Thumbnails table,
	 * this method will go to the original 
	 * source to obtain it. */
	protected static void getImageFromOriginalSource () {
		
	}

	protected void rescaleImage (String path) {
		
	}

	protected void insertThumbnailToThumbnails () {
		
	}

	public Integer getNumberImagesCached () {
		return null;		
	}
}
