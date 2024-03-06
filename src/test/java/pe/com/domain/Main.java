package pe.com.domain;

import pe.com.domain.acs.api.consumer.model.AlfrescoException;
import pe.com.domain.acs.api.consumer.model.Authentication;
import pe.com.domain.acs.api.consumer.service.AlfrescoService;

public class Main {
    public static void main(String[] args) {
        Authentication auth = new Authentication("http://localhost:8080/alfresco", "admin", "admin");
        AlfrescoService service = new AlfrescoService(auth);
        try {
            service.listVersionHistory("352587a0-7697-4d13-91a8-f2defbe77110").stream().forEach(version -> {
                System.out.println(version.toString());
            });
            System.out.println(service.getVersionInfo("352587a0-7697-4d13-91a8-f2defbe77110", "1.1").getName());
        } catch (AlfrescoException e) {
            e.printStackTrace();
        }
    }
}
