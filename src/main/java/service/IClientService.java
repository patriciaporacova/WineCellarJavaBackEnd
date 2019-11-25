package service;

import model.AbstractClient;
import model.ClientList;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IClientService {

    ClientList getAllClients() throws SQLException;
    ClientList getCustomers() throws SQLException;
    ClientList getContractors() throws SQLException;
    AbstractClient getClientById(String clientId) throws SQLException;
    AbstractClient populateClient(ResultSet resultSet) throws SQLException;
    void deleteClient(String id) throws  SQLException;
    void updateClient(AbstractClient client) throws  SQLException;
    void registerClient(AbstractClient client) throws SQLException;
}
