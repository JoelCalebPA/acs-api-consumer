package pe.com.domain.acs.api.consumer.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pe.com.domain.acs.api.consumer.model.AlfrescoApiError;
import pe.com.domain.acs.api.consumer.model.AlfrescoException;
import pe.com.domain.acs.api.consumer.util.Util;

public class AlfrescoClient {

    private static final String JSON_MIME = "application/json";
    private static final String UTF8 = "UTF-8";
    private HttpMethodRetryHandler myretryhandler;

    private String host;
    private String basicAuth;

    public AlfrescoClient(String host, String username, String password) {
        this.host = host;
        this.basicAuth = getBasicAuth(username, password);
    }

    public String callGetApi(String serviceURI) throws AlfrescoException {
        HttpClient client = setHttp();
        GetMethod mGet = null;
        String response = "";
        try {
            mGet = new GetMethod(this.host + serviceURI);
            mGet.setRequestHeader("Authorization", this.basicAuth);
            mGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);

            int status = client.executeMethod(mGet);
            response = IOUtils.toString(mGet.getResponseBodyAsStream(), StandardCharsets.UTF_8);

            if (status != HttpStatus.SC_OK) {
                JsonElement jsonElement = JsonParser.parseString(response).getAsJsonObject();
                JsonObject error = jsonElement.getAsJsonObject().getAsJsonObject("error");
                AlfrescoApiError alfrescoError = new Gson().fromJson(error, AlfrescoApiError.class);
                throw new AlfrescoException("API call failed: " + serviceURI, alfrescoError);
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String callPostApi(String serviceURI, String bodyJson, Part[] parts) throws AlfrescoException {
        HttpClient client = setHttp();
        PostMethod mPost = null;
        String response = "";
        try {
            mPost = new PostMethod(this.host + serviceURI);
            mPost.setRequestHeader("Authorization", this.basicAuth);
            mPost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);

            if (!Util.isBlank(bodyJson)) {
                StringRequestEntity requestEntity = new StringRequestEntity(bodyJson, JSON_MIME, UTF8);
                mPost.setRequestEntity(requestEntity);
            }
            if (parts != null) {
                MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, mPost.getParams());
                mPost.setRequestEntity(requestEntity);
            }

            int status = client.executeMethod(mPost);
            response = IOUtils.toString(mPost.getResponseBodyAsStream(), StandardCharsets.UTF_8);

            if (status != HttpStatus.SC_OK && status != HttpStatus.SC_CREATED) {
                JsonElement jsonElement = JsonParser.parseString(response).getAsJsonObject();
                JsonObject error = jsonElement.getAsJsonObject().getAsJsonObject("error");
                AlfrescoApiError alfrescoError = new Gson().fromJson(error, AlfrescoApiError.class);
                throw new AlfrescoException("API call failed: " + serviceURI, alfrescoError);
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String callPutApi(String serviceURI, String bodyJson, Part[] parts) throws AlfrescoException {
        HttpClient client = setHttp();
        PutMethod mPut = null;
        String response = "";
        try {
            mPut = new PutMethod(this.host + serviceURI);
            mPut.setRequestHeader("Authorization", this.basicAuth);
            mPut.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);

            if (!Util.isBlank(bodyJson)) {
                StringRequestEntity requestEntity = new StringRequestEntity(bodyJson, JSON_MIME, UTF8);
                mPut.setRequestEntity(requestEntity);
            }
            if (parts != null) {
                MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, mPut.getParams());
                mPut.setRequestEntity(requestEntity);
            }

            int status = client.executeMethod(mPut);
            response = IOUtils.toString(mPut.getResponseBodyAsStream(), StandardCharsets.UTF_8);

            if (status != HttpStatus.SC_OK && status != HttpStatus.SC_CREATED) {
                JsonElement jsonElement = JsonParser.parseString(response).getAsJsonObject();
                JsonObject error = jsonElement.getAsJsonObject().getAsJsonObject("error");
                AlfrescoApiError alfrescoError = new Gson().fromJson(error, AlfrescoApiError.class);
                throw new AlfrescoException("API call failed: " + serviceURI, alfrescoError);
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public int callDeleteApi(String serviceURI) throws AlfrescoException {
        HttpClient client = setHttp();
        DeleteMethod mDelete = null;
        String response = "";
        int status = 0;
        try {
            mDelete = new DeleteMethod(this.host + serviceURI);
            mDelete.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
            mDelete.setRequestHeader("Authorization", this.basicAuth);

            status = client.executeMethod(mDelete);

            if (status != HttpStatus.SC_NO_CONTENT) {
                response = IOUtils.toString(mDelete.getResponseBodyAsStream(), StandardCharsets.UTF_8);
                JsonElement jsonElement = JsonParser.parseString(response).getAsJsonObject();
                JsonObject error = jsonElement.getAsJsonObject().getAsJsonObject("error");
                AlfrescoApiError alfrescoError = new Gson().fromJson(error, AlfrescoApiError.class);
                throw new AlfrescoException("API call failed: " + serviceURI, alfrescoError);
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    public InputStream getContent(String serviceURI) {
        HttpClient client = setHttp();
        InputStream is = null;
        try {
            GetMethod mGet = new GetMethod(this.host + serviceURI);
            mGet.setRequestHeader("Authorization", this.basicAuth);
            mGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
            client.executeMethod(mGet);
            is = mGet.getResponseBodyAsStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * Metodo que permite configurar el componente Http
     */
    private HttpClient setHttp() {
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        myretryhandler = new HttpMethodRetryHandler() {
            public boolean retryMethod(final HttpMethod method, final IOException exception, int executionCount) {
                if (executionCount >= 1) {
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {
                    return true;
                }
                if (!method.isRequestSent()) {
                    return true;
                }
                return false;
            }
        };
        return client;
    }

    private String getBasicAuth(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
        return "Basic " + new String(encodedAuth);
    }

}
