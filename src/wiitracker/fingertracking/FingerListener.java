package wiitracker.fingertracking;

import java.util.EventListener;

public interface FingerListener extends EventListener {
	public void fingerChanged(FingerEvent evt);
}
