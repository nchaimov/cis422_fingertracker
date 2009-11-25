package wiitracker.fingertracking;

import java.util.EventListener;
/**
 * Interface implemented by anything that listens to a FingerNotifier.
 * @author areinder
 *
 */
public interface FingerListener extends EventListener {
	/**
	 * Method to be called by any FingerNotifier the implementing class is listening to.
	 * @param evt Contains incoming points.
	 */
	public void fingerChanged(FingerEvent evt);
}
