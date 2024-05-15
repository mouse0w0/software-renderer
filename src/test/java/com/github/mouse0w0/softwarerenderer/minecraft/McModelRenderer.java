package com.github.mouse0w0.softwarerenderer.minecraft;

import com.github.mouse0w0.softwarerenderer.Renderer;
import com.github.mouse0w0.softwarerenderer.minecraft.model.McElement;
import com.github.mouse0w0.softwarerenderer.minecraft.model.McFace;
import com.github.mouse0w0.softwarerenderer.minecraft.model.McFacing;
import com.github.mouse0w0.softwarerenderer.minecraft.model.McModel;
import com.github.mouse0w0.softwarerenderer.texture.Texture2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Map;
import java.util.function.Function;

public final class McModelRenderer {
    private static final Matrix4f IDENTIFY = new Matrix4f();

    private static final float SCALE_ROTATION_22_5 = 1.0f / (float) Math.cos(Math.PI / 8d) - 1.0f;
    private static final float SCALE_ROTATION_GENERAL = 1.0f / (float) Math.cos(Math.PI / 4d) - 1.0f;

    private static final float TO_RADIANS = (float) (Math.PI / 180.0);

    private static final int[][] VERTEX_DATA_INDICES = {
            {1, 0, 4, 1, 4, 5}, // Down
            {2, 3, 7, 2, 7, 6}, // Up
            {6, 4, 0, 6, 0, 2}, // North
            {3, 1, 5, 3, 5, 7}, // South
            {2, 0, 1, 2, 1, 3}, // West
            {7, 5, 4, 7, 4, 6}  // East
    };
    private static final int[] TEX_COORDS_INDICES = {0, 1, 3, 0, 3, 2};
    private static final int[] TEX_COORDS_90_INDICES = {1, 3, 2, 1, 2, 0};
    private static final int[] TEX_COORDS_180_INDICES = {3, 2, 0, 3, 0, 1};
    private static final int[] TEX_COORDS_270_INDICES = {2, 0, 1, 2, 1, 3};

    public void render(McModel model,
                       Function<String, Texture2D> textureGetter,
                       Renderer<MinecraftVertex> renderer,
                       MinecraftShader shader) {
        Map<String, String> textures = model.getTextures();
        for (McElement element : model.getElements()) {
            Vector3f[] boxPositions = getBoxPositions(element);
            Map<McFacing, McFace> faces = element.getFaces();
            for (McFacing facing : faces.keySet()) {
                renderFace(facing, faces.get(facing), textures, boxPositions, textureGetter, renderer, shader);
            }
        }
    }

    private final Vector4f tempTexCoord = new Vector4f();
    // private final Vector4f tempColor = new Vector4f();
    private final Vector3f tempAB = new Vector3f();
    private final Vector3f tempAC = new Vector3f();
    private final Vector3f tempNormal = new Vector3f();

    private void renderFace(McFacing facing,
                            McFace face,
                            Map<String, String> textures,
                            Vector3f[] boxPositions,
                            Function<String, Texture2D> textureGetter,
                            Renderer<MinecraftVertex> renderer,
                            MinecraftShader shader) {
        Vector4f texCoord = getTexCoord(face, tempTexCoord);
        // Vector4f color = getFaceColor(facing, tempColor);
        int[] posIndices = VERTEX_DATA_INDICES[facing.ordinal()];
        int[] texCoordIndices = getTexCoordIndices(face);

        shader.sampler.setTexture(textureGetter.apply(getTextureName(face.getTexture(), textures)));

        boxPositions[posIndices[1]].sub(boxPositions[posIndices[0]], tempAB);
        boxPositions[posIndices[2]].sub(boxPositions[posIndices[0]], tempAC);
        tempAB.cross(tempAC, tempNormal);
        tempNormal.normalize();

        MinecraftVertex a = renderer.a();
        MinecraftVertex b = renderer.b();
        MinecraftVertex c = renderer.c();

        a.position.set(boxPositions[posIndices[0]], 1f);
        a.color.set(1f);
        texCoord(texCoord, texCoordIndices[0], a.texCoord);
        a.normal.set(tempNormal);

        b.position.set(boxPositions[posIndices[1]], 1f);
        b.color.set(1f);
        texCoord(texCoord, texCoordIndices[1], b.texCoord);
        b.normal.set(tempNormal);

        c.position.set(boxPositions[posIndices[2]], 1f);
        c.color.set(1f);
        texCoord(texCoord, texCoordIndices[2], c.texCoord);
        c.normal.set(tempNormal);

        renderer.drawTriangle();

        a.position.set(boxPositions[posIndices[3]], 1f);
        a.color.set(1f);
        texCoord(texCoord, texCoordIndices[3], a.texCoord);
        a.normal.set(tempNormal);

        b.position.set(boxPositions[posIndices[4]], 1f);
        b.color.set(1f);
        texCoord(texCoord, texCoordIndices[4], b.texCoord);
        b.normal.set(tempNormal);

        c.position.set(boxPositions[posIndices[5]], 1f);
        c.color.set(1f);
        texCoord(texCoord, texCoordIndices[5], c.texCoord);
        c.normal.set(tempNormal);

        renderer.drawTriangle();
    }

