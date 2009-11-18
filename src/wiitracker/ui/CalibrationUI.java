package wiitracker.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import wiitracker.driver.Driver;
import wiitracker.fingertracking.FingerLabeler;
import wiitracker.ui.PointTrackerUI.MoteSettingsPanel;

import motej.IrCameraMode;
import motej.IrPoint;
import motej.Mote;
import motej.event.MoteDisconnectedEvent;
import motej.event.MoteDisconnectedListener;

public class CalibrationUI extends JFrame implements MoteDisconnectedListener {
	
	private static SwingPointTracker tracker;
	private static IrPoint[] points;
	
	protected class MapCalibrationPanel extends JPanel {

		private static final long serialVersionUID = -1893034921967850816L;


		private Mote mote;
		private JButton setPoint;
		private JButton delPoint;
		private JButton stop;
		private JButton showStack;
		private Stack<IrPoint> pointStack;


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
			
			pointStack = new Stack<IrPoint>();

			setPoint = new JButton("Set Point");
			delPoint = new JButton("Delete Point");
			stop = new JButton("Stop");
			//showStack = new JButton("Show Stack");

			ActionListener buttonListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton b = (JButton)e.getSource();
					if (b.equals(setPoint)) { 
						points = tracker.getPointArray();
						pointStack.push(new IrPoint(points[1].x, points[1].y));
					}
					else if (b.equals(delPoint)) {
						if (!pointStack.isEmpty()) { pointStack.pop(); }
					}
					//else if (b.equals(showStack)) {
					//	for (int i=0; i< pointStack.size(); i++) {
					//		System.out.println(pointStack.peek().x + " " + pointStack.pop().y);
					//	}
					else if (b.equals(stop)) {
						Driver.startPointTrackerUI(CalibrationUI.this);
					}
				}
			};
			
			setPoint.addActionListener(buttonListener);
			delPoint.addActionListener(buttonListener);
			stop.addActionListener(buttonListener);

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

			this.add(stop, c);
			
		}
	};
	
	public CalibrationUI(Mote m) {
		//super("WiiMote Point Tracker");
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
		m.addIrCameraListener(tracker);
	}


	public void moteDisconnected(MoteDisconnectedEvent evt) {
		JOptionPane.showMessageDialog(this,
				"The finger tracking software has lost its connection to the Wiimote!",
				"Connection Lost", JOptionPane.ERROR_MESSAGE);
	}
}



