package pe.com.domain.bean.api;

public class Target
{
    private String targetId;
    private String assocType;

    public Target()
    {
        super();
    }

    public String getTargetId()
    {
        return targetId;
    }

    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }

    public String getAssocType()
    {
        return assocType;
    }

    public void setAssocType(String assocType)
    {
        this.assocType = assocType;
    }
}
