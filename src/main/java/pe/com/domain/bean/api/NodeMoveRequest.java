package pe.com.domain.bean.api;

public class NodeMoveRequest
{
    private String targetParentId;
    private String name;

    public NodeMoveRequest()
    {
        super();
    }

    public NodeMoveRequest(String targetParentId)
    {
        super();
        this.targetParentId = targetParentId;
    }

    public String getTargetParentId()
    {
        return targetParentId;
    }

    public void setTargetParentId(String targetParentId)
    {
        this.targetParentId = targetParentId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
