package pe.com.domain.acs.api.consumer.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pe.com.domain.acs.api.consumer.model.AlfrescoException;
import pe.com.domain.acs.api.consumer.model.AlfrescoSite;
import pe.com.domain.acs.api.consumer.model.AlfrescoSiteContainer;
import pe.com.domain.acs.api.consumer.model.api.ChildAssociation;
import pe.com.domain.acs.api.consumer.model.api.Endpoint;
import pe.com.domain.acs.api.consumer.model.api.NodeCreate;
import pe.com.domain.acs.api.consumer.model.api.NodeEntry;
import pe.com.domain.acs.api.consumer.model.api.NodeMoveRequest;
import pe.com.domain.acs.api.consumer.util.Util;

public class AlfrescoService {

    private static final String UTF8 = "UTF-8";

    private AlfrescoClient alfrescoClient;

    static HttpMethodRetryHandler myretryhandler;
    
    public AlfrescoService(String host, String username, String password) {
        this.alfrescoClient = new AlfrescoClient(host, username, password);
    }

    /*
     * Sites APIs
     */
    public List<AlfrescoSite> getSites() throws AlfrescoException {
        List<AlfrescoSite> sites = new ArrayList<>();
        String sitesRaw = this.alfrescoClient.callGetApi(Endpoint.API_SITES + "?skipCount=0&maxItems=1000");
        JsonElement jsonElement = JsonParser.parseString(sitesRaw).getAsJsonObject();
        JsonObject list = jsonElement.getAsJsonObject().getAsJsonObject("list");
        JsonArray entries = list.getAsJsonArray("entries");
        for (JsonElement entry : entries) {
            JsonObject siteObject = entry.getAsJsonObject().getAsJsonObject("entry");
            sites.add(new Gson().fromJson(siteObject, AlfrescoSite.class));
        }
        return sites;
    }

    public AlfrescoSite createSite(AlfrescoSite site) throws AlfrescoException {
        String siteCreated = this.alfrescoClient.callPostApi(Endpoint.API_SITES, toJson(site), null);
        JsonElement jsonElement = JsonParser.parseString(siteCreated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, AlfrescoSite.class);
    }

    public int deleteSite(String siteId, boolean permanent) throws AlfrescoException {
        return this.alfrescoClient.callDeleteApi(Endpoint.API_SITES + "/" + siteId + "?permanent=" + permanent);
    }

    public AlfrescoSiteContainer getDocumentLibrary(String siteId) throws AlfrescoException {
        String siteCreated = this.alfrescoClient
                .callGetApi(Endpoint.API_SITES + "/" + siteId + "/containers/documentLibrary");
        JsonElement jsonElement = JsonParser.parseString(siteCreated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, AlfrescoSiteContainer.class);
    }

    /*
     * Node APIs
     */

    public NodeEntry getNode(String nodeId, String relativePath)
            throws AlfrescoException, UnsupportedEncodingException {
        String node = "";
        if (Util.isBlank(relativePath))
            node = this.alfrescoClient.callGetApi(Endpoint.API_NODES + "/" + nodeId + "?include=path");
        else
            node = this.alfrescoClient.callGetApi(
                    Endpoint.API_NODES + "/" + nodeId + "?include=path&relativePath=" + encodeValue(relativePath));
        JsonElement jsonElement = JsonParser.parseString(node).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }

    public NodeEntry createNode(String parentId, NodeCreate nodeCreate, File file)
            throws AlfrescoException, IOException {
        Part[] parts;
        int i = 3;

        if (file != null)
            i++;

        if (!nodeCreate.getProperties().isEmpty())
            i += nodeCreate.getProperties().size();

        parts = new Part[i];
        parts[0] = new StringPart("name", nodeCreate.getName(), UTF8);
        parts[1] = new StringPart("nodeType", nodeCreate.getNodeType(), UTF8);
        parts[2] = new StringPart("relativePath", nodeCreate.getRelativePath(), UTF8);
        int j = 3;

        if (file != null) {
            parts[j] = new FilePart("filedata", new FilePartSource(nodeCreate.getName(), file),
                    Files.probeContentType(file.toPath()), UTF8);
            j++;
        }

        if (!nodeCreate.getProperties().isEmpty()) {
            for (Map.Entry<String, String> entry : nodeCreate.getProperties().entrySet()) {
                parts[j] = new StringPart(entry.getKey(), entry.getValue(), UTF8);
                j++;
            }
        }
        String nodeCreated = "";
        if (file != null)
            nodeCreated = this.alfrescoClient.callPostApi(Endpoint.API_NODES + "/" + parentId + "/children", null,
                    parts);
        else
            nodeCreated = this.alfrescoClient.callPostApi(Endpoint.API_NODES + "/" + parentId + "/children",
                    toJson(nodeCreate), null);

        JsonElement jsonElement = JsonParser.parseString(nodeCreated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }

    public NodeEntry updateNode(String nodeId, NodeCreate nodeCreate) throws AlfrescoException {
        String nodeUpdated = this.alfrescoClient.callPutApi(Endpoint.API_NODES + "/" + nodeId, toJson(nodeCreate),
                null);
        JsonElement jsonElement = JsonParser.parseString(nodeUpdated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }

    public List<NodeEntry> getNodeChildren(String nodeId, String relativePath, int skipCount, int maxItems)
            throws AlfrescoException, UnsupportedEncodingException {
        List<NodeEntry> nodes = new ArrayList<>();
        String nodesRaw = "";
        if (Util.isBlank(relativePath))
            nodesRaw = this.alfrescoClient
                    .callGetApi(Endpoint.API_NODES + "/" + nodeId + "/children?skipCount=" + skipCount + "&maxItems="
                            + maxItems + "&include=path");
        else
            nodesRaw = this.alfrescoClient
                    .callGetApi(Endpoint.API_NODES + "/" + nodeId + "/children?skipCount=" + skipCount + "&maxItems="
                            + maxItems + "&include=path&relativePath=" + encodeValue(relativePath));
        JsonElement jsonElement = JsonParser.parseString(nodesRaw).getAsJsonObject();
        JsonObject list = jsonElement.getAsJsonObject().getAsJsonObject("list");
        JsonArray entries = list.getAsJsonArray("entries");
        for (JsonElement entry : entries) {
            JsonObject nodeObject = entry.getAsJsonObject().getAsJsonObject("entry");
            nodes.add(new Gson().fromJson(nodeObject, NodeEntry.class));
        }
        return nodes;
    }

    public int deleteNode(String nodeId, boolean permanent) throws AlfrescoException {
        return this.alfrescoClient.callDeleteApi(Endpoint.API_NODES + "/" + nodeId + "?permanent=" + permanent);
    }

    public NodeEntry moveNode(String nodeId, String parentId) throws AlfrescoException {
        NodeMoveRequest toMove = new NodeMoveRequest(parentId);
        String nodeMoved = this.alfrescoClient.callPostApi(Endpoint.API_NODES + "/" + nodeId + "/move?include=path",
                toJson(toMove), null);
        JsonElement jsonElement = JsonParser.parseString(nodeMoved).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }

    public ChildAssociation createChildAssociation(String nodeId, String childId, String assocType)
            throws AlfrescoException {
        ChildAssociation assoc = new ChildAssociation(childId, assocType);
        String nodeMoved = this.alfrescoClient.callPostApi(Endpoint.API_NODES + "/" + nodeId + "/secondary-children",
                toJson(assoc), null);
        JsonElement jsonElement = JsonParser.parseString(nodeMoved).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, ChildAssociation.class);
    }

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    private String toJson(Object jsonObject) {
        return new Gson().toJson(jsonObject);
    }

}
