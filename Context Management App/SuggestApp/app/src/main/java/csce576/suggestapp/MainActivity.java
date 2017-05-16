package csce576.suggestapp;

/**
 * Created by tsarkar on 23/04/17.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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
import csce576.asynctasks.ParkingAsyncTask;
import csce576.beans.Parking_Bean;
import csce576.beans.RestaurantBean;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback,AsyncResponse1,
        GoogleApiClient.ConnectionCallbacks,GoogleMap.InfoWindowAdapter,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

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
    TextView txt_addvalue;
    TextView txt_title;
    TextView txt_adv;
    ImageView img_weather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("% in on create");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btn_getSugg = (Button)findViewById(R.id.btn_getSugg);
        txt_title = (TextView)findViewById(R.id.txt_title);
        txt_locationheader = (TextView)findViewById(R.id.txt_locationheader);
        txt_locationvalue = (TextView)findViewById(R.id.txt_locationvalue);
        txt_tempheader = (TextView)findViewById(R.id.txt_tempheader);
        txt_addvalue = (TextView)findViewById(R.id.txt_addvalue);
        txt_adv = (TextView)findViewById(R.id.txt_adv);
        img_weather = (ImageView)findViewById(R.id.img_weather);

        txt_locationheader.setText("You have booked Parking Spot D");
        txt_tempheader.setText("You are currently located at");
        txt_addvalue.setText(" ");

        /*txt_adv.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                System.out.println("in txt_adv clicked");
                *//*Intent intent = new Intent(MainScreen_Activity.this, AdvancedScreenActivity.class);
                intent.putExtra("lat",Double.toString(latitude));
                intent.putExtra("lon", Double.toString(longitude));
                startActivity(intent);*//*
            }
        });
*/
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

                ParkingAsyncTask parkingAsyncTask = new ParkingAsyncTask(
                        MainActivity.this);
                parkingAsyncTask.delegate = MainActivity.this;
                parkingAsyncTask.execute();



            }
        });

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
//            ArrayList<String> add_weather_params = new ArrayList<String>();
            HashMap add_weather_params = new HashMap();
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
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
            txt_addvalue.setText(exact_locn +" "+ city);
            /*if(country.equals("USA"))
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

//                add_weather_params.add(city);
//                add_weather_params.add(state);
//                add_weather_params.add(country);
            }*/


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
        LinearLayout infoView = new LinearLayout(MainActivity.this);
        LinearLayout.LayoutParams infoViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        infoView.setOrientation(LinearLayout.HORIZONTAL);
        infoView.setLayoutParams(infoViewParams);

        ImageView infoImageView = new ImageView(MainActivity.this);
        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        Drawable drawable = getResources().getDrawable(android.R.drawable.ic_dialog_map);
        infoImageView.setImageDrawable(drawable);
        infoView.addView(infoImageView);


        System.out.println("snippet is "+marker.getSnippet());
        System.out.println("title is "+marker.getTitle());

        LinearLayout subInfoView = new LinearLayout(MainActivity.this);
        LinearLayout.LayoutParams subInfoViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subInfoView.setOrientation(LinearLayout.VERTICAL);
        subInfoView.setLayoutParams(subInfoViewParams);

        TextView subInfoLat = new TextView(MainActivity.this);
        subInfoLat.setText(marker.getTitle());
        TextView subInfoLnt = new TextView(MainActivity.this);
        subInfoLnt.setText(marker.getSnippet());
        subInfoView.addView(subInfoLat);
        subInfoView.addView(subInfoLnt);
        infoView.addView(subInfoView);

        return infoView;
    }

    public void processFinish(HashMap output) {

        System.out.println("in process finish");
        System.out.println("size is "+ output.size());



        int size = output.size();

        ArrayList parking_detls = new ArrayList();
        Iterator it = output.entrySet().iterator();
        int i =0 ;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            Parking_Bean pb = (Parking_Bean) pairs.getValue();
            parking_detls.add(i,pb);
            i++;
            it.remove(); // avoids a ConcurrentModificationException
        }


        for(int j =0 ;j < size;j++)
        {

            Parking_Bean pb  = (Parking_Bean)parking_detls.get(j);


            Double lat = (Double)pb.getlat();
            Double lon = (Double)pb.getlon();
//            LatLng latLng = new LatLng(lat,lon);
            LatLng latLng = new LatLng(lon,lat);
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(pb.getParking_id());

//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

            System.out.println("setting marker");
            System.out.println("lat is "+lat);
//                    Integer res_id = (Integer)r.get("res_id");
            System.out.println("lon is "+lon);

            System.out.println("address is "+pb.getAddress());
//                    Integer res_id = (Integer)r.get("res_id");
            System.out.println("parking_id is "+pb.getParking_id());

            if(pb.isOccupied() == true)
            {
                markerOptions.snippet(pb.getAddress());
                markerOptions.title("Your booking "+pb.getParking_id());

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            else if(pb.isOccupied() == false)
            {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            }
            restMarker = mMap.addMarker(markerOptions);
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
//            restMarker = mMap.addMarker(markerOptions);
           /* mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
            {

                @Override
                public boolean onMarkerClick(Marker marker) {
//                    System.out.println(" marker.getTitle() "+    marker.getTitle());

//                    System.out.println(" pb.getLatitude() "+   marker.getPosition().latitude);
//                    System.out.println(" pb.getLongitude() "+  marker.getPosition().longitude);

                    marker.showInfoWindow();
//                  Toast.makeText(MainScreen_Activity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                    return true;


                }

            });
*/
        }

    }

    @Override
    public void processFinish2(HashMap output) {

    }

    @Override
    public void processFinish3(HashMap output) {

    }

    @Override
    public void processFinish4(HashMap output) {

    }
}
