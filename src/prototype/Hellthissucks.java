package prototype;

import java.util.ArrayList;

import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;

public class Hellthissucks {
	static ArrayList<Mote> motes = new ArrayList<Mote>();
	public static void main(String[] arrrgs) throws InterruptedException
	{
	    MoteFinderListener listener = new MoteFinderListener() {
	    	
	    	public void moteFound(Mote mote) {
	    		System.out.println("Found mote: " + mote.getBluetoothAddress());
	    		mote.rumble(2000l);
	    		motes.add(mote);
	    		
            }
            
	    };
            
    MoteFinder finder = MoteFinder.getMoteFinder();
    finder.addMoteFinderListener(listener);
    
    finder.startDiscovery();
    Thread.sleep(300000l);
    finder.stopDiscovery();
	}
}
