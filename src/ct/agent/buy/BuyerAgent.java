package ct.agent.buy;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import ct.agent.buy.behaviour.BuyCarBehaviour;
import ct.model.msg.Messages;
import jade.core.AID;

public class BuyerAgent extends Agent {
	private static final long serialVersionUID = 8488379474730664834L;
	private static int timeToNewOfertInSeconds = 5;
	private List<AID> sellersAgents;
	private String[] carsToBuy = {};
	public static final int basketPrice = 500000;
	public static HashMap<String, Integer> carsBasket = new HashMap<String, Integer>();
	
	
	public BuyerAgent() {
		this.sellersAgents = new ArrayList<AID>();
	}
	
	
	protected void setup() {
		System.out.println("Witaj! Agent Kupujacy ==> " + 
					getAID().getLocalName() + " <== zostal utworzony."
		);
		
		Object[] carInfo = getArguments();
		if (carInfo != null && carInfo.length > 0) {
			System.out.println(" > Poszukiwane samochody:");
			int counter = 1;
			for(Object car : carInfo) {
				String carName = car.toString().trim();
				System.out.println(" > " + counter + ".) " + carName);
				carsToBuy = ArrayUtils.add(carsToBuy, carName);
				counter++;
			}
			
			carsBasket.put(getAID().getLocalName(), carsToBuy.length);
			carsBasket.put("basket#" + getAID().getLocalName(), 0);
			addBehaviourToBuyerAgent();
		}
	}
	protected void takeDown() {
		System.out.println(" >> Agent kupujacy " + getAID().getLocalName() + " dokonal zakupu wszystkich poszukiwanych aut lub zabraklo mu pieniedzy i zostal usuniety...");
	}
	private void addBehaviourToBuyerAgent() {
		addBehaviour(createTickerBehaviour());
	}
	private TickerBehaviour createTickerBehaviour() {	
		return new TickerBehaviour(this, timeToNewOfertInSeconds * 1000) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				DFAgentDescription dfAgentDescription = new DFAgentDescription();
				ServiceDescription serviceDescription = new ServiceDescription();
				
				searchServiceForBuyerAgent(dfAgentDescription, serviceDescription, myAgent);
				myAgent.addBehaviour(new BuyCarBehaviour(sellersAgents, carsToBuy));
			}
		};
	}
	private void searchServiceForBuyerAgent(DFAgentDescription dfAgentDescription, ServiceDescription serviceDescription, Agent myAgent) {
		serviceDescription.setType(Messages.Selling_Service.getMsg());
		dfAgentDescription.addServices(serviceDescription);
		
		try {
			DFAgentDescription[] result = DFService.search(myAgent, dfAgentDescription);
			for(DFAgentDescription dfAgent : result) {
				sellersAgents.add(dfAgent.getName());
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
}