package prototype;

import motej.IrCameraMode;
import motej.IrCameraSensitivity;
import motej.Mote;
import motej.request.ReportModeRequest;

public class Driver {

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

		// Workaround for bug in Bluecove which prevents connection
		// to the Wiimote.
		System.setProperty("bluecove.jsr82.psm_minimum_off", "true");

		// Attempt to establish a connection to the WiiMote
		Mote mote = new Mote(BLUETOOTH_ADDRESS);

		// Turn on the Wiimote IR camera.
		// Uses the "Full" report format, which provides
		// X and Y coordinates, a "size" value, X and Y bounding
		// boxes, and intensity for the 4 brightest points in the
		// field of view of the Wiimote camera. Use a high
		// sensitivity.
		mote.enableIrCamera(IrCameraMode.FULL, IrCameraSensitivity.values()[1]);

		// Set the report mode, which specifies the format the Wiimote uses
		// to communicate with us. Report 3E is needed to get Full reports
		// from the IR camera.
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x3e);

		// Start the Swing UI.
		PointTrackerUI ui = new PointTrackerUI(mote);
		ui.setVisible(true);
		ui.pack();

	}
}
