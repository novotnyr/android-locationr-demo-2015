package sk.upjs.ics.android.locationr;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlResourceCityLocationService {

    public static final String ELEMENT_PLACE = "place";
    public static final String ATTRIBUTE_PLACE_NAME = "name";
    public static final String ATTRIBUTE_PLACE_LATITUDE = "lat";
    public static final String ATTRIBUTE_PLACE_LONGITUDE = "lon";
    private static final String NO_NAMESPACE = null;

    private Resources resources;

    public XmlResourceCityLocationService(Resources resources) {
        this.resources = resources;
    }

    public List<CityLocation> list() {
        List<CityLocation> cityLocations = new ArrayList<>();
        XmlResourceParser xml = this.resources.getXml(R.xml.cities);
        try {
            int event = xml.getEventType();
            while(event != XmlPullParser.END_DOCUMENT) {
                if(event == XmlPullParser.START_TAG && ELEMENT_PLACE.equals(xml.getName())) {
                    String city = xml.getAttributeValue(NO_NAMESPACE, ATTRIBUTE_PLACE_NAME);
                    double lat = Double.parseDouble(xml.getAttributeValue(NO_NAMESPACE, ATTRIBUTE_PLACE_LATITUDE));
                    double lon = Double.parseDouble(xml.getAttributeValue(NO_NAMESPACE, ATTRIBUTE_PLACE_LONGITUDE));

                    CityLocation cityLocation = new CityLocation(city, lat, lon);
                    cityLocations.add(cityLocation);
                }
                event = xml.next();
            }
        } catch (XmlPullParserException e) {
            throw new IllegalStateException("Cannot load city location list", e);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load city location list", e);
        } finally {
            xml.close();
        }
        return cityLocations;
    }
}
