package sk.upjs.ics.android.locationr;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class CurrentLocationAsyncTask extends AsyncTask<Location, Void, Address> {
    private static final String TAG = CurrentLocationAsyncTask.class.getName();

    public static final Address NO_ADDRESS = null;

    private final WeakReference<Activity> activityReference;

    private int targetTextViewId;

    public CurrentLocationAsyncTask(Activity activity, @IdRes int targetTextViewId) {
        this.targetTextViewId = targetTextViewId;
        this.activityReference = new WeakReference<Activity>(activity);
    }

    @Override
    protected Address doInBackground(Location... locations) {
        try {
            if (!Geocoder.isPresent()) {
                return NO_ADDRESS;
            }
            if (locations.length != 1) {
                Log.e(TAG, "Illegal number of parameters, expecting 1, but found " + locations.length);
                return NO_ADDRESS;
            }
            Location location = locations[0];

            Context context = activityReference.get();
            if (context == null) {
                return NO_ADDRESS;
            }


            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address currentAddress = addresses.get(0);
            return currentAddress;
        } catch (IOException e) {
            Log.e(TAG, "Geocoder failed for location", e);
        }
        return NO_ADDRESS;
    }

    @Override
    protected void onPostExecute(Address currentAddress) {
        Activity activity = activityReference.get();
        if (activity == null) {
            return;
        }
        View targetView = activity.findViewById(this.targetTextViewId);
        if (targetView == null) {
            return;
        }
        if (!(targetView instanceof TextView)) {
            throw new ClassCastException("Target view must be TextView, but is " + targetView.getClass());
        }
        TextView targetTextView = (TextView) targetView;
        targetTextView.setText("Now at " + currentAddress.getLocality());
    }
}