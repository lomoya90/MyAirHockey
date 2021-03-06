package com.example.lomoya.myairhockey.objects;

import com.example.lomoya.myairhockey.VertexArray;
import com.example.lomoya.myairhockey.shader.ColorShaderProgram;

import java.util.List;

/**
 * Created by liyan36 on 2017-06-06.
 */

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 3;

    private VertexArray vertexArray;
    private List<ObjectBuilder.DrawCommand> drawCommands;

    public float radius;
    public float height;

    public Mallet(float radius, float height, int numVertices) {
        this.radius = radius;
        this.height = height;
        ObjectBuilder.GeneratedData generatedData =
                ObjectBuilder.createMallet(
                        new Geometry.Point(0f, 0f, 0f), radius, height, numVertices);
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
