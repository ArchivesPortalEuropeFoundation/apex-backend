package eu.apenet.persistence.vo;


public enum ValidatedState {
	FATAL_ERROR("fatal_error"), NOT_VALIDATED("not_validated"), VALIDATED("validated") ;

	private String name;
	private ValidatedState(String name){
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}
    public static ValidatedState getValidatedState(String name){
        for(ValidatedState validatedState : ValidatedState.values()){
            if(validatedState.name.equals(name))
                return validatedState;
        }
        return null;
    }
	
}
