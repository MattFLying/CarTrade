package ct.model.car;

import java.io.Serializable;
import java.util.HashMap;

public class Car implements Serializable {
	private static final long serialVersionUID = -2110013806479314229L;
	private String carBrand, carModel;
	private int yearOfProduction;
	private float price, totalPrice, additionalPrice;
	private Engine engine;
	private CarBody typeOfBody;
	private HashMap<String, Float> additionalFees;
	
	
	public Car() {
		this.initialize(new Engine(null, 0.0f), CarBody.NONE, null, null, 0, 0);
	}
	public Car(Engine engine, CarBody typeOfBody, String carBrand, String carModel, int yearOfProduction, float price) {
		this.initialize(engine, typeOfBody, carBrand, carModel, yearOfProduction, price);
	}
	public Car(float engineCapacity, String engineType, CarBody typeOfBody, String carBrand, String carModel, int yearOfProduction, float price) {
		this.initialize(new Engine(engineCapacity, engineType), typeOfBody, carBrand, carModel, yearOfProduction, price);
	}
	public Car(EngineType engineType, float engineCapacity, CarBody typeOfBody, String carBrand, String carModel, int yearOfProduction, float price) {
		this.initialize(new Engine(engineType, engineCapacity), typeOfBody, carBrand, carModel, yearOfProduction, price);
	}


	private void initialize(Engine engine, CarBody typeOfBody, String carBrand, String carModel, int yearOfProduction, float price) {
		this.engine = engine;
		this.typeOfBody = typeOfBody;
		this.carBrand = carBrand;
		this.carModel = carModel;
		this.yearOfProduction = yearOfProduction;
		this.price = price;
		this.totalPrice = price;
		this.additionalFees = new HashMap<String, Float>();
		this.additionalPrice = 0.0f;
	}
	public String getCarBrand() {
		return carBrand;
	}
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public String getTypeOfBodyName() {
		return typeOfBody.getName();
	}
	public CarBody getTypeOfBody() {
		return typeOfBody;
	}
	public void setTypeOfBodyName(String typeOfBody) {
		for(CarBody body : CarBody.values()) {
			if(body.getName().equals(typeOfBody)) {
				this.typeOfBody = body;
			}
		}
	}
	public void setTypeOfBody(CarBody typeOfBody) {
		this.typeOfBody = typeOfBody;
	}
	public String getEngineTypeName() {
		return engine.getEngineType().getName();
	}
	public void setEngineTypeName(String engineType) {
		this.engine.setEngineTypeName(engineType);
	}
	public EngineType getEngineType() {
		return engine.getEngineType();
	}
	public void setEngineType(EngineType engineType) {
		this.engine.setEngineType(engineType);
	}
	public float getEngineCapacity() {
		return engine.getEngineCapacity();
	}
	public void setEngineCapacity(float engineCapacity) {
		this.engine.setEngineCapacity(engineCapacity);
	}
	public int getYearOfProduction() {
		return yearOfProduction;
	}
	public void setYearOfProduction(int yearOfProduction) {
		this.yearOfProduction = yearOfProduction;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public Engine getEngine() {
		return engine;
	}
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	public HashMap<String, Float> getAdditionalFees() {
		return additionalFees;
	}
	public void setAdditionalFees(HashMap<String, Float> additionalFees) {
		this.additionalFees = additionalFees;
	}
	public void addAdditionalFees(String name, Float cost) {
		this.additionalFees.put(name, cost);
	}
	public float getTotalPrice() {
		if(!additionalFees.isEmpty() && totalPrice == price) {
			for(float value : additionalFees.values()) {
				totalPrice += value;
			}
		}
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public float getAdditionalPrice() {
		if(!additionalFees.isEmpty() && additionalPrice == 0.0f) {
			for(float value : additionalFees.values()) {
				additionalPrice += value;
			}
		}
		return additionalPrice;
	}
	public void setAdditionalPrice(float additionalPrice) {
		this.additionalPrice = additionalPrice;
	}
}