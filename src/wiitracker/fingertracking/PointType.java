package wiitracker.fingertracking;

import java.awt.Color;

public enum PointType {
	THUMB(0, Color.ORANGE), INDEX(1, Color.RED), MIDDLE(2, Color.GREEN), RING(3, Color.BLUE), OFF_SCREEN(
			-2, Color.BLACK), UNKNOWN(-1, Color.YELLOW);

	public final int value;
	public final Color color;
	public static final int NUMBER_OF_FINGERS = 4;

	private PointType(int value, Color color) {
		this.value = value;
		this.color = color;
	}

	public boolean isKnown() {
		return value >= 0;
	}

	public boolean hasPosition() {
		return value > OFF_SCREEN.value;
	}

}
