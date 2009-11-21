package wiitracker.fingertracking;

import java.awt.geom.Point2D;

import javax.swing.event.EventListenerList;

public class FingerLabeler implements FingerListener, FingerNotifier {
	EventListenerList listenerList = new EventListenerList();
	boolean doLabel;

	// Finger[] fingers = new Finger[4];
	long[] lastSeen = new long[4];

	public void fingerChanged(FingerEvent evt) {
		doLabel = true;
		int[] order = new int[4]; // order[i] is the number of the finger
		// corresponding to the point i.
		Finger[] in = evt.getFingers();
		
		for (int i = 0; i < 4; i++) {
			if (! in[i].getType().isKnown()) {
				doLabel = false;
				order[i] = -1;
			}
		}

		
		//if all four fingers are visible, label the fingers relative to the thumb
		if(doLabel) {
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
					if (thisFingerMin > distances[i][j] && distances[i][j] > 0.000001) {
						thisFingerMin = distances[i][j];
						perFingerMin[i] = j;

					}
				}
			}
			boolean isIThumb = d(in[maxI], in[perFingerMin[maxI]]) > d(in[maxJ],
					in[perFingerMin[maxJ]]);
			int thumb = isIThumb ? maxI : maxJ, ring = isIThumb ? maxJ : maxI;
			
			order[thumb] = 0;
			order[perFingerMin[thumb]] = 1;
			order[perFingerMin[ring]] = 2;
			order[ring] = 3;
			in[thumb].setType(PointType.THUMB); 
			in[perFingerMin[thumb]].setType(PointType.INDEX);
			in[perFingerMin[ring]].setType(PointType.MIDDLE); 
			in[ring].setType(PointType.RING);

			FingerListener[] listeners = listenerList.getListeners(FingerListener.class);
			
			FingerEvent event = new FingerEvent(in);
			for (FingerListener l : listeners) {
				l.fingerChanged(event);
			}
		}
		else {
			
		}

	}

	private float d(Point2D a, Point2D b) {
		return (float) Math.sqrt(Math.pow(a.getX() - b.getX(), 2)
				+ Math.pow(a.getY() - b.getY(), 2));
	}

	public void addFingerListener(FingerListener listener) {
		listenerList.add(FingerListener.class, listener);
	}

}
