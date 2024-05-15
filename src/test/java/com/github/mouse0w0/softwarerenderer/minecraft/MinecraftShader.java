package com.github.mouse0w0.softwarerenderer.minecraft;

import com.github.mouse0w0.softwarerenderer.Shader;
import com.github.mouse0w0.softwarerenderer.sampler.DefaultSampler2D;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

public class MinecraftShader implements Shader<MinecraftVertex> {
    public static final float DIFFUSE_LIGHT = 0.6f;
    public static final float AMBIENT_LIGHT = 0.4f;

    public final Matrix4f modelViewProjectionMatrix = new Matrix4f();

    public final DefaultSampler2D sampler = new DefaultSampler2D();

    public final Vector3f light0Dir = new Vector3f();
    public final Vector3f light1Dir = new Vector3f();

    public void setupGui3DLighting() {
        // THIS IS A TEMPORARY SOLUTION, ONLY APPLIES TO SOME CASES. NEED FIX IT.
        light0Dir.set(0.41900876f,0.105269626f, 0.9018591f);
        light1Dir.set(-0.15313458f, 0.94161505f, 0.29985133f);

        // light0Dir.set(0.2f, 1.0f, -0.7f).normalize();
        // light1Dir.set(-0.2f, 1.0f, 0.7f).normalize();

        // light0Dir.set(-0.16169035f, 0.80845207f, 0.5659164f);
        // light1Dir.set(0.16169035f, 0.80845207f, -0.5659164f);
    }

    @Override
    public void vertex(MinecraftVertex vertex) {
        vertex.position.mul(modelViewProjectionMatrix, vertex.position);
        lighting(vertex);
    }

    private void lighting(MinecraftVertex vertex) {
        float light0 = Math.max(0, light0Dir.dot(vertex.normal));
        float light1 = Math.max(0, light1Dir.dot(vertex.normal));
        float light = Math.min(1, (light0 + light1) * DIFFUSE_LIGHT + AMBIENT_LIGHT);
        mulXYZ(vertex.color, light);
    }

    private Vector4f mulXYZ(Vector4f v, float scalar) {
        v.x = v.x * scalar;
        v.y = v.y * scalar;
        v.z = v.z * scalar;
        return v;
    }

    @Override
    public boolean fragment(MinecraftVertex fragment, Vector4f color) {
        fragment.color.mul(sampler.sample(fragment.texCoord, color), color);
        return false;
    }
}
