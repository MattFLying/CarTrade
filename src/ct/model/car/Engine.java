package ct.model.car;

import java.io.Serializable;

public class Engine implements Serializable {
	private static final long serialVersionUID = -1412959766823097163L;
	private EngineType engineType;
	private float engineCapacity;
	
	
	protected Engine(EngineType engineType, float engineCapacity) {
		this.initialize(engineType, null, engineCapacity);
	}
	protected Engine(float engineCapacity, String engineType) {
		this.initialize(null, engineType, engineCapacity);
	}


	private void initialize(EngineType engineType, String engineTypeName, float engineCapacity) {
		if(engineTypeName == null) {
			this.setEngineType(engineType);
		} else {
			this.setEngineTypeName(engineTypeName);
		}
		this.setEngineCapacity(engineCapacity);
	}
	protected String getEngineTypeName() {
		return engineType.getName();
	}
	protected void setEngineTypeName(String engineType) {
		for(EngineType type : EngineType.values()) {
			if(type.toString().trim().equals(engineType)) {
				this.engineType = type;
			}
		}
	}
	protected EngineType getEngineType() {
		return engineType;
	}
	protected void setEngineType(EngineType engineType) {
		this.engineType = engineType;
	}
	protected float getEngineCapacity() {
		return engineCapacity;
	}
	protected void setEngineCapacity(float engineCapacity) {
		this.engineCapacity = engineCapacity;
	}
}