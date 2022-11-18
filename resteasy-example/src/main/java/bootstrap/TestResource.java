package bootstrap;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class TestResource {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response test()
	{
		return Response.ok("TEST").build();
	}
}
