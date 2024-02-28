package pe.com.domain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import pe.com.domain.bean.api.NodeEntry;
import pe.com.domain.service.AlfrescoService;

public class ConsumerTestApp
{

    public static void main(String[] args)
    {
        AlfrescoService alfService = new AlfrescoService();
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("HOST", "http://localhost:8080/alfresco");
        credenciales.put("USER", "admin");
        credenciales.put("PASSWORD", "admin");
        alfService.setConexion(credenciales);
        try
        {
            NodeEntry entry = alfService.getNode("712db8fa-670d-4344-a389-179038d821fa", "");
            System.out.println(entry.getName() + " - " + entry.getId() + " - " + entry.getSizeInBytes());
            File file = new File(entry.getName());
            FileUtils.copyInputStreamToFile(alfService.getContentV2(entry.getId()), file);
            OutputStream out = new FileOutputStream(file);
            IOUtils.copyLarge(alfService.getContentV2(entry.getId()), out);
            IOUtils.closeQuietly(out);
            System.out.println(file.getAbsolutePath());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
