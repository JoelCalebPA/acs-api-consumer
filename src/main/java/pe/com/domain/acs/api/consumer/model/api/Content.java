package pe.com.domain.acs.api.consumer.model.api;

public class Content {
    private String mimeType;
    private String mimeTypeName;
    private long sizeInBytes;
    private String encoding;

    public Content() {
        super();
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeTypeName() {
        return mimeTypeName;
    }

    public void setMimeTypeName(String mimeTypeName) {
        this.mimeTypeName = mimeTypeName;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return "Content [mimeType=" + mimeType + ", mimeTypeName=" + mimeTypeName + ", sizeInBytes=" + sizeInBytes
                + ", encoding=" + encoding + "]";
    }
}