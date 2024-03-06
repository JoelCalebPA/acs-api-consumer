package pe.com.domain.acs.api.consumer;

import java.io.UnsupportedEncodingException;

import pe.com.domain.acs.api.consumer.model.AlfrescoException;
import pe.com.domain.acs.api.consumer.service.AlfrescoService;

public class App {
    public static void main(String[] args) {
        AlfrescoService service = new AlfrescoService("http://localhost:8080/alfresco", "admin", "admin");
        try {
            System.out.println(service.getNode("-shared-", null).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (AlfrescoException e) {
            e.printStackTrace();
        }
    }
}
