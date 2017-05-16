package csce576.beans;

/**
 * Created by tsarkar on 19/04/17.
 */

public class CuisineBean {
    String cuisine_name;
    String cuisine_id;
    String is_checked;


    public String getCuisine_name() {
        return cuisine_name;
    }

    public void setCuisine_name(String cuisine_name) {
        this.cuisine_name = cuisine_name;
    }

    public String getCuisine_id() {
        return cuisine_id;
    }

    public void setCuisine_id(String cuisine_id) {
        this.cuisine_id = cuisine_id;
    }

    public String getIs_checked() {

        return is_checked;
    }

    public void setIs_checked(String is_checked) {
        this.is_checked = is_checked;
    }
}
