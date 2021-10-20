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
    public Vector3 target = new Vector3(1.5f, 2, 0);

    void Start() {
        FabricPoint source = new FabricPoint(new Vector3(0, 1, 0)),
            shoulderLeft = new FabricPoint(new Vector3(0.5f, 2, 0)),
            shoulderRight = new FabricPoint(new Vector3(-0.5f, 2, 0)),
            ArmLeft = new FabricPoint(new Vector3(1, 2, 0)),
            ArmRight = new FabricPoint(new Vector3(-1, 2, 0)),
            HandLeft = new FabricPoint(new Vector3(1.5f, 2, 0)),
            HandRight = new FabricPoint(new Vector3(-1.5f, 2, 0)),
            LegLeft = new FabricPoint(new Vector3(0.5f, 0.5f, 0)),
            LegRight = new FabricPoint(new Vector3(-0.5f, 0.5f, 0)),
            FootLeft = new FabricPoint(new Vector3(0.5f, 0, 0)),
            FootRight = new FabricPoint(new Vector3(-0.5f, 0, 0));
        
        hand = FootLeft;
        target = hand.position;
        
        source.locked = true;
        ArmLeft.maxangle = 45;
        ArmRight.maxangle = 45;
        LegLeft.maxangle = 35;
        LegRight.maxangle = 35;

        points.Add(source);
        points.Add(shoulderLeft);
        points.Add(shoulderRight);
        points.Add(ArmLeft);
        points.Add(ArmRight);
        points.Add(HandLeft);
        points.Add(HandRight);
        points.Add(LegLeft);
        points.Add(LegRight);
        points.Add(FootLeft);
        points.Add(FootRight);
        segments.Add(new FabricSegment(source, shoulderLeft));
        segments.Add(new FabricSegment(source, shoulderRight));
        segments.Add(new FabricSegment(shoulderRight, shoulderLeft));
        segments.Add(new FabricSegment(shoulderLeft, ArmLeft));
        segments.Add(new FabricSegment(shoulderRight, ArmRight));
        segments.Add(new FabricSegment(ArmLeft, HandLeft));
        segments.Add(new FabricSegment(ArmRight, HandRight));
        segments.Add(new FabricSegment(source, LegLeft));
        segments.Add(new FabricSegment(source, LegRight));
        segments.Add(new FabricSegment(LegLeft, FootLeft));
        segments.Add(new FabricSegment(LegRight, FootRight));
    }


    void Update() {
        //target.z = 0;
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
                if (!changedPoints.Contains(pointBack)) {
                    pointBack.position = pointFront.position + currentsegment.targetLength * (pointBack.position - pointFront.position).normalized; 
                    
                }
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
            Gizmos.DrawSphere(p.position, 0.05f);
        });
        segments.ForEach(s => {
            Gizmos.color = Color.red;
            Gizmos.DrawLine(s.p1.position, s.p2.position);
            if (s.p1.maxangle > 0) {
                GizmosDrawCone(s.p1.position, s.p1.position - s.p2.position, s.p1.maxangle, 0.2f);
            }
            if (s.p2.maxangle > 0) {
                GizmosDrawCone(s.p2.position, s.p2.position - s.p1.position, s.p2.maxangle, 0.2f);
            }
        });

        Handles.color = Color.green;
        Handles.SphereHandleCap(0, target, Quaternion.identity, 0.2f, EventType.Repaint);
    }

    private void GizmosDrawCone(Vector3 position, Vector3 direction, float angle = 45, float length = 0.5f) {
        GizmosDrawCone(position, direction, Color.blue, angle, length);
    }

    private void GizmosDrawCone(Vector3 position, Vector3 direction, Color color, float angle = 45, float length = 0.5f) {
        Handles.color = color;
        Gizmos.color = color;

        Matrix4x4 originalMatGizmos = Gizmos.matrix;
        Matrix4x4 originalMatHandles = Handles.matrix;

        Matrix4x4 trs = Matrix4x4.TRS(position, Quaternion.LookRotation(direction), Vector3.one);
        Gizmos.matrix = trs;
        Handles.matrix = trs;

        float size = Mathf.Cos(angle * Mathf.Deg2Rad);
        float radius = (Quaternion.AngleAxis(angle, Vector3.right) * Vector3.forward * length).y;

        Gizmos.DrawRay(Vector3.zero, Quaternion.AngleAxis(angle, Vector3.right) * Vector3.forward * length);
        Gizmos.DrawRay(Vector3.zero, Quaternion.AngleAxis(-angle, Vector3.right) * Vector3.forward * length);
        Gizmos.DrawRay(Vector3.zero, Quaternion.AngleAxis(angle, Vector3.up) * Vector3.forward * length);
        Gizmos.DrawRay(Vector3.zero, Quaternion.AngleAxis(-angle, Vector3.up) * Vector3.forward * length);

        Handles.CircleHandleCap(0, Vector3.forward * length * size, Quaternion.identity, radius, EventType.Repaint);

        Gizmos.matrix = originalMatGizmos;
        Handles.matrix = originalMatHandles;
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