    private int[] getTexCoordIndices(McFace face) {
        switch (face.getRotation()) {
            default:
                return TEX_COORDS_INDICES;
            case 90:
                return TEX_COORDS_90_INDICES;
            case 180:
                return TEX_COORDS_180_INDICES;
            case 270:
                return TEX_COORDS_270_INDICES;
        }
    }

    // private static Vector4f getFaceColor(McFacing facing, Vector4f dest) {
    //     switch (facing) {
    //         case DOWN:
    //             return dest.set(0.5f, 0.5f, 0.5f, 1f);
    //         case NORTH:
    //         case SOUTH:
    //             return dest.set(0.8f, 0.8f, 0.8f, 1f);
    //         case WEST:
    //         case EAST:
    //             return dest.set(0.6f, 0.6f, 0.6f, 1f);
    //         case UP:
    //         default:
    //             return dest.set(1f, 1f, 1f, 1f);
    //     }
    // }

    private final Vector3f[] boxPositions = new Vector3f[]{
            new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f(),
            new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f()
    };

    private Vector3f[] getBoxPositions(McElement element) {
        Vector3f from = element.getFrom();
        Vector3f to = element.getTo();
        Matrix4f matrix = getRotationMatrix(element.getRotation());

        for (int i = 0; i < 8; i++) {
            getBoxPosition(from, to, i, boxPositions[i]).mulPosition(matrix);
        }
        return boxPositions;
    }

    private static Vector3f getBoxPosition(Vector3f from, Vector3f to, int index, Vector3f dest) {
        float x = (index & 0x4) == 0 ? from.x : to.x;
        float y = (index & 0x2) == 0 ? from.y : to.y;
        float z = (index & 0x1) == 0 ? from.z : to.z;
        return dest.set(x, y, z).div(16);
    }

    private static Matrix4f getRotationMatrix(McElement.Rotation rotation) {
        if (rotation == null) return IDENTIFY;

        Matrix4f matrix = new Matrix4f();
        Vector3f scale = new Vector3f();
        switch (rotation.getAxis()) {
            case X:
                matrix.rotateX(rotation.getAngle() * TO_RADIANS);
                scale.set(0f, 1f, 1f);
                break;
            case Y:
                matrix.rotateY(rotation.getAngle() * TO_RADIANS);
                scale.set(1f, 0f, 1f);
                break;
            case Z:
                matrix.rotateZ(rotation.getAngle() * TO_RADIANS);
                scale.set(1f, 1f, 0f);
                break;
        }

        if (rotation.isRescale()) {
            if (Math.abs(rotation.getAngle()) == 22.5F) {
                scale.mul(SCALE_ROTATION_22_5);
            } else {
                scale.mul(SCALE_ROTATION_GENERAL);
            }

            scale.add(1f, 1f, 1f);
        } else {
            scale.set(1f);
        }

        Vector3f origin = rotation.getOrigin().div(16, new Vector3f());
        return matrix.scale(scale).translateLocal(origin).translate(origin.negate());
    }

    private static Vector4f getTexCoord(McFace face, Vector4f dest) {
        Vector4f faceUv = face.getUv();
        float u0 = normalizeTexCoord(faceUv.x);
        float v0 = normalizeTexCoord(faceUv.y);
        float u1 = normalizeTexCoord(faceUv.z);
        float v1 = normalizeTexCoord(faceUv.w);
        return dest.set(shrinkTexCoord(u0, u1), shrinkTexCoord(v0, v1), shrinkTexCoord(u1, u0), shrinkTexCoord(v1, v0));
    }

    private static float normalizeTexCoord(float v) {
        if (v < 0f) return 0f;
        if (v > 16f) return 16f;
        return v / 16f;
    }

    private static float shrinkTexCoord(float a, float b) {
        // return a * .999f + b * .001f;
        return a;
    }

    private static void texCoord(Vector4f texCoord, int flag, Vector2f dest) {
        float u = (flag & 0x2) == 0 ? texCoord.x : texCoord.z;
        float v = (flag & 0x1) == 0 ? texCoord.y : texCoord.w;
        dest.set(u, v);
    }

    private static String getTextureName(String name, Map<String, String> textures) {
        while (name.charAt(0) == '#') {
            name = textures.get(name.substring(1));
        }
        return name;
    }
}
