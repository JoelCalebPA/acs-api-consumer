package pe.com.domain.acs.api.consumer.model.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class NodeVersionEntry {

    private String id;
    private String versionComment;
    private String name;
    private String nodeType;
    private boolean isFolder;
    private boolean isFile;
    private Date modifiedAt;
    private ModifiedByUser modifiedByUser;
    private Content content;
    private ArrayList<String> aspectNames;
    private Map<String, Object> properties;

    public NodeVersionEntry() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersionComment() {
        return versionComment;
    }

    public void setVersionComment(String versionComment) {
        this.versionComment = versionComment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public ModifiedByUser getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(ModifiedByUser modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public ArrayList<String> getAspectNames() {
        return aspectNames;
    }

    public void setAspectNames(ArrayList<String> aspectNames) {
        this.aspectNames = aspectNames;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "NodeEntry [id=" + id + ", name=" + name + ", versionComment=" + versionComment + "]";
    }

}