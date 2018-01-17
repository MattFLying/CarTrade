package ct.model.car;

import java.io.Serializable;

public enum EngineType implements Serializable {
	NONE("Brak"), 
	DIESEL("Diesel"), 
	BENZINE("Benzyna"), 
	GAS("Gaz"), 
	ELECTRIC("Elektryczny"), 
	HYBRID("Hybrydowy");
	
	
	private String name;
	
	EngineType(String name) {
		this.name= name;
	}
	
	public String getName() {
		return name;
	}
}