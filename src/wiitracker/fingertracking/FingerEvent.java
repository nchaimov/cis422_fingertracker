package wiitracker.fingertracking;

import java.util.ArrayList;
import java.util.Iterator;


public class FingerEvent {

		Finger[] fingers = new Finger[PointType.NUMBER_OF_FINGERS];
		ArrayList <Finger> unknowns = new ArrayList <Finger>();
		/**
		 * Makes a new event, containing all the points from the array. Can hold up to one of each known type.
		 * @param inFingers
		 */
		public FingerEvent(Finger[] inFingers) {
			for (Finger finger : inFingers)	{
				//if (finger.getType() == PointType.UNKNOWN) unknowns.add(finger);
				if (finger.getType().isKnown())
					fingers[finger.getType().value] = (Finger) finger.clone();
				else unknowns.add((Finger) finger.clone());
			}
		}

		/**
		 * Returns the finger corresponding to any PointType fingerType.isKnown() is true.
		 * @param fingerType - The type of the finger requested.
		 * @return A Finger, of type fingerType, if fingerType.isKnown. Otherwise, returns null
		 */
		public Finger getFinger(PointType fingerType) {
			Finger outFinger = fingers[fingerType.value];
			if (fingerType.isKnown() && outFinger.getType() != null) 
				return outFinger;
			return null;
		}
		
		/**
		 * Gets all unlabeled, on-screen points from the event.
		 * 
		 * @return
		 */
		public Finger[] getUnknowns() {
			return unknowns.toArray(new Finger[0]);
		}

		
		/**
		 * Returns an array containing all on-screen points from the event.
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