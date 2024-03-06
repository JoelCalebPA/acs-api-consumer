package pe.com.domain.acs.api.consumer.model.api.actions;

import java.io.Serializable;
import java.util.Map;

public class ActionBodyExec {

    private String actionDefinitionId;
    private String targetId;
    private Map<String, Serializable> params;

    public ActionBodyExec() {
    }

    public String getActionDefinitionId() {
        return actionDefinitionId;
    }

    public void setActionDefinitionId(String actionDefinitionId) {
        this.actionDefinitionId = actionDefinitionId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Map<String, Serializable> getParams() {
        return params;
    }

    public void setParams(Map<String, Serializable> params) {
        this.params = params;
    }

}
