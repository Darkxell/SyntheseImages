package fr.darkxell.engine.shapes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;

import fr.darkxell.engine.HitResult;
import fr.darkxell.engine.Point;
import fr.darkxell.engine.materials.Material;

/** A mesh made by a list of triangles */
public class Mesh extends SceneElement {

	private ArrayList<Triangle> data;
	protected Cube bounds;

	public Mesh(ArrayList<Triangle> data) {
		this.data = data == null ? data : new ArrayList<>(0);
		recomputeBounds();
	}

	public Mesh(String fileurl, double x, double y, double z) {
		this(fileurl, x, y, z, 1d);
	}

	public Mesh(String fileurl, double x, double y, double z, double scale) {
		// Open the file
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(fileurl);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			int triangles = 0, vertices = 0, line = 0;
			ArrayList<Point> verticeslist = new ArrayList<>(100);
			ArrayList<Triangle> triangleslist = new ArrayList<>(100);
			while ((strLine = br.readLine()) != null) {
				line++;
				if (line == 1)
					continue;
				String[] cut = strLine.split(" ");
				if (line == 2) {
					vertices = Integer.parseInt(cut[0]);
					triangles = Integer.parseInt(cut[1]);
					continue;
				}
				if (line <= vertices + 2) {
					verticeslist.add(new Point(Double.parseDouble(cut[0]) * scale + x,
							Double.parseDouble(cut[1]) * scale + y, Double.parseDouble(cut[2]) * scale + z));
				} else if (line <= vertices + triangles + 2) {
					if (cut[0].equals("3")) {
						int t1 = Integer.parseInt(cut[1]), t2 = Integer.parseInt(cut[2]), t3 = Integer.parseInt(cut[3]);
						triangleslist
								.add(new Triangle(verticeslist.get(t1), verticeslist.get(t2), verticeslist.get(t3)));
					} else {
						System.err.println("Mesh creation Error is off file parsing, malformed triangles list!");
						Thread.dumpStack();
					}
				}
			}
			fstream.close();
			this.data = triangleslist;
			recomputeBounds();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Optional<HitResult> intersect(Point source, Point vector) {
		// If shitty mesh, can't compute an intersection
		if (bounds == null || data == null || data.size() == 0)
			return Optional.empty();
		// Tests if the boundingbox intersects the ray
		Optional<HitResult> boundingtest = bounds.intersect(source, vector);
		if (boundingtest.isEmpty() || boundingtest.get().hitDistance < 0)
			return Optional.empty();
		// Fetches triangle data to know
		double closestTriangledist = Double.MAX_VALUE;
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
		if (data == null || data.size() == 0) {
			bounds = null;
			return;
		}
		double minX = this.data.get(0).minX(), maxX = this.data.get(0).maxX(), minY = this.data.get(0).minY(),
				maxY = this.data.get(0).maxY(), minZ = this.data.get(0).minZ(), maxZ = this.data.get(0).maxZ();
		for (int i = 1; i < this.data.size(); i++) {
			if (this.data.get(i).minX() < minX)
				minX = this.data.get(i).minX();
			if (this.data.get(i).maxX() > maxX)
				maxX = this.data.get(i).maxX();
			if (this.data.get(i).minY() < minY)
				minY = this.data.get(i).minY();
			if (this.data.get(i).maxY() > maxY)
				maxY = this.data.get(i).maxY();
			if (this.data.get(i).minZ() < minZ)
				minZ = this.data.get(i).minZ();
			if (this.data.get(i).maxZ() > maxZ)
				maxZ = this.data.get(i).maxZ();
		}
		double sizeX = maxX - minX, sizeY = maxY - minY, sizeZ = maxZ - minZ;
		this.bounds = new Cube(new Point(minX + sizeX / 2, minY + sizeY / 2, minZ + sizeZ / 2), (float) sizeX,
				(float) sizeY, (float) sizeZ);
	}

	@Override
	public String toString() {
		return "[Mesh with " + data.size() + " triangles bound by:" + bounds + ")]";
	}

	/** Setter for this Element's material */
	@Override
	public void setMat(Material m) {
		super.setMat(m);
		if (data != null)
			for (int i = 0; i < data.size(); i++)
				data.get(i).setMat(m);
	}

}
