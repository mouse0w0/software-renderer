package com.github.mouse0w0.softwarerenderer;

import org.joml.Vector4f;

public interface Vertex<V extends Vertex<V>> {
    Vector4f position();

    void lerp(V other, float t, V dest);

    void perspectiveDivide();

    void beforeFragmentShader();
}
