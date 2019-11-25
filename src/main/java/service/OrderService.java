package service;

import model.Address;
import model.Order;
import model.OrderList;
import utils.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OrderService implements IOrderService
{
    private static String DB_NAME;
    private final SimpleDateFormat SDF;
    private IAddressService addressService;
    private Connection connection;

    public OrderService(Connection dbConnection) {
        this.SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.addressService = new AddressService();
        this.connection = dbConnection;
        this.DB_NAME = Database.getDbNameFromConnection(connection);
    }

    /**
     * Queries a database and returns a specific order
     *
     * @param orderNumber   of the order to be returned
     * @return order
     */
    @Override
    public Order getOrderById(String orderNumber) throws SQLException
    {
        Order order = null;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".order WHERE ID_order = '" + orderNumber + "';");

        if (resultSet.next())
        {
            order = populateOrder(resultSet);
        }

        return order;
    }

    /**
     * Queries a database and returns an OrderList of unassigned orders (with not yet contractor assigned to an order)
     *
     * @return orders
     */
    @Override
    public OrderList getUnassignedOrders() throws SQLException
    {
        OrderList orders = new OrderList();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                + ".order AS o WHERE NOT EXISTS(SELECT * FROM "
                + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order);");

        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }

        return orders;
    }

    /**
     * Queries a database and returns an OrderList of all orders
     *
     * @return orders
     */
    @Override
    public OrderList getAllOrders() throws SQLException
    {
        OrderList orders = new OrderList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".order");

        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }

        return orders;
    }

    /**
     * Queries a database and returns an OrderList of all orders sorted by pick-up deadline
     *
     * @return orders
     */
    @Override
    public OrderList getAllOrdersOrderByDeadline() throws SQLException
    {
        OrderList orders = new OrderList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".order ORDER BY pick_up_deadline ASC");

        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }

        return orders;
    }

    /**
     * Queries a database and returns an OrderList of assigned orders (contractor is assigned to an order)
     *
     * @return orders
     */
    @Override
    public OrderList getAssignedOrders() throws SQLException
    {
        OrderList orders = new OrderList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                + ".order AS o WHERE EXISTS(SELECT * FROM "
                + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order);");

        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }
        return orders;
    }

    /**
     * Queries a database and returns an OrderList of all orders related to a client with the clientId passed
     * in as a parameter. It checks for a clientType and selects a right query to use.
     *
     * For customer clientType it returns all orders created by a customer
     * For contractor clientType it returns all orders accepted by a contractor
     *
     * @return orders
     */
    @Override
    public OrderList getMyOrders(String clientId) throws SQLException
    {
        OrderList orders = new OrderList();
        String clientType = getClientType(clientId);
        Statement statement = connection.createStatement();
        ResultSet resultSet;
        if (clientType.equals("customer")) {
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".order WHERE ID_company = " + clientId + " ORDER BY pick_up_deadline;");
        } else {
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                    + ".order AS o WHERE EXISTS(SELECT * FROM "
                    + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.ID_responsible_company = "+clientId+");");
        }

        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }

        return orders;
    }

    /**
     * Queries a database and returns an OrderList of all orders sorted by a status, related to a client with the clientId passed
     * in as a parameter. It checks for a clientType and selects a right query to use.
     *
     * For customer clientType it returns all orders created by a customer
     * For contractor clientType it returns all orders accepted by a contractor
     *
     * @return orders
     */
    @Override
    public OrderList getMyOrdersByStatus(String clientId) throws SQLException
    {
        OrderList orders = new OrderList();
        String clientType = getClientType(clientId);
        Statement statement = connection.createStatement();
        ResultSet resultSet;
        if (clientType.equals("customer"))
        {
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                    + ".order AS o WHERE EXISTS(SELECT * FROM "
                    + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.awaiting_pick_up = true AND o.ID_company = "
                    + clientId + ");");
            while (resultSet.next())
            {
                orders.getOrders().add(populateOrder(resultSet));
            }
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                    + ".order AS o WHERE EXISTS(SELECT * FROM "
                    + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.picked_up = true AND o.ID_company = "
                    + clientId + ");");
            while (resultSet.next())
            {
                orders.getOrders().add(populateOrder(resultSet));
            }
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                    + ".order AS o WHERE EXISTS(SELECT * FROM "
                    + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.late_delivery = true AND o.ID_company = "
                    + clientId + ");");
            while (resultSet.next())
            {
                orders.getOrders().add(populateOrder(resultSet));
            }
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                    + ".order AS o WHERE EXISTS(SELECT * FROM "
                    + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.delivered = true AND o.ID_company = "
                    + clientId + ");");
            while (resultSet.next())
            {
                orders.getOrders().add(populateOrder(resultSet));
            }

        }
        else
        {
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                    + ".order AS o WHERE EXISTS(SELECT * FROM "
                    + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.awaiting_pick_up = true AND o2.ID_responsible_company = "
                    + clientId + ");");
            while (resultSet.next())
            {
                orders.getOrders().add(populateOrder(resultSet));
            }
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                    + ".order AS o WHERE EXISTS(SELECT * FROM "
                    + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.picked_up = true AND o2.ID_responsible_company = "
                    + clientId + ");");
            while (resultSet.next())
            {
                orders.getOrders().add(populateOrder(resultSet));
            }
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                    + ".order AS o WHERE EXISTS(SELECT * FROM "
                    + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.late_delivery = true AND o2.ID_responsible_company = "
                    + clientId + ");");
            while (resultSet.next())
            {
                orders.getOrders().add(populateOrder(resultSet));
            }
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                    + ".order AS o WHERE EXISTS(SELECT * FROM "
                    + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.delivered = true AND o2.ID_responsible_company = "
                    + clientId + ");");
            while (resultSet.next())
            {
                orders.getOrders().add(populateOrder(resultSet));
            }
        }

        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }

        return orders;
    }

    /**
     * Queries a database and returns an OrderList of all orders sorted by a pick-up deadline,
     * related to a client with the clientId passed in as a parameter. It checks for a clientType
     * and selects a right query to use.
     *
     * For customer clientType it returns all orders created by a customer
     * For contractor clientType it returns all orders accepted by a contractor
     *
     * @return orders
     */
    @Override
    public OrderList getMyOrdersByDeadline(String clientId) throws SQLException
    {
        OrderList orders = new OrderList();
        String clientType = getClientType(clientId);
        Statement statement = connection.createStatement();
        ResultSet resultSet;
        if (clientType.equals("customer"))
        {
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".order WHERE ID_company = " + clientId + " ORDER BY pick_up_deadline;");
        }
        else if(clientType.equals("contractor"))
        {
            resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                    + ".order AS o WHERE EXISTS(SELECT * FROM "
                    + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.ID_responsible_company = "+clientId+") ORDER BY pick_up_deadline;");
        }
        else
        {
            resultSet = statement.executeQuery("");
        }
        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }

        return orders;
    }

    /**
     * Queries a database and returns an OrderList of all orders sorted by a status
     *
     * @return orders
     */
    @Override
    public OrderList getOrdersByStatus() throws SQLException
    {
        OrderList orders = getUnassignedOrders();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                + ".order AS o WHERE EXISTS(SELECT * FROM "
                + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.awaiting_pick_up = true);");
        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }
        resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                + ".order AS o WHERE EXISTS(SELECT * FROM "
                + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.picked_up = true);");
        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }
        resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                + ".order AS o WHERE EXISTS(SELECT * FROM "
                + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.late_delivery = true);");
        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }
        resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME
                + ".order AS o WHERE EXISTS(SELECT * FROM "
                + DB_NAME + ".takenorders AS o2 WHERE o.ID_order = o2.ID_order AND o2.delivered = true);");
        while (resultSet.next())
        {
            orders.getOrders().add(populateOrder(resultSet));
        }

        return orders;
    }

    /**
     * Insert a new order into a database
     *
     * @param order     order to be inserted
     */
    @Override
    public void addOrder(Order order) throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO " + DB_NAME + ".order VALUES (NULL,"
                + addressService.getAddressId(order.getPickUpAddress()) + ","
                + addressService.getAddressId(order.getDropOffAddress())
                + "," + order.getCompanyID()
                + ",'" + SDF.format(order.getPickUpDeadline())
                + "','" + SDF.format(order.getDropOffDeadline())
                + "'," + order.getPrice()
                + ",'" + order.getContentDescription()
                + "','" + order.getSize()
                + "'," + order.getWeight()
                + ",'" + order.getContainerSize()
                + "','" + order.getDistance() + "');");
    }

    /**
     * Updates an existing order with details from orderObj
     *
     * @param orderObj      updated object
     */
    @Override
    public void updateOrder(Order orderObj) throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE " + DB_NAME + ".order SET pick_up_address= '"
                + addressService.getAddressId(orderObj.getPickUpAddress())
                + "',drop_off_address= '" + addressService.getAddressId(orderObj.getDropOffAddress())
                + "',pick_up_deadline= '" + SDF.format(orderObj.getPickUpDeadline())
                + "',drop_off_deadline= '" + SDF.format(orderObj.getDropOffDeadline())
                + "',price= '" + orderObj.getPrice()
                + "',description= '" + orderObj.getContentDescription()
                + "',`size` = '" + orderObj.getSize()
                + "',weight= '" + orderObj.getWeight()
                + "',container_size= '" + orderObj.getContainerSize()
                + "',distance= '" + orderObj.getDistance() + "' WHERE ID_order ="+orderObj.getOrderNumber()+";");

        statement.executeUpdate(" UPDATE " + DB_NAME + ".takenorders SET awaiting_pick_up = " + orderObj.isAwaitingPickUp()
                + ", picked_up = " + orderObj.isPickedUp() + ", delivered = " + orderObj.isDelivered()
                + ", late_delivery = " + orderObj.isLateDelivery()
                + " WHERE ID_order = " + orderObj.getOrderNumber() + ";");
    }

    /**
     * Assigns a contractor to an order
     *
     * @param orderNumber       of the order to assign contractor to
     * @param companyId         of the contractor which accepted an order
     */
    @Override
    public void acceptOrder(String orderNumber, String companyId) throws SQLException
    {
        Statement statement = connection.createStatement();
        connection.setAutoCommit(false);
        statement.executeUpdate("INSERT INTO " + DB_NAME + ".takenorders VALUES(NULL," + orderNumber + "," + companyId
                + ",'1','0','0','0');");
        connection.commit();
    }

    /**
     * Updates a status of an existing order
     *
     * @param orderNumber       orderNumber of the order to be updated
     * @param updateType        defines which fields should be updated
     */
    @Override
    public void updateStatus(String orderNumber, String updateType) throws SQLException
    {
        Statement statement = connection.createStatement();
        switch (updateType){
            case "lateOrder":
                statement.executeUpdate("UPDATE " + DB_NAME + ".takenorders SET late_delivery = " + 1 + " WHERE ID_order = " + orderNumber + ";");
                break;
            case "orderPickedUp":
                statement.executeUpdate("UPDATE " + DB_NAME + ".takenorders SET awaiting_pick_up = " + 0
                        + ", picked_up = " + 1 + ", delivered = " + 0
                        + " WHERE ID_order = " + orderNumber + ";");
                break;
            case "orderDelivered":
                statement.executeUpdate("UPDATE " + DB_NAME + ".takenorders SET awaiting_pick_up = " + 0
                        + ", picked_up = " + 0 + ", delivered = " + 1
                        + " WHERE ID_order = " + orderNumber + ";");
                break;
        }
    }

    /**
     * Deletes an order
     *
     * @param orderNumber       orderNumber of the order to be deleted
     */
    @Override
    public void deleteOrder(String orderNumber) throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + DB_NAME + ".takenorders WHERE ID_order = '" + orderNumber
                + "';");
        statement.executeUpdate("DELETE FROM " + DB_NAME + ".order WHERE ID_order = '" + orderNumber + "';");
    }

    /**
     * Deletes an unassigned order (Order not yet assigned to a contractor)
     *
     * @param orderNumber       orderNumber of the order to be deleted
     */
    @Override
    public void deleteUnsignedOrder(String orderNumber) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + DB_NAME + ".order WHERE ID_order = '" + orderNumber + "' AND NOT EXISTS "
                + "(SELECT * FROM " + DB_NAME + ".takenorders WHERE ID_order = '" + orderNumber + "');");
    }

    /**
     * Deletes an assigned order (order assigned to a contractor)
     *
     * @param orderNumber       orderNumber of the order to be deleted
     */
    @Override
    public void refuseTakenOrder(String orderNumber) throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + DB_NAME + ".takenorders WHERE ID_order = '" + orderNumber + "';");
    }

    /**
     * Initializes an Order object from the ResultSet and returns it
     *
     * @param resultSet         resultSet from database query
     * @return order
     */
    private Order populateOrder(ResultSet resultSet) throws SQLException
    {
        Date pickUpDate = null;
        Date dropOffDate = null;

        try {
            pickUpDate = SDF.parse(resultSet.getString("pick_up_deadline"));
            dropOffDate = SDF.parse(resultSet.getString("drop_off_deadline"));
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Couldn't parse SimpleDateFormat object in GetOrders() method.");
        }

        Address pickUpAddress = addressService.getAddressById(resultSet.getInt("pick_up_address"));
        Address dropOffAddress = addressService.getAddressById(resultSet.getInt("drop_off_address"));

        Order order = new Order(
                resultSet.getString("ID_order"),
                resultSet.getString("ID_company"),
                pickUpAddress,
                pickUpDate,
                dropOffAddress,
                dropOffDate,
                resultSet.getString("description"),
                resultSet.getString("container_size"),
                resultSet.getDouble("weight"),
                resultSet.getString("size"),
                resultSet.getDouble("price"),
                false,
                false,
                false,
                false,
                resultSet.getDouble("distance")

        );
        Statement statement = connection.createStatement();

        resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".takenorders WHERE ID_order = " + order.getOrderNumber() + ";");

        if(resultSet.next())
        {
            order = populateOrderWithStatus(order,resultSet);
        }
        return order;
    }

    /**
     * Helper method which initializes additional fields of assigned orders
     *
     * @param order             order where extra fields will be initialized
     * @param resultSet         resultSet from database query
     * @return order
     */
    private Order populateOrderWithStatus(Order order, ResultSet resultSet) throws  SQLException
    {
        order.setAwaitingPickUp(resultSet.getBoolean("awaiting_pick_up"));
        order.setPickedUp(resultSet.getBoolean("picked_up"));
        order.setDelivered(resultSet.getBoolean("delivered"));
        order.setResponsibleCompany(resultSet.getString("ID_responsible_company"));
        order.setLateDelivery(resultSet.getBoolean("late_delivery"));
        return order;
    }

    /**
     * Helper method which queries a database for a specific client and returns its clientType
     *
     * @param clientId          id of the client
     * @return clientType
     */
    private String getClientType(String clientId) throws  SQLException
    {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT client_type FROM " + DB_NAME + ".clients WHERE ID_client = " + clientId + ";");

        if (!resultSet.next()) { throw new SQLException("Attribute clientType for a client " + clientId + " wasn't found."); }

        return resultSet.getString("client_type");
    }


}
