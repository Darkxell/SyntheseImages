package fr.darkxell.engine.shapes;

import java.util.Optional;

import fr.darkxell.engine.Point;
import fr.darkxell.engine.SceneElement;
import fr.darkxell.utility.MathUtil;

public class Cube implements SceneElement {

	public Point center;
	public float sizeX;
	public float sizeY;
	public float sizeZ;

	public Cube(Point center, float sizeX, float sizeY, float sizeZ) {
		this.center = center;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
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
		System.out.println("t1 :" + t1);
		System.out.println("t2 :" + t2);

		double tN = MathUtil.ieeemax(MathUtil.ieeemax(t1.x(), t1.y()), t1.z());
		double tF = MathUtil.ieeemin(MathUtil.ieeemin(t2.x(), t2.y()), t2.z());

		System.out.println(tN + " | " + tF);

		if (tN > tF || tF < 0.0)
			return Optional.empty();

		// return Optional.of(1f); // // return Optional.of((float)tN);

		if (tN > 0) {
			System.out.println("ayaya");
			return Optional.of((float) tN);
		} else {
			return Optional.of((float) tF);
		}

		// return Optional.empty();
	}

//	@Override
//	public Optional<Float> intersect(Point ro, Point rd) {
//		Point boxmin = center.clone().substract(new Point(sizeX / 2, sizeY / 2, sizeZ / 2));
//		Point boxmax = center.clone().add(new Point(sizeX / 2, sizeY / 2, sizeZ / 2));
//		System.out.println(boxmin +" / "+boxmax);
//		double tmin = Double.NEGATIVE_INFINITY, tmax = Double.POSITIVE_INFINITY;
//
//		for (int i = 0; i < 3; ++i) {
//			System.out.println("Computing " + (i+1) + "th dimention");
//			if (rd.pp()[i] != 0.0d) {
//				double t1 = (boxmin.pp()[i] - ro.pp()[i]) / rd.pp()[i];
//				double t2 = (boxmax.pp()[i] - ro.pp()[i]) / rd.pp()[i];
//
//				tmin = MathUtil.ieeemax(tmin, MathUtil.ieeemin(t1, t2));
//				tmax = MathUtil.ieeemin(tmax, MathUtil.ieeemax(t1, t2));
//				System.out.println("tmax :" + tmax + " | tmin :" + tmin);
//			} else if (ro.pp()[i] <= boxmin.pp()[i] || ro.pp()[i] >= boxmax.pp()[i]) {
//				System.out.println("Empty return because : ro[i]=" + ro.pp()[i] + " isn't within boxmin and boxmax ("
//						+ boxmin.pp()[i] + "->" + boxmax.pp()[i] + ")");
//				return Optional.empty();
//			}
//		}
//
//		
//		if (tmax > tmin && tmax > 0.0) {
//			System.out.println("tmin:" + tmin);
//			return Optional.of((float) tmin);
//		} else {
//			System.out.println("Empty return.");
//			return Optional.empty();
//		}
//	}

	@Override
	public Point normal(Point reference) {
		return reference.clone().substract(center).normalize();
	}

}
