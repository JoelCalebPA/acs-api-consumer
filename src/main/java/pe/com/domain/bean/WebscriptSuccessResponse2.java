package pe.com.domain.bean;

public class WebscriptSuccessResponse2 {

    private AlfrescoStatus status;
    private String code;
    private String uuid;
    private String fileName;
    private String message;

    public WebscriptSuccessResponse2() {
        super();
    }

    public AlfrescoStatus getStatus() {
        return status;
    }

    public void setStatus(AlfrescoStatus status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
