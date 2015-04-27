package sk.upjs.ics.android.locationr;

import java.util.Arrays;
import java.util.List;

public class DefaultCityLocationService {

    public List<CityLocation> list() {
        return Arrays.asList(
                new CityLocation("Košice", 48.697265, 21.2644253429128),
                new CityLocation("Prešov", 48.997631, 21.2401873),
                new CityLocation("Bratislava", 48.1535383, 17.1096711)
        );
    }
}
