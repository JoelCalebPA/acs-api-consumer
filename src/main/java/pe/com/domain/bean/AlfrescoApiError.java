package pe.com.domain.bean;

public class AlfrescoApiError
{

    private String errorKey;
    private int statusCode;

    public AlfrescoApiError()
    {
        super();
    }

    public String getErrorKey()
    {
        return errorKey;
    }

    public void setErrorKey(String errorKey)
    {
        this.errorKey = errorKey;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    @Override
    public String toString()
    {
        return "AlfrescoApiError [errorKey=" + errorKey + ", statusCode=" + statusCode + "]";
    }

}
