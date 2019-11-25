package service;

import model.Address;
import utils.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static utils.Database.DB_NAME;

public class AddressService implements IAddressService {

    /**
     * Queries a database and returns a specific address
     *
     * @param addressId   of the address to be returned
     * @return address
     */
    public Address getAddressById(int addressId) throws SQLException {
        Statement statement = Database.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT co.country, ci.city, ci.postcode, ad.street FROM "
                + DB_NAME + ".country AS co" + " LEFT JOIN " + DB_NAME + ".city AS ci ON co.ID_country = ci.ID_country "
                + "AND ci.city IS NOT NULL LEFT JOIN " + DB_NAME +".address AS ad ON ci.ID_city = ad.ID_city "
                + "AND ID_address ='" + addressId
                + "' WHERE ad.street IS NOT NULL;"
        );

        Address address = null;
        if (resultSet.next()) {
            address = new Address(
                    resultSet.getString("street"),
                    resultSet.getString("city"),
                    resultSet.getString("postcode"),
                    resultSet.getString("country")
            );
        }

        return address;
    }

    /**
     * Queries a database and returns an id of the specific address
     *
     * @param address   of which id will be returned
     * @return address
     */
    public int getAddressId(Address address) throws SQLException {
        Statement statement = Database.getConnection().createStatement();

        ResultSet rs = statement.executeQuery("SELECT ad.ID_address FROM " + DB_NAME
                + ".address AS ad INNER JOIN " +DB_NAME + ".city AS ci ON ci.ID_city = ad.ID_city AND ci.postcode = '"
                + address.getZipCode() + "' AND ad.street='"+address.getStreet()+"';");
        int addressID = 0;
        if(!rs.next()) //if rs.next() is false there are no rows
        {
                statement.executeUpdate("INSERT INTO " + DB_NAME +".country (country) SELECT * FROM (SELECT'" +address.getCountry()+"') AS tmp "
                + "WHERE NOT EXISTS (SELECT country FROM " + DB_NAME + ".country WHERE country ='" + address.getCountry()+"') LIMIT 1;");

                rs = statement.executeQuery("SELECT ID_country FROM " + DB_NAME +".country WHERE country = '" + address.getCountry() + "';");
                int countryID = 0;
                if(rs.next())
                {
                    countryID = rs.getInt("ID_country");
                }
                statement.executeUpdate("INSERT INTO " + DB_NAME +".city (ID_country,city,postcode) SELECT * FROM (SELECT'"+ countryID
                        + "' AS COL1,'" + address.getCity()+ "' AS COL2,'" +address.getZipCode() + "' AS COL3) AS tmp "
                        + "WHERE NOT EXISTS (SELECT postcode FROM " + DB_NAME + ".city WHERE postcode ='"
                        + address.getZipCode()+"' AND city = '"+address.getCity()+"') LIMIT 1;");
                rs = statement.executeQuery("SELECT ID_city FROM " + DB_NAME +".city WHERE postcode = '" + address.getZipCode() + "' AND city = '" + address.getCity() + "';");
                int cityID = 0;
                if(rs.next())
                {
                    cityID = rs.getInt("ID_city");
                }
                int result = statement.executeUpdate("INSERT INTO "+ DB_NAME+".address (ID_city, street) VALUES ('" + cityID + "', '" +address.getStreet()+"');");

                if(result != 0)
                {
                    rs = statement.executeQuery("SELECT ad.ID_address FROM " + DB_NAME
                            + ".address AS ad INNER JOIN " +DB_NAME + ".city AS ci ON ci.ID_city= ad.ID_city AND ci.postcode = '"
                            + address.getZipCode() + "' AND ad.street='"+address.getStreet()+"';");
                    if(rs.next())
                    {
                        addressID = rs.getInt("ID_address");
                    }
                }
                else
                {
                    System.out.println("Problem with inserting new address to database");
                }

        }
        else
        {
            addressID = rs.getInt("ID_address");
        }

        return addressID;
    }
}
