package csce576.beans;

import java.io.Serializable;


public class Parking_Bean implements Serializable {
	 /**
	 * 
	 */

			
	private static final long serialVersionUID = 1L;
	private double lat;
	 private double lon;
	 private boolean occupied;
	 private String parking_id;
	 private String platenumber;
	 private String address;
	 private int spot_number;
	  
	 /** Creates a new instance of Parking_Bean */
	 public void setlat(double lat) {
		 this.lat= lat;
	    }
	 public double getlat() {
       return lat;
   }
	 
	 public void setlon(double lon) {
		 this.lon= lon;
	    }
	 public double getlon() {
       return lon;
   }
	 
	 public void setspot_number(int spot_number) {
		 this.setSpot_number(spot_number);
	    }
	 public double getspot_number(int spot_number ) {
       return spot_number;
   }
	public boolean isOccupied() {
		return occupied;
	}
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	
	
	public String getParking_id() {
		return parking_id;
	}
	public void setParking_id(String parking_id) {
		this.parking_id = parking_id;
	}
	public String getPlatenumber() {
		return platenumber;
	}
	public void setPlatenumber(String platenumber) {
		this.platenumber = platenumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getSpot_number() {
		return spot_number;
	}
	public void setSpot_number(int spot_number) {
		this.spot_number = spot_number;
	}
	 
	


		
};

