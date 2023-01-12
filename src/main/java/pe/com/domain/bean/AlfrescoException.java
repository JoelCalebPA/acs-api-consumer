package pe.com.domain.bean;

public class AlfrescoException extends Exception {

	private static final long serialVersionUID = 1L;

	private AlfrescoApiError alfrescoApiError;
	private AlfrescoWebscriptError alfrescoWebscriptError;

	public AlfrescoException(String errorMessage, AlfrescoApiError alfrescoApiError) {
		super(errorMessage);
		this.alfrescoApiError = alfrescoApiError;
	}
	
	public AlfrescoException(String errorMessage, AlfrescoWebscriptError alfrescoWebscriptError) {
		super(errorMessage);
		this.alfrescoWebscriptError = alfrescoWebscriptError;
	}

	public AlfrescoApiError getAlfrescoApiError() {
		return alfrescoApiError;
	}

	public void setAlfrescoApiError(AlfrescoApiError alfrescoApiError) {
		this.alfrescoApiError = alfrescoApiError;
	}

	public AlfrescoWebscriptError getAlfrescoWebscriptError() {
		return alfrescoWebscriptError;
	}

	public void setAlfrescoWebscriptError(AlfrescoWebscriptError alfrescoWebscriptError) {
		this.alfrescoWebscriptError = alfrescoWebscriptError;
	}

}
