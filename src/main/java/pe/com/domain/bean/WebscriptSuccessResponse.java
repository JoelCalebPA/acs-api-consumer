package pe.com.domain.bean;

public class WebscriptSuccessResponse {

	private AlfrescoStatus status;
	private String message;

	public WebscriptSuccessResponse() {
		super();
	}

	public AlfrescoStatus getStatus() {
		return status;
	}

	public void setStatus(AlfrescoStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
