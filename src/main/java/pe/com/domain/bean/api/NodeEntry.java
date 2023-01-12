package pe.com.domain.bean.api;

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

    @Override
    public String toString() {
        return "NodeEntry [isFile=" + isFile + ", createdByUser=" + createdByUser + ", modifiedAt=" + modifiedAt
                + ", nodeType=" + nodeType + ", content=" + content + ", parentId=" + parentId + ", aspectNames="
                + aspectNames + ", createdAt=" + createdAt + ", isFolder=" + isFolder + ", modifiedByUser="
                + modifiedByUser + ", name=" + name + ", id=" + id + ", properties=" + properties + "]";
    }

}

class Content {
    private String mimeType;
    private String mimeTypeName;
    private int sizeInBytes;
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

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(int sizeInBytes) {
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

class CreatedByUser {
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

class ModifiedByUser {
    private String id;
    private String displayName;

    public ModifiedByUser() {
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
        return "ModifiedByUser [id=" + id + ", displayName=" + displayName + "]";
    }
}