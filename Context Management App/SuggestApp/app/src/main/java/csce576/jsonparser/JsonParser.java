    package csce576.jsonparser;

    import android.content.Context;

    import org.json.JSONArray;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.HashMap;

    import csce576.beans.CuisineBean;
    import csce576.beans.EstablishmentBean;
    import csce576.beans.Parking_Bean;
    import csce576.beans.RestaurantBean;

    /**
     * Created by tsarkar on 24/03/17.
     */
    public class JsonParser {

        public HashMap parse_parkingresults(String jsonData){
//            HashMap restaurant_detls = null;
            HashMap parking_detls = new HashMap();
            try{
                JSONObject jsonObject = new JSONObject(jsonData);


                System.out.println(jsonObject);

                JSONArray arr_parking = (JSONArray) jsonObject.get("ParkingList");
                int size = arr_parking.length();
                System.out.println("size is >> "+ size);
//                parking_detls = new HashMap();
                for(int i = 0;i < size; i++)
                {
                    Parking_Bean pb = new Parking_Bean();
                    JSONObject obj = (JSONObject) arr_parking.get(i);
                    String address = (String)obj.get("address");
                    String parking_id = (String)obj.get("parking_id");
                    System.out.println("address is "+address);
//                    Integer res_id = (Integer)r.get("res_id");
                    System.out.println("parking_id is "+parking_id);



                    Double lon = (Double)obj.get("lon");
                    System.out.println("lon >> "+lon);
                    Double lat = (Double)obj.get("lat");
                    System.out.println("lat >> "+lat);
                    boolean isOccupied = (boolean)obj.get("occupied");
                    System.out.println("occupied >> "+isOccupied);
                    pb.setAddress(address);
                    pb.setlat(lat);
                    pb.setlon(lon);
                    pb.setParking_id(parking_id);
                    pb.setOccupied(isOccupied);
//                    parking_detls.add(pb);
                    parking_detls.put(pb.getParking_id().toString(),pb);
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return parking_detls;
        }
        public ArrayList parse_searchresult(String jsonData){
//            HashMap restaurant_detls = null;
            ArrayList restaurant_detls = null;
            try{


                JSONObject jsonObject = new JSONObject(jsonData);


                System.out.println(jsonObject);

                JSONArray arr_restaurant = (JSONArray) jsonObject.get("restaurants");
                int size = arr_restaurant.length();
                System.out.println("size is >> "+ size);
                restaurant_detls = new ArrayList();
                for(int i = 0;i < size; i++)
                {
                    RestaurantBean rb = new RestaurantBean();
                    JSONObject obj = (JSONObject) arr_restaurant.get(i);
                    JSONObject restaurant_details = (JSONObject)obj.get("restaurant");
                    JSONObject r = (JSONObject)restaurant_details.get("R");
                    System.out.println("R is "+restaurant_details);
                    Integer res_id = (Integer)r.get("res_id");
                    System.out.println("res_id is "+res_id);

                    String name = (String)restaurant_details.get("name");
                    System.out.println("name of restaurant is "+name);

                    JSONObject location = (JSONObject)restaurant_details.get("location");
                    System.out.println("location >> "+location);

                    String city_name = (String)location.get("city");
                    String country_id = location.get("country_id").toString();
                    String city_id = location.get("city_id").toString();

                    String longitude = (String)location.get("longitude");
                    String latitude = (String)location.get("latitude");
//                    String locality_verbose = (String)location.get("locality_verbose");
                    String address = (String)location.get("address");

                    System.out.println("city_name >> "+city_name);
                    System.out.println("country_id >> "+country_id);
                    System.out.println("city_id >> "+city_id);
                    System.out.println("longitude >> "+longitude);
                    System.out.println("latitude >> "+latitude);
                    System.out.println("address >> "+address);
                    rb.setId(res_id.toString());
                    rb.setLatitude(latitude);
                    rb.setLongitude(longitude);
                    rb.setName(name);
                    rb.setAddress(address);
//                    restaurant_detls.put(res_id.toString(),rb);
                    restaurant_detls.add(rb);
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return restaurant_detls;
        }
        public HashMap parse_nearby(String jsonData){
            HashMap restaurant_detls = null;

            try{


                JSONObject jsonObject = new JSONObject(jsonData);


                System.out.println(jsonObject);

                JSONObject location = (JSONObject) jsonObject.get("location");
                System.out.println("location >> "+location);

                String city_name = (String)location.get("city_name");
                String country_id = location.get("country_id").toString();
                String city_id = location.get("city_id").toString();


                System.out.println("city_name >> "+city_name);
                System.out.println("country_id >> "+country_id);
                System.out.println("city_id >> "+city_id);



                String popularity = (String) jsonObject.get("popularity");

                System.out.println(">> "+popularity); // store popularity
                JSONArray best_rated_restaurant = (JSONArray) jsonObject.get("best_rated_restaurant");
                System.out.println(">> "+best_rated_restaurant);  // store top cuisines
    //            JSONArray nearby_res = (JSONArray) jsonObject.get("nearby_restaurants");
    //            System.out.println(">> "+nearby_res);  // store nearby_res
                int size = best_rated_restaurant.length();
                System.out.println("size is >> "+ size);


                restaurant_detls = new HashMap();
                for(int i =0;i<size;i++)
                {

                    RestaurantBean rb = new RestaurantBean();
                    JSONObject restaurant = (JSONObject) best_rated_restaurant.get(i);
                    System.out.println("restaurant is "+restaurant);
                    JSONObject restaurant_details = (JSONObject)restaurant.get("restaurant");
                    System.out.println("restaurant details is "+restaurant_details);
                    JSONObject r = (JSONObject)restaurant_details.get("R");
                    System.out.println("R is "+restaurant_details);
                    Integer res_id = (Integer)r.get("res_id");
                    System.out.println("res_id is "+res_id);
                    String name = (String)restaurant_details.get("name");
                    System.out.println("name of restaurant is "+name);
                    JSONObject loc = (JSONObject)restaurant_details.get("location");
                    System.out.println("location details is "+loc);
                    String address = (String)loc.get("address");
                    String latitude = (String)loc.get("latitude");
                    String longitude = (String)loc.get("longitude");

                    System.out.println("address "+ address);
                    System.out.println("latitude "+ latitude);
                    System.out.println("longitude "+ longitude);
                    rb.setId(res_id.toString());
                    rb.setLatitude(latitude);
                    rb.setLongitude(longitude);
                    rb.setName(name);
                    rb.setAddress(address);
                    restaurant_detls.put(res_id.toString(),rb);


                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return restaurant_detls;
        }

        public HashMap parse_GeoCodes(String jsonData){
            HashMap restaurant_detls = null;

            try{


                JSONObject jsonObject = new JSONObject(jsonData);


                System.out.println(jsonObject);

                JSONObject location = (JSONObject) jsonObject.get("location");
                System.out.println("location >> "+location);

                String city_name = (String)location.get("city_name");
                String country_id = location.get("country_id").toString();
                String city_id = location.get("city_id").toString();


                System.out.println("city_name >> "+city_name);
                System.out.println("country_id >> "+country_id);
                System.out.println("city_id >> "+city_id);



                JSONObject popularity = (JSONObject) jsonObject.get("popularity");

                System.out.println(">> "+popularity.toString()); // store popularity
                JSONArray top_cuisines = (JSONArray) popularity.get("top_cuisines");
                System.out.println(">> "+top_cuisines);  // store top cuisines
                JSONArray nearby_res = (JSONArray) jsonObject.get("nearby_restaurants");
                System.out.println(">> "+nearby_res);  // store nearby_res
                int size = nearby_res.length();
                System.out.println("size is >> "+ size);

    //            JSONArray nearby_restaurants = (JSONArray) nearby_res.get("nearby_restaurants");
                restaurant_detls = new HashMap();
                for(int i =0;i<size;i++)
                {
    //                String temp_obj = (String) nearby_res.get(i);
    //                System.out.println("temp_obj is "+temp_obj);
                    RestaurantBean rb = new RestaurantBean();
                    JSONObject restaurant = (JSONObject) nearby_res.get(i);
                    System.out.println("restaurant is "+restaurant);
                    JSONObject restaurant_details = (JSONObject)restaurant.get("restaurant");
                    System.out.println("restaurant details is "+restaurant_details);
                    JSONObject r = (JSONObject)restaurant_details.get("R");
                    System.out.println("R is "+restaurant_details);
                    Integer res_id = (Integer)r.get("res_id");
                    System.out.println("res_id is "+res_id);
                    String name = (String)restaurant_details.get("name");
                    System.out.println("name of restaurant is "+name);
                    JSONObject loc = (JSONObject)restaurant_details.get("location");
                    System.out.println("location details is "+loc);
                    String address = (String)loc.get("address");
                    String latitude = (String)loc.get("latitude");
                    String longitude = (String)loc.get("longitude");

                    System.out.println("address "+ address);
                    System.out.println("latitude "+ latitude);
                    System.out.println("longitude "+ longitude);
                    rb.setId(res_id.toString());
                    rb.setLatitude(latitude);
                    rb.setLongitude(longitude);
                    rb.setName(name);
                    rb.setAddress(address);
                    restaurant_detls.put(res_id.toString(),rb);


                }

     }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return restaurant_detls;
        }
        public HashMap parse_weather_time(String response_string, Context ctx) {
            HashMap weather_values = new HashMap();
            String success_result = "false";
            String message = "";
            JSONObject info ;
            String user_id = "";
            try {

                JSONObject json = new JSONObject(response_string);

                // JSONArray list = null;
                // list = json.getJSONArray("Data");
                // JSONObject jObj = json.getJSONObject("Message");
                JSONObject curr_obs = json.getJSONObject("current_observation");

                String temp_f;
                String temp_c;
                String weather;
                String wind_kph;
                String wind_gust_kph;
                String precip_today_metric;
                String icon_url;

                System.out.println("++ curr_obs is " + curr_obs);
                System.out.println("++ tempf " + curr_obs.get("temp_f"));
                System.out.println("++ temp_c " + curr_obs.get("temp_c"));
                System.out.println("++ weather "+ curr_obs.get("weather"));
                System.out.println("++ icon_url "+ curr_obs.get("icon_url"));
                System.out.println("++ wind_kph "+ curr_obs.get("wind_kph"));
                System.out.println("++ wind_gust_kph "+ curr_obs.get("wind_gust_kph"));
                System.out.println("++ precip_today_metric "+ curr_obs.get("precip_today_metric"));

                weather_values.put("tempf",curr_obs.get("temp_f").toString());
                weather_values.put("temp_c",curr_obs.get("temp_c").toString());
                weather_values.put("weather",curr_obs.get("weather").toString());
                weather_values.put("icon_url",curr_obs.get("icon_url").toString());
                weather_values.put("wind_kph",curr_obs.get("wind_kph").toString());
                weather_values.put("wind_gust_kph",curr_obs.get("wind_gust_kph").toString());
                weather_values.put("precip_today_metric",curr_obs.get("precip_today_metric").toString());


               /* if (curr_obs.equals("1")) {
                    System.out.println("in success condition");
                    success_result = "true";
                    message = (String) json.get("Message");
                    info = json.getJSONObject("Info");
                    user_id = info.getString("id");
                    app_id = (String) info.get("uniqueAppId");
                    fName = (String) info.get("fname");
                    lName = (String) info.get("lname");
                    emailAddress = (String) info.get("email");
                    dob = (String) info.get("dob");
                    mobileNumber = (String) info.get("cellPhone");
                    gender = (String) info.get("gender");
                    patient_id = (String) info.get("patient_id");



                } else {
                    System.out.println("in pasword mismatch  condition");
                    success_result = "false";
                    message = (String) json.get("Message");
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return weather_values;
        }

        public  HashMap parse_entity_type_id(String response_string) {
            HashMap entity_values = new HashMap();
            String success_result = "false";
            String message = "";
            JSONObject info ;
            String user_id = "";
            try {

                JSONObject json = new JSONObject(response_string);

                // JSONArray list = null;
                // list = json.getJSONArray("Data");
                // JSONObject jObj = json.getJSONObject("Message");
                JSONArray arr = json.getJSONArray("location_suggestions");

                System.out.println("length of arr "+ arr.length());
                int size = arr.length();

                for(int i =0; i<size;i++)
                {
                    JSONObject jObj = arr.getJSONObject(i);

                    String entity_type = jObj.getString("entity_type");
                    String entity_id = jObj.getString("entity_id");
                    entity_values.put("entity_type",entity_type);
                    entity_values.put("entity_id",entity_id);
                    System.out.println(entity_type + "// " + entity_id);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return entity_values;
        }

        public ArrayList parse_cuisines(String response_string) {
            HashMap cuisine_values = new HashMap();

            ArrayList cuisines = new ArrayList();
            String success_result = "false";
            String message = "";
            JSONObject info ;
            String user_id = "";
            try {

                JSONObject json = new JSONObject(response_string);

                // JSONArray list = null;
                // list = json.getJSONArray("Data");
    //             JSONObject jObj = json.getJSONObject("Message");
                JSONArray arr = json.getJSONArray("cuisines");

                System.out.println("length of arr "+ arr.length());
                int size = arr.length();

                for(int i =0; i<size;i++)
                {
                    JSONObject jObj = arr.getJSONObject(i);
                    JSONObject cuisine = jObj.getJSONObject("cuisine");
                    String cuisine_id = cuisine.getString("cuisine_id");
                    String cuisine_name = cuisine.getString("cuisine_name");
                    CuisineBean cb = new CuisineBean();
                    cuisine_values.put("cuisine_id",cuisine_id);
                    cuisine_values.put("cuisine_name",cuisine_name);

                    cb.setCuisine_id(cuisine_id);
                    cb.setCuisine_name(cuisine_name);
                    cb.setIs_checked("false");

                    System.out.println(cuisine_id + "// " + cuisine_name);
//                    cuisines.add(cuisine_values);
                    cuisines.add(cb);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("size is "+ cuisine_values.size());
            return cuisines;


        }
    public ArrayList parse_establishments(String response) {
        ArrayList establishments = new ArrayList();
        HashMap establishment_values = new HashMap();

        String success_result = "false";
        String message = "";
        JSONObject info ;
        String user_id = "";
        try {

            JSONObject json = new JSONObject(response);

            // JSONArray list = null;
            // list = json.getJSONArray("Data");
            //             JSONObject jObj = json.getJSONObject("Message");
            JSONArray arr = json.getJSONArray("establishments");

            System.out.println("length of arr "+ arr.length());
            int size = arr.length();

            for(int i =0; i<size;i++)
            {
                JSONObject jObj = arr.getJSONObject(i);
                JSONObject cuisine = jObj.getJSONObject("establishment");
                String id = cuisine.getString("id");
                String name = cuisine.getString("name");

                establishment_values.put("id",id);
                establishment_values.put("name",name);

                EstablishmentBean eb = new EstablishmentBean();

                eb.setEst_id(id);
                eb.setEst_name(name);
                eb.setIs_checked("false");

                System.out.println(id + "// " + name);
//                establishments.add(establishment_values);
                establishments.add(eb);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("size is "+ establishments.size());

        return establishments;
    }

    }
