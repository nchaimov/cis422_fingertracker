package wiitracker.fingertracking;

import java.awt.Point;
import java.awt.geom.Point2D;

import javax.media.jai.PerspectiveTransform;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import motej.event.IrCameraListener;
/**
 * 
 * This singleton class wraps the PerspectiveTransform class for detail hiding and to make it fit in the pipeline.
 * PerspectiveTransform creates a projective transform from one plane in space to another. Specifically, since the map is identical to the image, it takes any point from the camera, and returns its location relative to the map.
 * 
 * @author areinder
 *
 */
public class TransformNotifier implements FingerListener, FingerNotifier {
	
	private final Logger log = Logger.getLogger(this.getClass());
	
	private static final TransformNotifier INSTANCE = new TransformNotifier(); 
	protected PerspectiveTransform transform = new PerspectiveTransform();
	EventListenerList listenerList = new EventListenerList();

	private TransformNotifier() {}
	
	/**
	 * Template-required irImageChanged method. Transforms all points in evt and passes a new event to all listeners containing the new points.
	 */
	public void fingerChanged(FingerEvent evt) {
		Finger[] in = evt.getFingers();
		
		for(int i = 0; i < in.length; ++i) {
			log.debug("In (before transform): " + in[i].toString());
		}

		
		Finger[] out = (Finger[]) this.transform(in);		
		
		for(int i = 0; i < in.length; ++i) {
			log.debug("In: " + in[i].toString());
			log.debug("Out: " + out[i].toString());
		}
		
		FingerListener[] listeners = listenerList.getListeners(FingerListener.class);
		FingerEvent event = new FingerEvent(out);
		for (FingerListener l : listeners) {
			l.fingerChanged(event);
		}
	}

	private void setTransform(PerspectiveTransform p) {
		transform.setTransform(p);
	}
	
	
	/**
	 * Sets the transform to transform from four selected points on the physical map to the same four points on the map image.
	 * Points should be given either clockwise or counter-clockwise around the map, starting from any corner. 
	 * @param fromMote 	Point2D data from the camera for the four corners.
	 * @param fromMap	Pixel locations for the four corners.
	 */
	
	public void setTransform(Point2D[] fromMote, Point2D[] fromMap) {
		this.setTransform(PerspectiveTransform.getQuadToQuad(
				fromMote[0].getX(), fromMote[0].getY(),
				fromMote[1].getX(), fromMote[1].getY(),
				fromMote[2].getX(), fromMote[2].getY(),
				fromMote[3].getX(), fromMote[3].getY(), 
				
				fromMap[0].getX(), fromMap[0].getY(),
				fromMap[1].getX(), fromMap[1].getY(),
				fromMap[2].getX(), fromMap[2].getY(),
				fromMap[3].getX(), fromMap[3].getY()));
	}

	/**
	 * Transforms the points in array points using the current transform.
	 * Transforms them in-place, and returns the array that was passed in.
	 * @param points Points to be transformed.
	 * @return points - the transformed points, in the same array.
	 */
	public Point2D[] transform(Point2D[] points) {
		Point2D[] newPoints = points.clone();
		for (int i = 0; i < newPoints.length; i++) {
			newPoints[i] = (Point2D) points[i].clone();
		}
		transform.transform(points, 0, newPoints, 0, points.length);
		return newPoints;
	}
	
	/**
	 * Adds a FingerListener to be notified.
	 */
	public void addFingerListener(FingerListener listener) {
		listenerList.add(FingerListener.class, listener);
	}
	
	/**
	 * Returns the single instance of the TransformNotifier class.
	 * @return
	 */
	public static TransformNotifier getInstance() {
		return INSTANCE;
	}
}
