package prototype;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import motej.IrCameraMode;
import motej.IrCameraSensitivity;
import motej.Mote;

public class PointTrackerUI extends JFrame {

	private SwingPointTracker 	tracker;
	private static XMLWriter 	writer;
	private static boolean		writing;
	
	protected static class MoteSettingsPanel extends JPanel {
		
		private Mote 		mote;
		private JButton		XMLstart;
		
		public MoteSettingsPanel(Mote m) {
			super();
			this.setLayout(new GridBagLayout());
			
			Dimension d = new Dimension(1024, 100);
			this.setSize(d);
			this.setPreferredSize(d);
			this.setMinimumSize(d);
			this.setMaximumSize(d);
			
			GridBagConstraints c = new GridBagConstraints();
			
			this.mote 	= m;
			writing 	= false;
			
			
			JLabel sensitivityLabel = new JLabel("Sensitivity: ");
			JButton b1 	= new JButton("1");
			JButton b2 	= new JButton("2");
			JButton b3	= new JButton("3");
			JButton b4 	= new JButton("4");
			JButton b5 	= new JButton("5");
			XMLstart = new JButton("XML start");
			
			ActionListener senstivityListener = new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					int sensitivity = Integer.parseInt(((JButton) e.getSource()).getText());
					System.out.println("Setting sensitivity to " + sensitivity);
					mote.disableIrCamera();
					mote.enableIrCamera(IrCameraMode.FULL, IrCameraSensitivity.values()[sensitivity]);
				}
				
			};
			
			ActionListener xmlListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!writing) {
						try {
							writer = new XMLWriter(new Date().toString(), "./data.xml");
							mote.addIrCameraListener(writer);
							mote.addMoteDisconnectedListener(writer);
							writing = true;
							((JButton)e.getSource()).setText("XML stop");
						}
						catch (FileNotFoundException ex) {
							ex.printStackTrace();
						}
					}
					else { 
						writer.stopWrite();
						writing = false;
						((JButton)e.getSource()).setText("XML start");
					}
				}
			};
			
			b1.addActionListener(senstivityListener);
			b2.addActionListener(senstivityListener);
			b3.addActionListener(senstivityListener);
			b4.addActionListener(senstivityListener);
			b5.addActionListener(senstivityListener);
			XMLstart.addActionListener(xmlListener);
			
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weighty = 1;
			
			this.add(sensitivityLabel, c);
			
			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weighty = 1;
			
			this.add(b1, c);
			
			c.gridx = 2;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weighty = 1;
			
			this.add(b2, c);
			
			c.gridx = 3;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weighty = 1;
			
			this.add(b3, c);
			
			c.gridx = 4;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weighty = 1;
			
			this.add(b4, c);
		
			c.gridx = 5;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weighty = 1;
			
			this.add(b5, c);
			
			c.gridx = 6;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weighty = 1;
			
			this.add(XMLstart, c);
			
		}
	}
	
	
	
	
	public PointTrackerUI(Mote m) {
		super("WiiMote Point Tracker");
		this.getContentPane().setLayout(new BorderLayout());
		MoteSettingsPanel settingsPanel = new MoteSettingsPanel(m);
		this.getContentPane().add(settingsPanel, BorderLayout.SOUTH);
		tracker = new SwingPointTracker();
		Dimension size = new Dimension(1024,824);
		this.setSize(size);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.getContentPane().add(tracker, BorderLayout.CENTER);	
		this.setVisible(true); 
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		
		
		
		m.addIrCameraListener(tracker);
	}
	
	public SwingPointTracker getSwingPointTracker() {
		return tracker;
	}
	
	public void setWriter(XMLWriter writer) { this.writer = writer; }
}