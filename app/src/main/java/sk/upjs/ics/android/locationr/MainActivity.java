package sk.upjs.ics.android.locationr;

import android.app.ActionBar;
import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;


public class MainActivity extends Activity implements LocationListener, ActionBar.OnNavigationListener {

    private static final long TEN_SECONDS = 10 * 1000;

    private static final float ONE_HUNDRED_METERS = 100f;

    private LocationManager locationManager;

    private static final boolean ONLY_ENABLED_LOCATION_PROVIDERS = true;

    private SpinnerAdapter actionBarAdapter;

    private CityLocation selectedCityLocation;

    private TextView distanceTextView;

    private String locationProviderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureActionBarSpinnerAdapter();
        configureActionBar();

        this.distanceTextView = (TextView) findViewById(R.id.distanceTextView);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    private void configureActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(this.actionBarAdapter, this);
    }

    private void configureActionBarSpinnerAdapter() {
        List<CityLocation> cityLocations = new DefaultCityLocationService().list();
        this.actionBarAdapter = new ArrayAdapter<CityLocation>(this, android.R.layout.simple_list_item_1, cityLocations);
        this.selectedCityLocation = cityLocations.get(0); // first city
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        locationProviderName = locationManager.getBestProvider(criteria, ONLY_ENABLED_LOCATION_PROVIDERS);
        locationManager.requestLocationUpdates(locationProviderName, TEN_SECONDS, ONE_HUNDRED_METERS, this);
    }

    @Override
    protected void onPause() {
        locationManager.removeUpdates(this);
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        float distanceInMeters = selectedCityLocation.getLocation().distanceTo(location);

        DecimalFormat distanceFormatter = new DecimalFormat("#.# km");
        this.distanceTextView.setText(distanceFormatter.format(distanceInMeters / 1000));
        updateCurrentLocation(location);
    }

    private void updateCurrentLocation(Location location) {
        new CurrentLocationAsyncTask(this, R.id.currentLocationTextView).execute(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        requestLocationUpdates();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        this.selectedCityLocation = (CityLocation) this.actionBarAdapter.getItem(itemPosition);
        Location lastKnownLocation = this.locationManager.getLastKnownLocation(this.locationProviderName);
        if(lastKnownLocation != null) {
            onLocationChanged(lastKnownLocation);
        } else {
            this.locationManager.requestSingleUpdate(this.locationProviderName, this, getMainLooper());
        }
        return true;
    }

}
