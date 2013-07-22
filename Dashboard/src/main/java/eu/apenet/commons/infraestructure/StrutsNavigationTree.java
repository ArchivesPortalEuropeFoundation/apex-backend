package eu.apenet.commons.infraestructure;

import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.exceptions.APEnetException;

public class StrutsNavigationTree extends NavigationTree {

	public StrutsNavigationTree() throws APEnetException {
		super(new StrutsResourceBundleSource());

	}

}
