package com.example.lomoya.myairhockey.objects;

import com.example.lomoya.myairhockey.VertexArray;
import com.example.lomoya.myairhockey.shader.ColorShaderProgram;

import java.util.List;

/**
 * Created by liyan36 on 2017-06-07.
 */

public class Puck {

    private static final int POSITION_COMPONENT_COUNT = 3;

    private VertexArray vertexArray;
    private List<ObjectBuilder.DrawCommand> drawCommands;

    public float radius;
    public float height;

    public Puck(float radius, float height, int numPoints) {
        this.radius = radius;
        this.height = height;

        Geometry.Cylinder cylinder = new Geometry.Cylinder(
                new Geometry.Point(0f, 0f, 0f), radius, height);
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createPuck(cylinder, numPoints);

        vertexArray = new VertexArray(generatedData.vertexData);
        drawCommands = generatedData.drawCommands;
    }

    public void bindData(ColorShaderProgram program) {
        vertexArray.setVertextAttrPointer(0, POSITION_COMPONENT_COUNT,
                0, program.getPositionAttrLocation());
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawCommands) {
            drawCommand.draw();
        }
    }
}
