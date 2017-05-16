package csce576.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import csce576.jsonparser.JsonParser;

/**
 * Created by tsarkar on 26/04/17.
 */
public class ParkingAsyncTask extends AsyncTask<Void, Void, String> {

        String fetchparkingSpotsURL = "http://10.231.243.14:8080/test_jsp/parking/webservice/parkinglocations  http://tsarkarwebservice.ddns.net/test_jsp/parking/webservice/parkinglocations";
//    String fetchparkingSpotsURL = "http://192.168.43.64:8080/test_jsp/parking/webservice/parkinglocations";
//    http://10.231.243.14:8080/test_jsp/parking/webservice/parkinglocations
    private Context mContext;
    ProgressDialog mProgress;
    public AsyncResponse1 delegate = null;
    InputStream in = null;
    String weather_url = "";//AppConstants.login_url;

    public ParkingAsyncTask(Context context) {
        this.mContext = context;
        System.out.println("in constructor login asynctask");
    }

    @Override
    protected String doInBackground(Void... voids) {
        System.out.println("fetch parking spots url is " + fetchparkingSpotsURL);
        String response = "";
        try {
            //        String webserviceURL = "http://tsarkarwebservice.ddns.net/RestfulWebServiceCA/contextaware/webservice/ccstationlocations";
            String webserviceURL = "http://tsarkarwebservice.ddns.net/test_jsp/parking/webservice/parkinglocations";
//            String webserviceURL =  "http://10.231.243.14:8080/test_jsp/parking/webservice/parkinglocations";

            //        192.168.43.198  //192.168.1.106  140609
            InputStream in = null;
            String result = "";
            System.out.println("++ In new connection function");

            URL url = new URL(webserviceURL);
            System.out.println("++ hello 1");
            URLConnection connection = url.openConnection();
            System.out.println("++ hello 2");
            HttpURLConnection httpConnection = (HttpURLConnection) connection;

            System.out.println("++ hello 3");
            System.out.println("++ url is " + connection.getURL());


            System.out.println("++ hello 3");
            httpConnection.setRequestMethod("GET");
            httpConnection.setDoInput(true);
            //            httpConnection.setDoOutput(true);
            System.out.println("++ hello 4");
            //            httpConnection.setInstanceFollowRedirects( false );
            System.out.println("++ hello 5");
            connection.setDoInput(true);
            httpConnection.setDoOutput(true);
            System.out.println("++ hello 6");
            //            httpConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            System.out.println("++ hello 7");
            //            httpConnection.setRequestProperty( "charset", "utf-8");
            System.out.println("++ hello 8");
            //  			httpConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

            httpConnection.setUseCaches(false);

            System.out.println("++ hello 9");
            System.out.println("++ final url is " + connection.getURL());


            httpConnection.setRequestProperty("Content-Type", "application/plain");
            //			httpConnection.setRequestProperty ("user-key", new String(URLEncoder.encode("a472f0195e685a4eeae803aef02d0d4a")));
            //                httpConnection.setRequestProperty ("user-key", "a472f0195e685a4eeae803aef02d0d4a");
            httpConnection.setRequestProperty("Content-Language", "en-US");
            //                httpConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            //		    httpConnection.setConnectTimeout(5000);


            in = httpConnection.getInputStream();
            System.out.println("++ hello 8");
            int responseCode = httpConnection.getResponseCode();

            System.out.println("++ hello 9");
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("++ respone code is ok");
                StringBuilder sb = new StringBuilder("");
                // int line = 0;
                int length = httpConnection.getContentLength();
                System.out.println("++ Length = " + length);

                int i;
                do {
                    i = in.read();
                    if (i != -1) {
                        // System.out.println("Character >>> "+ (char)i);
                        sb.append((char) i);
                    }
                } while (i != -1);

                response = new String(sb);
                in.close();
                // result = sb.toString();
                System.out.println("++ response is !!!" + response);
                // return result;
            }
        } catch (Exception e) {

            e.printStackTrace();
            response = "failure";

            System.out.println("++ Exception e is " + e);
            //timeout = true;

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Final response....." + response);
        return response;
    }

    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;


        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode((String) pairs.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) pairs.getValue(), "UTF-8"));
            it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("result in url parameter query formation is " + result);
        return result.toString();
    }




    @Override
    protected void onPreExecute() {
        System.out.println("in on preexecute");
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Fetching Parking Spots");
        mProgress.setCancelable(true);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
    }

    @Override
    protected void onPostExecute(String response) {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
            mProgress = null;
        }
        System.out.println("+ result is " + response);

        if (response.equals("Exception")) {
            Toast.makeText(mContext, "Sorry some error occurred",
                    Toast.LENGTH_LONG).show();
        } else {
            System.out.println("++ result is " + response);
            JsonParser json_parser = new JsonParser();
            HashMap parking_detls = json_parser.parse_parkingresults(response);
            int size = parking_detls.size();
            System.out.println("size of returned hashmap is >> "+ size);
                        delegate.processFinish(parking_detls);
            //Intent login_activity = new Intent(mContext, StoreListScreenActivity.class);
            //mContext.startActivity(login_activity);
        }
    }

}

