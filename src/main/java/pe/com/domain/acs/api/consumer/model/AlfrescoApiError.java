package pe.com.domain.acs.api.consumer.model;

public class AlfrescoApiError {

    private String errorKey;
    private int statusCode;
    private String briefSummary;

    public AlfrescoApiError() {
        super();
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBriefSummary() {
        return briefSummary;
    }

    public void setBriefSummary(String briefSummary) {
        this.briefSummary = briefSummary;
    }

    @Override
    public String toString() {
        return "AlfrescoApiError [errorKey=" + errorKey + ", statusCode=" + statusCode + ", briefSummary=" + briefSummary + "]";
    }

}
