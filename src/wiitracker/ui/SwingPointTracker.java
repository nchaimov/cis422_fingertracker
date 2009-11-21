package wiitracker.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import motej.IrPoint;
import motej.event.IrCameraEvent;
import wiitracker.fingertracking.Finger;
import wiitracker.fingertracking.FingerEvent;
import wiitracker.fingertracking.FingerListener;
import wiitracker.fingertracking.PointType;

/**
 * Listens for points from the Wiimote and draws them to the screen.
 * 
 */
public class SwingPointTracker extends JPanel implements FingerListener {

	private final Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5847175429104608669L;

	/**
	 * The points we've seen as of the last update. The Wiimote has 4 "slots"
	 * that it uses to report IR points.
	 */
	private Finger[] points = new Finger[PointType.NUMBER_OF_FINGERS];

	/**
	 * Maximum X coordinate.
	 */
	private static final int xAdjust = 1024;

	/**
	 * Maximum Y coordinate.
	 */
	private static final int yAdjust = 768;

	/**
	 * The image for the map on which we are displaying.
	 */
	// TODO Don't hardcode the image.
	private ImageIcon image = new ImageIcon("resources/map.png");

	private int[][] cornerarray = new int[4][2];
	private int cornerindex;

	private boolean showMap = false;

	/**
	 * Create a new point tracker.
	 */
	public SwingPointTracker() {
		super();
		Dimension d = new Dimension(1024, 768);
		this.setPreferredSize(d);
		this.setSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		this.setBackground(Color.BLACK);

		for (int i = 0; i < 4; ++i) {
			points[i] = new Finger();
		}
	}

	/**
	 * Draws the 4 points from the last Wiimote update.<br/>
	 * Slot 0 is drawn in Red.<br/>
	 * Slot 1 is drawn in Green.<br/>
	 * Slot 2 is drawn in Blue.<br/>
	 * Slot 3 is drawn in White.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// Paint background image, if we have one.
		if (showMap && image != null) {
			image.paintIcon(this, g, 0, 0);
		}

		g.setColor(Color.GRAY);
		g.drawRect(0, 0, 1024, 768);

		for (Finger f : points) {
				log.debug("Drawing " + f.getType().toString() + " at (" + (int) Math.round(f.x) + ", " + (int) Math.round(yAdjust - f.y) + ")");
				g.setColor(f.getType().color);
				g.fillOval((int) Math.round(f.x), (int) Math.round(yAdjust - f.y), 10, 10);
		}

		// paint corners for CalibrationUI
		g.setColor(Color.MAGENTA);
		for (int i = 0; i < cornerindex; i++) {
			g.fillOval(cornerarray[i][0], yAdjust - cornerarray[i][1], 10, 10);
		}

	}

	/**
	 * This is called whenever the Wiimote senses a change in the position of an
	 * IR point. It paints the new changes.
	 */
	public void irImageChanged(IrCameraEvent evt) {
		for (int i = 0; i < 4; ++i) {
			// points[i] = evt.getIrPoint(i);
		}
		repaint();
	}

	public Finger[] getPointArray() {
		return points;
	}

	// paint the selected corners of the map onto the screen for reference
	// during calibration
	public void updateCalibrationPoints(boolean increase, Stack<IrPoint> p) {
		if (increase) {
			if (cornerindex < 4) {
				cornerarray[cornerindex][0] = p.peek().x;
				cornerarray[cornerindex][1] = p.peek().y;
				cornerindex++;
			}
		} else {
			cornerindex--;
		}
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public void setMapVisible(boolean showMap) {
		this.showMap = showMap;
	}

	public boolean isMapVisible() {
		return showMap;
	}

	public void fingerChanged(FingerEvent evt) {
		log.debug("Got a FingerEvent!");
		points = evt.getFingers();
		repaint();
	}

}
