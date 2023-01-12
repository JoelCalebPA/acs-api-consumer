package pe.com.domain.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pe.com.domain.bean.AlfrescoApiError;
import pe.com.domain.bean.AlfrescoException;
import pe.com.domain.bean.AlfrescoSite;
import pe.com.domain.bean.AlfrescoSiteContainer;
import pe.com.domain.bean.AlfrescoWebscriptError;
import pe.com.domain.bean.api.ChildAssociation;
import pe.com.domain.bean.api.NodeCreate;
import pe.com.domain.bean.api.NodeEntry;
import pe.com.domain.bean.api.NodeMoveRequest;
import pe.com.domain.util.Util;

public class AlfrescoService
{
    private static final String JSON_MIME = "application/json";
    private static final String UTF8 = "UTF-8";

    private String HOST;
    private String USER;
    private String PASSWORD;
    static HttpMethodRetryHandler myretryhandler;

    /**
     * Metodo que setear los parametros de conexion a Alfresco
     * 
     * @param credenciales Hashmap con los datos de conexion.
     */
    public void setConexion(Map<String, String> credenciales)
    {
        HOST = credenciales.get("HOST");
        USER = credenciales.get("USER");
        PASSWORD = credenciales.get("PASSWORD");
    }

    public AlfrescoSiteContainer findFolderInSite(String siteContainerId, String path) throws AlfrescoException
    {
        Part[] parts = new Part[2];
        parts[0] = new StringPart("uuid", siteContainerId, UTF8);
        parts[1] = new StringPart("path", path, UTF8);
        String siteCreated = callPostWebscript(parts, "/service/findFolderInSite");
        JsonElement jsonElement = JsonParser.parseString(siteCreated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, AlfrescoSiteContainer.class);
    }
    
    public List<AlfrescoSite> getSites() throws AlfrescoException
    {
        List<AlfrescoSite> sites = new ArrayList<>();
        String sitesRaw = callGetApi("/api/-default-/public/alfresco/versions/1/sites?skipCount=0&maxItems=1000");
        JsonElement jsonElement = JsonParser.parseString(sitesRaw).getAsJsonObject();
        JsonObject list = jsonElement.getAsJsonObject().getAsJsonObject("list");
        JsonArray entries = list.getAsJsonArray("entries");
        for (JsonElement entry : entries)
        {
            JsonObject siteObject = entry.getAsJsonObject().getAsJsonObject("entry");
            sites.add(new Gson().fromJson(siteObject, AlfrescoSite.class));
        }
        return sites;
    }

    public int deleteSite(AlfrescoSite site) throws AlfrescoException
    {
        return callDeleteApi("/api/-default-/public/alfresco/versions/1/sites/" + site.getId() + "?permanent=true");
    }

    public AlfrescoSite createSite(AlfrescoSite site) throws AlfrescoException
    {
        String siteCreated = callPostApi("/api/-default-/public/alfresco/versions/1/sites", new Gson().toJson(site), null);
        JsonElement jsonElement = JsonParser.parseString(siteCreated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, AlfrescoSite.class);
    }

    public AlfrescoSiteContainer getSiteContainer(String siteId) throws AlfrescoException
    {
        String siteCreated = callGetApi("/api/-default-/public/alfresco/versions/1/sites/" + siteId + "/containers/documentLibrary");
        JsonElement jsonElement = JsonParser.parseString(siteCreated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, AlfrescoSiteContainer.class);
    }

