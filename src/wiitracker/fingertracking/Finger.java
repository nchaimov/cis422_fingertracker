package wiitracker.fingertracking;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Wraps Point2D.Double with a point type enumerator, labeling it as one of potentially several 
 * @author areinder
 *
 */
public class Finger extends Point2D.Double implements Cloneable {
	PointType type = PointType.UNKNOWN;
	public Finger() {
		// TODO Auto-generated constructor stub
	}

	public Finger(double arg0, double arg1) {
		super(arg0, arg1);
	}
	
	public Finger(double arg0, double arg1, PointType t) {
		super(arg0, arg1);
		this.setType(t);
	}
	
	public Finger(Point2D point, PointType t) {
		this(point.getX(), point.getY(), t);
	}
	
	public Finger(Point2D point) {
		this(point.getX(), point.getY());
	}
	
	
	public Finger(PointType t)
	{
		super();
		type = t;
	}
	
	public void setType(PointType t)
	{
		type = t;
	}
	
	public PointType getType()
	{
		return type;
	}
	
	@Override
	public String toString() {
		return "" + type.toString() + " (" + this.x + ", " + this.y + ")";
	}
}
