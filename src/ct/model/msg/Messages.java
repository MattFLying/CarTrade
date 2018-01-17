package ct.model.msg;

public enum Messages {
	Service("Gielda Samochodow - Java JADE"),
	Selling_Service("sprzedaz-auta"),
	Buy_Behaviour("kupowanie-auta"),
	Cyclic_Behaviour("cfp"),
	Order("zamowienie"),
	Content_Not_Available("not-available");
	
	
	private String message;
	
	Messages(String message) {
		this.message = message;
	}
	
	public String getMsg() {
		return message;
	}
}