package com.example.lomoya.myairhockey.objects;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyan36 on 2017-06-06.
 */

public class ObjectBuilder {

    private static final int FLOAT_PER_VERTEX = 3;
    private final float[] vertexData;
    private int offset;

    private final List<DrawCommand> drawCommandList = new ArrayList<>();

    static interface DrawCommand {
        void draw();
    }

    private ObjectBuilder(int sizeOfVertices) {
        vertexData = new float[sizeOfVertices * FLOAT_PER_VERTEX];
    }

    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }

    private static int sizeOfCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }

    private void appendCircle(Geometry.Circle circle, int numPoints) {

        final int start = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        Geometry.Point center = circle.center;

        vertexData[offset++] = center.x;
        vertexData[offset++] = center.y;
        vertexData[offset++] = center.z;

        for (int i = 0; i <= numPoints; i++) {
            float angle = (float) (((float) i / (float) numPoints) * (Math.PI * 2f));
            vertexData[offset++] = (float) (center.x + circle.radius * Math.cos(angle));
            vertexData[offset++] = center.y;
            vertexData[offset++] = (float) (center.z + circle.radius * Math.sin(angle));
        }

        DrawCommand drawCommand = new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, start, numVertices);
            }
        };
        drawCommandList.add(drawCommand);
    }

    private void appendOpenCylinder(Geometry.Cylinder cylinder, int numPoints) {
        final int start = offset / FLOAT_PER_VERTEX;
        final int numVertices = sizeOfCylinderInVertices(numPoints);

        float startY = cylinder.center.y + cylinder.height / 2f;
        float endY = cylinder.center.y - cylinder.height / 2f;

        for (int i = 0; i <= numPoints; i++) {
            float angle = (float) (((float) i / (float) numPoints) * (Math.PI * 2f));
            float x = (float) (cylinder.center.x + cylinder.radius * Math.cos(angle));
            float z = (float) (cylinder.center.z + cylinder.radius * Math.sin(angle));

            vertexData[offset++] = x;
            vertexData[offset++] = startY;
            vertexData[offset++] = z;

            vertexData[offset++] = x;
            vertexData[offset++] = endY;
            vertexData[offset++] = z;
        }

        drawCommandList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, start, numVertices);
            }
        });
    }

    public static class GeneratedData {
        public final float[] vertexData;
        public final List<DrawCommand> drawCommands;

        public GeneratedData(float[] vertexData, List<DrawCommand> drawCommands) {
            this.vertexData = vertexData;
            this.drawCommands = drawCommands;
        }
    }

    public GeneratedData build() {
        return new GeneratedData(vertexData, drawCommandList);
    }

    /**
     * 创建冰球对象
     *
     * @param cylinder
     * @param numVertices
     * @return
     */
    public static GeneratedData createPuck(Geometry.Cylinder cylinder, int numVertices) {

        int size = sizeOfCircleInVertices(numVertices) + sizeOfCylinderInVertices(numVertices);

        Geometry.Circle puckTop = new Geometry.Circle(
                cylinder.center.translateY(cylinder.height / 2f),
                cylinder.radius);

        ObjectBuilder builder = new ObjectBuilder(size);
        builder.appendCircle(puckTop, numVertices);
        builder.appendOpenCylinder(cylinder, numVertices);
        return builder.build();
    }

    public static GeneratedData createMallet(Geometry.Point center, float radius, float height,
                                             int numVertices) {
        int size = sizeOfCircleInVertices(numVertices) * 2
                + sizeOfCylinderInVertices(numVertices) * 2;
        ObjectBuilder builder = new ObjectBuilder(size);
        // create mallet base:
        float baseHeight = height * 0.25f;

        Geometry.Circle baseCircle = new Geometry.Circle(center.translateY(-baseHeight), radius);
        Geometry.Cylinder baseCylinder = new Geometry.Cylinder(center.translateY(-baseHeight / 2f),
                radius, baseHeight);

        builder.appendCircle(baseCircle, numVertices);
        builder.appendOpenCylinder(baseCylinder, numVertices);

        //--------------------------------//
        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Geometry.Circle handleCircle = new Geometry.Circle(
                center.translateY(height / 2f), handleRadius);
        Geometry.Cylinder handleCylinder = new Geometry.Cylinder(
                center.translateY(handleHeight / 2f), handleRadius, handleHeight);

        builder.appendCircle(handleCircle, numVertices);
        builder.appendOpenCylinder(handleCylinder, numVertices);
        return builder.build();
    }
}
