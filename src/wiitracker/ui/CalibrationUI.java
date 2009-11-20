package wiitracker.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import wiitracker.driver.Driver;
import wiitracker.fingertracking.FingerLabeler;
import wiitracker.fingertracking.IrCameraNotifier;
import wiitracker.fingertracking.TransformNotifier;
import wiitracker.ui.PointTrackerUI.MoteSettingsPanel;

import motej.IrCameraMode;
import motej.IrPoint;
import motej.Mote;
import motej.event.MoteDisconnectedEvent;
import motej.event.MoteDisconnectedListener;
/**
 * Creates a window for gathering the corners of the map. Sends the data off to be
 * used for Perspective Transformation.
 * @author mbintz
 *
 */
public class CalibrationUI extends JFrame implements MoteDisconnectedListener {
	/**
	 * The panel which draws the points. 
	 */
	private static SwingPointTracker tracker;
	private static IrPoint[] points;
	private Stack<IrPoint> pointStack;
	/**
	 * Array of corners sent to be transformed.
	 */
	private Point2D[] pointarray = new Point2D[4];
	/**
	 * Done gathering corner points
	 */
	private boolean finished;
	/**
	 * Panel which provides buttons for adding/deleting corner points and one to declare your 
	 * finished gathering corner points.
	 * @author mbintz
	 *
	 */
	protected class MapCalibrationPanel extends JPanel {

		private static final long serialVersionUID = -1893034921967850816L;

		private Mote mote;
		private JButton setPoint;
		private JButton delPoint;
		private JButton done;
		private JButton showStack;


		public MapCalibrationPanel(Mote mote) {
			
			super();
			this.setLayout(new GridBagLayout());

			Dimension d = new Dimension(1024, 100);
			this.setSize(d);
			this.setPreferredSize(d);
			this.setMinimumSize(d);
			this.setMaximumSize(d);

			GridBagConstraints c = new GridBagConstraints();

			this.mote = mote;
			finished = false;

			pointStack = new Stack<IrPoint>();

			setPoint = new JButton("Set Point");
			delPoint = new JButton("Delete Point");
			done = new JButton("Done");

			ActionListener buttonListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton b = (JButton) e.getSource();
					if (b.equals(setPoint) && (pointStack.size() < 4)) {
						points = tracker.getPointArray();
						if (points[1].x == 1023 && points[1].y ==1023 ){
							JOptionPane.showMessageDialog(MapCalibrationPanel.this,
									"Corner Point not in viewable area of Wii Remote", "Corner Point Collection Problem", JOptionPane.WARNING_MESSAGE);
						}
						else {
							
							pointStack.push(new IrPoint(points[1].x, points[1].y));
							tracker.updateCalibrationPoints(true, pointStack);
						}
					} else if (b.equals(delPoint)) {
						if (!pointStack.isEmpty()) { 
							pointStack.pop(); 
							tracker.updateCalibrationPoints(false, pointStack);
						}
					}
					else if (b.equals(done)) {
						sendData(pointStack);
						if(finished) { Driver.startPointTrackerUI(CalibrationUI.this, pointarray); }
					}
				}
			};

			setPoint.addActionListener(buttonListener);
			delPoint.addActionListener(buttonListener);
			done.addActionListener(buttonListener);

			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weighty = 1;

			this.add(setPoint, c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weighty = 1;

			this.add(delPoint, c);

			c.gridx = 2;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weighty = 1;

			this.add(done, c);

		}
	};
/**
 * Creates the UI for gathering points from the Wii Remote to be used as
 * the corner points.
 * 
 * @param m
 * 			The Wiimote to get data from and control.
 * @param pipeline
 */
	public CalibrationUI(Mote m, IrCameraNotifier pipeline) {
		// super("WiiMote Point Tracker");
		this.getContentPane().setLayout(new BorderLayout());

		// The settings panel lets us change sensitivity and start/stop XML
		// writing.
		MapCalibrationPanel settingsPanel = new MapCalibrationPanel(m);
		this.getContentPane().add(settingsPanel, BorderLayout.SOUTH);

		tracker = new SwingPointTracker();

		Dimension size = new Dimension(1024, 824);
		this.setSize(size);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.getContentPane().add(tracker, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pipeline.addIrCameraListener(tracker);
		m.addMoteDisconnectedListener(this);
	}
	
	public void sendData(Stack<IrPoint> stack) {
		if(pointStack.size() < 4) {
			JOptionPane.showMessageDialog(this,
				pointStack.size() + " points have been entered\n" + "Please enter 4 corner points before proceeding",
				"Insufficient Data", JOptionPane.WARNING_MESSAGE);
		}
		else {
			for(int i=0; i<4; i++) {
				if(!pointStack.isEmpty()) { pointarray[i] = pointStack.pop(); }
			}
			finished = true;
		}
	}

	public void moteDisconnected(MoteDisconnectedEvent evt) {
		JOptionPane.showMessageDialog(this,
				"The finger tracking software has lost its connection to the Wiimote!",
				"Connection Lost", JOptionPane.ERROR_MESSAGE);
	}
}
