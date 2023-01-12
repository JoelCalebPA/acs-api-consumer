package pe.com.domain.bean;

import java.util.List;

public class AlfrescoDirectory extends AlfrescoNode {
	private List<AlfrescoNode> children;

	private Integer childrenCount;

	public AlfrescoDirectory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<AlfrescoNode> getChildren() {
		return this.children;
	}

	public void setChildren(List<AlfrescoNode> children) {
		this.children = children;
	}

	public Integer getChildrenCount() {
		return this.childrenCount;
	}

	public void setChildrenCount(Integer childrenCount) {
		this.childrenCount = childrenCount;
	}
}