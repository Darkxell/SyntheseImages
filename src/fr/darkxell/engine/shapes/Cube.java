package fr.darkxell.engine.shapes;

import java.util.Optional;

import fr.darkxell.engine.Point;
import fr.darkxell.engine.SceneElement;

public class Cube implements SceneElement {

	public Point center;
	public float sizeX;
	public float sizeY;
	public float sizeZ;

	public Cube(Point center, float sizeX,float sizeY,float sizeZ) {
		this.center = center;
		this.sizeX = sizeX;
		this.sizeX = sizeY;
		this.sizeX = sizeZ;
	}

	@Override
	public Optional<Float> intersect(Point ro, Point rd) {
		Point boxsize = new Point(sizeX, sizeY, sizeZ);
		ro = ro.clone().substract(center);
		
		Point m = rd.clone().oneOn();
		System.out.println(m);
		Point n = m.clone().mul(ro);
		System.out.println(n);
		Point k = m.clone().abs().mul(boxsize);
		System.out.println(k);
		
		Point t1 = n.clone().multiply(-1d).substract(k);
		Point t2 = n.clone().multiply(-1d).add(k);
		
	    double tN = Math.max(Math.max(t1.x(), t1.y()), t1.z());
	    double tF = Math.min(Math.min(t2.x(), t2.y()), t2.z());
	    
	    System.out.println(tN + " | " + tF);
	    
	    if (tN > tF || tF < 0.0)
	    	return Optional.empty();

//	    return Optional.of(1f);
//	    
//	    return Optional.of((float)tN);
	    
	    if (tN > 0) {
	    	System.out.println("ayaya");
	    	return Optional.of((float)tN);
	    } else {
	    	return Optional.of((float)tF);
	    }
	        

	    //return Optional.empty();
	}

	@Override
	public Point normal(Point reference) {
		return reference.clone().substract(center).normalize();
	}

}
