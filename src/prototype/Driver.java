package prototype;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;

import motej.*;
import motej.event.*;
import motej.request.ReportModeRequest;

public class Hellthissucks {
	static ArrayList<Mote> motes = new ArrayList<Mote>();
	public static void main(String[] arrrgs) throws InterruptedException
	{
		
		/*
		JFrame frame = new JFrame("WiiMote Point Tracker");
		SwingPointTracker tracker = new SwingPointTracker();
		frame.add(tracker);
		Dimension size = new Dimension(1024,768);
		frame.setSize(size);
		frame.setPreferredSize(size);
		frame.setMinimumSize(size);
		frame.pack();
		frame.setVisible(true);
		*/
		
		System.setProperty("bluecove.jsr82.psm_minimum_off", "true");
		Mote mote = new Mote("00191da7389d");
/*		MoteFinderListener listener = new MoteFinderListener() {
			
			public void moteFound(Mote mote) {
				System.out.println("Found mote: " + mote.getBluetoothAddress());
				mote.rumble(2000l);
				motes.add(mote);
			}
		};
*/		

		
		
/*		MoteFinder finder = MoteFinder.getMoteFinder();
		finder.addMoteFinderListener(listener);
		
		finder.startDiscovery();
		Thread.sleep(300000l);
		finder.stopDiscovery();
		System.out.println(motes.size());
*/		IrCameraListener cameraListener = new IrCameraListener() {
			public void irImageChanged(IrCameraEvent evt){
				//System.out.println("X = " + evt.getIrPoint(0).x + ", Y = " + evt.getIrPoint(0).y);
				
			}
		};

		//Mote mote = motes.get(0);
		mote.enableIrCamera(IrCameraMode.FULL, IrCameraSensitivity.WII_LEVEL_5);
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x3e);
		//mote.rumble(2);
		//Thread.sleep(20001);
		//mote.setReportMode(ReportModeRequest.DATA_REPORT_0x36);
		
		
		PointTrackerUI ui = new PointTrackerUI(mote);
		//mote.addIrCameraListener(cameraListener);
		
		Thread.sleep(6000001);
	}
}
