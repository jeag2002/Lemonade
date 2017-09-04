package be.vsko.jss.common;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
	private Boolean valid;
	private List<FeedbackMessage> messages;
	
	public ValidationResult() {
		valid = true;
		messages = new ArrayList<FeedbackMessage>();
	}
	
	public Boolean isValid() {
		return valid;
	}
	
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	
	public List<FeedbackMessage> getMessages() {
		return messages;
	}

	public void addMessage(String message, String field) {
		FeedbackMessage fm = new FeedbackMessage(message, field);
		messages.add(fm);
		valid = false;
	}
	
	public void addMessage(String message) {
		addMessage(message, "");
	}
}
