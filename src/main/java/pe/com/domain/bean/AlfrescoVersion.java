package pe.com.domain.bean;

public class AlfrescoVersion {
	private String createdDate;

	private String creator;

	private String label;

	private String type;

	private String description;

	private String nodeRef;
	
	private String id;

	public AlfrescoVersion() {
		super();
	}

	public AlfrescoVersion(String createdDate, String creator, String label, String type, String description,
			String nodeRef) {
		this.createdDate = createdDate;
		this.creator = creator;
		this.label = label;
		this.type = type;
		this.description = description;
		this.nodeRef = nodeRef;
	}

	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNodeRef() {
		return this.nodeRef;
	}

	public void setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
	}

	public String getId() {
		return this.nodeRef.substring(nodeRef.lastIndexOf("/")+1);
	}

	public void setId(String id) {
		this.id = id;
	}
	
}