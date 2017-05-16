package csce576.suggestapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import csce576.asynctasks.AsyncResponse1;
import csce576.asynctasks.Entity_AsyncTask;
import csce576.asynctasks.Restaurants_near_LocationAsyncTask;
import csce576.asynctasks.TopLocationsAsyncTask;
import csce576.asynctasks.WeatherAsyncTask;
import csce576.beans.RestaurantBean;

public class MainScreen_Activity extends FragmentActivity implements AsyncResponse1, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,GoogleMap.InfoWindowAdapter,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker restMarker;
    LocationRequest mLocationRequest;
    double latitude  ;
    double longitude ;
    String country = "";
    String city = "";
    String state = "";
    String exact_locn = "";
    String pincode = "";
    Button btn_getSugg;
    TextView txt_locationheader;
    TextView txt_locationvalue;
    TextView txt_tempheader;
    TextView txt_tempvalue;
    TextView txt_title;
    TextView txt_adv;
    ImageView img_weather;
    Typeface type1;
    Typeface type2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        System.out.println("% in on create");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        type1 = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Regular.ttf");

        btn_getSugg = (Button)findViewById(R.id.btn_getSugg);
        txt_title = (TextView)findViewById(R.id.txt_title);
        txt_locationheader = (TextView)findViewById(R.id.txt_locationheader);
        txt_locationvalue = (TextView)findViewById(R.id.txt_locationvalue);
        txt_tempheader = (TextView)findViewById(R.id.txt_tempheader);
        txt_tempvalue = (TextView)findViewById(R.id.txt_tempvalue);
        txt_adv = (TextView)findViewById(R.id.txt_adv);
        img_weather = (ImageView)findViewById(R.id.img_weather);
//android:background="#3b9fc6"
        txt_locationheader.setText("You are currently located at");
        txt_tempheader.setText("Current Weather is");
        txt_tempvalue.setText("Temperature is ");
        txt_title.setTypeface(type1);
        txt_locationheader.setTypeface(type1);
        txt_locationvalue.setTypeface(type1);
        txt_tempheader.setTypeface(type1);
        txt_tempvalue.setTypeface(type1);
        txt_adv.setTypeface(type1);

        txt_adv.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                System.out.println("in txt_adv clicked");
                Intent intent = new Intent(MainScreen_Activity.this, AdvancedScreenActivity.class);
                intent.putExtra("lat",Double.toString(latitude));
                intent.putExtra("lon", Double.toString(longitude));
                startActivity(intent);
            }
        });

//        txt_adv.setTypeface(type1);
        btn_getSugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("Button is clicked >> ");
//                System.out.println("latitude >> is "+ latitude);
//                System.out.println("longitude >> is "+ longitude);

                // upload all context values to server

