package com.github.mouse0w0.softwarerenderer.minecraft.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.lang.reflect.Type;
import java.util.Objects;

@JsonAdapter(ModelTransform.Serializer.class)
public class ModelTransform {
    public static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    public static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    public static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

    private static final float TO_RADIANS = (float) (Math.PI / 180.0);

    private Vector3f translation;
    private Vector3f rotation;
    private Vector3f scale;

    public ModelTransform() {
        this(TRANSLATION_DEFAULT, ROTATION_DEFAULT, SCALE_DEFAULT);
    }

    public ModelTransform(Vector3f translation, Vector3f rotation, Vector3f scale) {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Matrix4f getMatrix() {
        return getMatrix(new Matrix4f());
    }

    public Matrix4f getMatrix(Matrix4f dest) {
        return dest.identity()
                .rotateX(rotation.x() * TO_RADIANS)
                .rotateY(rotation.y() * TO_RADIANS)
                .rotateZ(rotation.z() * TO_RADIANS)
                .scale(scale)
                .translate(translation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelTransform transform = (ModelTransform) o;
        return translation.equals(transform.translation) &&
                rotation.equals(transform.rotation) &&
                scale.equals(transform.scale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translation, rotation, scale);
    }

    public static class Serializer implements JsonSerializer<ModelTransform> {

        private static final ModelTransform TRANSFORM_DEFAULT = new ModelTransform();

        @Override
        public JsonElement serialize(ModelTransform src, Type typeOfSrc, JsonSerializationContext context) {
            if (src.equals(TRANSFORM_DEFAULT)) return JsonNull.INSTANCE;

            JsonObject jsonTransform = new JsonObject();
            if (!TRANSLATION_DEFAULT.equals(src.getTranslation()))
                jsonTransform.add("translation", context.serialize(src.getTranslation()));
            if (!ROTATION_DEFAULT.equals(src.getRotation()))
                jsonTransform.add("rotation", context.serialize(src.getRotation()));
            if (!SCALE_DEFAULT.equals(src.getScale()))
                jsonTransform.add("scale", context.serialize(src.getScale()));
            return jsonTransform;
        }
    }
}
