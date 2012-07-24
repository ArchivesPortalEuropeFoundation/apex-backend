package eu.apenet.commons.view.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import net.tanesha.recaptcha.ReCaptchaImpl;
import eu.apenet.commons.utils.APEnetUtilities;
/**
 * 
 * @author Bastiaan Verhoef
 */
public class RecapthaTag extends SimpleTagSupport {



	@Override
	public void doTag() throws JspException, IOException {
		String publicKey =APEnetUtilities.getDashboardConfig().getPublicReCaptchaKey();
		String privateKey =APEnetUtilities.getDashboardConfig().getPrivateReCaptchaKey();
		ReCaptchaImpl recaptcha = new ReCaptchaImpl();
		recaptcha.setIncludeNoscript(false); 
		recaptcha.setPrivateKey(privateKey);
		recaptcha.setPublicKey(publicKey);
		recaptcha.setRecaptchaServer("https://www.google.com/recaptcha/api");
		getJspContext().getOut().print(recaptcha.createRecaptchaHtml(null, null));
	}


}
