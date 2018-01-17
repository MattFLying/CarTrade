package ct.agent.sell;

import java.util.Hashtable;
import ct.agent.sell.behaviour.OfferCarBehaviour;
import ct.agent.sell.behaviour.PurchaseCarBehaviour;
import ct.agent.sell.gui.SellerGui;
import ct.model.car.Car;
import ct.model.msg.Messages;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class SellerAgentGui extends Agent {
	private static final long serialVersionUID = 5633568447791272917L;
	private Hashtable<String, Car> carsCatalog;	
	private SellerGui gui;
	
	
	public SellerAgentGui() {
		this.carsCatalog = new Hashtable<String, Car>();
	}
	
	
	protected void setup() {
		this.gui = new SellerGui(this);
		this.gui.showGui();
		
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

		gui.dispose();
		System.out.println(" > Sprzedawca " + getAID().getName() + " usuniety...");
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