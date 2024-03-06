package pe.com.domain.acs.api.consumer.model.api;

public class CreatedByUser {

    private String id;
    private String displayName;

    public CreatedByUser() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "CreatedByUser [id=" + id + ", displayName=" + displayName + "]";
    }
    
}