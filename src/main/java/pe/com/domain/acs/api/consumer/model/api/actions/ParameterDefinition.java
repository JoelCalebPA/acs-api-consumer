package pe.com.domain.acs.api.consumer.model.api.actions;

public class ParameterDefinition {

    private String name;
    private String type;
    private Boolean multiValued;
    private Boolean mandatory;
    private String displayLabel;

    public ParameterDefinition() {
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

    public Boolean isMultiValued() {
        return multiValued;
    }

    public void setMultiValued(Boolean multiValued) {
        this.multiValued = multiValued;
    }

    public Boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    @Override
    public String toString() {
        return "ParameterDefinition [name=" + name + ", type=" + type + ", multiValued=" + multiValued + ", mandatory="
                + mandatory + ", displayLabel=" + displayLabel + "]";
    }

}
