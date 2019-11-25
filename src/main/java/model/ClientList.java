package model;

import java.util.ArrayList;
import java.util.List;

public class ClientList
{
    private List<AbstractClient> clients;

    public ClientList() {
        clients = new ArrayList<>();
    }

    public List<AbstractClient> getClients() {
        return clients;
    }
}