package com.github.mouse0w0.softwarerenderer.sampler;

import org.joml.Vector2f;
import org.joml.Vector4f;

public interface Sampler2D {

    Vector4f sample(float u, float v, Vector4f dest);

    default Vector4f sample(Vector2f uv, Vector4f dest) {
        return sample(uv.x, uv.y, dest);
    }
}
