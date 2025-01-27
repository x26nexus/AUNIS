package mrjake.aunis.stargate;

public enum EnumStargateState {
	IDLE(0),
	COMPUTER_DIALING(1),
	DHD_DIALING(2),
	ENGAGED(3),
	FAILING(4);
	
	public int id;
	
	private EnumStargateState(int id) {
		this.id = id;
	}
	
	public boolean idle() {
		return this == IDLE;
	}
	
	public static EnumStargateState valueOf(int id) {
		return EnumStargateState.values()[id];
	}
}
