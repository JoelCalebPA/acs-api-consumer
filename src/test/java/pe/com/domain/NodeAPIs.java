package pe.com.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import pe.com.domain.acs.api.consumer.model.api.NodeCreate;
import pe.com.domain.acs.api.consumer.model.api.NodeEntry;

public class NodeAPIs extends Base {
       
    @Test()
    @Order(1)
    void testGetSharedNode() throws Exception {
        String expectedName = "Shared";
        String expectedNodeType = "cm:folder";

        NodeEntry node = service.getNode("-shared-", null);
        String actualName = node.getName();
        String actualNodeType = node.getNodeType();
        Assertions.assertEquals(expectedName, actualName);
        Assertions.assertEquals(expectedNodeType, actualNodeType);
    }

    @Test
    @Order(2)
    void testCreateNode() throws Exception {
        NodeCreate nodeCreate = new NodeCreate();
        nodeCreate.setName("Prueba.txt");
        nodeCreate.setNodeType("cm:content");
        nodeCreate.setRelativePath("TEST");
        NodeEntry node = service.createNode("-shared-", nodeCreate, null);
        NodeEntry testFolder = service.getNode(node.getParentId(), null);
        Assertions.assertEquals("Prueba.txt", node.getName());
        Assertions.assertEquals("TEST", testFolder.getName());
    }

    @Test
    @Order(3)
    void testDeleteNode() throws Exception {
        NodeEntry node = service.getNode("-shared-", "TEST/Prueba.txt");
        int status = service.deleteNode(node.getId(), true);
        Assertions.assertEquals(204, status);
    }

}
