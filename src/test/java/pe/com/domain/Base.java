package pe.com.domain;

import pe.com.domain.acs.api.consumer.service.AlfrescoService;

public abstract class Base {

    AlfrescoService service = new AlfrescoService("http://localhost:8080/alfresco", "admin", "admin");

}