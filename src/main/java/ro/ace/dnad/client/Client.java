package ro.ace.dnad.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

    public final static String API_ROOT = "http://localhost:8080/services/TaskAPI/";

    public static void main(String[] argv) {
        DefaultHttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(API_ROOT + "listTasks");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
                StringBuilder builder = new StringBuilder();
                String aux = "";
                while ((aux = reader.readLine()) != null) {
                    builder.append(aux);
                }

                String instruction = builder.toString();
                System.out.println(instruction);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null)
                httpClient.getConnectionManager().shutdown();
        }

    }
}
