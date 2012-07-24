package eu.apenet.dashboard.manual;

import com.opensymphony.xwork2.ActionSupport;

/**
 * User: Eloy García
 * Date: Nov 3d, 2010
 */

/**
 * This class is in charge of redirecting the user to 
 * Dashboard once he has uploaded correctly an EAG file 
 */

public class DashboardRedirectionAction extends ActionSupport {

	private Integer ai_id;
	
	
    public Integer getAi_id() {
		return ai_id;
	}


	public void setAi_id(Integer ai_id) {
		this.ai_id = ai_id;
	}

	@Override
    public String execute() throws Exception {		
			return SUCCESS;			
    }

}