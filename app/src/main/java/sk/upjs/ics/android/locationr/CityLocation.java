package sk.upjs.ics.android.locationr;

import android.location.Location;

public class CityLocation {
    public static final String NO_PROVIDER = "";
    private String city;

    private Location location;

    public CityLocation(String city, double latitude, double longitude) {
        this.city = city;

        Location location = new Location(NO_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return this.city;
    }
}
