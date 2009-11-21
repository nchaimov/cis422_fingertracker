package wiitracker.fingertracking;

import java.util.ArrayList;


public class FingerEvent {

		Finger[] fingers = new Finger[PointType.NUMBER_OF_FINGERS];
		ArrayList <Finger> unknowns = new ArrayList <Finger>();
		
		public FingerEvent(Finger[] inFingers) {
			for (Finger finger : inFingers)	{
				if (finger.getType() == PointType.UNKNOWN) unknowns.add(finger);
				fingers[finger.getType().value] = (Finger) finger.clone();
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
			Finger[] outFingers = new Finger[PointType.NUMBER_OF_FINGERS];
			int i = 0;
			
			for(int j = 0; j < PointType.NUMBER_OF_FINGERS; j++)
				if ((outFingers[i] = fingers[j]) != null) i++;
			
			for (int j = 0; j < unknowns.size(); j++)
				outFingers[i] = unknowns.get(j);
			
			while (i < PointType.NUMBER_OF_FINGERS)
				outFingers[i++] = new Finger(PointType.OFF_SCREEN);
			
			return outFingers;
		}
}