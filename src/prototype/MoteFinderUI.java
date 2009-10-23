package prototype;

import info.clearthought.layout.TableLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class MoteFinderUI extends JFrame implements MoteFinderListener {

	private static final String SAVED_MOTES_FILE = "motes.list";

	private final Logger log = Logger.getLogger(this.getClass());

	private DefaultListModel listModel = new DefaultListModel();
	private JList moteList;
	private JButton addButton;
	private JButton removeButton;
	private JButton useButton;

	private JScrollPane moteListScrollPane;

	private MoteFinder moteFinder;

	protected final transient ActionListener addActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String result = (String) JOptionPane.showInputDialog(MoteFinderUI.this,
					"Enter the Bluetooth address of the Wiimote you wish to use.", "Add Wiimote",
					JOptionPane.QUESTION_MESSAGE, new ImageIcon("resources/mote.gif"), null, null);
			if (result != null) {
				listModel.addElement(result);
				log.info("Address added manually: " + result);
				writeSavedAddresses();
			}
		}
	};

	public MoteFinderUI() {
		super();

		moteList = new JList(listModel);
		moteListScrollPane = new JScrollPane(moteList);

		readSavedAddresses();

		addButton = new JButton("Add by address");
		addButton.addActionListener(addActionListener);
		removeButton = new JButton("Remove address");
		useButton = new JButton("Connect");

		final Dimension d = new Dimension(640, 480);
		this.setSize(d);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);

		final double[] colSize = { 10, TableLayout.FILL, 0.25, 10 };
		final double[] rowSize = { 10, 30, 30, TableLayout.FILL, 30, 10 };
		final TableLayout layout = new TableLayout(colSize, rowSize);
		layout.setVGap(2);
		layout.setHGap(5);
		this.getContentPane().setLayout(layout);

		this.getContentPane().add(moteListScrollPane, "1,1,1,4");
		this.getContentPane().add(addButton, "2,1");
		this.getContentPane().add(removeButton, "2,2");
		this.getContentPane().add(useButton, "2,4");

		moteFinder = MoteFinder.getMoteFinder();
		moteFinder.addMoteFinderListener(this);
		// moteFinder.startDiscovery();

	}

	protected void writeSavedAddresses() {
		log.debug("Writing saved addresses...");
		try {
			FileOutputStream fout = new FileOutputStream(SAVED_MOTES_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(listModel);
			oos.close();
			log.debug("Addresses written");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void readSavedAddresses() {
		log.debug("Reading saved addresses...");
		FileInputStream fin;
		try {
			fin = new FileInputStream(SAVED_MOTES_FILE);
			ObjectInputStream ois = new ObjectInputStream(fin);
			listModel = (DefaultListModel) ois.readObject();
			ois.close();
			moteList.setModel(listModel);
			log.debug("Addresses read: " + listModel.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void moteFound(Mote mote) {
		log.info("Found a Wiimote: " + mote.getBluetoothAddress());
		if (!listModel.contains(mote.getBluetoothAddress())) {
			listModel.addElement(mote);
			writeSavedAddresses();
		}
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		MoteFinderUI finderUI = new MoteFinderUI();
		finderUI.pack();
		finderUI.setVisible(true);
	}

}
