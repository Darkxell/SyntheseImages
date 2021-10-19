using System.Collections;
using System.Collections.Generic;
using UnityEngine;
#if UNITY_EDITOR
using UnityEditor;
#endif

public class StickFabric : MonoBehaviour {

    public List<FabricPoint> points = new List<FabricPoint>(10);
    public List<FabricSegment> segments = new List<FabricSegment>(10);

    public FabricPoint hand = null;
    public Vector3 target = new Vector3(5, 0, 0);

    void Start() {
        FabricPoint p1 = new FabricPoint(new Vector3(0, 0, 0)),
            p2 = new FabricPoint(new Vector3(1, 0, 1)),
            p3 = new FabricPoint(new Vector3(2, 0, 2)),
            p4 = new FabricPoint(new Vector3(3, 0, 3)),
            tail1 = new FabricPoint(new Vector3(-1, 0, 0)),
            tail2 = new FabricPoint(new Vector3(-1.1f, 0, 0)),
            tail3 = new FabricPoint(new Vector3(-1.2f, 0, 0));
        hand = p4;
        p1.locked = true;

        points.Add(p1);
        points.Add(p2);
        points.Add(p3);
        points.Add(p4);
        points.Add(tail1);
        points.Add(tail2);
        points.Add(tail3);
        segments.Add(new FabricSegment(p1, p2));
        segments.Add(new FabricSegment(p2, p3));
        segments.Add(new FabricSegment(p3, p4));
        segments.Add(new FabricSegment(p1, tail1));
        segments.Add(new FabricSegment(p1, tail2));
        segments.Add(new FabricSegment(p1, tail3));
    }


    void Update() {
        //target.y = 0;
        if (hand != null && Vector3.Distance(hand.position, target) > 0.01) {
            iterateFABRIK();
        }
    }

    private void iterateFABRIK() {
        List<FabricSegment> parsedlist = new List<FabricSegment>(segments.Count);
        SetList<FabricSegment> nextSeg = new SetList<FabricSegment>(10);

        SetList<FabricPoint> changedPoints = new SetList<FabricPoint>(points.Count);
        Vector3 snapback = new Vector3();

        connected(hand).ForEach(e => {
            e.flagFrom(hand);
            nextSeg.Add(e);
        });
        changedPoints.Add(hand);

        while (nextSeg.Count != 0) {
            // Gets a segment in the list of segments to parse, and tags it as parsed
            FabricSegment currentsegment = nextSeg[0];
            parsedlist.Add(currentsegment);
            nextSeg.Remove(currentsegment);
            // Fetches connected segments for further iterations
            List<FabricSegment> connectedlist = connected(currentsegment.p1);
            if (!currentsegment.p1.locked) {
                connectedlist.ForEach(e => {
                    if (!parsedlist.Contains(e)) {
                        e.flagFrom(currentsegment.p1);
                        nextSeg.Add(e);
                    }
                });
            }
            if (!currentsegment.p2.locked) {
                connectedlist = connected(currentsegment.p2);
                connectedlist.ForEach(e => {
                    if (!parsedlist.Contains(e)) {
                        e.flagFrom(currentsegment.p2);
                        nextSeg.Add(e);
                    }
                });
            }
            // Determines the direction of the segment
            FabricPoint pointFront, pointBack;
            if (currentsegment.targetTowardsP1) {
                pointFront = currentsegment.p1;
                pointBack = currentsegment.p2;
            } else {
                pointFront = currentsegment.p2;
                pointBack = currentsegment.p1;
            }
            if (pointFront == hand) {
                pointFront.position = target;
            }
            // Recalculates the back point of the segment

            if (!pointBack.locked) {
                pointBack.position = pointFront.position + currentsegment.targetLength * (pointBack.position - pointFront.position).normalized;
                changedPoints.Add(pointBack);
            } else {
                snapback = (pointFront.position + currentsegment.targetLength * (pointBack.position - pointFront.position).normalized) - pointBack.position;
            }
        }

        for (int i = 0; i < changedPoints.Count; ++i) {
            changedPoints[i].position -= snapback;
        }

    }



    // Returns the list of segments connected to a point
    private List<FabricSegment> connected(FabricPoint p) {
        List<FabricSegment> toreturn = new List<FabricSegment>(4);
        for (int i = 0; i < segments.Count; ++i)
            if (segments[i].p1 == p || segments[i].p2 == p)
                toreturn.Add(segments[i]);
        return toreturn;
    }

#if UNITY_EDITOR
    private void OnDrawGizmos() {
        points.ForEach(p => { 
            Gizmos.color = (p.locked) ? Color.red : Color.blue;
            Gizmos.DrawSphere(p.position, 0.1f); 
        });
        Gizmos.color = Color.red;
        segments.ForEach(s => Gizmos.DrawLine(s.p1.position, s.p2.position));

        Handles.color = Color.green;
        Handles.SphereHandleCap(0, target, Quaternion.identity, 0.2f, EventType.Repaint);
    }
#endif



}

#if UNITY_EDITOR
[CustomEditor(typeof(StickFabric))]
public class TargetEditor : Editor {

    void OnSceneGUI() {
        StickFabric fabric = (StickFabric)target;
        fabric.target = Handles.PositionHandle(fabric.target, Quaternion.identity);
    }
}
#endif
