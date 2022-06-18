package com.firstapp.fa_mohitkumarbhagi_c0851229_android;

public class AddressBean
{
    private int id;
    private String placename;
    private String address;
    private String latitude;
    private String longitude;
    private String visited;
    private String createdon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getVisited() {
        return visited;
    }

    public void setVisited(String visited) {
        this.visited = visited;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }
}
