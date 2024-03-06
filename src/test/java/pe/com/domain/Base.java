package pe.com.domain;

import pe.com.domain.acs.api.consumer.model.Authentication;
import pe.com.domain.acs.api.consumer.service.AlfrescoService;

public abstract class Base {

    Authentication auth = new Authentication("http://localhost:8080/alfresco", "admin", "admin");
    AlfrescoService service = new AlfrescoService(auth);

}