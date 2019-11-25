package model;

import service.IAddressService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Contractor extends AbstractClient
{
    public Contractor(){}

    public Contractor(String clientId, Address address, String companyName, String email, String telephoneNumber,
                      String clientType, String passwordHash)
    {
        super(clientId, address, companyName, email, telephoneNumber, clientType, passwordHash);
    }

    @Override
    public Contractor populateFromResultSet(ResultSet resultSet, IAddressService addressService) throws SQLException {
        Contractor contractor = new Contractor(
            resultSet.getString("ID_client"),
            addressService.getAddressById(resultSet.getInt("ID_address")),
            resultSet.getString("company_name"),
            resultSet.getString("email"),
            resultSet.getString("tel_no"),
            resultSet.getString("client_type"),
            resultSet.getString("password_hash")
        );

        return contractor;
    }

}
