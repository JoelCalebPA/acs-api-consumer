package pe.com.domain.bean;

public class AlfrescoFile extends AlfrescoNode {

	private byte[] content;

	private String mimetype;

	private String size;

	private Boolean isLocked;

	private String downloadUrl;

	private String workingCopyOwner;

	public AlfrescoFile() {
		super();
	}

	public String getDownloadUrl() {
		return this.downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getMimetype() {
		return this.mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Boolean getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public String getWorkingCopyOwner() {
		return this.workingCopyOwner;
	}

	public void setWorkingCopyOwner(String workingCopyOwner) {
		this.workingCopyOwner = workingCopyOwner;
	}
}
