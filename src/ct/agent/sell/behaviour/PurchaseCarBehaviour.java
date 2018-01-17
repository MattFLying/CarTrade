package ct.agent.sell.behaviour;

import java.util.Hashtable;
import ct.model.car.Car;
import ct.model.msg.Messages;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PurchaseCarBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private Hashtable<String, Car> carsCatalog;
	
	
	public PurchaseCarBehaviour(Hashtable<String, Car> carsCatalog) {
		this.carsCatalog = carsCatalog;
	}
	
	
	public void action() {
		MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
		ACLMessage aclMessage = myAgent.receive(messageTemplate);
		
		if (aclMessage != null) {
			String carName = aclMessage.getContent();
			ACLMessage reply = aclMessage.createReply();
			try {
				Car car = carsCatalog.remove(carName);
				if (car != null) {
					reply.setPerformative(ACLMessage.INFORM);
				} else {
					reply.setPerformative(ACLMessage.FAILURE);
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