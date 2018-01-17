package ct.agent.buy.behaviour;

import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import ct.agent.buy.BuyerAgent;
import ct.model.car.Car;
import ct.model.msg.Messages;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BuyCarBehaviour extends Behaviour {
	private static final long serialVersionUID = 1L;
	private MessageTemplate messageTemplate; 
	private List<AID> sellersAgents;
	private String carName;
	private String[] carsToBuy;
	private AID bestSeller; 
	private float bestPrice;
	private int repliesCount, step; 
	
	
	public BuyCarBehaviour(List<AID> sellersAgents, String[] carsToBuy) {
		this.sellersAgents = sellersAgents;
		this.bestPrice = 0.0f;
		this.repliesCount = 0;
		this.step = 1;
		this.carsToBuy = carsToBuy;
	}
	

	public void action() {
		int carIndex = BuyerAgent.carsBasket.get(myAgent.getAID().getLocalName()) - 1;
		
		if(carIndex >= 0) {
			this.carName = carsToBuy[carIndex];
			switch (step) {
				case 1:
					stepOne(carName);
					break;
				case 2:
					ACLMessage reply = myAgent.receive(messageTemplate);
					stepTwo(reply,carName);
					break;
				case 3:
					stepThree(carName);
					break;
				case 4:
					reply = myAgent.receive(messageTemplate);
					stepFour(reply,carName);
					break;
			}
		}
	}
	public boolean done() {
		if (step == 3 && bestSeller == null) {
			System.out.println("Poszukiwane auto " + carName + " zostalo juz sprzedane innemu kupujacemu. Poczekaj na nowa oferte!");
		}
		return ((step == 3 && bestSeller == null) || step == 5);
	}
	private void replies() {
		this.repliesCount++;
		if (repliesCount >= sellersAgents.size()) {
			nextStep();
		}
	}
	private void nextStep() {
		this.step++;
	}
	private void stepOne(String carName) {
		ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
		
		for(AID agent : sellersAgents) {
			cfp.addReceiver(agent);
		}
		cfp.setContent(carName);
		cfp.setConversationId(Messages.Buy_Behaviour.getMsg());
		cfp.setReplyWith(Messages.Cyclic_Behaviour.getMsg() + System.currentTimeMillis()); 
		
		myAgent.send(cfp);
		this.messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(Messages.Buy_Behaviour.getMsg()), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
		nextStep();
	}
	private void stepTwo(ACLMessage reply,String carName) {		
		if (reply != null) {
			try {
				Car car = (Car) reply.getContentObject();
				if (reply.getPerformative() == ACLMessage.PROPOSE) {
					float price = car.getTotalPrice();
					if (bestSeller == null || price < bestPrice) {
						this.bestPrice = price;
						this.bestSeller = reply.getSender();
					}
				}
				
				replies();
			} catch (Exception e) {
				if (reply.getPerformative() == ACLMessage.REFUSE || reply.getPerformative() == ACLMessage.FAILURE) {
					replies();
				}
			}
		} else {
			block();
		}
	}
	private void stepThree(String carName) {
		ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		
		order.addReceiver(bestSeller);
		order.setContent(carName);
		order.setConversationId(Messages.Buy_Behaviour.getMsg());
		order.setReplyWith(Messages.Order.getMsg() + System.currentTimeMillis());
		myAgent.send(order);

		this.messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(Messages.Buy_Behaviour.getMsg()), MessageTemplate.MatchInReplyTo(order.getReplyWith()));
		nextStep();
	}
	private void stepFour(ACLMessage reply,String carName) {
		if (reply != null) {
			if (reply.getPerformative() == ACLMessage.INFORM) {
				String agentName = myAgent.getAID().getLocalName();
				String basketId = "basket#" + agentName;
				
				BuyerAgent.carsBasket.computeIfPresent(basketId, (k, cash) -> cash + Math.round(bestPrice));
				this.carsToBuy = ArrayUtils.removeElement(carsToBuy, carName);
				BuyerAgent.carsBasket.computeIfPresent(myAgent.getAID().getLocalName(), (k, cars) -> cars - 1);
				if(BuyerAgent.carsBasket.get(myAgent.getAID().getLocalName()) <= 0) {
					myAgent.doDelete();
				}
				if((BuyerAgent.basketPrice - BuyerAgent.carsBasket.get(basketId)) <= 0) {
					myAgent.doDelete();
				} else {
					System.out.println(
							"\n > Auto [" + carName + "] sprzedane dla Agenta [" + agentName +
							"]. Pomyslnie zakupiony od sprzedawcy [" + reply.getSender().getLocalName() + 
							"] za cene [" + Math.round(bestPrice) + " $]" +
							"\n > Agentowi [" + agentName + "] pozostalo do wydania: " +
							(BuyerAgent.basketPrice - BuyerAgent.carsBasket.get(basketId)) +
							" $ / " + BuyerAgent.basketPrice + " $"
					);
				}
			} else {
				System.out.println("Poszukiwane auto " + carName + " zostalo juz sprzedane innemu kupujacemu. Poczekaj na nowa oferte!");
			}
			nextStep();
		} else {
			block();
		}
	}
}