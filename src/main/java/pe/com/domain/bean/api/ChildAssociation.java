package pe.com.domain.bean.api;

public class ChildAssociation
{
    private String childId;
    private String assocType;

    public ChildAssociation()
    {
        super();
    }

    public ChildAssociation(String childId, String assocType)
    {
        super();
        this.childId = childId;
        this.assocType = assocType;
    }

    public String getChildId()
    {
        return childId;
    }

    public void setChildId(String childId)
    {
        this.childId = childId;
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