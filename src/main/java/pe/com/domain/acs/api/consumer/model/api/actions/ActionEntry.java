package pe.com.domain.acs.api.consumer.model.api.actions;

import java.util.List;

/**
 * Clase que representa una acción de Alfresco
 * @author Joel Paytan
 */
public class ActionEntry {

    private String id;
    private String name;
    private String title;
    private String description;
    private List<String> applicableTypes;
    private Boolean trackStatus;
    private List<ParameterDefinition> parameterDefinitions;

    public ActionEntry() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getApplicableTypes() {
        return applicableTypes;
    }

    public void setApplicableTypes(List<String> applicableTypes) {
        this.applicableTypes = applicableTypes;
    }

    public Boolean getTrackStatus() {
        return trackStatus;
    }

    public void setTrackStatus(Boolean trackStatus) {
        this.trackStatus = trackStatus;
    }

    public List<ParameterDefinition> getParameterDefinitions() {
        return parameterDefinitions;
    }

    public void setParameterDefinitions(List<ParameterDefinition> parameterDefinitions) {
        this.parameterDefinitions = parameterDefinitions;
    }

    @Override
    public String toString() {
        return "ActionEntry [id=" + id + ", name=" + name + ", title=" + title + ", description=" + description
                + ", applicableTypes=" + applicableTypes + ", trackStatus=" + trackStatus + ", parameterDefinitions="
                + parameterDefinitions + "]";
    }

}
