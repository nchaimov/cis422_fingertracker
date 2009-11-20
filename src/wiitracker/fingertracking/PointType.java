package wiitracker.fingertracking;

public enum PointType {
	THUMB (0), INDEX (1), MIDDLE (2), RING (3), OFF_SCREEN (-2), UNKNOWN (-1);
	
	public final int value;
	public static final int NUMBER_OF_FINGERS = 4;
	
	private PointType(int value) {
		this.value = value;
	}
	
	public boolean isKnown() {
		return value >= 0;
	}
	
	public boolean hasPosition() {
		return value > OFF_SCREEN.value;
	}
	
}
