package com.github.mouse0w0.softwarerenderer;

import org.joml.Vector4f;

public interface Shader<V extends Vertex<V>> {
    void vertex(V vertex);

    /**
     * @return If true, discard current fragment.
     */
    boolean fragment(V fragment, Vector4f color);
}
