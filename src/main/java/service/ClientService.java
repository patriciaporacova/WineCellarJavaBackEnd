package service;

import model.AbstractClient;
import model.ClientList;
import model.Contractor;
import model.Customer;
import utils.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static utils.Database.DB_NAME;

public class ClientService implements IClientService
{
    IAddressService addressService = new AddressService();

    /**
     * Queries a database and returns a specific client
     *
     * @param clientId   of the client to be returned
     * @return client
     */
    public AbstractClient getClientById(String clientId) throws SQLException
    {
        AbstractClient client = null;

        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".clients WHERE ID_client = '" + clientId + "';");

        if (resultSet.next())
        {
            client = populateClient(resultSet);
        }

        return client;
    }

    /**
     * Queries a database and returns list of all clients
     *
     * @return clients
     */
    public ClientList getAllClients() throws SQLException
    {
        ClientList clients = new ClientList();
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".clients;");

        while (resultSet.next())
        {
            clients.getClients().add(populateClient(resultSet));
        }

        return clients;
    }

    /**
     * Queries a database and returns a all customers
     *
     * @return clients (of type Customer)
     */
    public ClientList getCustomers() throws SQLException
    {
        ClientList clients = new ClientList();
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".clients WHERE client_type = 'customer';");

        while (resultSet.next())
        {
            clients.getClients().add(populateClient(resultSet));
        }

        return clients;
    }

    /**
     * Queries a database and returns a all contractors
     *
     * @return clients (of type Contractor)
     */
    public ClientList getContractors() throws SQLException
    {
        ClientList clients = new ClientList();
        Statement statement = Database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + DB_NAME + ".clients WHERE client_type = 'contractor';");

        while (resultSet.next())
        {
            clients.getClients().add(populateClient(resultSet));
        }

        return clients;}

    /**
     * Inserts a new client to a database
     *
     * @param client     client to be inserted
     */
    public void registerClient(AbstractClient client) throws SQLException
    {
        Statement statement = Database.getConnection().createStatement();
        client.setClientType(client.getClass() == Contractor.class ? "contractor" : "customer");
        statement.executeUpdate("INSERT INTO " + DB_NAME + ".clients VALUES (null,"
                + addressService.getAddressId(client.getAddress())
                + ",'"+ client.getCompanyName() + "','" + client.getEmail() + "','" + client.getTelephoneNumber()
                + "','" + client.getClientType() + "','" + client.getPasswordHash()+"');");
    }

    /**
     * Initializes a Contractor or Customer object (based on the clientType attribute) from the ResultSet and returns it
     *
     * @param resultSet         resultSet from database query
     * @return client
     */
    public AbstractClient populateClient(ResultSet resultSet) throws SQLException
    {
        AbstractClient client;

        if (resultSet.getString("client_type").equals("customer")) {
            client = new Customer().populateFromResultSet(resultSet, addressService);
        } else {
            client = new Contractor().populateFromResultSet(resultSet, addressService);
        }

        return client;
    }

    /**
     * Deletes a client
     *
     * @param id       clientId of the client to be deleted
     */
    @Override
    public void deleteClient(String id) throws SQLException
    {
        Statement statement = Database.getConnection().createStatement();
        statement.executeUpdate("DELETE FROM " + DB_NAME + ".clients WHERE(ID_client = '" + id + "');");
    }

    /**
     * Updates an existing client with details from client object passed into parameter
     *
     * @param client      updated client
     */
    @Override
    public void updateClient(AbstractClient client) throws SQLException
    {
        Statement statement = Database.getConnection().createStatement();
        statement.executeUpdate("UPDATE " + DB_NAME + ".clients SET company_name = '"+ client.getCompanyName()
                + "',ID_address= '" +addressService.getAddressId(client.getAddress())+"',email= '" +client.getEmail()
                + "',tel_no='" + client.getTelephoneNumber()
                + "',client_type='" +client.getClientType() + "' WHERE ID_client = "+client.getClientId()+";");
    }
}




