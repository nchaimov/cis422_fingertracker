package wiitracker.fingertracking;

import java.awt.geom.Point2D.Double;

public class Finger extends Double implements Cloneable {
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
}
