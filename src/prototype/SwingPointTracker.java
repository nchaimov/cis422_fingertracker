package prototype;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import motej.IrPoint;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;

public class SwingPointTracker extends JPanel implements IrCameraListener {
	
	private IrPoint[] points = new IrPoint[4];
	
	public SwingPointTracker() {
		super();
		this.setSize(1024, 768);
		this.setBackground(Color.BLACK);
		
		for(int i = 0; i < 4; ++i) {
			points[i] = new IrPoint();
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.WHITE);
		for(IrPoint point : points) {
			g.drawOval(point.x, point.y, 10, 10);
		}
	}
	
	public void irImageChanged(IrCameraEvent evt) {
		for(int i = 0; i < 4; ++i) {
			points[i] = evt.getIrPoint(i);
		}
		repaint();
	}
	
	
}
