package ro.ace.dnad.server;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LongSummaryStatistics;

// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/TaskAPI")
public class Server {

    @GET
    @Path(value = "/listTasks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskList() {

        TaskHandler taskHandler = TaskHandler.getInstance();

        return Response.ok("List size" + taskHandler.getCurrentTaskList().size(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path(value = "/getTask")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getTask(@QueryParam("taskId") String taskId) {
        TaskHandler taskHandler = TaskHandler.getInstance();
        Long id;
        try {
            id = Long.valueOf(taskId);
        } catch (NumberFormatException ex) {
            System.out.println("Wrong task id");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(taskHandler.getTaskInstructions(id)).build();
    }

    @POST
    @Path(value = "/processResponse")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response processResponse(String response) {
        if(response != null) {
            TaskHandler taskHandler = TaskHandler.getInstance();
            //client found password
            System.out.println("Client found password: " + response);
            //clear task list;
            taskHandler.clearTaskList();
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }
}