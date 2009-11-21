package wiitracker.fingertracking;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import motej.Mote;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;
import motej.IrPoint;

public class FingerConverter implements FingerNotifier, IrCameraListener {
	
	private final Logger log = Logger.getLogger(this.getClass());
	
	private static final int 	X_OFF_SCREEN = 1023,
								Y_OFF_SCREEN = 1023;
	EventListenerList listenerList = new EventListenerList();
	
	public FingerConverter(Mote mote) {
		mote.addIrCameraListener(this);
	}

	public void addFingerListener(FingerListener listener) {
		listenerList.add(FingerListener.class, listener);
		
	}

	public synchronized void irImageChanged(IrCameraEvent evt) {
		IrPoint[] in = new IrPoint[4];
		for(int i = 0; i < 4; ++i) {
			in[i] = evt.getIrPoint(i);
		}
		
		IrPoint temp;
		Finger[] out = new Finger[4];
		for (int i = 0; i < 4; i ++) {
			if (isOnScreen(evt.getIrPoint(i))) 
				out[i] = new Finger(evt.getIrPoint(i), PointType.UNKNOWN);
			else out[i] = new Finger(evt.getIrPoint(i), PointType.OFF_SCREEN);
		}
		FingerListener[] listeners = listenerList.getListeners(FingerListener.class);
		FingerEvent event = new FingerEvent(out);
		
		for(int i = 0; i < 4; ++i) {
			log.debug("In: " + i + " (" + in[i].x + ", " + in[i].y + ")");
			log.debug("Out: " + out[i].toString());
		}
		
		for (FingerListener l : listeners) {
			l.fingerChanged(event);
		}
	}
	
	private boolean isOnScreen(IrPoint point) {
		return (point.getX() != X_OFF_SCREEN) || (point.getY() != Y_OFF_SCREEN);
	}
}
