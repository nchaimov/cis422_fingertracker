package prototype;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import motej.IrCameraMode;
import motej.IrCameraSensitivity;
import motej.Mote;

public class PointTrackerUI extends JFrame {

	private SwingPointTracker tracker;
	
	protected static class MoteSettingsPanel extends JPanel {
		
		private Mote mote;
		
		public MoteSettingsPanel(Mote m) {
			super();
			
			this.mote = m;
			
			JButton b1 = new JButton("1");
			JButton b2 = new JButton("2");
			JButton b3 = new JButton("3");
			JButton b4 = new JButton("4");
			JButton b5 = new JButton("5");
			
			ActionListener listener = new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int sensitivity = Integer.parseInt(((JButton) e.getSource()).getText());
					System.out.println("Setting sensitivity to " + sensitivity);
					mote.disableIrCamera();
					mote.enableIrCamera(IrCameraMode.FULL, IrCameraSensitivity.values()[sensitivity]);
				}
				
			};
			
			b1.addActionListener(listener);
			b2.addActionListener(listener);
			b3.addActionListener(listener);
			b4.addActionListener(listener);
			b5.addActionListener(listener);
			
			this.add(b1);
			this.add(b2);
			this.add(b3);
			this.add(b4);
			this.add(b5);
		}
	}
	
	public PointTrackerUI(Mote m) {
		super("WiiMote Point Tracker");
		tracker = new SwingPointTracker();
		Dimension size = new Dimension(1024,868);
		this.setSize(size);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.pack();
		this.setVisible(true);
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(tracker, BorderLayout.CENTER);
		
		MoteSettingsPanel settingsPanel = new MoteSettingsPanel(m);
		
		this.getContentPane().add(settingsPanel, BorderLayout.SOUTH);
		
		m.addIrCameraListener(tracker);
	}
	
	public SwingPointTracker getSwingPointTracker() {
		return tracker;
	}
}
