package pe.com.domain.acs.api.consumer.model;

public class AlfrescoException extends Exception {

	private static final long serialVersionUID = 1L;

	private AlfrescoApiError alfrescoApiError;

	public AlfrescoException(String errorMessage, AlfrescoApiError alfrescoApiError) {
		super(errorMessage + " - " + alfrescoApiError.getBriefSummary());
		this.alfrescoApiError = alfrescoApiError;
	}

	public AlfrescoApiError getAlfrescoApiError() {
		return alfrescoApiError;
	}

	public void setAlfrescoApiError(AlfrescoApiError alfrescoApiError) {
		this.alfrescoApiError = alfrescoApiError;
	}

}
