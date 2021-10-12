package fr.darkxell.engine.shapes;

import java.util.ArrayList;
import java.util.Optional;

import fr.darkxell.engine.HitResult;
import fr.darkxell.engine.Point;

public class MeshTransform extends Mesh {

	public MeshTransform(String fileurl, double x, double y, double z, double scale) {
		super(fileurl, x, y, z, scale);
	}

	public MeshTransform(String fileurl, double x, double y, double z) {
		super(fileurl, x, y, z);
	}

	public MeshTransform(ArrayList<Triangle> data) {
		super(data);
	}

	public boolean flippedX = false;
	public boolean flippedY = false;
	public boolean flippedZ = false;

	@Override
	public Optional<HitResult> intersect(Point source, Point vector) {
		Point newvector = new Point(flippedX ? -vector.x() : vector.x(), flippedY ? -vector.y() : vector.y(),
				flippedZ ? -vector.z() : vector.z());
		Point newsource = new Point(flippedX ? source.x() + (super.bounds.center.x() - source.x()) * 2 : source.z(),
				flippedY ? source.y() + (super.bounds.center.y() - source.y()) * 2 : source.y(),
				flippedZ ? source.z() + (super.bounds.center.z() - source.z()) * 2 : source.z());
		return super.intersect(newsource, newvector);
	}

}
