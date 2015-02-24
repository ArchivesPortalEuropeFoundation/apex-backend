package eu.apenet.dashboard.manual.eag.utils.loaderEAG2012;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.utils.EAG2012Loader;
import eu.apenet.dpt.utils.eag2012.Eag;

/**
 *Interface of LoaderEAG2012
 */
public interface LoaderEAG2012 {
	/**
	 * Method for loader {@link Eag2012} eag2012
	 * @param eag {@link Eag} the eag
	 * @param eag2012Loader {@link EAG2012Loader} the eag2012Loader
	 * @return eag {@link Eag} the eag
	 */
	public Eag LoaderEAG2012(Eag eag, EAG2012Loader eag2012Loader);
}
