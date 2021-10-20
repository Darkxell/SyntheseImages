using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FabricPoint {

    public Vector3 position;
    public bool locked = false;

    public float maxangle = 0;

    public FabricPoint(Vector3 position) {
        this.position = position;
    }

    public FabricPoint(Vector3 position, bool locked) {
        this.position = position;
        this.locked = locked;
    }

    public override string ToString() {
        return "(FPoint " + position + (locked ? ":locked" : "") + ")";
    }
}
