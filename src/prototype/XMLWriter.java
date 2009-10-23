package prototype;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import motej.IrPoint;
import motej.Mote;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;
import motej.event.MoteDisconnectedEvent;
import motej.event.MoteDisconnectedListener;

/**
 * Writes IR points to a file in XML format.
 * 
 * Based upon the code from the existing video finger tracker.
 */
public class XMLWriter implements IrCameraListener, MoteDisconnectedListener<Mote> {

	/**
	 * Maximum X coordinate.
	 */
	static final int xAdjust = 1024;

	/**
	 * Maximum Y coordinate.
	 */
	static final int yAdjust = 768;

	/**
	 * The time that we started writing.
	 */
	private long startTime;

	/**
	 * Are we now writing to a file?
	 */
	private boolean active;

	/**
	 * What is the current time?
	 */
	private long time;

	/**
	 * File output stream used to write to a file.
	 */
	private FileOutputStream fos = null;

	/**
	 * Line separator used by this operating system.
	 */
	private static String LineSep = System.getProperty("line.separator");

	/**
	 * Create a new XML writer.
	 * 
	 * @param date
	 *            Text representation of today's date (used for header).
	 * @param dataPath
	 *            Path where the output file should be written.
	 * @throws FileNotFoundException
	 */
	public XMLWriter(String date, String dataPath) throws FileNotFoundException {
		active = true;
		this.fos = new FileOutputStream("./" + dataPath);
		startTime = System.currentTimeMillis();
		String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + LineSep;
		String root = "<coordStream StartDateTime=\"" + date + "\">" + LineSep;
		try {
			fos.write(head.getBytes("UTF-8"));
			fos.write(root.getBytes("UTF-8"));
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * output &lt;/coordStream&gt; at the end of the xml file
	 */
	public void outputXML() {
		String info = "</coordStream>" + LineSep;
		try {
			fos.write(info.getBytes("UTF-8"));
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * add element event as &lt;event fingerID="1" time="33.338" X="100" Y="200"
	 * /&gt;
	 * 
	 * @param FId
	 *            Finger ID number
	 * @param X
	 *            X coordinate of the event
	 * @param Y
	 *            Y coordinate of the event.
	 */
	public void addEvent(String FId, int X, int Y) {
		time = System.currentTimeMillis() - startTime;

		String info = "  <event fingerID=\"" + FId + "\" time=\"" + time + "\" X=\"" + X
				+ "\" Y=\"" + Y + "\" />" + LineSep;
		try {
			fos.write(info.getBytes("UTF-8"));
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when the Wiimote senses a change in the position of an IR point.
	 * Writes the event to a file, if we are currently writing.
	 */
	public void irImageChanged(IrCameraEvent evt) {
		if (active) {
			for (int i = 0; i < 4; i++) {
				IrPoint pt = evt.getIrPoint(i);
				addEvent(Integer.valueOf(i + 1).toString(), (int) pt.getX(), (int) (yAdjust - pt
						.getY()));
			}
		}
	}

	/**
	 * Called when the Wiimote disconnects. Finishes writing the XML file.
	 */
	public void moteDisconnected(MoteDisconnectedEvent<Mote> evt) {
		stopWrite();
	}

	/**
	 * Write the end of the XML file and stop processing points.
	 */
	public void stopWrite() {
		active = false;
		outputXML();
	}
}