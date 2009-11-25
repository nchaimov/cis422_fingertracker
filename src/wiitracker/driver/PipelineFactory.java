package wiitracker.driver;

import wiitracker.fingertracking.FingerConverter;
import wiitracker.fingertracking.FingerLabeler;
import wiitracker.fingertracking.FingerNotifier;
import wiitracker.fingertracking.IrCameraNotifier;
import wiitracker.fingertracking.TransformNotifier;
import motej.Mote;
/**
 * 
 * Constructs the data processing pipeline for the program to use. All point data comes through this pipeline.
 * The pipeline uses all the relevant classes for manipulating point data from the remote.
 * Direct manipulation of the points should be done by classes added to the pipeline in this class.
 * A pipeline ends with a single FingerNotifier. More than one FingerListener can be added to listen to the events.
 * 
 * @author Group7
 *
 */
public class PipelineFactory {

	public PipelineFactory() {
	}

	/**
	 * Constructs a pipeline around a Mote. The transform is singleton, so editing the transform is done by retrieving the singleton TransformNotifier.
	 * @param mote - The Mote feeding into the pipeline.
	 * @return A FingerNotifier that reports the output of the pipeline to all listeners.
	 */
	public static FingerNotifier createPipe(Mote mote) {
		FingerConverter fingerConverter = new FingerConverter(mote);
		
		TransformNotifier transformNotifier = TransformNotifier.getInstance();
		fingerConverter.addFingerListener(transformNotifier);
		
		FingerLabeler fingerLabel = new FingerLabeler();
		transformNotifier.addFingerListener(fingerLabel);
		
		return fingerLabel;
	}
}