package eu.apenet.dashboard.manual.eag.utils.parseJsonObj;

import org.json.JSONException;
import org.json.JSONObject;

import eu.apenet.dashboard.manual.eag.Eag2012;

/**
 *Interface of JsonObjToEag2012
 */
public interface JsonObjToEag2012 {
	
	/**
	 * Method for Fill a {@link Eag2012} eag2012 object got from params, fill it with the information provided into
	 * {@link JSONObject} JSONObject (got from params).
	 * @param eag2012 {@link EAG2012} the eag2012
	 * @param jsonObj {@link JSONObject} the jsonObj
	 * @return {@link EAG2012} the eag2012
	 * @throws JSONException
	 */
	public Eag2012 JsonObjToEag2012(Eag2012 eag2012,JSONObject jsonObj) throws JSONException;
}
