package model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Order {
    private String orderNumber;
    private String companyID;
    private Address pickUpAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date pickUpDeadline;
    private Address dropOffAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date dropOffDeadline;
    private String contentDescription;
    private String containerSize;
    private double weight;
    private String size;
    private double price;
    private String responsibleCompany;
    private boolean awaitingPickUp;
    private boolean pickedUp;
    private boolean lateDelivery;
    private boolean delivered;
    private double distance;

    public Order() {}

    // Constructor for getting orders (includes orderNumber and responsibleCompany
    public Order(String orderNumber, String companyID, Address pickUpAddress, Date pickUpDeadline,
                 Address dropOffAddress, Date dropOffDeadline, String contentDescription,
                 String containerSize, double weight, String size, double price,boolean awaitingPickUp, boolean pickedUp, boolean delivered,
                 boolean lateDelivery, double distance
    )
    {
        this.distance = distance;
        this.lateDelivery = lateDelivery;
        this.orderNumber = orderNumber;
        this.companyID = companyID;
        this.pickUpAddress = pickUpAddress;
        this.pickUpDeadline = pickUpDeadline;
        this.dropOffAddress = dropOffAddress;
        this.dropOffDeadline = dropOffDeadline;
        this.contentDescription = contentDescription;
        this.containerSize = containerSize;
        this.weight = weight;
        this.price = price;
        this.size = size;
        this.responsibleCompany = null;
        this.awaitingPickUp = awaitingPickUp;
        this.pickedUp = pickedUp;
        this.delivered = delivered;
    }

    // Constructor for creating orders (doesn't include orderNumber and responsibleCompany)
    public Order(String companyID, Address pickUpAddress, Date pickUpDeadline,
                 Address dropOffAddress, Date dropOffDeadline, String contentDescription,
                 String containerSize, double weight, String size, double price, boolean pickedUp, boolean delivered, boolean awaitingPickUp
    )
    {
        this.companyID = companyID;
        this.pickUpAddress = pickUpAddress;
        this.pickUpDeadline = pickUpDeadline;
        this.dropOffAddress = dropOffAddress;
        this.dropOffDeadline = dropOffDeadline;
        this.contentDescription = contentDescription;
        this.containerSize = containerSize;
        this.weight = weight;
        this.price = price;
        this.size = size;
        this.awaitingPickUp = awaitingPickUp;
        this.pickedUp = pickedUp;
        this.delivered = delivered;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public Address getPickUpAddress() {
        return pickUpAddress;
    }

    public void setPickUpAddress(Address pickUpAddress) {
        this.pickUpAddress = pickUpAddress;
    }

    public Date getPickUpDeadline() {
        return pickUpDeadline;
    }

    public String getResponsibleCompany()
    {
        return responsibleCompany;
    }

    public void setResponsibleCompany(String responsibleCompany)
    {
        this.responsibleCompany = responsibleCompany;
    }

    public void setPickUpDeadline(Date pickUpDeadline) {
        this.pickUpDeadline = pickUpDeadline;
    }

    public Address getDropOffAddress() {
        return dropOffAddress;
    }

    public void setDropOffAddress(Address dropOffAddress) {
        this.dropOffAddress = dropOffAddress;
    }

    public Date getDropOffDeadline() {
        return dropOffDeadline;
    }

    public void setDropOffDeadline(Date dropOffDeadline) {
        this.dropOffDeadline = dropOffDeadline;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    public String getContainerSize() {
        return containerSize;
    }

    public void setContainerSize(String containerSize) {
        this.containerSize = containerSize;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isAwaitingPickUp() {
        return awaitingPickUp;
    }

    public void setAwaitingPickUp(boolean awaitingPickUp) {
        this.awaitingPickUp = awaitingPickUp;
    }

    public boolean isLateDelivery()
    {
        return lateDelivery;
    }

    public void setLateDelivery(boolean lateDelivery)
    {
        this.lateDelivery = lateDelivery;
    }

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }
}