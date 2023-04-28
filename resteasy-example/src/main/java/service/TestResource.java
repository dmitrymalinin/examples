package service;

import java.util.logging.Logger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/")
public class TestResource {
	private static final Logger logger = Logger.getLogger(TestResource.class.getName());
	
	private final DataItem[] items1 = {new DataItem("DataItem_1"), new DataItem("DataItem_2"), new DataItem("DataItem_3")};
	private final DataItem2[] items2 = {new DataItem2("DataItem2_1"), new DataItem2("DataItem2_2"), new DataItem2("DataItem2_3")};
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response test()
	{
		logger.info("");
		return Response.ok("TEST").build();
	}
	
	@GET
	@Path("getText")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getText()
	{
		logger.info("");
		return Response.ok("Text").build();
	}
	
	// curl -w '\n' -D - -X GET http://localhost:8081/rest/items
	@GET
	@Path("items")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItems()
	{
		logger.info("");
		// Call latest version
		DataItem2[] result = getItems_v2(); 
		return Response.ok(result).build();
	}
	
	// curl -w '\n' -D - -X GET -H "Accept: application/vnd.malinin.api-v1.0+json" http://localhost:8081/rest/items
	@GET
	@Path("items")
	@Produces("application/vnd.malinin.api-v1.0+json")
	public DataItem[] getItems_v1()
	{
		logger.info("");
		return items1;
	}
	
	// curl -w '\n' -D - -X GET -H "Accept: application/vnd.malinin.api-v2.0+json" http://localhost:8081/rest/items
	@GET
	@Path("items")
	@Produces("application/vnd.malinin.api-v2.0+json")
	public DataItem2[] getItems_v2()
	{
		logger.info("");
		return items2;
	}
	
	// curl -w '\n' -D - -X GET http://localhost:8081/rest/item/1
	@GET
	@Path("item/{index}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItem(@PathParam("index") int index)
	{
		logger.info("index: "+index);
		return getItem_v2(index);
	}
	
	// curl -w '\n' -D - -X GET -H "Accept: application/vnd.malinin.api-v1.0+json" http://localhost:8081/rest/item/1
	@GET
	@Path("item/{index}")
	@Produces("application/vnd.malinin.api-v1.0+json")
	public Response getItem_v1(@PathParam("index") int index)
	{
		logger.info("index: "+index);
		if (index < 0 || index > items1.length-1)
			return Response.status(Status.BAD_REQUEST).build();
		
		return Response.ok(items1[index]).build();
	}
	
	// curl -w '\n' -D - -X GET -H "Accept: application/vnd.malinin.api-v2.0+json" http://localhost:8081/rest/item/1
	@GET
	@Path("item/{index}")
	@Produces("application/vnd.malinin.api-v2.0+json")
	public Response getItem_v2(@PathParam("index") int index)
	{
		logger.info("index: "+index);
		if (index < 0 || index > items2.length-1)
			return Response.status(Status.BAD_REQUEST).build();
		
		return Response.ok(items2[index]).build();
	}
}
