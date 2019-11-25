package service;

import model.Address;

import java.sql.SQLException;

public interface IAddressService {
    Address getAddressById(int addressId) throws SQLException;
    int getAddressId(Address address) throws SQLException;
}
