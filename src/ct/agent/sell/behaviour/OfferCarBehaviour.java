package ct.agent.sell.behaviour;

import java.util.Hashtable;
import ct.model.car.Car;
import ct.model.msg.Messages;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class OfferCarBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private Hashtable<String, Car> carsCatalog;
	
	
	public OfferCarBehaviour(Hashtable<String, Car> carsCatalog) {
		this.carsCatalog = carsCatalog;
	}
	
	
	public void action() {
		MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		ACLMessage aclMessage = myAgent.receive(messageTemplate);
		
		if (aclMessage != null) {
			String carName = aclMessage.getContent();
			ACLMessage reply = aclMessage.createReply();
			try {
				Car car = carsCatalog.get(carName.trim());
				if (car != null) {
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContentObject(car);
				} else {
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent(Messages.Content_Not_Available.getMsg());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			myAgent.send(reply);
		} else {
			block();
		}
	}
}