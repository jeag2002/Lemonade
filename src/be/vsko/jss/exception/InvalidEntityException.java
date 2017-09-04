package be.vsko.jss.exception;

import be.vsko.jss.common.AbstractEntity;
import be.vsko.jss.common.ValidationResult;

public class InvalidEntityException extends JSSException {

	private static final long serialVersionUID = 1L;

	private ValidationResult validationResult;
	
	public InvalidEntityException(AbstractEntity entity, ValidationResult validationResult) {				
		super("Invalid entity " + entity);
		
		this.validationResult = validationResult;
	}
	
	public ValidationResult getValidationResult() {
		return validationResult;
	}
	
}
