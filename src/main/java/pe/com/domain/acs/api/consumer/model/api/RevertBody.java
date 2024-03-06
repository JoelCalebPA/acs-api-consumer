package pe.com.domain.acs.api.consumer.model.api;

public class RevertBody {

    private boolean majorVersion;
    private String comment;

    public RevertBody() {
    }

    public RevertBody(boolean majorVersion, String comment) {
        this.majorVersion = majorVersion;
        this.comment = comment;
    }

    public boolean isMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(boolean majorVersion) {
        this.majorVersion = majorVersion;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
