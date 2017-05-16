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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import csce576.jsonparser.JsonParser;

/**
 * Created by tsarkar on 09/04/17.
 */
public class Establishments_AsyncTask extends AsyncTask<HashMap<String,String>, Void, String> {

    public AsyncResponse2 delegate = null;
    ProgressDialog mProgress;
    private Context mContext;
    public void setParams(HashMap params)
    {
        try{
            getQuery(params);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

    public Establishments_AsyncTask(Context context){
        this.mContext = context;
        System.out.println("in constructor login asynctask");
    }
    @Override
    protected void onPreExecute() {
        System.out.println("in on preexecute");
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Fetching establishment types");
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
            byte[] postData = params_formed.getBytes();
            int postDataLength = postData.length;
            URL url = new URL("https://developers.zomato.com/api/v2.1/establishments?" + params_formed);
            System.out.println("url is " + url);
            URLConnection connection = url.openConnection();

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);


            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("user-key", "a472f0195e685a4eeae803aef02d0d4a");
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
//  {"cuisines":[{"cuisine":{"cuisine_id":152,"cuisine_name":"African"}},{"cuisine":{"cuisine_id":1,"cuisine_name":"American"}},{"cuisine":{"cuisine_id":3,"cuisine_name":"Asian"}},{"cuisine":{"cuisine_id":193,"cuisine_name":"BBQ"}},{"cuisine":{"cuisine_id":5,"cuisine_name":"Bakery"}},{"cuisine":{"cuisine_id":227,"cuisine_name":"Bar Food"}},{"cuisine":{"cuisine_id":270,"cuisine_name":"Beverages"}},{"cuisine":{"cuisine_id":159,"cuisine_name":"Brazilian"}},{"cuisine":{"cuisine_id":182,"cuisine_name":"Breakfast"}},{"cuisine":{"cuisine_id":247,"cuisine_name":"Bubble Tea"}},{"cuisine":{"cuisine_id":168,"cuisine_name":"Burger"}},{"cuisine":{"cuisine_id":30,"cuisine_name":"Cafe"}},{"cuisine":{"cuisine_id":491,"cuisine_name":"Cajun"}},{"cuisine":{"cuisine_id":25,"cuisine_name":"Chinese"}},{"cuisine":{"cuisine_id":161,"cuisine_name":"Coffee and Tea"}},{"cuisine":{"cuisine_id":928,"cuisine_name":"Creole"}},{"cuisine":{"cuisine_id":153,"cuisine_name":"Cuban"}},{"cuisine":{"cuisine_id":192,"cuisine_name":"Deli"}},{"cuisine":{"cuisine_id":100,"cuisine_name":"Desserts"}},{"cuisine":{"cuisine_id":541,"cuisine_name":"Diner"}},{"cuisine":{"cuisine_id":959,"cuisine_name":"Donuts"}},{"cuisine":{"cuisine_id":268,"cuisine_name":"Drinks Only"}},{"cuisine":{"cuisine_id":38,"cuisine_name":"European"}},{"cuisine":{"cuisine_id":40,"cuisine_name":"Fast Food"}},{"cuisine":{"cuisine_id":45,"cuisine_name":"French"}},{"cuisine":{"cuisine_id":501,"cuisine_name":"Frozen Yogurt"}},{"cuisine":{"cuisine_id":274,"cuisine_name":"Fusion"}},{"cuisine":{"cuisine_id":156,"cuisine_name":"Greek"}},{"cuisine":{"cuisine_id":143,"cuisine_name":"Healthy Food"}},{"cuisine":{"cuisine_id":233,"cuisine_name":"Ice Cream"}},{"cuisine":{"cuisine_id":148,"cuisine_name":"Indian"}},{"cuisine":{"cuisine_id":154,"cuisine_name":"International"}},{"cuisine":{"cuisine_id":135,"cuisine_name":"Irish"}},{"cuisine":{"cuisine_id":55,"cuisine_name":"Italian"}},{"cuisine":{"cuisine_id":60,"cuisine_name":"Japanese"}},{"cuisine":{"cuisine_id":67,"cuisine_name":"Korean"}},{"cuisine":{"cuisine_id":136,"cuisine_name":"Latin American"}},{"cuisine":{"cuisine_id":66,"cuisine_name":"Lebanese"}},{"cuisine":{"cuisine_id":70,"cuisine_name":"Mediterranean"}},{"cuisine":{"cuisine_id":73,"cuisine_name":"Mexican"}},{"cuisine":{"cuisine_id":137,"cuisine_name":"Middle Eastern"}},{"cuisine":{"cuisine_id":996,"cuisine_name":"New American"}},{"cuisine":{"cuisine_id":82,"cuisine_name":"Pizza"}},{"cuisine":{"cuisine_id":970,"cuisine_name":"Po'Boys"}},{"cuisine":{"cuisine_id":304,"cuisine_name":"Sandwich"}},{"cuisine":{"cuisine_id":83,"cuisine_name":"Seafood"}},{"cuisine":{"cuisine_id":471,"cuisine_name":"Southern"}},{"cuisine":{"cuisine_id":966,"cuisine_name":"Southwestern"}},{"cuisine":{"cuisine_id":89,"cuisine_name":"Spanish"}},{"cuisine":{"cuisine_id":141,"cuisine_name":"Steak"}},{"cuisine":{"cuisine_id":177,"cuisine_name":"Sushi"}},{"cuisine":{"cuisine_id":997,"cuisine_name":"Taco"}},{"cuisine":{"cuisine_id":179,"cuisine_name":"Tapas"}},{"cuisine":{"cuisine_id":163,"cuisine_name":"Tea"}},{"cuisine":{"cuisine_id":150,"cuisine_name":"Tex-Mex"}},{"cuisine":{"cuisine_id":95,"cuisine_name":"Thai"}},{"cuisine":{"cuisine_id":308,"cuisine_name":"Vegetarian"}},{"cuisine":{"cuisine_id":99,"cuisine_name":"Vietnamese"}}]}

                // return result;
            }
        } catch (Exception e) {

            e.printStackTrace();
            result = "failure";


        } finally {
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

        ArrayList establishments_values = json_parser.parse_establishments(response);
        System.out.println(">> arraylist size is " + establishments_values.size());
        delegate.processFinish6(establishments_values);


    }
}