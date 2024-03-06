package pe.com.domain.acs.api.consumer.model.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class NodeEntry {

    private boolean isFile;
    private CreatedByUser createdByUser;
    private Date modifiedAt;
    private String nodeType;
    private Content content;
    private String parentId;
    private ArrayList<String> aspectNames;
    private Date createdAt;
    private boolean isFolder;
    private ModifiedByUser modifiedByUser;
    private String name;
    private String id;
    private Map<String, Object> properties;
    private Path path;

    public NodeEntry() {
        super();
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }

    public CreatedByUser getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(CreatedByUser createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public ArrayList<String> getAspectNames() {
        return aspectNames;
    }

    public void setAspectNames(ArrayList<String> aspectNames) {
        this.aspectNames = aspectNames;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    public ModifiedByUser getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(ModifiedByUser modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public long getSizeInBytes() {
        return this.content.getSizeInBytes();
    }

    public String getPath() {
        return path.getName();
    }

    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "NodeEntry [isFile=" + isFile + ", createdByUser=" + createdByUser + ", modifiedAt=" + modifiedAt
                + ", nodeType=" + nodeType + ", content=" + content + ", parentId=" + parentId + ", aspectNames="
                + aspectNames + ", createdAt=" + createdAt + ", isFolder=" + isFolder + ", modifiedByUser="
                + modifiedByUser + ", name=" + name + ", id=" + id + ", properties=" + properties + "]";
    }

}