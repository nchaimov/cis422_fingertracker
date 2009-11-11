/*
 * Modified by Group 7
 * to return Strings instead of Mote objects
 */

/*
 * Copyright 2007-2008 Volker Fritzsch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package wiitracker.motefinder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.swing.event.EventListenerList;

import motej.MoteFinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.bluetooth.BlueCoveConfigProperties;

/**
 * 
 * <p>
 * 
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class MoteAddressFinder {

	// initialization on demand holder idiom
	private static class SingletonHolder {

		private static final MoteAddressFinder INSTANCE = new MoteAddressFinder();

	}

	/**
	 * Returns the <code>WiimoteFinder</code> instance.
	 * 
	 * @return WiimoteFinder
	 */
	public static MoteAddressFinder getMoteFinder() {
		try {
			// disable PSM minimum flag because the wiimote has a PSM below
			// 0x1001
			System.setProperty(BlueCoveConfigProperties.PROPERTY_JSR_82_PSM_MINIMUM_OFF, "true");

			SingletonHolder.INSTANCE.localDevice = LocalDevice.getLocalDevice();
			SingletonHolder.INSTANCE.discoveryAgent = SingletonHolder.INSTANCE.localDevice
					.getDiscoveryAgent();
			return SingletonHolder.INSTANCE;
		} catch (BluetoothStateException ex) {
			throw new RuntimeException(ex);
		}
	}

	private Logger log = LoggerFactory.getLogger(MoteFinder.class);

	private EventListenerList listenerList = new EventListenerList();

	private DiscoveryAgent discoveryAgent;

	private Set<String> bluetoothAddressCache = new HashSet<String>();

	protected final DiscoveryListener listener = new DiscoveryListener() {

		public void deviceDiscovered(final RemoteDevice device, DeviceClass clazz) {
			if (log.isInfoEnabled()) {
				try {
					log
							.info("found device: " + device.getFriendlyName(true) + " - "
									+ device.getBluetoothAddress() + " - "
									+ clazz.getMajorDeviceClass() + ":"
									+ clazz.getMinorDeviceClass() + " - "
									+ clazz.getServiceClasses());
				} catch (IOException ex) {
					log.error(ex.getMessage(), ex);
					throw new RuntimeException(ex);
				}
			}

			try {
				if (!device.getFriendlyName(true).startsWith("Nintendo")) {
					log.info("Device " + device.getFriendlyName(true) + " isn't a Wiimote");
					return;
				}
			} catch (IOException ex) {
				log.error(ex.getMessage(), ex);
				throw new RuntimeException(ex);
			}

			final String address = device.getBluetoothAddress();

			// is this already registered?
			if (!bluetoothAddressCache.contains(address)) {
				Thread connectThread = new Thread("connect: " + address) {
					@Override
					public void run() {
						// Mote mote = new Mote(address);
						fireMoteFound(address);
						bluetoothAddressCache.add(address);
					};
				};
				connectThread.start();
			}
		}

		public void inquiryCompleted(int discType) {
			if (discType == DiscoveryListener.INQUIRY_COMPLETED) {
				if (log.isInfoEnabled()) {
					log.info("inquiry completed");
				}
			}

			if (discType == DiscoveryListener.INQUIRY_TERMINATED) {
				if (log.isInfoEnabled()) {
					log.info("inquiry terminated");
				}
			}

			if (discType == DiscoveryListener.INQUIRY_ERROR) {
				if (log.isInfoEnabled()) {
					log.info("inquiry error");
				}
			}
		}

		public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
			log.debug("Service discovered.");
		}

		public void serviceSearchCompleted(int arg0, int arg1) {
			log.debug("Service search completed.");
		}
	};

	private LocalDevice localDevice;

	private MoteAddressFinder() {
	};

	public void addMoteFinderListener(MoteAddressFinderListener listener) {
		listenerList.add(MoteAddressFinderListener.class, listener);
	}

	protected void fireMoteFound(String mote) {
		MoteAddressFinderListener[] listeners = listenerList
				.getListeners(MoteAddressFinderListener.class);
		for (MoteAddressFinderListener l : listeners) {
			l.moteFound(mote);
		}
	}

	public void removeMoteFinderListener(MoteAddressFinderListener listener) {
		listenerList.remove(MoteAddressFinderListener.class, listener);
	}

	public void startDiscovery() {
		try {
			discoveryAgent.startInquiry(DiscoveryAgent.GIAC, listener);
		} catch (BluetoothStateException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void stopDiscovery() {
		discoveryAgent.cancelInquiry(listener);
	}

}
