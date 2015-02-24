package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.Eag;

/**
 * Interface of ObjectJAXB
 */
public interface ObjectJAXB {
	/**
	 * Method for fill elements {@link ObjectJAXB} objectJAXB
	 * @param eag2012 {@link Eag2012} the eag2012
	 * @param eag {@link Eag} the eag
	 * @return {@link Eag} eag
	 */
	public Eag ObjectJAXB(Eag2012 eag2012,Eag eag);
}
