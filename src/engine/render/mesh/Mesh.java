package traingame.engine.render.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public abstract class Mesh {
    protected int VAO;
    protected int VBO;
    protected int EBO;
    protected int numIndices;

    protected Mesh() {
        // vertex array object
        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);
        // Vertex Buffer Object
        this.VBO = GL20.glGenBuffers();
        // Element Buffer Object
        this.EBO = GL20.glGenBuffers();
        
    }

    protected abstract int getVertexBufferStride();

    protected void bufferVertexData(float[] vertices) {
        // Needs to be off-heap memory
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
        vertexBuffer.put(vertices).flip();
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, VBO);
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, vertexBuffer, GL20.GL_STATIC_DRAW);
        MemoryUtil.memFree(vertexBuffer);
    }

    protected void bufferFaceElements(int[] faces) {
        IntBuffer indexBuffer = MemoryUtil.memAllocInt(faces.length);
        indexBuffer.put(faces).flip();
        GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, EBO);
        GL20.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL20.GL_STATIC_DRAW);
        MemoryUtil.memFree(indexBuffer);
        // Note faces is already a flattened array; its length is the number
        // of vertices to draw
        this.numIndices = faces.length;
    }

    // Calling function is responsible for setting the shader
    public void render() {
        GL30.glBindVertexArray(VAO);
        GL20.glDrawElements(GL20.GL_TRIANGLES, this.numIndices, GL20.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);
    }

    public void clean() {
        GL30.glBindVertexArray(0);
        GL20.glDeleteBuffers(VBO);
        GL20.glDeleteBuffers(EBO);
        GL30.glDeleteVertexArrays(VAO);
        VAO = 0;
        VBO = 0;
        EBO = 0;
        numIndices = 0;
    }
}
