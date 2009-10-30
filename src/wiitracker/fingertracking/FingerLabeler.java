package wiitracker.fingertracking;

import java.awt.geom.Point2D;

import javax.swing.event.EventListenerList;

import motej.IrPoint;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;
import wiitracker.ui.SwingPointTracker;

public class FingerLabeler implements IrCameraListener {
	EventListenerList listenerList = new EventListenerList();

	// Finger[] fingers = new Finger[4];
	long[] lastSeen = new long[4];

	public void irImageChanged(IrCameraEvent evt) {
		IrPoint[] in = new IrPoint[4];
		for (int i = 0; i < 4; i++) {
			in[i] = evt.getIrPoint(i);
		}

		float[][] distances = new float[4][4];
		float maxDistance = -1;
		int maxI = 0;
		int maxJ = 1;
		int[] perFingerMin = new int[4];
		float thisFingerMin = -1;
		for (int i = 0; i < 4; i++) {
			thisFingerMin = Integer.MAX_VALUE * (float) Math.sqrt(2);
			for (int j = 0; j < 4; j++) {
				distances[i][j] = d(in[i], in[j]);
				if (maxDistance < distances[i][j]) {
					maxI = i;
					maxJ = j;
					maxDistance = distances[i][j];
				}
				if (thisFingerMin > distances[i][j] && distances[i][j] > 0.5) {
					thisFingerMin = distances[i][j];
					perFingerMin[i] = j;

				}
			}
		}
		boolean isIThumb = d(in[maxI], in[perFingerMin[maxI]]) > d(in[maxJ], in[perFingerMin[maxJ]]);
		int thumb = isIThumb ? maxI : maxJ, pinky = isIThumb ? maxJ : maxI;
		IrCameraListener[] listeners = listenerList.getListeners(IrCameraListener.class);
		IrCameraEvent event = new IrCameraEvent(evt.getSource(), evt.getMode(), in[thumb],
				in[perFingerMin[thumb]], in[perFingerMin[pinky]], in[pinky]);
		for (IrCameraListener l : listeners) {
			l.irImageChanged(event);
		}
	}

	private float d(Point2D a, Point2D b) {
		return (float) Math.sqrt(Math.pow(a.getX() - b.getX(), 2)
				+ Math.pow(a.getY() - b.getY(), 2));
	}

	public void addIrCameraListener(SwingPointTracker tracker) {
		listenerList.add(IrCameraListener.class, tracker);

	}

}
