package fr.darkxell.engine.shapes;

import java.util.Optional;

import fr.darkxell.engine.HitResult;
import fr.darkxell.engine.Point;
import fr.darkxell.utility.MathUtil;

public class Cube extends SceneElement implements NormalPrimitive {

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
	public Optional<HitResult> intersect(Point ro, Point rd) {
		// Shitty solution for shitty problems
		Point rd2 = new Point(rd.x() == 0d ? 0.00001 : rd.x(), rd.y() == 0d ? 0.00001 : rd.y(),
				rd.z() == 0d ? 0.00001 : rd.z());

		Point boxsize = new Point(sizeX / 2, sizeY / 2, sizeZ / 2);
		ro = ro.clone().substract(center);

		Point m = rd2.clone().oneOn();
		Point n = m.clone().mul(ro);
		Point k = m.abs().mul(boxsize);

		Point t1 = n.clone().multiply(-1d).substract(k);
		Point t2 = n.multiply(-1d).add(k);

		double tN = MathUtil.ieeemax(MathUtil.ieeemax(t1.x(), t1.y()), t1.z());
		double tF = MathUtil.ieeemin(MathUtil.ieeemin(t2.x(), t2.y()), t2.z());

		if (tN > tF || tF < 0d)
			return Optional.empty();

		HitResult toreturn = new HitResult(ro, rd2, (tN > 0) ? tN : tF, this);
		return Optional.of(toreturn);
	}

	@Override
	public Point normal(Point reference) {
		boolean iscube = sizeX == sizeY && sizeX == sizeZ;
		// If the box is a cube, we can calculate the sphere normal and snap it to the
		// closest normal orthographic vector.
		if (iscube) {
			Point ssph = reference.clone().substract(center).normalize();
			if (Math.abs(ssph.x()) >= Math.abs(ssph.y()) && Math.abs(ssph.x()) >= Math.abs(ssph.z()))
				return new Point(ssph.x() >= 0 ? 1 : -1, 0, 0);
			if (Math.abs(ssph.y()) >= Math.abs(ssph.x()) && Math.abs(ssph.y()) >= Math.abs(ssph.z()))
				return new Point(0, ssph.y() >= 0 ? 1 : -1, 0);
			return new Point(0, 0, ssph.z() >= 0 ? 1 : -1);
		} else {
			// FIXME: this garbage apparently doesn't always work. Corner padding seems way
			// too big and offset far from 0,0,0. Left to investigate.
			float halfX = sizeX / 2, halfY = sizeY / 2, halfZ = sizeZ / 2;
			float cornerpadding = (halfX + halfY + halfZ) / 10000;
			boolean inaxisX = reference.x() >= center.x() - halfX + cornerpadding
					&& reference.x() <= center.x() + halfX - cornerpadding;
			boolean inaxisY = reference.y() >= center.y() - halfY + cornerpadding
					&& reference.y() <= center.y() + halfY - cornerpadding;
			boolean inaxisZ = reference.z() >= center.z() - halfZ + cornerpadding
					&& reference.z() <= center.z() + halfZ - cornerpadding;
			if (inaxisX && inaxisY && !inaxisZ)
				return new Point(0, 0, 1);
			if (inaxisX && !inaxisY && inaxisZ)
				return new Point(0, 1, 0);
			if (!inaxisX && inaxisY && inaxisZ)
				return new Point(1, 0, 0);
			return reference.clone().substract(center).normalize();
		}
	}
	
	@Override
	public String toString() {
		return "[Cube at " + center + " with size " + sizeX + "/" + sizeY + "/" + sizeZ + "]";
	}

}
