package pe.com.domain.bean;

public class AlfrescoSite {

	private String id;
	private String title;
	private String description;
	private String visibility;

	public AlfrescoSite() {
		// blank
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		builder.append("id=" + this.getId());
		builder.append(",title=" + this.getTitle());
		builder.append(",description=" + this.getDescription());
		builder.append(",visibility=" + this.getVisibility());
		builder.append("}");
		return builder.toString();
	}

}
