package eu.apenet.dashboard;

import eu.apenet.dashboard.security.SecurityContext;

/**
 * Created by yoannmoranville on 22/04/15.
 */
public class AbstractCountryAction extends AbstractInstitutionAction {
    public final Integer getCountryId(){
        SecurityContext securityContext = SecurityContext.get();
        if (securityContext == null) {
            return null;
        }else {
            return securityContext.getCountryId();
        }
    }
}
