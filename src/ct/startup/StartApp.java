package ct.startup;

import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import ct.model.car.CarBody;
import ct.model.car.EngineType;

public class StartApp {
	private static final int SELLERS_COUNT = 10;
	private static final int BUYERS_COUNT = 3;
	private static final int SELLERS_CARS = 8;
	private static final int BUYERS_CARS = 3;
	
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		String sellers = generateAllSellers();
		String buyers = generateAllBuyers();
		
		sb.append(sellers);
		sb.append(buyers);
		
		String[] bootArgs = {"-gui", sb.toString()};
		run(bootArgs);
	}
	
	private static void run(String[] bootArgs) {
		jade.Boot.main(bootArgs);
	}
	private static String generateAllBuyers() {
		StringBuilder sb = new StringBuilder();
					
		for(int i = 0; i < BUYERS_COUNT; i++) {
			sb.append(BUYER_TITLE);
			sb.append(i + 1);
			sb.append(BUYER_PACKGAE);
			sb.append(generateAllCarsForBuyer());
			sb.append(";");
		}
		
		return sb.toString();		
	}
	private static String generateAllCarsForBuyer() {
		StringBuilder sb = new StringBuilder("(");
		int counter = 0;
		String[] cars = carList;
		
		for(int i = 0; i < BUYERS_CARS; i++) {
			counter++;
			
			String[] car = generateCar(cars);
			String carName = car[0] + "-" + car[1];
			cars = ArrayUtils.removeElement(cars, carName);
			
			sb.append(generateOneCarForBuyer(car));
			if(counter < BUYERS_CARS) {
				sb.append(",");
			}
		}
		
		return sb.append(")").toString();
	}
	private static String generateOneCarForBuyer(String[] car) {
		String semicolon = " ";
		StringBuilder sb = new StringBuilder();
		
		sb.append(car[0].trim());
		sb.append(semicolon);
		sb.append(car[1].trim());
		
		return sb.toString();
	}
	private static String generateAllSellers() {
		StringBuilder sb = new StringBuilder();
					
		for(int i = 0; i < SELLERS_COUNT; i++) {
			sb.append(SELLER_TITLE);
			sb.append(i + 1);
			sb.append(SELLER_PACKGAE);
			sb.append(generateAllCarsForOneSeller());
			sb.append(";");
		}
		
		return sb.toString();		
	}
	private static String generateAllCarsForOneSeller() {
		StringBuilder sb = new StringBuilder("(");
		int counter = 0;
		String[] cars = carList;
		
		for(int i = 0; i < SELLERS_CARS; i++) {
			counter++;
			
			String[] car = generateCar(cars);
			String carName = car[0] + "-" + car[1];
			cars = ArrayUtils.removeElement(cars, carName);
			
			sb.append(generateOneCarForSeller(car));
			if(counter < SELLERS_CARS) {
				sb.append(",");
			}
		}
		
		return sb.append(")").toString();		
	}
	private static String generateOneCarForSeller(String[] car) {
		String semicolon = "-";
		StringBuilder sb = new StringBuilder();
		
		sb.append(car[0].trim());
		sb.append(semicolon);
		sb.append(car[1].trim());
		sb.append(semicolon);
		sb.append(generateEngineType());
		sb.append(semicolon);
		sb.append(generateCarBody());
		sb.append(semicolon);
		sb.append(generateProductionYear());
		sb.append(semicolon);
		sb.append(generateEngineCapacity());
		sb.append(semicolon);
		sb.append(generatePrice());
		sb.append(semicolon);
		sb.append(ADDITIONAL_FEES);
		sb.append(semicolon);
		sb.append(generateAdditionalCosts());
		
		return sb.toString();
	}
	private static int generateAdditionalCosts() {
		int chance = new Random().nextInt(5) + 1;
		switch(chance) {
			case 1:
				return new Random().nextInt(150) + 0;
			case 2:
				return new Random().nextInt(300) + 200;
			case 3:
				return new Random().nextInt(500) + 500;
			case 4:
				return new Random().nextInt(1500) + 1000;
			case 5:
				return new Random().nextInt(7500) + 2500;
			default:
				return new Random().nextInt(7500) + 0;
		}
	}
	private static int generatePrice() {
		int chance = new Random().nextInt(5) + 1;
		switch(chance) {
			case 1:
				return new Random().nextInt(7000) + 3000;
			case 2:
				return new Random().nextInt(20000) + 10000;
			case 3:
				return new Random().nextInt(45000) + 30000;
			case 4:
				return new Random().nextInt(25000) + 75000;
			case 5:
				return new Random().nextInt(50000) + 100000;
			default:
				return new Random().nextInt(147000) + 3000;
		}
	}
	private static int generateProductionYear() {
		return new Random().nextInt(28) + 1990;
	}
	private static float generateEngineCapacity() {
		String value;
		int chance = new Random().nextInt(4) + 1;
		switch(chance) {
			case 1:
				value = String.format("%.1f", new Random().nextFloat() * (1.9f - 1.0f) + 1.0f);
				return Float.parseFloat(value.replace(',', '.'));
			case 2:
				 value = String.format("%.1f", new Random().nextFloat() * (2.9f - 2.0f) + 2.0f);
				return Float.parseFloat(value.replace(',', '.'));
			case 3:
				value = String.format("%.1f", new Random().nextFloat() * (3.8f - 3.0f) + 3.0f);
				return Float.parseFloat(value.replace(',', '.'));
			case 4:
				value = String.format("%.1f", new Random().nextFloat() * (4.8f - 4.0f) + 4.0f);
				return Float.parseFloat(value.replace(',', '.'));
			default:
				value = String.format("%.1f", new Random().nextFloat() * (4.8f - 1.0f) + 1.0f);
				return Float.parseFloat(value.replace(',', '.'));
		}
	}
	private static String[] generateCar(String[] cars) {
		int size = cars.length;
		int index = new Random().nextInt(size);
		
		String[] result = cars[index].split("-");
		
		return result;
	}
	private static String generateEngineType() {
		EngineType[] engines = EngineType.values();
		int engineId = new Random().nextInt(engines.length - 1) + 1;
		
		return engines[engineId].name();
	}
	private static String generateCarBody() {
		CarBody[] bodies = CarBody.values();
		int bodyId = new Random().nextInt(bodies.length - 1) + 1;
		
		return bodies[bodyId].name();
	}
	
	private static final String SELLER_TITLE = "Sprzedawca#";
	private static final String BUYER_TITLE = "Kupujacy#";
	private static final String SELLER_PACKGAE = ":ct.agent.sell.SellerAgent";
	private static final String BUYER_PACKGAE = ":ct.agent.buy.BuyerAgent";
	private static final String ADDITIONAL_FEES = "Cechy dodatkowe";
	private static String[] carList = {
			"Audi-A4", "Audi-A8", "Audi-RS6", "Audi-TT", "Audi-R8", "Citroen-C5", "Citroen-Xsara",
			"BMW-F30", "BMW-E36", "BMW-M5", "Dacia-Duster", "Dacia-Logan", "Aston Martin-DB9",
			"Aston Martin-DBS", "Bentley-Continental GT", "Bugatti-Veyron"
	};
}