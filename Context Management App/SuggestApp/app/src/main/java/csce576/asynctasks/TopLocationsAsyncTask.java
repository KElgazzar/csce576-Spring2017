package csce576.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import csce576.jsonparser.JsonParser;

/**
 * Created by tsarkar on 06/04/17.
 */
public class TopLocationsAsyncTask extends AsyncTask<HashMap<String,String>, Void, String> {


    public AsyncResponse1 delegate = null;
    ProgressDialog mProgress;
    private Context mContext;
    private String getQuery(HashMap<String,String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;




        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
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
        System.out.println("result in url parameter query formation is "+ result);
        return result.toString();
    }
//    String fetchWeatherURL = "http://tsarkarwebservice.ddns.net/RestfulWebServiceCA/contextaware/webservice/hello";

//    InputStream in = null;
//    String weather_url = "";//AppConstants.login_url;
    public TopLocationsAsyncTask(Context context){
        this.mContext = context;
        System.out.println("in constructor login asynctask");
    }
    @Override
    protected void onPreExecute() {
        System.out.println("in on preexecute");
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Fetching Best Suggestions in Town");
        mProgress.setCancelable(true);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
    }

    @Override
    protected String doInBackground(HashMap<String,String>... params) {
        InputStream in = null;
        String result = "";
        try {

            String params_formed = getQuery(params[0]);
            byte[] postData  = params_formed.getBytes();
            int postDataLength = postData.length;
            URL url = new URL("https://developers.zomato.com/api/v2.1/geocode?" + params_formed);
            System.out.println("url is "+ url);
            URLConnection connection = url.openConnection();

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);


            httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty ("user-key", "a472f0195e685a4eeae803aef02d0d4a");
            httpConnection.setRequestProperty("Content-Language", "en-US");
//            httpConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));




            in = httpConnection.getInputStream();

            int responseCode = httpConnection.getResponseCode();


            if (responseCode == HttpURLConnection.HTTP_OK) {

                StringBuilder sb = new StringBuilder("");
                // int line = 0;
                int length = httpConnection.getContentLength();
                System.out.println(" Length= " + length);

                int i;
                do {
                    i = in.read();
                    if (i != -1) {
                        // System.out.println("Character >>> "+ (char)i);
                        sb.append((char) i);
                    }
                } while (i != -1);

                result = new String(sb);
                in.close();
                // result = sb.toString();

                // return result;
            }
        } catch (Exception e) {

            e.printStackTrace();
            result = "failure";



        }

        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Final result....." + result);
        return result;






    }



    @Override
    protected void onPostExecute(String response) {
        // TODO Auto-generated method stub
//			super.onPostExecute(result);

        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
            mProgress = null;
        }
        System.out.println("++ result is " + response);

        JsonParser json_parser = new JsonParser();
        HashMap restaurants = json_parser.parse_GeoCodes(response);
        System.out.println(">> hashmap size is "+ restaurants.size());
        delegate.processFinish2(restaurants);


    }


}
