package ct.model.car;

import java.io.Serializable;

public enum CarBody implements Serializable {
	NONE("Brak"),
	SEDAN("Sedan"), 
	HATCHBACK("Hatchback"), 
	LIFTBACK("Liftback"), 
	COUPE("Coupe"), 
	CABRIO("Kabriolet"), 
	ROADSTER("Roadster"), 
	COMBI("Kombi"), 
	MINIVAN("Minivan"), 
	PICKUP("Pick-up");
	
	
	private String name;
	
	CarBody(String name) {
		this.name= name;
	}
	
	public String getName() {
		return name;
	}
}