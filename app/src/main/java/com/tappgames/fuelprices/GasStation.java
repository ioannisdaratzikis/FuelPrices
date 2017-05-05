package com.tappgames.fuelprices;

/**
 * Created by johndaratzikis on 25/03/2017.
 */

public class GasStation {

    private String name;
    private String address;
    private String price;

    public GasStation(String name, String address, String price) {
        this.name = name;
        this.address = address;
        this.price = price;
    }

    public GasStation() {
        this.name = "";
        this.address = "";
        this.price = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
