package model;

import java.io.Serializable;

public class SocketRequest implements Serializable {

    private ACTION action;
    private Object obj;

    public SocketRequest(ACTION action, Object obj) {
        this.action = action;
        this.obj = obj;
    }

    public enum ACTION
    {
        GET_ORDERS,
        GET_ORDERS_GROUPBY_DEADLINE,
        GET_ORDERS_GROUPBY_STATUS,
        GET_ORDER_BY_ID,
        GET_ASSIGNED_ORDERS,
        GET_UNASSIGNED_ORDERS,
        GET_CLIENTS,
        UPDATE_ORDER,
        DELETE_ORDER,

        GET_CUSTOMERS,
        GET_CONTRACTORS,
        GET_CLIENT_BY_ID,
        UPDATE_CLIENT,
        DELETE_CLIENT,
        ADD_CLIENT
    }

    public ACTION getAction() {
        return action;
    }

    public void setAction(ACTION action) {
        this.action = action;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }
}
