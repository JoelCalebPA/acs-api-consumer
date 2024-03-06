package pe.com.domain;

import pe.com.domain.acs.api.consumer.model.AlfrescoException;
import pe.com.domain.acs.api.consumer.model.Authentication;
import pe.com.domain.acs.api.consumer.service.AlfrescoService;

public class Main {
    public static void main(String[] args) {
        Authentication auth = new Authentication("http://localhost:8080/alfresco", "admin", "admin");
        AlfrescoService service = new AlfrescoService(auth);
        try {
            service.listActions().stream().forEach(action -> {
                System.out.println(action.getId() + "\t" + action.getTitle());
            });
        } catch (AlfrescoException e) {
            e.printStackTrace();
        }
    }
}
