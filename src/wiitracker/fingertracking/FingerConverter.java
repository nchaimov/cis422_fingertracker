package wiitracker.fingertracking;

import javax.swing.event.EventListenerList;
import motej.Mote;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;
import motej.IrPoint;

public class FingerConverter implements FingerNotifier, IrCameraListener {
	
	private static final int 	X_OFF_SCREEN = 1023,
								Y_OFF_SCREEN = 1023;
	EventListenerList listenerList = new EventListenerList();
	
	public FingerConverter(Mote mote) {
		mote.addIrCameraListener(this);
	}

	public void addFingerListener(FingerListener listener) {
		listenerList.add(FingerListener.class, listener);
		
	}

	public void irImageChanged(IrCameraEvent evt) {
		IrPoint temp;
		Finger[] out = new Finger[4];
		for (int i = 0; i < 4; i ++) {
			if (isOnScreen(temp = evt.getIrPoint(i))) out[i] = new Finger(temp, PointType.UNKNOWN);
			else out[i] = new Finger(PointType.OFF_SCREEN);
		}
	}
	
	private boolean isOnScreen(IrPoint point) {
		return (point.getX() != X_OFF_SCREEN) || (point.getY() != Y_OFF_SCREEN);
	}
}
