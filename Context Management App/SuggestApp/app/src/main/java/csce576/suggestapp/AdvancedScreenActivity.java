package csce576.suggestapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import csce576.asynctasks.AsyncResponse1;
import csce576.asynctasks.AsyncResponse2;
import csce576.asynctasks.Cuisines_AsyncTask;
import csce576.asynctasks.Establishments_AsyncTask;
import csce576.asynctasks.FetchResultsAsyncTask;
import csce576.beans.CuisineBean;
import csce576.beans.EstablishmentBean;
import csce576.beans.RestaurantBean;

/**
 * Created by tsarkar on 07/04/17.
 */
public class AdvancedScreenActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,AsyncResponse2,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, AdapterClickItems {


    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker restMarker;
    LocationRequest mLocationRequest;
    HashMap<String, String> params_Establishments_AsyncTask;
    double lat;
    double lon;
    TextView txt_filter;
    TextView txt_search;
    TextView txt_adv;
    TextView txt_title;
    TextView txt_filter_header;
    TextView tv_est_fil_list_header;
    TextView tv_cuisines_filter_list_header;
    ArrayList est_list;
    ArrayList cuisine_list;
    Dialog dialog;
    LinearLayout cust_dialog_text_header;
    ArrayList est_filter_string;
    ArrayList cuisine_filter_string;
    Typeface type1;
    Typeface type2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_advanced);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        lon = Double.parseDouble(getIntent().getStringExtra("lon"));
        System.out.println("latitude is " + lat);
        System.out.println("longitude is " + lon);
        type1 = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Regular.ttf");

        est_filter_string = new ArrayList<EstablishmentBean>();
        cuisine_filter_string = new ArrayList<CuisineBean>();
        txt_filter = (TextView) findViewById(R.id.txt_filter);
        txt_filter.setTypeface(type1);
        txt_search = (TextView) findViewById(R.id.txt_search);
        txt_search.setTypeface(type1);
        txt_adv  = (TextView) findViewById(R.id.txt_adv);
        txt_adv.setTypeface(type1);
        txt_title = (TextView) findViewById(R.id.txt_title);

         txt_filter_header = (TextView) findViewById(R.id.txt_filter_header);
         tv_est_fil_list_header = (TextView) findViewById(R.id.tv_est_fil_list_header);
         tv_cuisines_filter_list_header = (TextView) findViewById(R.id.tv_cuisines_filter_list_header);

        txt_filter_header.setTypeface(type1);
        tv_est_fil_list_header.setTypeface(type1);
        tv_cuisines_filter_list_header.setTypeface(type1);
        txt_title.setTypeface(type1);


        txt_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txt_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap cuisine_params = new HashMap();
                cuisine_params.put("lat", String.valueOf(lat));
                cuisine_params.put("lon", String.valueOf(lon));

                params_Establishments_AsyncTask = new HashMap();

                params_Establishments_AsyncTask.put("lat", String.valueOf(lat));
                params_Establishments_AsyncTask.put("lon", String.valueOf(lon));

                Cuisines_AsyncTask asyncTask = new Cuisines_AsyncTask(AdvancedScreenActivity.this);
                asyncTask.delegate = AdvancedScreenActivity.this;
                asyncTask.execute(cuisine_params);
            }
        });
        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                HashMap fetch_results_params = new HashMap();
                fetch_results_params.put("lat", String.valueOf(lat));
                fetch_results_params.put("lon", String.valueOf(lon));
                StringBuffer sb_est = new StringBuffer();
                StringBuffer sb_cuisine = new StringBuffer();


                String cuisines = "";
                String ests = "";
                String q_param = "";
                System.out.println("est_filter_string size > " + est_filter_string.size());
                System.out.println("cuisine_filter_string size > " + cuisine_filter_string.size());

                if (cuisine_filter_string.size() != 0) {
                    for (int i = 0; i < cuisine_filter_string.size(); i++) {
                        CuisineBean cb = (CuisineBean) cuisine_filter_string.get(i);
                        sb_cuisine.append(cb.getCuisine_name() + ",");

                    }
                    System.out.println("sb_cuisine > " + sb_cuisine);
                    cuisines = new String(sb_cuisine);
                    cuisines = cuisines.substring(0, cuisines.lastIndexOf(","));
                    System.out.println("cuisines > " + cuisines);
                    q_param = q_param + cuisines;
                }
                if (est_filter_string.size() != 0) {
                    for (int i = 0; i < est_filter_string.size(); i++) {
                        EstablishmentBean eb = (EstablishmentBean) est_filter_string.get(i);
                        sb_est.append(eb.getEst_name() + ",");
                    }
                    System.out.println("sb_est > " + sb_est);
                    ests = new String(sb_est);
                    ests = ests.substring(0, ests.lastIndexOf(","));
                    System.out.println("ests > " + ests);
                    q_param = q_param + ests;
                }

                fetch_results_params.put("q", q_param);
                System.out.println("q_param > " + q_param);
                /*
                if(!(cuisines.length()!=0 || ests.length()!=0))
                {
                    System.out.println("in if condition");

                }
*/

                FetchResultsAsyncTask fr_asynctask = new FetchResultsAsyncTask(AdvancedScreenActivity.this);
                fr_asynctask.delegate = AdvancedScreenActivity.this;
                fr_asynctask.execute(fetch_results_params);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("on location changed called");
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            System.out.println("mCurrLocationMarker is not null");
            mCurrLocationMarker.remove();
            System.out.println("mCurrLocationMarker removed");
        }
        System.out.println("Setting marker");

        //Place current location marker
        //        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng latLng = new LatLng(lat, lon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }


    @Override
    public void processFinish5(ArrayList output) {
        cuisine_list = output;
        System.out.println("<< arraylist size is " + cuisine_list.size());
        String lat = (String) params_Establishments_AsyncTask.get("lat");
        String lon = (String) params_Establishments_AsyncTask.get("lon");
        System.out.println("lat " + lat + " // " + "lon " + lon);
        Establishments_AsyncTask asyncTask = new Establishments_AsyncTask(AdvancedScreenActivity.this);
        asyncTask.delegate = AdvancedScreenActivity.this;
        //        asyncTask.setParams(params_Establishments_AsyncTask);
        asyncTask.execute(params_Establishments_AsyncTask);
    }

    @Override
    public void processFinish6(ArrayList output) {

        // display dialog.
        // prepare dialog
        // show filters in dialog
        // fetch values from the filetr in dialog
        est_list = output;
        AlertDialog.Builder adb = new AlertDialog.Builder(AdvancedScreenActivity.this);
        View view = getLayoutInflater().inflate(R.layout.customdialog_filter, null);

        //        Dialog dialog = adb.setView(new View(AdvancedScreenActivity.this)).create();
        dialog = adb.setView(view).create();

        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        dialog.getWindow().setAttributes(lp);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        //      dialog.setTitle("Search Criteria Filters");
        TextView text = (TextView) view.findViewById(R.id.cust_dialog_text_header);
//        text.setText("Search Criteria Filters");


            /*final Dialog dialog = new Dialog(AdvancedScreenActivity.this);
            dialog.setContentView(R.layout.customdialog_filter);
            dialog.setTitle("Search Criterias");*/

        // set the custom dialog components - text, image and button
            /*TextView text = (TextView) dialog.findViewById(R.id.txt_header);
            text.setText("Narrow down your choices");
            ImageView image = (ImageView) dialog.findViewById(R.id.image);
            image.setImageResource(R.drawable.);*/

        ListView establishment_list = (ListView) view.findViewById(R.id.establishment_list);
        ListView cuisines_list = (ListView) view.findViewById(R.id.cuisines_list);

        CuisineAdapter cus_adapter = new CuisineAdapter(AdvancedScreenActivity.this, cuisine_list, AdvancedScreenActivity.this);
        cuisines_list.setAdapter(cus_adapter);

        EstablishmentAdapter est_adapter = new EstablishmentAdapter(AdvancedScreenActivity.this, est_list, AdvancedScreenActivity.this);
        establishment_list.setAdapter(est_adapter);

        Button dialogButton = (Button) view.findViewById(R.id.btn_done);


        final ListView cuisines_filter_list = (ListView) findViewById(R.id.cuisines_filter_list);
        final ListView est_fil_list = (ListView) findViewById(R.id.est_fil_list);

//        setListViewHeightBasedOnChildren(cuisines_filter_list);
//        setListViewHeightBasedOnChildren(est_fil_list);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CuisineChildAdapter cus_adapter = new CuisineChildAdapter(AdvancedScreenActivity.this, cuisine_filter_string, AdvancedScreenActivity.this);
                cuisines_filter_list.setAdapter(cus_adapter);


                EstablishmentChildAdapter est_adapter = new EstablishmentChildAdapter(AdvancedScreenActivity.this, est_filter_string, AdvancedScreenActivity.this);
                est_fil_list.setAdapter(est_adapter);
                txt_search.setEnabled(true);
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void addEstablishments(EstablishmentBean eb) {
//        System.out.println("adding item");
//        est_filter_string.add(eb.getEst_name());
        est_filter_string.add(eb);
    }


    public void removeEstablishments(EstablishmentBean eb) {

        Iterator itr = est_filter_string.iterator(); // remove all even numbers while (itr.hasNext()) { Integer number = itr.next(); if (number % 2 == 0) { numbers.remove(number); } }

        while (itr.hasNext()) {
            EstablishmentBean bean = (EstablishmentBean) itr.next();
            if (bean.getEst_name().equals(eb.getEst_name())) {
                itr.remove();
            }
        }


    }

    public void addCuisines(CuisineBean cb) {
        cuisine_filter_string.add(cb);
    }

    public void removeCuisines(CuisineBean cb) {
        Iterator itr = cuisine_filter_string.iterator(); // remove all even numbers while (itr.hasNext()) { Integer number = itr.next(); if (number % 2 == 0) { numbers.remove(number); } }


        while (itr.hasNext()) {
            CuisineBean bean = (CuisineBean) itr.next();
            if (bean.getCuisine_name().equals(cb.getCuisine_name())) {
                itr.remove();
            }
            System.out.println("cuisine_filter_string is " + cuisine_filter_string.size());
            for (int i = 0; i < cuisine_filter_string.size(); i++) {
                CuisineBean c_bean = (CuisineBean) cuisine_filter_string.get(i);
                System.out.println("cb.getCuisine_name() is " + c_bean.getCuisine_name());
            }
        }

    }





    public void processFinish7(ArrayList output) {
        for (int j = 0; j < output.size(); j++) {

            RestaurantBean rb = (RestaurantBean) output.get(j);

            System.out.println(" rb.getNAme() is " + rb.getName());
            System.out.println(" rb.getAddress() is " + rb.getAddress());
            Double lat = Double.parseDouble(rb.getLatitude());
            Double lon = Double.parseDouble(rb.getLongitude());
            LatLng latLng = new LatLng(lat, lon);
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(rb.getName());
            markerOptions.snippet(rb.getAddress());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            restMarker = mMap.addMarker(markerOptions);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {
//                    System.out.println(" marker.getTitle() "+    marker.getTitle());

//                    System.out.println(" rb.getLatitude() "+   marker.getPosition().latitude);
//                    System.out.println(" rb.getLongitude() "+  marker.getPosition().longitude);

                    marker.showInfoWindow();
//                  Toast.makeText(MainScreen_Activity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                    return true;


                }

            });

        }

}
}