    public NodeEntry createNode(String containerId, NodeCreate nodeCreate, File file) throws AlfrescoException, IOException
    {
        Part[] parts;
        int i = 3;
        if (file != null)
        {
            i++;
        }
        if (!nodeCreate.getProperties().isEmpty())
        {
            i += nodeCreate.getProperties().size();
        }
        parts = new Part[i];
        parts[0] = new StringPart("name", nodeCreate.getName(), UTF8);
        parts[1] = new StringPart("nodeType", nodeCreate.getNodeType(), UTF8);
        parts[2] = new StringPart("relativePath", nodeCreate.getRelativePath(), UTF8);
        int j = 3;
        if (file != null)
        {
            parts[j] = new FilePart("filedata", new FilePartSource(nodeCreate.getName(), file), Files.probeContentType(file.toPath()), UTF8);
            j++;
        }
        if (!nodeCreate.getProperties().isEmpty())
        {
            for (Map.Entry<String, String> entry : nodeCreate.getProperties().entrySet())
            {
                parts[j] = new StringPart(entry.getKey(), entry.getValue(), UTF8);
                j++;
            }
        }
        String nodeCreated;
        if (file != null)
        {
            nodeCreated = callPostApi("/api/-default-/public/alfresco/versions/1/nodes/" + containerId + "/children", null, parts);
        } 
        else
        {
            nodeCreated = callPostApi("/api/-default-/public/alfresco/versions/1/nodes/" + containerId + "/children", new Gson().toJson(nodeCreate), null);
        }
        JsonElement jsonElement = JsonParser.parseString(nodeCreated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }

    public NodeEntry updateNode(String nodeId, NodeCreate nodeCreate) throws AlfrescoException
    {
        String nodeUpdated = callPutApi("/api/-default-/public/alfresco/versions/1/nodes/" + nodeId, new Gson().toJson(nodeCreate), null);
        JsonElement jsonElement = JsonParser.parseString(nodeUpdated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }

    public NodeEntry getNode(String nodeId, String relativePath) throws AlfrescoException, UnsupportedEncodingException
    {
        String node = "";
        if (relativePath.isEmpty())
            node = callGetApi("/api/-default-/public/alfresco/versions/1/nodes/" + nodeId);
        else
            node = callGetApi("/api/-default-/public/alfresco/versions/1/nodes/" + nodeId + "?relativePath=" + encodeValue(relativePath));
        JsonElement jsonElement = JsonParser.parseString(node).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }

    public NodeEntry moveNode(String nodeId, String parentId) throws AlfrescoException
    {
        NodeMoveRequest toMove = new NodeMoveRequest(parentId);
        String nodeMoved = callPostApi("/api/-default-/public/alfresco/versions/1/nodes/" + nodeId + "/move", new Gson().toJson(toMove), null);
        JsonElement jsonElement = JsonParser.parseString(nodeMoved).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }
    
    public ChildAssociation createChildAssociation(String nodeId, String childId, String assocType) throws AlfrescoException
    {
        ChildAssociation assoc = new ChildAssociation(childId, assocType);
        String nodeMoved = callPostApi("/api/-default-/public/alfresco/versions/1/nodes/" + nodeId + "/secondary-children", new Gson().toJson(assoc), null);
        JsonElement jsonElement = JsonParser.parseString(nodeMoved).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, ChildAssociation.class);
    }
    
    /** GOVERNACE SERVICES **/
    
    public NodeEntry createCategoryForFilePlan(String rmSite, NodeCreate category) throws AlfrescoException
    {
        String recordCategory = callPostApi("/api/-default-/public/gs/versions/1/file-plans/" + rmSite + "/categories", new Gson().toJson(category), null);
        JsonElement jsonElement = JsonParser.parseString(recordCategory).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }
    
    public NodeEntry createFilePlanComponent(String recordCategoryId, NodeCreate component) throws AlfrescoException
    {
        String filePlancomponent = callPostApi("/api/-default-/public/gs/versions/1/record-categories/" + recordCategoryId + "/children", new Gson().toJson(component), null);
        JsonElement jsonElement = JsonParser.parseString(filePlancomponent).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }
    
    public NodeEntry declareFileAsRecord(String fileId, String parentId) throws AlfrescoException
    {
        String fileAsRecord = callPostApi("/api/-default-/public/gs/versions/1/files/" + fileId + "/declare?parentId=" + parentId, null, null);
        JsonElement jsonElement = JsonParser.parseString(fileAsRecord).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }
    
    public NodeEntry completeRecord(String recordId) throws AlfrescoException
    {
        String record = callPostApi("/api/-default-/public/gs/versions/1/records/" + recordId + "/complete", null, null);
        JsonElement jsonElement = JsonParser.parseString(record).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }

    /** API CALLS **/
    
    public String callPostApi(String serviceURI, String bodyJson, Part[] parts) throws AlfrescoException
    {
        HttpClient client = setHttp();
        PostMethod mPost = null;
        String response = "";
        try
        {
            String auth = USER + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);

            mPost = new PostMethod(HOST + serviceURI);
            mPost.setRequestHeader("Authorization", authHeader);
            mPost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
            if (!Util.isBlank(bodyJson))
            {
                StringRequestEntity requestEntity = new StringRequestEntity(bodyJson, JSON_MIME, UTF8);
                mPost.setRequestEntity(requestEntity);
            }

            if (parts != null)
            {
                MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, mPost.getParams());
                mPost.setRequestEntity(requestEntity);
            }

            int status = client.executeMethod(mPost);
            response = IOUtils.toString(mPost.getResponseBodyAsStream(), StandardCharsets.UTF_8);
            if (status != HttpStatus.SC_OK && status != HttpStatus.SC_CREATED)
            {
                JsonElement jsonElement = JsonParser.parseString(response).getAsJsonObject();
                JsonObject error = jsonElement.getAsJsonObject().getAsJsonObject("error");
                AlfrescoApiError alfrescoError = new Gson().fromJson(error, AlfrescoApiError.class);
                throw new AlfrescoException("Ocurrio un error al llamar al servicio: " + serviceURI, alfrescoError);
            }
        } catch (HttpException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }

