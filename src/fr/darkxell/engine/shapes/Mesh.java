package fr.darkxell.engine.shapes;

import java.util.ArrayList;
import java.util.Optional;

import fr.darkxell.engine.HitResult;
import fr.darkxell.engine.Point;

/** A mesh made by a list of triangles */
public class Mesh extends SceneElement {

	private ArrayList<Triangle> data;
	private Cube bounds;
	private double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE, minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE,
			minZ = Double.MAX_VALUE, maxZ = Double.MIN_VALUE;

	public Mesh(ArrayList<Triangle> data) {
		this.data = data == null ? data : new ArrayList<>(0);
		recomputeBounds();
	}

	@Override
	public Optional<HitResult> intersect(Point source, Point vector) {
		Optional<HitResult> boundingtest = bounds.intersect(source, vector);
		if (!boundingtest.isEmpty() && boundingtest.get().hitDistance > 0)
			return Optional.empty();
		double closestTriangledist = Float.MAX_VALUE;
		Triangle returnpointer = null;
		for (int i = 0; i < this.data.size(); i++) {
			Optional<HitResult> triangletest = this.data.get(i).intersect(source, vector);
			if (!triangletest.isEmpty() && triangletest.get().hitDistance < closestTriangledist
					&& triangletest.get().hitDistance > 0d) {
				closestTriangledist = triangletest.get().hitDistance;
				returnpointer = this.data.get(i);
			}
		}
		if (closestTriangledist == Double.MAX_VALUE)
			return Optional.empty();
		return Optional.of(new HitResult(source, vector, closestTriangledist, returnpointer));
	}

	/** Rather expensive method that recomputes the bounding cube of this mesh. */
	private void recomputeBounds() {
		for (int i = 0; i < this.data.size(); i++) {
			if (this.data.get(i).v1.x() < minX)
				minX = this.data.get(i).v1.x();
			if (this.data.get(i).v1.x() > maxX)
				maxX = this.data.get(i).v1.x();
			if (this.data.get(i).v1.y() < minY)
				minY = this.data.get(i).v1.y();
			if (this.data.get(i).v1.y() > maxY)
				maxY = this.data.get(i).v1.y();
			if (this.data.get(i).v1.z() < minZ)
				minZ = this.data.get(i).v1.z();
			if (this.data.get(i).v1.z() > maxZ)
				maxZ = this.data.get(i).v1.z();

			if (this.data.get(i).v2.x() < minX)
				minX = this.data.get(i).v2.x();
			if (this.data.get(i).v2.x() > maxX)
				maxX = this.data.get(i).v2.x();
			if (this.data.get(i).v2.y() < minY)
				minY = this.data.get(i).v2.y();
			if (this.data.get(i).v2.y() > maxY)
				maxY = this.data.get(i).v2.y();
			if (this.data.get(i).v2.z() < minZ)
				minZ = this.data.get(i).v2.z();
			if (this.data.get(i).v2.z() > maxZ)
				maxZ = this.data.get(i).v2.z();

			if (this.data.get(i).v3.x() < minX)
				minX = this.data.get(i).v3.x();
			if (this.data.get(i).v3.x() > maxX)
				maxX = this.data.get(i).v3.x();
			if (this.data.get(i).v3.y() < minY)
				minY = this.data.get(i).v3.y();
			if (this.data.get(i).v3.y() > maxY)
				maxY = this.data.get(i).v3.y();
			if (this.data.get(i).v3.z() < minZ)
				minZ = this.data.get(i).v3.z();
			if (this.data.get(i).v3.z() > maxZ)
				maxZ = this.data.get(i).v3.z();
		}
		double sizeX = maxX - minX, sizeY = maxY - minY, sizeZ = maxZ - minZ;
		this.bounds = new Cube(new Point(minX + sizeX / 2, minY + sizeY / 2, minZ + sizeZ / 2), (float) sizeX,
				(float) sizeY, (float) sizeZ);
	}

}
