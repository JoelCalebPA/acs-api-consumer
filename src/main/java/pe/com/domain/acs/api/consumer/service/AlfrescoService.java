package pe.com.domain.acs.api.consumer.service;

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
import pe.com.domain.acs.api.consumer.model.Authentication;
import pe.com.domain.acs.api.consumer.model.api.ChildAssociation;
import pe.com.domain.acs.api.consumer.model.api.Endpoint;
import pe.com.domain.acs.api.consumer.model.api.NodeCreate;
import pe.com.domain.acs.api.consumer.model.api.NodeEntry;
import pe.com.domain.acs.api.consumer.model.api.NodeMoveRequest;
import pe.com.domain.acs.api.consumer.model.api.NodeVersionEntry;
import pe.com.domain.acs.api.consumer.model.api.RevertBody;
import pe.com.domain.acs.api.consumer.model.api.actions.ActionBodyExec;
import pe.com.domain.acs.api.consumer.model.api.actions.ActionEntry;
import pe.com.domain.acs.api.consumer.util.Util;

/**
 * Clase que expone los servicios de las APIs de Alfresco
 * @author Joel Paytan
 */
public class AlfrescoService {

    private static final String UTF8 = "UTF-8";

    private AlfrescoClient alfrescoClient;

    static HttpMethodRetryHandler myretryhandler;
    
    /**
     * 
     * @param host alfresco host (e.g. http://localhost:8080/alfresco)
     * @param username valid alfresco username
     * @param password valid alfresco password
     */
    public AlfrescoService(Authentication auth) {
        this.alfrescoClient = new AlfrescoClient(auth);
    }

    /** Sites **/

    /**
     * Listar todos los sitios de Alfresco.
     * @return List<{@link AlfrescoSite}>
     * @throws AlfrescoException
     * @since Alfresco 5.2
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

    /**
     * Crear un sitio de Alfresco.
     * @param site propiedades del sitio, revisar {@link AlfrescoSite} 
     * @return {@link AlfrescoSite}
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public AlfrescoSite createSite(AlfrescoSite site) throws AlfrescoException {
        String siteCreated = this.alfrescoClient.callPostApi(Endpoint.API_SITES, toJson(site), null);
        JsonElement jsonElement = JsonParser.parseString(siteCreated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, AlfrescoSite.class);
    }

    /**
     * Eliminar un sitio de Alfresco.
     * @param siteId shortName del sitio
     * @param permanent si es <b>true</b>, el sitio se elimina permanentemente
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public int deleteSite(String siteId, boolean permanent) throws AlfrescoException {
        return this.alfrescoClient.callDeleteApi(Endpoint.API_SITES + "/" + siteId + "?permanent=" + permanent);
    }

    /**
     * Obtener el documentLibrary de un sitio.
     * @param siteId shortName del sitio
     * @return uuid del documentLibrary
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public String getDocumentLibrary(String siteId) throws AlfrescoException {
        String siteCreated = this.alfrescoClient
                .callGetApi(Endpoint.API_SITES + "/" + siteId + "/containers/documentLibrary");
        JsonElement jsonElement = JsonParser.parseString(siteCreated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, AlfrescoSiteContainer.class).getId();
    }

    /** Nodes **/

