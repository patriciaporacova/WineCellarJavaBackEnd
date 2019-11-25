package model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import service.IAddressService;

import java.sql.ResultSet;
import java.sql.SQLException;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "clientType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Customer.class, name = "customer"),

        @JsonSubTypes.Type(value = Contractor.class, name = "contractor") }
)
public abstract class AbstractClient {
    private String clientId;
    private Address address;
    private String companyName;
    private String email, telephoneNumber;
    private String clientType;
    private String passwordHash;

    public AbstractClient() {}

    public AbstractClient(String clientId, Address address, String companyName, String email, String telephoneNumber,
                          String clientType, String passwordHash)
    {
        this.clientId = clientId;
        this.address = address;
        this.companyName = companyName;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.clientType = clientType;
        this.passwordHash = passwordHash;
    }

    public abstract AbstractClient populateFromResultSet(ResultSet resultSet, IAddressService addressService) throws SQLException;

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }


    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getTelephoneNumber()
    {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber)
    {
        this.telephoneNumber = telephoneNumber;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    public String getClientType()
    {
        return clientType;
    }

    public void setClientType(String clientType)
    {
        this.clientType = clientType;
    }

    public String getPasswordHash()
    {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}