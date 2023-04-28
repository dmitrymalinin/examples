package rsclient;

import java.util.Arrays;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import service.DataItem;
import service.DataItem2;

public class Main {
//	private static final String baseUri = "http://localhost:8081/rest/";
	private static final String baseUri = "http://localhost:8080/resteasy-example/rest/";
	
	public static void main(String[] args) {
		try (Client client = ClientBuilder.newClient()) 
		{
			final WebTarget target = client.target(baseUri);
            
			final Response test_response = target.request().get();
            System.out.printf("test_response: %d - %s%n", test_response.getStatus(), test_response.readEntity(String.class));
                        
            final Response items_response = target.path("items").request().get();
            System.out.printf("items_response: %d - %s%n", items_response.getStatus(), Arrays.toString(items_response.readEntity(DataItem2[].class)));
            
            final Response items_v1_response = target.path("items").request().accept("application/vnd.malinin.api-v1.0+json").get();
            System.out.printf("items_v1_response: %d - %s%n", items_v1_response.getStatus(), Arrays.toString(items_v1_response.readEntity(DataItem[].class)));
            
            final Response item_response = target.path("item").path("1").request().get();
            System.out.printf("item_response: %d - %s%n", item_response.getStatus(), item_response.readEntity(DataItem2.class));            
        }
	}

}
