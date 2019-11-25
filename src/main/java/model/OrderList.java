package model;

import java.util.ArrayList;
import java.util.List;

public class OrderList {

    private List<Order> orders;

    public OrderList() {
        orders = new ArrayList<Order>();
    }

    public List<Order> getOrders() {
        return orders;
    }
}
