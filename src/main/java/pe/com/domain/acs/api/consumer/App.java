package pe.com.domain.acs.api.consumer;

import pe.com.domain.acs.api.consumer.model.AlfrescoException;
import pe.com.domain.acs.api.consumer.service.AlfrescoService;

public class App {
    public static void main(String[] args) {
        AlfrescoService service = new AlfrescoService("http://localhost:8080/alfresco", "admin", "admin");
        try {
            System.out.println(service.getNode("-shared", null).toString());
            System.out.println(service.getDocumentLibrary("swsd"));
        }catch (AlfrescoException e) {
            System.err.println(e.getMessage());
        }
    }
}
