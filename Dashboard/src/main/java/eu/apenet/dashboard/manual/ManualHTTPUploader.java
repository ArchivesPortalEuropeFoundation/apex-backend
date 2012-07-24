package eu.apenet.dashboard.manual;

//import org.apache.log4j.Logger;

/**
 * 
 * @author eloy
 *
 */

public class ManualHTTPUploader extends ManualUploader {

    //private final Logger log = Logger.getLogger(getClass());
    

	public ManualHTTPUploader(String uploadMethod){
        this.setUploadingMethod(uploadMethod);
    }
	
}
