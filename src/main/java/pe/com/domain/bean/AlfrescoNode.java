package pe.com.domain.bean;

public class AlfrescoNode {

	private String qnamePath;

	private String id;

	private String nodeRef;

	private String name;

	private String type;

	private String displayPath;

	public AlfrescoNode() {
		super();
	}

	public String getQnamePath() {
		return qnamePath;
	}

	public void setQnamePath(String qnamePath) {
		this.qnamePath = qnamePath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodeRef() {
		return nodeRef;
	}

	public void setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDisplayPath() {
		return displayPath;
	}

	public void setDisplayPath(String displayPath) {
		this.displayPath = displayPath;
	}

}