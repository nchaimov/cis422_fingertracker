package wiitracker.fingertracking;

public interface FingerNotifier {
	/**
	 * Adds listeners to a list. Should be implemented by anything that intends to fire FingerEvents to another class.
	 * 
	 * Implementing classes should call listener.fingerChanged(evt) for every event to be sent.
	 * @param listener The listener to be added.
	 */
	public void addFingerListener(FingerListener listener);
}
