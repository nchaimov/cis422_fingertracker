package wiitracker.driver;

import java.awt.Component;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

import motej.IrCameraMode;
import motej.IrCameraSensitivity;
import motej.Mote;
import motej.request.ReportModeRequest;

import org.apache.log4j.PropertyConfigurator;

import wiitracker.fingertracking.IrCameraNotifier;
import wiitracker.ui.CalibrationUI;
import wiitracker.ui.MoteFinderUI;
import wiitracker.ui.PointTrackerUI;

public class Driver {

	private static IrCameraNotifier pipeline;
	private static Mote mote;
	public static void enableMote(Mote mote) {
		try {
			// Turn on the Wiimote IR camera.
			// Uses the "Full" report format, which provides
			// X and Y coordinates, a "size" value, X and Y bounding
			// boxes, and intensity for the 4 brightest points in the
			// field of view of the Wiimote camera. Use a high
			// sensitivity.
			Driver.mote = mote;
			boolean[] playerLeds = { true, false, false, true };
			
			mote.setPlayerLeds(playerLeds);
			mote.enableIrCamera(IrCameraMode.FULL, IrCameraSensitivity.values()[1]);

			// Set the report mode, which specifies the format the Wiimote uses
			// to communicate with us. Report 3E is needed to get Full reports
			// from the IR camera.
			mote.setReportMode(ReportModeRequest.DATA_REPORT_0x3e);

			pipeline = PipelineFactory.createPipe(mote);
			
			// initialize CalibrationUI 
			CalibrationUI calui = new CalibrationUI(mote, pipeline);
			calui.setVisible(true);
			calui.pack();

			

		} catch (RuntimeException ex) {
			System.err.println("A bad thing done happened.");
			ex.printStackTrace();
		}
	}

	public static void startPointTrackerUI(CalibrationUI calui, Point2D[] corners) {
		// Start the Swing UI.
		
		PointTrackerUI ui = new PointTrackerUI(mote, pipeline, corners);
		ui.setVisible(true);
		calui.setVisible(false);
		ui.pack();
	}

	public static void errorPopup(Component parent) {
		JOptionPane.showMessageDialog(parent, "Bluetooth Device is not ready", "Bluetooth Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * MAC address for the Wiimote's Bluetooth controller.
	 */
	public static final String BLUETOOTH_ADDRESS = "0022D7C0A8A6";

	/**
	 * 
	 * Connects to the Wiimote and starts the user interface.
	 * 
	 * @param args
	 *            Ignored
	 */
	public static void main(String[] args) {

		PropertyConfigurator.configure("log4j.properties");

		// Workaround for bug in Bluecove which prevents connection
		// to the Wiimote.
		System.setProperty("bluecove.jsr82.psm_minimum_off", "true");

		MoteFinderUI finderUI = new MoteFinderUI();
		finderUI.pack();
		finderUI.setVisible(true);
		try {
			finderUI.setMoteFinder();
		} catch (RuntimeException e) {
			errorPopup(finderUI);
			System.exit(1);
		}

	}
}
