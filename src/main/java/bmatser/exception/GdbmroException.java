package bmatser.exception;

public class GdbmroException extends RuntimeException {

private static final long serialVersionUID = 8564321330691400404L;
	
	private int code;
	
	public GdbmroException(String message){
		super(message);
	}

	public GdbmroException(int code,String message) {
		super(message);
		this.code = code;
	}
	
	public static void check(boolean expression, String message, int code){
		if (!expression){
			throw new GdbmroException(code,message);
		}
	}

	public int getCode() {
		return code;
	}

}
