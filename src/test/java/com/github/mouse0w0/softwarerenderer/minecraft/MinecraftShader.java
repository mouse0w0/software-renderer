package com.github.mouse0w0.softwarerenderer.minecraft;

import com.github.mouse0w0.softwarerenderer.Shader;
import com.github.mouse0w0.softwarerenderer.sampler.DefaultSampler2D;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MinecraftShader implements Shader<MinecraftVertex> {
    public final Matrix4f modelViewProjectionMatrix = new Matrix4f();

    public final DefaultSampler2D sampler = new DefaultSampler2D();

    public final Vector3f light0Dir = new Vector3f();
    public final Vector3f light1Dir = new Vector3f();

    public boolean enableLight = true;

    public float diffuseLight = 0.6f;
    public float ambientLight = 0.4f;

    public void setupGui3DLighting() {
        // light0Dir.set(0.4166f, 0.1043f, 0.8975f).normalize();
        // light1Dir.set(-0.1527f, 0.9317f, 0.2985f).normalize();

        light0Dir.set(0.41871133f, 0.104828596f, 0.9020486f);
        light1Dir.set(-0.15421219f, 0.94092655f, 0.30145603f);
    }

    @Override
    public void vertex(MinecraftVertex vertex) {
        vertex.position.mul(modelViewProjectionMatrix, vertex.position);
        if (enableLight) lighting(vertex);
    }

    private void lighting(MinecraftVertex vertex) {
        float light0 = Math.max(0, light0Dir.dot(vertex.normal));
        float light1 = Math.max(0, light1Dir.dot(vertex.normal));
        float light = Math.min(1, (light0 + light1) * diffuseLight + ambientLight);
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
