package eu.apenet.dashboard.manual.eag.utils;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.WebFormEAG2012Action;

public class ParseEag2012Errors{

	private String warning;
	private boolean validationDefault;
	private ActionSupport targetAction;


	public ParseEag2012Errors(String warning, boolean validationDefault,ActionSupport targetAction) {
		this.setWarning(warning);
		this.setValidationDefault(validationDefault);
		this.targetAction = targetAction;
	}

	/**
	 * @return the warning
	 */
	public String getWarning() {
		return this.warning;
	}
	/**
	 * @param warning the warning to set
	 */
	public void setWarning(String warning) {
		this.warning = warning;
	}
	/**
	 * @return the validationDefault
	 */
	public boolean isValidationDefault() {
		return this.validationDefault;
	}
	/**
	 * @param validationDefault the validationDefault to set
	 */
	public void setValidationDefault(boolean validationDefault) {
		this.validationDefault = validationDefault;
	}
	public String errorsValidation(){
		//This method parse the incomprehensible messages of error to comprehensive messages for the user 
        String errorValidation = null; 
        String errorDate;
        
        if(this.getWarning().contains("in the attribute 'standardDate' of the element ")){
        	errorValidation=null;
        }else if(this.getWarning().contains("cvc-datatype-valid.1.2.3:")){
        	errorDate=errorString(this.getWarning());
			errorValidation = targetAction.getText("label.ai.error.standardDate")+" '"+errorDate+"' "+targetAction.getText("label.ai.error.formatStandardDate");
        }else if(this.getWarning().contains("The element 'location' is missing") || this.getWarning().contains("The element 'street' has been found")
        		|| this.getWarning().contains("The element 'municipalityPostalcode' has been found")
        		|| this.getWarning().contains("The element 'localentity' has been found but should not appear here")){
			errorValidation = targetAction.getText("label.ai.error.location");
        }else if(this.getWarning().contains("complex-type.4: Attribute 'eagRelationType' must appear on element 'eagRelation'")){
			errorValidation = targetAction.getText("label.ai.error.eagRelation");
        }else if(this.getWarning().contains("The element 'conventionDeclaration' is missing ")){
			errorValidation = targetAction.getText("label.ai.error.fullName");
        }else if(this.getWarning().contains("The element 'languageDeclaration' is missing ")){
			errorValidation = targetAction.getText("label.ai.error.script");
        }else if(this.getWarning().contains("The element 'script' has been found but should not appear here")){
			errorValidation = targetAction.getText("label.ai.error.language");
        }else if(this.getWarning().contains("The element 'rule' has been found but should not appear here")){
			errorValidation = targetAction.getText("label.ai.error.rule");
        }else if(this.getWarning().contains("The element 'repositorsup' is missing")){
			errorValidation = targetAction.getText("label.ai.error.repositorsup");
        }else if(this.getWarning().contains("The element 'repositorfound' is missing")){
			errorValidation = targetAction.getText("label.ai.error.repositorfound");
        }else if(this.getWarning().contains("The element 'nonpreform' is missing")){
			errorValidation = targetAction.getText("label.ai.error.nonpreform");
		} else if (!this.getWarning().contains("for type 'recordId'")) {
			errorValidation = this.getWarning();
		} else if (this.getWarning().contains("of element 'recordId' is not valid")) {
			if(targetAction instanceof WebFormEAG2012Action){
				errorValidation = targetAction.getText("label.ai.error.defaultIdUsedInAPE") + " ("+ ((WebFormEAG2012Action)targetAction).getIdUsedInAPE() +")";
				if (this.isValidationDefault()) {
					((WebFormEAG2012Action)targetAction).getLoader().setRecordId(((WebFormEAG2012Action)targetAction).getIdUsedInAPE());
					((WebFormEAG2012Action)targetAction).getLoader().setRecordIdISIL(Eag2012.OPTION_NO);
				}
			}
		} else if (this.getWarning().contains("recordId already used")) {
			if(targetAction instanceof WebFormEAG2012Action){
				errorValidation = targetAction.getText("label.ai.error.defaultIdUsedInAPE") + " ("+ ((WebFormEAG2012Action)targetAction).getIdUsedInAPE() +")";
				if (this.isValidationDefault()) {
					((WebFormEAG2012Action)targetAction).getLoader().setRecordId(((WebFormEAG2012Action)targetAction).getIdUsedInAPE());
					((WebFormEAG2012Action)targetAction).getLoader().setRecordIdISIL(Eag2012.OPTION_NO);
				}
			}
		} else {
			errorValidation = this.getWarning();
		}
		return errorValidation;
   }  //end errorsValidaton 

	public String errorString(String warn) { 
		//This method find the wrong string
		int firstPosition = warn.indexOf("'");
		String subStringWarn = warn.substring(firstPosition+1);
		int secondPosition = subStringWarn.indexOf("'");
		subStringWarn = subStringWarn.substring(0,secondPosition);
		return subStringWarn;
	}
}

