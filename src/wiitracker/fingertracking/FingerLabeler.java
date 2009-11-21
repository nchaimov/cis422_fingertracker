package wiitracker.fingertracking;

import java.awt.geom.Point2D;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

public class FingerLabeler implements FingerListener, FingerNotifier {
	private final Logger log = Logger.getLogger(this.getClass());
	
	EventListenerList listenerList = new EventListenerList();
	boolean doLabel;

	// Finger[] fingers = new Finger[4];

	public void fingerChanged(FingerEvent evt) {
		
		log.debug("Processing a finger event");
		
		doLabel = true;
		PointType[] order = new PointType[4]; // order[i] is the number of the finger corresponding to the point i.
		Finger[] in = evt.getFingers();
		
		for (int i = 0; i < 4; i++) {
			if (! in[i].getType().hasPosition()) {
				doLabel = false;
				order[i] = PointType.UNKNOWN;
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
			
			order[thumb] = PointType.THUMB;
			order[perFingerMin[thumb]] = PointType.INDEX;
			order[perFingerMin[ring]] = PointType.MIDDLE;
			order[ring] = PointType.RING;
			in[thumb].setType(PointType.THUMB); 
			in[perFingerMin[thumb]].setType(PointType.INDEX);
			in[perFingerMin[ring]].setType(PointType.MIDDLE); 
			in[ring].setType(PointType.RING);
		}
		else {
			for (int i = 0; i < PointType.NUMBER_OF_FINGERS; i++)
			{
				if (order[i] != null && order[i].value >= 0) in[i].setType(order[i]);
			}

		}
		
		FingerListener[] listeners = listenerList.getListeners(FingerListener.class);
		
		for(int i = 0; i < in.length; ++i) {
			log.debug("Labeled finger; " + in[i].toString());
		}
		
		FingerEvent event = new FingerEvent(in);
		for (FingerListener l : listeners) {
			l.fingerChanged(event);
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
