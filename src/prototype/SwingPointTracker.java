package prototype;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import motej.IrPoint;
import motej.event.IrCameraEvent;
import motej.event.IrCameraListener;

public class SwingPointTracker extends JPanel implements IrCameraListener {
	
	private IrPoint[] points = new IrPoint[4];
	
	private final int xAdjust = 1024;
	private final int yAdjust = 768;
	
	public SwingPointTracker() {
		super();
		Dimension d = new Dimension(1024, 768);
		this.setPreferredSize(d);
		this.setSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		this.setBackground(Color.BLACK);
		
		for(int i = 0; i < 4; ++i) {
			points[i] = new IrPoint();
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.RED);
		g.fillOval(points[0].x, yAdjust - points[0].y, 10, 10);
		g.setColor(Color.GREEN);
		g.fillOval(points[1].x, yAdjust - points[1].y, 10, 10);
		g.setColor(Color.BLUE);
		g.fillOval(points[2].x, yAdjust - points[2].y, 10, 10);
		g.setColor(Color.WHITE);
		g.fillOval(points[3].x, yAdjust - points[3].y, 10, 10);
	}
	
	public void irImageChanged(IrCameraEvent evt) {
		for(int i = 0; i < 4; ++i) {
			points[i] = evt.getIrPoint(i);
		}
		repaint();
	}
	
	
}