//                HashMap<String,String> params = new HashMap<String,String>();
//                params.put("lat", "30.214003");
//                params.put("lon", "-92.0496332");

                HashMap<String,String> params = new HashMap<String,String>();
                System.out.println("query is >> "+ exact_locn+" "+city+" "+state);
                params.put("query", exact_locn+" "+city+" "+state);
                params.put("lat", Double.toString(latitude));
                params.put("lon", Double.toString(longitude));
                Entity_AsyncTask fetch_resulttaask = new Entity_AsyncTask(MainScreen_Activity.this);
                fetch_resulttaask.delegate = MainScreen_Activity.this;
                fetch_resulttaask.execute(params);


            }
        });
        btn_getSugg.setTypeface(type1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("% in on start");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services

        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(this);

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
     latitude  = location.getLatitude();
     longitude = location.getLongitude();
     System.out.println("latitude > is "+ latitude);
     System.out.println("longitude > is "+ longitude);
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mCurrLocationMarker = mMap.addMarker(markerOptions);


        //start the aynctasks
    try {
        ArrayList<String> add_weather_params = new ArrayList<String>();
        Geocoder geocoder = new Geocoder(MainScreen_Activity.this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        System.out.println("size of addresses is > "+ addresses.size());
        Address add = (Address)addresses.get(0);
        System.out.println(add.getAddressLine(0)+" >> "+
                add.getAddressLine(1) +" >>> "+add.getAddressLine(2));
        exact_locn = add.getAddressLine(0);
        String city_state = add.getAddressLine(1);

        country = add.getAddressLine(2);
        city = "";
        state ="";

        if(country.equals("USA"))
        {
            city = city_state.substring(0,city_state.indexOf(','));


            state= city_state.substring(city_state.indexOf(',')).trim();
            state= state.substring(state.indexOf(' '),state.lastIndexOf(' ')).trim();
            System.out.println("city is >> "+ city);
            System.out.println("state is >> "+ state);
            System.out.println("country is >> "+ country);
            pincode = city_state.substring(city_state.lastIndexOf(','));
            System.out.println("$ pincode is >> "+ pincode);
            add_weather_params.add(city);
            add_weather_params.add(state);
            add_weather_params.add(country);
        }
        else{
            city = city_state.substring(0,city_state.indexOf(','));


            city = city_state.substring(0,city_state.indexOf(','));
            state= city_state.substring(city_state.indexOf(',')).trim();
            state= state.substring(state.indexOf(' '),state.lastIndexOf(' ')).trim();
            System.out.println("city is >> "+ city);
            System.out.println("state is >> "+ state);
            System.out.println("country is >> "+ country);

            add_weather_params.add(city);
            add_weather_params.add(state);
            add_weather_params.add(country);
        }
        WeatherAsyncTask weatherAsyncTask = new WeatherAsyncTask(
                MainScreen_Activity.this);
        weatherAsyncTask.delegate = MainScreen_Activity.this;

        weatherAsyncTask.execute(add_weather_params);

        HashMap<String,String> params = new HashMap<String,String>();
        params.put("lat", Double.toString(latitude));
        params.put("lon", Double.toString(longitude));
        TopLocationsAsyncTask fetch_resulttaask = new TopLocationsAsyncTask(MainScreen_Activity.this);
        fetch_resulttaask.delegate = MainScreen_Activity.this;
        fetch_resulttaask.execute(params);

    }
    catch(Exception e)
    {
        e.printStackTrace();
    }



        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        }


public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
public boolean checkLocationPermission(){
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
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

    @Override
    public void processFinish(HashMap output) {

        System.out.println(">> hashmap size is " + output.size());
        /*Iterator it = output.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println("%% "+pairs.getKey() + " = " + pairs.getValue());


            it.remove(); // avoids a ConcurrentModificationException
        }*/
        String weather = (String)output.get("weather");
        String temp_c = (String)output.get("temp_c");
        txt_locationvalue.setText(city + " " + state + " "+country);
        System.out.println(">> "+txt_tempheader.getText());
        System.out.println(">> "+txt_tempvalue.getText());
        System.out.println(">> "+weather);
        System.out.println(">> "+temp_c);

        txt_tempheader.setText(txt_tempheader.getText() +" "+ weather);
        txt_tempvalue.setText(txt_tempvalue.getText() + temp_c +"˚C");
        txt_title.setText(exact_locn + pincode);


    }


    @Override
    public void processFinish2(HashMap output) {
    System.out.println("in process finish 2");

        ArrayList rest_detls = new ArrayList();
        Iterator it = output.entrySet().iterator();
        int i =0 ;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            RestaurantBean rb = (RestaurantBean) pairs.getValue();
            rest_detls.add(i,rb);
            i++;
            it.remove(); // avoids a ConcurrentModificationException
        }

        for(int j =0 ;j<rest_detls.size();j++)
        {

            RestaurantBean rb  = (RestaurantBean)rest_detls.get(j);

            System.out.println(" rb.getNAme() is "+ rb.getName());
            Double lat = Double.parseDouble(rb.getLatitude());
            Double lon = Double.parseDouble(rb.getLongitude());
            LatLng latLng = new LatLng(lat,lon);
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(rb.getName());
            markerOptions.snippet(rb.getAddress());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            restMarker = mMap.addMarker(markerOptions);
            System.out.println("** setting marker **");
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
            {

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

    @Override
    public void processFinish3(HashMap output) {


        String entity_type = (String)output.get("entity_type");
        String entity_id = (String)output.get("entity_id");
        System.out.println("entity_type >> "+entity_type);
        System.out.println("entity_id >> "+entity_id);
        Restaurants_near_LocationAsyncTask near_rest_task = new Restaurants_near_LocationAsyncTask(MainScreen_Activity.this);
        near_rest_task.delegate = MainScreen_Activity.this;
        near_rest_task.execute(output);



    }

    @Override
    public void processFinish4(HashMap output) {
        System.out.println("in process finish 4");

        ArrayList rest_detls = new ArrayList();
        Iterator it = output.entrySet().iterator();
        int i =0 ;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            RestaurantBean rb = (RestaurantBean) pairs.getValue();
            rest_detls.add(i,rb);
            i++;
            it.remove(); // avoids a ConcurrentModificationException
        }

        for(int j =0 ;j<rest_detls.size();j++)
        {

            RestaurantBean rb  = (RestaurantBean)rest_detls.get(j);

            System.out.println(" rb.getNAme() is "+ rb.getName());
            Double lat = Double.parseDouble(rb.getLatitude());
            Double lon = Double.parseDouble(rb.getLongitude());
            LatLng latLng = new LatLng(lat,lon);
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(rb.getName());
            markerOptions.snippet(rb.getAddress());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            restMarker = mMap.addMarker(markerOptions);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
            {

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




    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return prepareInfoView(marker);
    }


    private View prepareInfoView(Marker marker){
        //prepare InfoView programmatically
        System.out.println("prepareInfoView is called");
        LinearLayout infoView = new LinearLayout(MainScreen_Activity.this);
        LinearLayout.LayoutParams infoViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        infoView.setOrientation(LinearLayout.HORIZONTAL);
        infoView.setLayoutParams(infoViewParams);

        ImageView infoImageView = new ImageView(MainScreen_Activity.this);
        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        Drawable drawable = getResources().getDrawable(android.R.drawable.ic_dialog_map);
        infoImageView.setImageDrawable(drawable);
        infoView.addView(infoImageView);


        System.out.println("snippet is "+marker.getSnippet());
        System.out.println("title is "+marker.getTitle());

        LinearLayout subInfoView = new LinearLayout(MainScreen_Activity.this);
        LinearLayout.LayoutParams subInfoViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subInfoView.setOrientation(LinearLayout.VERTICAL);
        subInfoView.setLayoutParams(subInfoViewParams);

        TextView subInfoLat = new TextView(MainScreen_Activity.this);
        subInfoLat.setText(marker.getTitle());
        TextView subInfoLnt = new TextView(MainScreen_Activity.this);
        subInfoLnt.setText(marker.getSnippet());
        subInfoView.addView(subInfoLat);
        subInfoView.addView(subInfoLnt);
        infoView.addView(subInfoView);

        return infoView;
    }
}
