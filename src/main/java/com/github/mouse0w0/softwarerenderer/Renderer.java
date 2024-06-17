package com.github.mouse0w0.softwarerenderer;

import com.github.mouse0w0.softwarerenderer.framebuffer.FrameBuffer;
import org.joml.Vector4f;
import org.joml.Vector4i;

import java.util.function.Supplier;

public class Renderer<V extends Vertex<V>> {
    private Shader<? super V> shader;

    public Shader<? super V> getShader() {
        return shader;
    }

    public void setShader(Shader<? super V> shader) {
        this.shader = shader;
    }

    private FrameBuffer frameBuffer;

    public FrameBuffer getFrameBuffer() {
        return frameBuffer;
    }

    public void setFrameBuffer(FrameBuffer frameBuffer) {
        this.frameBuffer = frameBuffer;
    }

    public void clearColor(Vector4f color) {
        frameBuffer.clearColor(color);
    }

    public void clearColor(float red, float green, float blue, float alpha) {
        frameBuffer.clearColor(red, green, blue, alpha);
    }

    public void clearDepth(float depth) {
        frameBuffer.clearDepth(depth);
    }

    private int minX;
    private int minY;
    private int width;
    private int height;
    private int maxX;
    private int maxY;

    private float fMinX;
    private float fMinY;
    private float fHalfWidth;
    private float fHalfHeight;

    /**
     * @param v The vector4i
     * @return (x, y, width, height)
     */
    public Vector4i getViewport(Vector4i v) {
        v.set(minX, minY, width, height);
        return v;
    }

    public void setViewport(int x, int y, int width, int height) {
        this.minX = x;
        this.minY = y;
        this.width = width;
        this.height = height;
        this.maxX = x + height;
        this.maxY = y + height;
        this.fMinX = x;
        this.fMinY = y;
        this.fHalfWidth = width / 2f;
        this.fHalfHeight = height / 2f;
    }

    private CullMode cullMode = CullMode.NEVER;
    private boolean depthTest = false;
    private boolean depthWrite = true;
    private DepthFunc depthFunc = DepthFunc.LESS;
    private BlendMode blendMode = BlendMode.OFF;

    public CullMode getCullMode() {
        return cullMode;
    }

    public void setCullMode(CullMode cullMode) {
        this.cullMode = cullMode;
    }

    public boolean isDepthTest() {
        return depthTest;
    }

    public void setDepthTest(boolean depthTest) {
        this.depthTest = depthTest;
    }

    public boolean isDepthWrite() {
        return depthWrite;
    }

    public void setDepthWrite(boolean depthWrite) {
        this.depthWrite = depthWrite;
    }

    public DepthFunc getDepthFunc() {
        return depthFunc;
    }

    public void setDepthFunc(DepthFunc depthFunc) {
        this.depthFunc = depthFunc;
    }

