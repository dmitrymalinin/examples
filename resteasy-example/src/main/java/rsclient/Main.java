package rsclient;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;

public class Main {
	private static final String serverUri = "http://localhost:8081/rest/";
	
	public static void main(String[] args) {
		try (Client client = ClientBuilder.newClient()) 
		{
            final Response response = client.target(serverUri)
                    .request()
                    .get();
            System.out.printf("Response: %d - %s%n", response.getStatus(), response.readEntity(String.class));
        }
	}

}
