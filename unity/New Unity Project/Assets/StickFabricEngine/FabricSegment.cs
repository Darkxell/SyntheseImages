using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FabricSegment {

    public FabricPoint p1;
    public FabricPoint p2;

    // Iteratively set to true if p1 is towards the last target checked by a FABRIK run on this segment.
    public bool targetTowardsP1 = false;
    // Iteratively set to true if p2 is towards the last target checked by a FABRIK run on this segment.
    public bool targetTowardsP2 = false;

    public float targetLength;


    public FabricSegment(FabricPoint p1, FabricPoint p2, float length) {
        this.p1 = p1;
        this.p2 = p2;
        this.targetLength = length;
    }

    public FabricSegment(FabricPoint p1, FabricPoint p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.targetLength = Vector3.Distance(p1.position, p2.position);
    }

    //Sets the targetTowardsFlag
    public void flagFrom(FabricPoint from) {
        targetTowardsP1 = false;
        targetTowardsP2 = false;
        if (p1 == from)
            targetTowardsP1 = true;
        if (p2 == from)
            targetTowardsP2 = true;
    }

    public override string ToString() {
        return "[FSegment " + p1 + p2 +(targetTowardsP1 ? " anchored at P1 " : (targetTowardsP2 ? " anchored at P2 " : "")) + "]";
    }

}
