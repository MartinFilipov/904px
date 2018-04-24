package project.com.location;

import com.project.exceptions.LocationException;

public class Location {
	private static final int MAX_COUNTRY_LENGTH = 45;
	private static final int MAX_CITY_LENGTH = 45;
	
	private String city;
	private String country;
	
	public Location(String city, String country) {
		try {
			setCity(city);
			setCountry(country);
		} catch (LocationException e) {
			System.out.println("Error with location");
		}
	}
	
	private void setCity(String city) throws LocationException{
		if (city != null && city.trim().length() < MAX_CITY_LENGTH) {
			this.city = city;
		} else {
			throw new LocationException("Invalid city location");
		}
	}
	
	private void setCountry(String country) throws LocationException {
		if (country != null && country.trim().length() < MAX_COUNTRY_LENGTH) {
			this.country = country;
		} else {
			throw new LocationException("Invalid country location");
		}
	}
	
	public String getCity() {
		return city;
	}
	
	public String getCountry() {
		return country;
	}
}
