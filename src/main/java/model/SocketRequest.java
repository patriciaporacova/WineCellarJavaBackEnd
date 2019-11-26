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
        GET_SENSOR_DATA,

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