    /**
     * Obtener información de un nodo en base a su uuid.
     * @param nodeId uuid del nodo
     * @param relativePath ruta relativa partiendo desde el nodeId (opcional)
     * @return {@link NodeEntry}
     * @throws AlfrescoException
     * @throws UnsupportedEncodingException
     * @since Alfresco 5.2
     */
    public NodeEntry getNode(String nodeId, String relativePath) throws AlfrescoException {
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

    /**
     * Descargar contenido de un nodo
     * @param nodeId uuid del nodo a descargar
     * @return {@link InputStream}
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public InputStream getNodeContent(String nodeId) throws AlfrescoException {
        InputStream content = null;
        content = this.alfrescoClient.getContent(Endpoint.API_NODES + "/" + nodeId + "/content");
        return content;
    }

    /**
     * Crear un nodo con o sin contenido.
     * @param parentId uuid del nodo padre donde se creará el nuevo nodo
     * @param nodeCreate propiedades del nodo a crear, revisar {@link NodeCreate}
     * @param file archivo a cargar (opcional)
     * @return {@link NodeEntry}
     * @throws AlfrescoException
     * @throws IOException
     * @since Alfresco 5.2
     */
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

    /**
     * Actualizar propiedades de un nodo.
     * @param nodeId uuid del nodo
     * @param nodeCreate propiedades a actualizar del nodo
     * @return {@link NodeEntry}
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public NodeEntry updateNode(String nodeId, NodeCreate nodeCreate) throws AlfrescoException {
        String nodeUpdated = this.alfrescoClient.callPutApi(Endpoint.API_NODES + "/" + nodeId, toJson(nodeCreate),
                null);
        JsonElement jsonElement = JsonParser.parseString(nodeUpdated).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }

    /**
     * Obtener la lista de nodos hijos de una carpeta (limitado a 10000 resultados).
     * @param nodeId uuid de la carpeta
     * @param relativePath ruta relativa partiendo desde el {@link nodeId} (opcional)
     * @return List<{@link NodeEntry}>
     * @throws AlfrescoException
     * @throws UnsupportedEncodingException
     * @since Alfresco 5.2
     */
    public List<NodeEntry> getNodeChildren(String nodeId, String relativePath) throws AlfrescoException {
        List<NodeEntry> nodes = new ArrayList<>();
        String nodesRaw = "";
        if (Util.isBlank(relativePath))
            nodesRaw = this.alfrescoClient
                    .callGetApi(Endpoint.API_NODES + "/" + nodeId + "/children?skipCount=0&maxItems=10000&include=path");
        else
            nodesRaw = this.alfrescoClient
                    .callGetApi(Endpoint.API_NODES + "/" + nodeId + "/children?skipCount=0&maxItems=10000&include=path&relativePath=" + encodeValue(relativePath));
        JsonElement jsonElement = JsonParser.parseString(nodesRaw).getAsJsonObject();
        JsonObject list = jsonElement.getAsJsonObject().getAsJsonObject("list");
        JsonArray entries = list.getAsJsonArray("entries");
        for (JsonElement entry : entries) {
            JsonObject nodeObject = entry.getAsJsonObject().getAsJsonObject("entry");
            nodes.add(new Gson().fromJson(nodeObject, NodeEntry.class));
        }
        return nodes;
    }

    /**
     * Eliminación de un nodo.
     * @param nodeId uuid del nodo a eliminar
     * @param permanent si es <b>true</b>, el nodo se elimina permanentemente
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public int deleteNode(String nodeId, boolean permanent) throws AlfrescoException {
        return this.alfrescoClient.callDeleteApi(Endpoint.API_NODES + "/" + nodeId + "?permanent=" + permanent);
    }

    /**
     * Mover un nodo de una carpeta a otra.
     * @param nodeId uuid del nodo a mover
     * @param parentId uuid de la nueva carpeta padre
     * @return {@link NodeEntry}
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public NodeEntry moveNode(String nodeId, String parentId) throws AlfrescoException {
        NodeMoveRequest toMove = new NodeMoveRequest(parentId);
        String nodeMoved = this.alfrescoClient.callPostApi(Endpoint.API_NODES + "/" + nodeId + "/move?include=path",
                toJson(toMove), null);
        JsonElement jsonElement = JsonParser.parseString(nodeMoved).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeEntry.class);
    }

    /**
     * Crear una asociación entre un nodo padre y un nodo hijo.
     * @param nodeId uuid del nodo padre
     * @param childId uuid del nodo hijo
     * @param assocType tipo de asociación
     * @return {@link ChildAssociation}
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public ChildAssociation createChildAssociation(String nodeId, String childId, String assocType)
            throws AlfrescoException {
        ChildAssociation assoc = new ChildAssociation(childId, assocType);
        String nodeMoved = this.alfrescoClient.callPostApi(Endpoint.API_NODES + "/" + nodeId + "/secondary-children",
                toJson(assoc), null);
        JsonElement jsonElement = JsonParser.parseString(nodeMoved).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, ChildAssociation.class);
    }

    /** Versions **/

    /**
     * Listar las versiones de un nodo
     * @param nodeId uuid del nodo
     * @return List<{@link NodeVersionEntry}>
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public List<NodeVersionEntry> listVersionHistory(String nodeId) throws AlfrescoException {
        List<NodeVersionEntry> history = new ArrayList<>();
        String historyRaw = this.alfrescoClient.callGetApi(Endpoint.API_NODES + "/" + nodeId + "/versions");
        JsonElement jsonElement = JsonParser.parseString(historyRaw).getAsJsonObject();
        JsonObject list = jsonElement.getAsJsonObject().getAsJsonObject("list");
        JsonArray entries = list.getAsJsonArray("entries");
        for (JsonElement entry : entries) {
            JsonObject nodeObject = entry.getAsJsonObject().getAsJsonObject("entry");
            history.add(new Gson().fromJson(nodeObject, NodeVersionEntry.class));
        }
        return history;
    }

    /**
     * Obtener información de una versión del nodo
     * @param nodeId uuid del nodo
     * @param versionId id de la versión
     * @return {@link NodeVersionEntry}
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public NodeVersionEntry getVersionInfo(String nodeId, String versionId) throws AlfrescoException {
        String nodeRaw = this.alfrescoClient.callGetApi(Endpoint.API_NODES + "/" + nodeId + "/versions/" + versionId);
        JsonElement jsonElement = JsonParser.parseString(nodeRaw).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeVersionEntry.class);
    }

    /**
     * Descargar una versión en especifica del nodo
     * @param nodeId uuid del nodo
     * @param versionId id de la versión
     * @return {@link InputStream}
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public InputStream getVersionContent(String nodeId, String versionId) throws AlfrescoException {
        InputStream content = null;
        content = this.alfrescoClient.getContent(Endpoint.API_NODES + "/" + nodeId + "/versions/" + versionId + "/content");
        return content;
    }

    /**
     * Elimina una versión en especifica del nodo
     * @param nodeId uuid del nodo
     * @param versionId id de la versión
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public int deleteVersion(String nodeId, String versionId) throws AlfrescoException {
        return this.alfrescoClient.callDeleteApi(Endpoint.API_NODES + "/" + nodeId + "/versions/" + versionId);
    }

    /**
     * Revertir a una versión en especifica del nodo
     * @param nodeId uuid del nodo
     * @param versionId id de la versión
     * @param revertBody propiedades de la nueva versión, revisar {@link RevertBody}
     * @return {@link NodeVersionEntry}
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public NodeVersionEntry revertVersion(String nodeId, String versionId, RevertBody revertBody) throws AlfrescoException {
        String versionRaw = this.alfrescoClient.callPostApi(Endpoint.API_NODES + "/" + nodeId + "/versions/" + versionId + "/revert", toJson(revertBody), null);
        JsonElement jsonElement = JsonParser.parseString(versionRaw).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return new Gson().fromJson(entry, NodeVersionEntry.class);
    }

    /** Actions **/

    /**
     * Lista todas las acciónes de Alfresco
     * @return List<{@link ActionEntry}>
     * @throws AlfrescoException
     * @since Alfresco 5.2.2
     */
    public List<ActionEntry> listActions() throws AlfrescoException {
        List<ActionEntry> actions = new ArrayList<>();
        String actionsRaw = this.alfrescoClient.callGetApi(Endpoint.API_BASE + "/action-definitions");
        JsonElement jsonElement = JsonParser.parseString(actionsRaw).getAsJsonObject();
        JsonObject list = jsonElement.getAsJsonObject().getAsJsonObject("list");
        JsonArray entries = list.getAsJsonArray("entries");
        for (JsonElement entry : entries) {
            JsonObject nodeObject = entry.getAsJsonObject().getAsJsonObject("entry");
            actions.add(new Gson().fromJson(nodeObject, ActionEntry.class));
        }
        return actions;
    }

    /**
     * Ejecutar una acción de Alfresco
     * @param actionBodyExec parametros requeridos por la acción, revisar {@link ActionBodyExec}
     * @return id de la ejecución
     * @throws AlfrescoException
     * @since Alfresco 5.2
     */
    public String executeAction(ActionBodyExec actionBodyExec) throws AlfrescoException {
        String actionRaw = this.alfrescoClient.callPostApi(Endpoint.API_BASE + "/action-executions", toJson(actionBodyExec), null);
        JsonElement jsonElement = JsonParser.parseString(actionRaw).getAsJsonObject();
        JsonObject entry = jsonElement.getAsJsonObject().getAsJsonObject("entry");
        return entry.get("id").getAsString();
    }

    private String encodeValue(String value) {
        String encodedValue = "";
        try {
            encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedValue;
    }

    private String toJson(Object jsonObject) {
        return new Gson().toJson(jsonObject);
    }

}