    public String callPutApi(String serviceURI, String bodyJson, Part[] parts) throws AlfrescoException
    {
        HttpClient client = setHttp();
        PutMethod mPut = null;
        String response = "";
        try
        {
            String auth = USER + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);

            mPut = new PutMethod(HOST + serviceURI);
            mPut.setRequestHeader("Authorization", authHeader);
            mPut.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
            if (!Util.isBlank(bodyJson))
            {
                StringRequestEntity requestEntity = new StringRequestEntity(bodyJson, JSON_MIME, UTF8);
                mPut.setRequestEntity(requestEntity);
            }

            if (parts != null)
            {
                MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, mPut.getParams());
                mPut.setRequestEntity(requestEntity);
            }

            int status = client.executeMethod(mPut);
            response = IOUtils.toString(mPut.getResponseBodyAsStream(), StandardCharsets.UTF_8);
            if (status != HttpStatus.SC_OK && status != HttpStatus.SC_CREATED)
            {
                JsonElement jsonElement = JsonParser.parseString(response).getAsJsonObject();
                JsonObject error = jsonElement.getAsJsonObject().getAsJsonObject("error");
                AlfrescoApiError alfrescoError = new Gson().fromJson(error, AlfrescoApiError.class);
                throw new AlfrescoException("Ocurrio un error al llamar al servicio: " + serviceURI, alfrescoError);
            }
        } catch (HttpException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }

    public String callGetApi(String serviceURI) throws AlfrescoException
    {
        HttpClient client = setHttp();
        GetMethod mGet = null;
        String response = "";
        try
        {
            String auth = USER + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);

            mGet = new GetMethod(HOST + serviceURI);
            mGet.setRequestHeader("Authorization", authHeader);
            mGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
            int status = client.executeMethod(mGet);

            response = IOUtils.toString(mGet.getResponseBodyAsStream(), StandardCharsets.UTF_8);
            if (status != HttpStatus.SC_OK)
            {
                JsonElement jsonElement = JsonParser.parseString(response).getAsJsonObject();
                JsonObject error = jsonElement.getAsJsonObject().getAsJsonObject("error");
                AlfrescoApiError alfrescoError = new Gson().fromJson(error, AlfrescoApiError.class);
                throw new AlfrescoException("Ocurrio un error al llamar al servicio: " + serviceURI, alfrescoError);
            }

        } catch (HttpException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }

    public int callDeleteApi(String serviceURI) throws AlfrescoException
    {
        HttpClient client = setHttp();
        DeleteMethod mDelete = null;
        String response = "";
        int status = 0;
        try
        {
            String auth = USER + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);

            mDelete = new DeleteMethod(HOST + serviceURI);
            mDelete.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
            mDelete.setRequestHeader("Authorization", authHeader);
            status = client.executeMethod(mDelete);
            if (status != HttpStatus.SC_NO_CONTENT)
            {
                response = IOUtils.toString(mDelete.getResponseBodyAsStream(), StandardCharsets.UTF_8);
                JsonElement jsonElement = JsonParser.parseString(response).getAsJsonObject();
                JsonObject error = jsonElement.getAsJsonObject().getAsJsonObject("error");
                AlfrescoApiError alfrescoError = new Gson().fromJson(error, AlfrescoApiError.class);
                throw new AlfrescoException("Ocurrio un error al llamar al servicio: " + serviceURI, alfrescoError);
            }
        } catch (HttpException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return status;
    }

    public String callPostWebscript(Part[] parts, String webscriptURI) throws AlfrescoException
    {
        HttpClient client = setHttp();
        PostMethod mPost = null;
        String response = "";
        int status = 0;
        try
        {
            String auth = USER + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);

            mPost = new PostMethod(HOST + webscriptURI);
            MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, mPost.getParams());
            mPost.setRequestEntity(requestEntity);
            mPost.setRequestHeader("Authorization", authHeader);
            mPost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);

            status = client.executeMethod(mPost);
            response = IOUtils.toString(mPost.getResponseBodyAsStream(), StandardCharsets.UTF_8);
            // System.out.println(response);
            if (status != HttpStatus.SC_OK)
            {
                AlfrescoWebscriptError alfrescoError = new Gson().fromJson(response, AlfrescoWebscriptError.class);
                throw new AlfrescoException("Ocurrio un error al llamar al servicio: " + webscriptURI, alfrescoError);
            }
        } catch (HttpException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return response;
    }

    byte[] getContent(String uuid)
    {
        HttpClient client = setHttp();
        byte[] bFile = null;

        try
        {
            String auth = USER + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);
            String apiurl = HOST + "/service/api/node/content;cm:content/workspace/SpacesStore/" + uuid;
            GetMethod mGet = new GetMethod(apiurl);
            mGet.setRequestHeader("Authorization", authHeader);
            mGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
            client.executeMethod(mGet);
            bFile = IOUtils.toByteArray(mGet.getResponseBodyAsStream());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return bFile;
    }

    public InputStream getContentV2(String uuid)
    {
        HttpClient client = setHttp();
        InputStream is = null;

        try
        {
            String auth = USER + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);
            String apiurl = HOST + "/service/api/node/content;cm:content/workspace/SpacesStore/" + uuid;
            GetMethod mGet = new GetMethod(apiurl);
            mGet.setRequestHeader("Authorization", authHeader);
            mGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
            client.executeMethod(mGet);
            is = mGet.getResponseBodyAsStream();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return is;
    }

    public byte[] getVersionContent(String uuid)
    {
        HttpClient client = setHttp();
        byte[] bFile = null;

        try
        {
            String auth = USER + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);
            String apiurl = HOST + "/service/api/node/content;cm:content/versionStore/version2Store/" + uuid;
            GetMethod mGet = new GetMethod(apiurl);
            mGet.setRequestHeader("Authorization", authHeader);
            mGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
            client.executeMethod(mGet);
            bFile = IOUtils.toByteArray(mGet.getResponseBodyAsStream());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return bFile;
    }

    public InputStream getVersionContentV2(String uuid)
    {
        HttpClient client = setHttp();
        InputStream is = null;
        try
        {
            String auth = USER + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);
            String apiurl = HOST + "/service/api/node/content;cm:content/versionStore/version2Store/" + uuid;
            GetMethod mGet = new GetMethod(apiurl);
            mGet.setRequestHeader("Authorization", authHeader);
            mGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
            client.executeMethod(mGet);
            is = mGet.getResponseBodyAsStream();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * Metodo que permite configurar el componente Http
     * 
     */
    static HttpClient setHttp()
    {
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        myretryhandler = new HttpMethodRetryHandler()
        {
            public boolean retryMethod(final HttpMethod method, final IOException exception, int executionCount)
            {
                if (executionCount >= 1)
                {
                    return false;
                }
                if (exception instanceof NoHttpResponseException)
                {
                    return true;
                }
                if (!method.isRequestSent())
                {
                    return true;
                }
                return false;
            }
        };
        return client;
    }
    
    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

}
