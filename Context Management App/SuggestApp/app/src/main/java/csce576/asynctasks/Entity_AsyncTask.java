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
 * Created by tsarkar on 07/04/17.
 */
public class Entity_AsyncTask extends AsyncTask<HashMap<String,String>, Void, String> {

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

    public Entity_AsyncTask(Context context){

        this.mContext = context;

    }
    @Override
    protected void onPreExecute() {
        System.out.println("in on preexecute");
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Fetching places near you");
        mProgress.setCancelable(true);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
    }

    @Override
    protected String doInBackground(HashMap<String, String>... params) {

        InputStream in = null;
        String result = "";
        try {

            String params_formed = getQuery(params[0]);
            byte[] postData  = params_formed.getBytes();
            int postDataLength = postData.length;
            URL url = new URL("https://developers.zomato.com/api/v2.1/locations?" + params_formed);
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

        System.out.println("++ response is " + response);
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
            mProgress = null;
        }
        JsonParser json_parser = new JsonParser();
        HashMap entity_values = json_parser.parse_entity_type_id(response);
        System.out.println(">> hashmap size is "+ entity_values.size());
        delegate.processFinish3(entity_values);

    }
}
