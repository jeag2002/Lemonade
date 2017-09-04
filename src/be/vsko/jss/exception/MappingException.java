package be.vsko.jss.exception;

public class MappingException extends JSSException {
	private static final long serialVersionUID = 1L;
		
	public MappingException() {
		super();
	}
	
	
	public MappingException(String message) {		
		super(message);
	}
	
	public MappingException(Throwable e, String message) {		
		super(e, message);
	}
	
	public MappingException(Throwable e) {
		super(e);
	}
	
}
