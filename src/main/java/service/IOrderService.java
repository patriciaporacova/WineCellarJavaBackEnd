package service;

import model.Order;
import model.OrderList;

import java.sql.SQLException;

public interface IOrderService {
    Order getOrderById(String orderNumber) throws SQLException;
    OrderList getUnassignedOrders() throws SQLException;
    OrderList getAllOrders() throws SQLException;
    OrderList getAssignedOrders() throws SQLException;
    OrderList getMyOrders(String clientId) throws SQLException;
    OrderList getOrdersByStatus() throws SQLException;
    OrderList getAllOrdersOrderByDeadline() throws SQLException;
    OrderList getMyOrdersByStatus(String clientId) throws SQLException;
    OrderList getMyOrdersByDeadline(String clientId) throws SQLException;
    void addOrder(Order order) throws SQLException;
    void updateOrder(Order order) throws SQLException;
    void acceptOrder(String orderNumber, String companyId) throws  SQLException;
    void deleteOrder(String orderNumber) throws SQLException;
    void deleteUnsignedOrder(String orderNumber) throws SQLException;
    void refuseTakenOrder(String orderNumber) throws  SQLException;
    void updateStatus(String orderNumber, String updateType) throws SQLException;

}
