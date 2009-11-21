package wiitracker.fingertracking;

import java.util.ArrayList;
import java.util.Iterator;


public class FingerEvent {

		Finger[] fingers = new Finger[PointType.NUMBER_OF_FINGERS];
		ArrayList <Finger> unknowns = new ArrayList <Finger>();
		
		public FingerEvent(Finger[] inFingers) {
			for (Finger finger : inFingers)	{
				//if (finger.getType() == PointType.UNKNOWN) unknowns.add(finger);
				if (finger.getType().isKnown())
					fingers[finger.getType().value] = (Finger) finger.clone();
				else unknowns.add((Finger) finger.clone());
			}
		}

		public Finger getFinger(PointType fingerType) {
			Finger outFinger = fingers[fingerType.value];
			if (fingerType.isKnown() && outFinger.getType() != null) 
				return outFinger;
			return null;
		}
		
		
		public Finger[] getUnknowns() {
			return unknowns.toArray(new Finger[0]);
		}

		
		/**
		 * Returns an array of 
		 * @return The array of points.
		 */
		public Finger[] getFingers()
		{
			Finger[] outFingers = (Finger[]) fingers.clone();
			Iterator<Finger> unknownsIterator = unknowns.iterator();
			
			for(int i = 0; i < outFingers.length; ++i) {
				if(outFingers[i] == null && unknownsIterator.hasNext()) {
					outFingers[i] = unknownsIterator.next();
				}
			}
			
			return outFingers;
		}
}