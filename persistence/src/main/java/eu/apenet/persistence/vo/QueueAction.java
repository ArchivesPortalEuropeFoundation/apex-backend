package eu.apenet.persistence.vo;

public enum QueueAction {
	VALIDATE(true, false, false, false, false), CONVERT(false, true, false, false, false), INDEX(false, false, true,
			false, false), VALIDATE_CONVERT_INDEX(true, true, true, false, false), CONVERT_TO_ESE_EDM(false, false,
			false, true, false), DELIVER_TO_EUROPEANA(false, false, false, false, true);
	private boolean validateAction = false;
	private boolean convertAction = false;
	private boolean indexAction = false;
	private boolean eseEdmAction = false;
	private boolean europeanaAction = false;

	private QueueAction(boolean validateAction, boolean convertAction, boolean indexAction, boolean eseEdmAction,
			boolean europeanaAction) {
		this.validateAction = validateAction;
		this.convertAction = convertAction;
		this.indexAction = indexAction;
		this.eseEdmAction = eseEdmAction;
		this.europeanaAction = europeanaAction;
	}

	public boolean isValidateAction() {
		return validateAction;
	}

	public boolean isConvertAction() {
		return convertAction;
	}

	public boolean isIndexAction() {
		return indexAction;
	}

	public boolean isEseEdmAction() {
		return eseEdmAction;
	}

	public boolean isEuropeanaAction() {
		return europeanaAction;
	}

}
