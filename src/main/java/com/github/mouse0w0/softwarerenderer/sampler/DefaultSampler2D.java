package com.github.mouse0w0.softwarerenderer.sampler;

import com.github.mouse0w0.softwarerenderer.texture.Texture2D;
import org.joml.Vector4f;

public class DefaultSampler2D implements Sampler2D {
    private Texture2D texture;
    private int textureMaxX;
    private int textureMaxY;
    private FilterMode magFilter = FilterMode.NEAREST;
    private WrapMode wrapS = WrapMode.REPEAT;
    private WrapMode wrapT = WrapMode.REPEAT;
    private Vector4f borderColor;

    public DefaultSampler2D() {
    }

    public DefaultSampler2D(Texture2D texture) {
        setTexture(texture);
    }

    public Texture2D getTexture() {
        return texture;
    }

    public void setTexture(Texture2D texture) {
        this.texture = texture;
        if (texture != null) {
            this.textureMaxX = texture.getWidth() - 1;
            this.textureMaxY = texture.getHeight() - 1;
        } else {
            this.textureMaxX = 0;
            this.textureMaxY = 0;
        }
    }

    public FilterMode getMagFilter() {
        return magFilter;
    }

    public void setMagFilter(FilterMode magFilter) {
        this.magFilter = magFilter;
    }

    public WrapMode getWrapS() {
        return wrapS;
    }

    public void setWrapS(WrapMode wrapS) {
        this.wrapS = wrapS;
    }

    public WrapMode getWrapT() {
        return wrapT;
    }

    public void setWrapT(WrapMode wrapT) {
        this.wrapT = wrapT;
    }

    public void setBorderColor(Vector4f borderColor) {
        this.borderColor = borderColor;
    }

    public Vector4f getBorderColor() {
        return borderColor;
    }

    @Override
    public Vector4f sample(float u, float v, Vector4f dest) {
        if (u < 0 || u > 1 || v < 0 || v > 1) {
            if (wrapS == WrapMode.CLAMP_TO_BORDER || wrapT == WrapMode.CLAMP_TO_BORDER) {
                dest.set(borderColor);
            }

            u = wrap(u, wrapS);
            v = wrap(v, wrapT);
        }

        switch (magFilter) {
            case NEAREST:
                nearest(u, v, dest);
                break;
            default:
                dest.set(0);
                break;
        }

        return dest;
    }

    private float wrap(float v, WrapMode wrapMode) {
        switch (wrapMode) {
            case REPEAT:
                v = v - (int) v;
                if (v < 0) {
                    v = v + 1f;
                }
                return v;
            case MIRRORED_REPEAT:
                int i = (int) v;
                v = v - i;

                if (v < 0) {
                    v = -v;
                }

                if (i % 2 != 0) {
                    v = 1 - v;
                }
                return v;
            case CLAMP_TO_EDGE:
                if (v < 0) {
                    return 0;
                } else if (v > 1) {
                    return 1;
                }
                return v;
            case CLAMP_TO_BORDER:
            default:
                return v;
        }
    }

    private void nearest(float u, float v, Vector4f dest) {
        int x = Math.min((int) (u * texture.getWidth()), textureMaxX);
        int y = Math.min((int) (v * texture.getHeight()), textureMaxY);
        texture.getPixel(x, y, dest);
    }
}
