package wiitracker.driver;

import wiitracker.fingertracking.FingerConverter;
import wiitracker.fingertracking.FingerLabeler;
import wiitracker.fingertracking.FingerNotifier;
import wiitracker.fingertracking.IrCameraNotifier;
import wiitracker.fingertracking.TransformNotifier;
import motej.Mote;

public class PipelineFactory {

	public PipelineFactory() {
	}

	public static FingerNotifier createPipe(Mote mote) {
		FingerConverter fingerConverter = new FingerConverter(mote);
		
		TransformNotifier transformNotifier = TransformNotifier.getInstance();
		fingerConverter.addFingerListener(transformNotifier);
		
		FingerLabeler fingerLabel = new FingerLabeler();
		transformNotifier.addFingerListener(fingerLabel);
		
		return fingerLabel;
	}

	

}