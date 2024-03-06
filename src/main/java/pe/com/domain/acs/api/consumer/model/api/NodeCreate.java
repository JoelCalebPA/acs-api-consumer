package pe.com.domain.acs.api.consumer.model.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NodeCreate {

    private String name;
    private String nodeType;
    private ArrayList<String> aspectNames = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();
    private String relativePath;

    public NodeCreate() {
        super();
    }

    public NodeCreate(String name, String nodeType, String relativePath) {
        super();
        this.name = name;
        this.nodeType = nodeType;
        this.relativePath = relativePath;
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

    public ArrayList<String> getAspectNames() {
        return aspectNames;
    }

    public void setAspectNames(ArrayList<String> aspectNames) {
        this.aspectNames = aspectNames;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void addProperty(String key, String value) {
        this.properties.put(key, value);
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public void addAspect(String aspect) {
        this.aspectNames.add(aspect);
    }

}