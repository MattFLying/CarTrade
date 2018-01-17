package ct.agent.sell;

import java.util.HashMap;
import java.util.Hashtable;
import ct.agent.sell.behaviour.OfferCarBehaviour;
import ct.agent.sell.behaviour.PurchaseCarBehaviour;
import ct.model.car.Car;
import ct.model.car.CarBody;
import ct.model.msg.Messages;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class SellerAgent extends Agent {
	private static final long serialVersionUID = 5633568447791272917L;
	private Hashtable<String, Car> carsCatalog;
	
	
	public SellerAgent() {
		this.carsCatalog = new Hashtable<String, Car>();
	}
	

	protected void setup() {	
		Object[] carInfo = getArguments();
		if (carInfo != null && carInfo.length > 0) {
			buildCarFromArguments(carInfo);
		}
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();
		
		dfAgentDescription.setName(getAID());
		serviceDescription.setType(Messages.Selling_Service.getMsg());
		serviceDescription.setName(Messages.Service.getMsg());
		dfAgentDescription.addServices(serviceDescription);
		
		try {
			DFService.register(this, dfAgentDescription);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new OfferCarBehaviour(carsCatalog));
		addBehaviour(new PurchaseCarBehaviour(carsCatalog));
	}
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println(" > Sprzedawca " + getAID().getName() + " usuniety...");
	}
	private void buildCarFromArguments(Object[] args) {
		for(Object o : args) {
			String[] s = ((String)o).split("-");
			
			String brand = s[0].toString();
			String model = s[1].toString();
			String engineType = s[2].toString();
			CarBody carBody = CarBody.valueOf(s[3].toString());
			Integer productionYear = Integer.valueOf(s[4].toString());
			Float engineCap = Float.parseFloat(s[5].toString());
			Float price = Float.parseFloat(s[6].toString());
			String additionalDescription = s[7].toString();
			Float additionalPrice = Float.parseFloat(s[8].toString());
			
			HashMap<String, Float> additionals = new HashMap<String, Float>();
			additionals.put(additionalDescription, additionalPrice);
			
			Car car = new Car(engineCap, engineType, carBody, brand, model, productionYear, price);
			car.setAdditionalFees(additionals);
			
			String carName = brand.concat(" ").concat(model);
			updateCatalogue(car, carName.trim());
		}
	}
	private OneShotBehaviour createSellingBookBehaviour(final Car car, final String carName) {
		return new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			public void action() {
				carsCatalog.put(carName, car);
				System.out.println("[" + getLocalName() + "] + Auto [" + car.getCarBrand() + " " + 
							car.getCarModel() + "] dodane do katalogu, Cena samochodu [" + 
							Math.round(car.getPrice()) + " $] + oplaty dodatkowe [" +
							Math.round(car.getAdditionalPrice()) + " $], Suma: $" + Math.round(car.getTotalPrice())
				);
			}
		};
	}
	public void updateCatalogue(final Car car, final String carName) {
		addBehaviour(createSellingBookBehaviour(car, carName));
	}
}