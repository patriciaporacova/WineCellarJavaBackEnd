package controller;

import model.AbstractClient;
import service.ClientService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/api")
@Produces("application/json")
@Consumes("application/json")
public class ClientController {
    private ClientService clientService;

    public ClientController() { this.clientService = new ClientService(); }

    /**
     * Method triggered by GET request on the endpoint "/client/{clientId}"
     * This method and endpoint is accessible to customer and contractor
     *
     * Requests a specific client object whose clientId was passed in the url parameter
     * and returns it as a JSON in an HTTP Response.
     *
     * @param clientId   clientId of the client to be returned
     * @return order
     */
    @GET
    @Path("/client/{clientId}")
    public Response getClientById(@PathParam("clientId") String clientId) {
        try {
            AbstractClient client = clientService.getClientById(clientId);
            return Response.status(200).entity(client).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }

    /**
     * Method triggered by POST request on the endpoint "/client"
     * This method and endpoint is accessible to customer and contractor
     *
     * Requests a registration of the new client whose details are passed in the Request body
     * and returns it as a JSON in an HTTP Response.
     *
     * @param client   client to be registered
     * @return order
     */
    @POST
    @Path("/client")
    public Response registerClient(AbstractClient client)
    {
        try {
            clientService.registerClient(client);

            return Response.status(200).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }
}