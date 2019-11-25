package controller;

import model.Order;
import model.OrderList;
import org.json.JSONObject;
import service.IOrderService;
import service.OrderService;
import utils.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/api")
@Produces("application/json")
@Consumes("application/json")
public class OrderController {
    private IOrderService orderService;

    public OrderController() {
        orderService = new OrderService(Database.getConnection());
    }

    /**
     * Method triggered by GET request on the endpoint "/unsignedOrders"
     * This method and endpoint is only accessible to contractor
     *
     * Requests a list of all not-taken orders (not assigned to a contractor) and returns it as a JSON
     * in an HTTP Response.
     *
     * @return orders   list of orders not yet assigned to contractors
     */
    @GET
    @Path("/unsignedOrders")
    public Response getUnsignedOrders() {
        OrderList orders = null;
        try {
            orders = orderService.getUnassignedOrders();
            return Response.status(200).entity(orders).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }

    /**
     * Method triggered by GET request on the endpoint "/myorders/{clientId}"
     * This method and endpoint is only accessible to customer and contractor
     *
     * Requests a list of all orders assigned to a client whose clientId was passed in the url parameter
     * and returns it as a JSON in an HTTP Response.
     *
     * @param clientId  clientId of the client requesting orders
     * @return orders   list of orders assigned to a client's clientId
     */
    @GET
    @Path("/myorders/{clientId}")
    public Response getMyOrders(@PathParam("clientId") String clientId) {
        OrderList orders = null;
        try {
            orders = orderService.getMyOrders(clientId);
            return Response.status(200).entity(orders).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }

    /**
     * Method triggered by GET request on the endpoint "/myorders/orderbystatus/{clientId}"
     * This method and endpoint is only accessible to customer and contractor
     *
     * Requests a list of all orders sorted by a status, assigned to a client whose clientId was passed in the
     * url parameter and returns it as a JSON in an HTTP Response.
     *
     * @param clientId  clientId of the client requesting orders
     * @return orders   list of orders assigned to a client's clientId, sorted by a status
     */
    @GET
    @Path("/myorders/orderbystatus/{clientId}")
    public Response getMyOrdersByStatus(@PathParam("clientId") String clientId) {
        OrderList orders = null;
        try {
            orders = orderService.getMyOrdersByStatus(clientId);
            return Response.status(200).entity(orders).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }

    /**
     * Method triggered by GET request on the endpoint "/myorders/orderbydeadline/{clientId}"
     * This method and endpoint is only accessible to customer and contractor
     *
     * Requests a list of all orders sorted by a pick-up deadline, assigned to a client whose clientId was passed
     * in the url parameter and returns it as a JSON in an HTTP Response.
     *
     * @param clientId  clientId of the client requesting orders
     * @return orders   list of orders assigned to a client's clientId, sorted by a pick-up deadline
     */
    @GET
    @Path("/myorders/orderbydeadline/{clientId}")
    public Response getMyOrdersByDeadline(@PathParam("clientId") String clientId) {
        OrderList orders = null;
        try {
            orders = orderService.getMyOrdersByDeadline(clientId);
            return Response.status(200).entity(orders).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }

    /**
     * Method triggered by GET request on the endpoint "/order/{id}"
     * This method and endpoint is accessible to customer and contractor
     *
     * Requests a specific order whose orderNumber was passed in the url parameter
     * and returns it as a JSON in an HTTP Response.
     *
     * @param orderNumber   orderNumber of the order to be returned
     * @return order
     */
    @GET
    @Path("/order/{id}")
    public Response getOrderById(@PathParam("id") String orderNumber) {
        Order order = null;
        try {
            order = orderService.getOrderById(orderNumber);
            return Response.status(200).entity(order).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }

    /**
     * Method triggered by POST request on the endpoint "/order"
     * This method and endpoint is only accessible to customer
     *
     * Requests a new order passed in the Request body to be added to the system and returns an HTTP Response.
     *
     * @param order     order to be added to the system
     */
    @POST
    @Path("/order")
    public Response addOrder(Order order) {
        try {
            orderService.addOrder(order);
            return Response.status(200).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }

    /**
     * Method triggered by POST request on the endpoint "/updatestatus".
     * This method and endpoint is only accessible to contractor
     *
     * Requests an status update of the order whose orderNumber is passed in the Request body
     * and returns an HTTP Response.
     *
     * @param json     json containing orderNumber and updateType values
     */
    @POST
    @Path("/updatestatus")
    public Response updateStatus(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String orderNumber = String.valueOf(jsonObject.getInt("orderNumber"));
            String updateType = String.valueOf(jsonObject.getString("updateType"));

            orderService.updateStatus(orderNumber, updateType);

            return Response.status(200).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }

    /**
     * Method triggered by POST request on the endpoint "/acceptorder".
     * This method and endpoint is only accessible to contractor
     *
     * Requests an order whose orderNumber is passed in the Request body to be assigned to a contractor whose companyId
     * is also passed in the Request body and returns an HTTP Response.
     *
     * @param json      json containing orderNumber and companyId
     */
    @POST
    @Path("/acceptorder")
    public Response acceptOrder(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String orderNumber = String.valueOf(jsonObject.getInt("orderNumber"));
            String companyId = String.valueOf(jsonObject.getInt("companyId"));
            orderService.acceptOrder(orderNumber, companyId);
            return Response.status(200).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }

    /**
     * Method triggered by DELETE request on the endpoint "/refusetaken/{id}".
     * This method and endpoint is only accessible to contractor
     *
     * Requests to cancel a delivery of an order whose {orderNumber} is passed in the url parameter
     * and returns an HTTP Response.
     *
     * @param orderNumber     of the order to be canceled
     */
    @DELETE
    @Path("/refusetaken/{id}")
    public Response refuseTakenOrder(@PathParam("id") String orderNumber) {
        try {
            orderService.refuseTakenOrder(orderNumber);
            return Response.status(200).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }

    /**
     * Method triggered by DELETE request on the endpoint "/deleteunsigned/{id}".
     * This method and endpoint is only accessible to customer
     *
     * Requests to delete an unassigned (not yet assigned to a contractor) order whose orderNumber
     * is passed in the url parameter and returns an HTTP Response.
     *
     * @param orderNumber     of the order to be deleted
     */
    @DELETE
    @Path("/deleteunsigned/{id}")
    public Response deleteUnsignedOrder(@PathParam("id") String orderNumber) {
        try {
            orderService.deleteUnsignedOrder(orderNumber);
            return Response.status(200).build();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).entity(e).build();
        }
    }
}