    public BlendMode getBlendMode() {
        return blendMode;
    }

    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }

    private final V a;
    private final V b;
    private final V c;
    private final V m;
    private final V l;
    private final V r;
    private final V p;

    public Renderer(Supplier<V> vertexSupplier) {
        a = vertexSupplier.get();
        b = vertexSupplier.get();
        c = vertexSupplier.get();
        m = vertexSupplier.get();
        l = vertexSupplier.get();
        r = vertexSupplier.get();
        p = vertexSupplier.get();
    }

    public V a() {
        return a;
    }

    public V b() {
        return b;
    }

    public V c() {
        return c;
    }

    /**
     * Draw a triangle with vertices {@link Renderer#a()}, {@link Renderer#b()} and {@link Renderer#c()}.
     */
    public void drawTriangle() {
        drawTriangle(a, b, c);
    }

    public void drawTriangle(V a, V b, V c) {
        shader.vertex(a);
        shader.vertex(b);
        shader.vertex(c);

        if (cullFace(a, b, c)) {
            return;
        }

        transformToWindow(a);
        transformToWindow(b);
        transformToWindow(c);

        V temp;
        if (a.position().y > b.position().y) {
            temp = a;
            a = b;
            b = temp;
        }
        if (b.position().y > c.position().y) {
            temp = b;
            b = c;
            c = temp;
        }
        if (a.position().y > b.position().y) {
            temp = a;
            a = b;
            b = temp;
        }

        float ay = a.position().y, by = b.position().y, cy = c.position().y;
        if (ay == by) {
            if (a.position().x > b.position().x) {
                fillFlatTopTriangle(b, a, c);
            } else {
                fillFlatTopTriangle(a, b, c);
            }
        } else if (by == cy) {
            if (b.position().x > c.position().x) {
                fillFlatBottomTriangle(a, c, b);
            } else {
                fillFlatBottomTriangle(a, b, c);
            }
        } else {
            float t = (ay - by) / (ay - cy);
            a.lerp(c, t, m);
            if (m.position().x > b.position().x) {
                fillFlatBottomTriangle(a, b, m);
                fillFlatTopTriangle(b, m, c);
            } else {
                fillFlatBottomTriangle(a, m, b);
                fillFlatTopTriangle(m, b, c);
            }
        }
    }

    private boolean cullFace(V a, V b, V c) {
        switch (cullMode) {
            default:
            case NEVER:
                return false;
            case FRONT_AND_BACK:
                return true;
            case BACK:
                return dot(a, b, c) < 0;
            case FRONT:
                return dot(a, b, c) > 0;
        }
    }

    private static float dot(Vertex<?> a, Vertex<?> b, Vertex<?> c) {
        // v01 = v0 - v1;
        // v12 = v1 - v2;
        // normal = v01 x v12
        // dot = normal · (0, 0, 1)

        Vector4f v0 = a.position();
        Vector4f v1 = b.position();
        Vector4f v2 = c.position();

        float x01 = v0.x - v1.x;
        float y01 = v0.y - v1.y;
        float x12 = v1.x - v2.x;
        float y12 = v1.y - v2.y;

        return x01 * y12 - y01 * x12;
    }

    private void transformToWindow(V vertex) {
        // Clip coordinates to NDC.
        vertex.perspectiveDivide();

        // NDC to window coordinates.
        Vector4f position = vertex.position();
        position.x = (position.x + 1) * fHalfWidth + fMinX;
        position.y = (1 - position.y) * fHalfHeight + fMinY; // flip-Y
        position.z = (position.z + 1) * 0.5f; // TODO: depth range
    }

    /**
     * @param a The top left vertex
     * @param b The top right vertex
     * @param c The bottom vertex
     */
    private void fillFlatTopTriangle(V a, V b, V c) {
        float ay = a.position().y, cy = c.position().y, dy = cy - ay, ayWithPixelCenterOffset = ay - 0.5f;
        int minY = Math.max(round(ay), this.minY);
        int maxY = Math.min(round(cy), this.maxY);

        for (int y = minY; y < maxY; y++) {
            float t = (y - ayWithPixelCenterOffset) / dy;
            a.lerp(c, t, l);
            b.lerp(c, t, r);

            fillScanline(l, r, y);
        }
    }

    /**
     * @param a The top vertex
     * @param b The bottom left vertex
     * @param c The bottom right vertex
     */
    private void fillFlatBottomTriangle(V a, V b, V c) {
        float ay = a.position().y, cy = c.position().y, dy = cy - ay, ayWithPixelCenterOffset = ay - 0.5f;
        int minY = Math.max(round(ay), this.minY);
        int maxY = Math.min(round(cy), this.maxY);

        for (int y = minY; y < maxY; y++) {
            float t = (y - ayWithPixelCenterOffset) / dy;
            a.lerp(b, t, l);
            a.lerp(c, t, r);

            fillScanline(l, r, y);
        }
    }

    /**
     * @param l The left vertex
     * @param r The right vertex
     * @param y The y coordinate
     */
    private void fillScanline(V l, V r, int y) {
        float lx = l.position().x, rx = r.position().x, dx = rx - lx, lxWithPixelCenterOffset = lx - 0.5f;
        int minX = Math.max(round(lx), this.minX);
        int maxX = Math.min(round(rx), this.maxX);

        for (int x = minX; x < maxX; x++) {
            float t = (x - lxWithPixelCenterOffset) / dx;
            l.lerp(r, t, p);
            fillPixel(p, x, y);
        }
    }

    private static int round(float f) {
        // Negative input's result is incorrect.
        return (int) (f + 0.5f);
    }

    private final Vector4f srcColor = new Vector4f();
    private final Vector4f destColor = new Vector4f();

    private void fillPixel(V p, int x, int y) {
        p.beforeFragmentShader();

        if (shader.fragment(p, srcColor)) {
            return;
        }

        float depth = p.position().z;

        // Discard fragments with depths outside the range of [0, 1].
        if (depth < 0f || depth > 1f) {
            return;
        }

        if (depthTest) {
            if (!depthTest(depth, frameBuffer.getDepth(x, y))) {
                return;
            }

            if (depthWrite) {
                frameBuffer.setDepth(x, y, depth);
            }
        }

        blend(srcColor, x, y);

        frameBuffer.setColor(x, y, srcColor);
    }

    private boolean depthTest(float incomingDepth, float storedDepth) {
        switch (depthFunc) {
            case NEVER:
                return false;
            case ALWAYS:
                return true;
            default:
            case LESS:
                return incomingDepth < storedDepth;
            case LESS_EQUAL:
                return incomingDepth <= storedDepth;
            case GREATER:
                return incomingDepth > storedDepth;
            case GREATER_EQUAL:
                return incomingDepth >= storedDepth;
            case EQUAL:
                return incomingDepth == storedDepth;
            case NOT_EQUAL:
                return incomingDepth != storedDepth;
        }
    }

    private void blend(Vector4f src, int x, int y) {
        switch (blendMode) {
            default:
            case OFF: {
                break;
            }
            case SRC_OVER: {
                Vector4f dest = frameBuffer.getColor(x, y, destColor);
                float sFactor = src.w;
                float dFactor = 1 - sFactor;
                src.x = src.x * sFactor + dest.x * dFactor;
                src.y = src.y * sFactor + dest.y * dFactor;
                src.z = src.z * sFactor + dest.z * dFactor;
                src.w = src.w * sFactor + dest.w * dFactor;
                break;
            }
            case ADD: {
                Vector4f dest = frameBuffer.getColor(x, y, destColor);
                src.add(dest, src);
                break;
            }
        }
    }
}
