package prototype;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.*; 

import motej.IrPoint;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;

public class XMLWriter implements IrCameraListener {
	private long startTime;
	long time;
	private FileOutputStream fos = null;
	private static String LineSep = System.getProperty("line.separator");

	public XMLWriter(String date) {
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
 
	
	//output </coordStream> at the end of the xml file
	public void OutputXML() {
		String info = "</coordStream>" + LineSep;
		try {
			fos.write(info.getBytes("UTF-8"));
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//add element event as <event fingerID="1" time="33.338" X="100" Y="200" />
	public void AddEvent(String FId, int X, int Y) {
		time = System.currentTimeMillis() - startTime;

		String info = "  <event fingerID=\"" + FId + "\" time=\"" + time
						+ "\" X=\"" + X + "\" Y=\"" + Y + "\" />" + LineSep;
		try {
			fos.write(info.getBytes("UTF-8"));
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void irImageChanged(IrCameraEvent evt) {
			for (int i = 0; i < 4; i ++) {
					IrPoint pt = evt.getIrPoint(i);
					AddEvent(new Integer(i + 1).toString(), 
					(int) pt.getX(), (int) pt.getY());
			}
		// TODO Auto-generated method stub
		
	}
}