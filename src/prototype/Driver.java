package prototype;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

import motej.IrCameraMode;
import motej.IrCameraSensitivity;
import motej.Mote;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;
import motej.request.ReportModeRequest;

public class Driver {
	static ArrayList<Mote> motes = new ArrayList<Mote>();

	public static void main(String[] arrrgs) throws InterruptedException {

		System.setProperty("bluecove.jsr82.psm_minimum_off", "true");
		Mote mote = new Mote("00191da7389d");
		
		/*
		 * MoteFinderListener listener = new MoteFinderListener() {
		 * 
		 * public void moteFound(Mote mote) { System.out.println("Found mote: "
		 * + mote.getBluetoothAddress()); mote.rumble(2000l); motes.add(mote); }
		 * };
		 */

		/*
		 * MoteFinder finder = MoteFinder.getMoteFinder();
		 * finder.addMoteFinderListener(listener);
		 * 
		 * finder.startDiscovery(); Thread.sleep(300000l);
		 * finder.stopDiscovery(); System.out.println(motes.size());
		 */
		
		mote.enableIrCamera(IrCameraMode.FULL, IrCameraSensitivity.values()[1]);
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x3e);

		PointTrackerUI ui = new PointTrackerUI(mote);
		Thread.sleep(6000001);
	}
}
