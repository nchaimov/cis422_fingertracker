package wiitracker.fingertracking;

import java.awt.Color;

/**
 * 
 * Contains information accompanying Finger points, including a value, color, and the name of the finger.
 * @author areinder
 *
 */
public enum PointType {
	THUMB(0, Color.CYAN, "THUMB"), INDEX(1, Color.RED, "INDEX"), MIDDLE(2, Color.GREEN, "MIDDLE"), RING(3, Color.BLUE, "RING"), OFF_SCREEN(
			-2, Color.BLACK, "OFF_SCREEN"), UNKNOWN(-1, Color.YELLOW, "UNKNOWN");

	public final int value;
	public final Color color;
	public final String name;
	
	public static final int NUMBER_OF_FINGERS = 4;

	private PointType(int value, Color color, String name) {
		this.value = value;
		this.color = color;
		this.name = name;
	}

	/**
	 * 
	 * @return True if the point is labeled as a particular finger, and false otherwise.
	 */
	public boolean isKnown() {
		return value >= 0;
	}

	/**
	 * True if the point is on-screen. If false, position information does not carry relevant information.
	 * @return
	 */
	public boolean hasPosition() {
		return value > OFF_SCREEN.value;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
