package csce576.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import csce576.jsonparser.JsonParser;

public class WeatherAsyncTask extends AsyncTask<ArrayList<String>, Void, String> {

    String fetchWeatherURL = "http://api.wunderground.com/api/b59d969356e7552c/conditions/q/";
    private Context mContext;
    ProgressDialog mProgress;
    public AsyncResponse1 delegate = null;
    InputStream in = null;
    String weather_url = "";//AppConstants.login_url;
    public WeatherAsyncTask(Context context){
        this.mContext = context;
        System.out.println("in constructor login asynctask");
    }
    @Override
    protected void onPreExecute() {
        System.out.println("in on preexecute");
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Fetching Weather");
        mProgress.setCancelable(true);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
    }

    @Override
    protected String doInBackground(ArrayList<String>... params) {
        // TODO Auto-generated method stub

        String response ="";



        System.out.println("fetchWeatherURL url is "+ fetchWeatherURL);
        try{


            System.out.println("<>In new connection function");
			String parameters = getQuery(params[0]);
            URL url = new URL(fetchWeatherURL+parameters);
            System.out.println("<>hello 1");
            URLConnection connection = url.openConnection();
            System.out.println("<>hello 2");
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            System.out.println("<>url is " +connection.getURL());




            System.out.println("<>hello 3");
            httpConnection.setRequestMethod( "POST" );
            System.out.println("<>hello 4");
            httpConnection.setInstanceFollowRedirects( false );
            System.out.println("<>hello 5");
            connection.setDoInput(true);
            httpConnection.setDoOutput( true );
            System.out.println("<>hello 6");
            httpConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            System.out.println("<>hello 7");
            httpConnection.setRequestProperty( "charset", "utf-8");
            System.out.println("<>hello 8");
//  			httpConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

            httpConnection.setUseCaches( false );

            System.out.println("<>hello 9");
            System.out.println("<>final url is " +connection.getURL());

            int responseCode=httpConnection.getResponseCode();
            System.out.println("responseCode is "+ responseCode );

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br=new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));


                int r;
                r = br.read();
                //System.out.println("r is "+ r);
                while ((r = br.read()) != -1) {
                    char ch = (char) r;
                    response+=ch;
                }
            }
            else {
                response="";

            }
            System.out.println(">> response length is "+response.length());
            //System.out.println("response is "+response);



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }


    private String getQuery(ArrayList<String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        System.out.println("in get query >> ");

        /*firstname = params.get("fName");
        lastname = params.get("lName");
        gender = params.get("gender");
        dob = params.get("dob");
        */
        String city = (String)params.get(0);
        String state = (String)params.get(1);
        String country = (String)params.get(2);


            if(country.equals("USA"))
            {
                result.append(URLEncoder.encode((String) state, "UTF-8"));
                result.append("/");
                result.append(URLEncoder.encode((String) city, "UTF-8"));
                result.append(URLEncoder.encode(".json", "UTF-8"));
            }



        System.out.println(">> result in url parameter query formation is "+ result.toString());
        return result.toString();
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

        if(response.equals("Exception"))
        {
            Toast.makeText(mContext, "Sorry some error occurred",
                    Toast.LENGTH_LONG).show();
        }
        else{
            System.out.println("++ result is " + response);
            JsonParser json_parser = new JsonParser();
            HashMap weather_params = json_parser.parse_weather_time(response,mContext);
            System.out.println(">> hashmap size is "+ weather_params.size());
            delegate.processFinish(weather_params);
            //Intent login_activity = new Intent(mContext, StoreListScreenActivity.class);
            //mContext.startActivity(login_activity);
        }


    }
}




