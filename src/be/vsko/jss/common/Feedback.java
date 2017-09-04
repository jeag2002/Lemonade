package be.vsko.jss.common;

import java.util.ArrayList;
import java.util.List;

public class Feedback {
	private Boolean show;
	private List<FeedbackMessage> messages;
	
	public Feedback() {
		show = false;
		messages = new ArrayList<FeedbackMessage>();
	}
	
	public Feedback(Boolean show, List<FeedbackMessage> messages) {
		this.show = show;
		this.messages = messages;
	}
		
	public Boolean getShow() {
		return show;
	}
	
	public void setShow(Boolean show) {
		this.show = show;
	}
	
	public List<FeedbackMessage> getMessages() {
		return messages;
	}

	public void addMessage(FeedbackMessage message) {
		messages.add(message);
		show = true;
	}

}
