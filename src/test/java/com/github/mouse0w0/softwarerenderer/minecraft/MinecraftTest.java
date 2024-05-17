package com.github.mouse0w0.softwarerenderer.minecraft;

import com.github.mouse0w0.softwarerenderer.BlendMode;
import com.github.mouse0w0.softwarerenderer.CullMode;
import com.github.mouse0w0.softwarerenderer.Renderer;
import com.github.mouse0w0.softwarerenderer.framebuffer.DefaultFrameBuffer;
import com.github.mouse0w0.softwarerenderer.minecraft.model.McModel;
import com.github.mouse0w0.softwarerenderer.minecraft.model.McModelHelper;
import com.github.mouse0w0.softwarerenderer.sampler.WrapMode;
import com.github.mouse0w0.softwarerenderer.texture.*;
import org.joml.Matrix4f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MinecraftTest {
    public static void main(String[] args) throws IOException {
        Renderer<MinecraftVertex> renderer = new Renderer<>(MinecraftVertex::new);
        MinecraftShader shader = new MinecraftShader();
        shader.sampler.setWrapS(WrapMode.CLAMP_TO_EDGE);
        shader.sampler.setWrapT(WrapMode.CLAMP_TO_EDGE);
        shader.setupGui3DLighting();
        renderer.setShader(shader);
        DefaultFrameBuffer frameBuffer = new DefaultFrameBuffer(new RgbaTexture2D(64, 64), new FloatTexture2D(64, 64));
        renderer.setFrameBuffer(frameBuffer);
        renderer.setViewport(0, 0, 64, 64);
        renderer.clearDepth(1f);
        renderer.setDepthTest(true);
        renderer.setCullMode(CullMode.BACK);
        renderer.setBlendMode(BlendMode.SRC_OVER);

        McModel model = McModelHelper.load(MinecraftTest.class.getResourceAsStream("/model.json"));
        // McModel model = McModelHelper.load(MinecraftTest.class.getResourceAsStream("/white_concrete.json"));

        Map<String, Texture2D> textures = new HashMap<>();
        textures.put("texture", new RgbaTexture2D(ImageIO.read(MinecraftTest.class.getResourceAsStream("/texture.png"))));
        // textures.put("block/white_concrete", new RgbaTexture2D(ImageIO.read(MinecraftTest.class.getResourceAsStream("/white_concrete.png"))));

        shader.modelViewProjectionMatrix.ortho(0f, 16f, 0f, 16f, -1000f, 1000f)
                .translate(8f, 8f, 0f)
                .scale(16f, 16f, 16f)
                .rotateX((float) Math.toRadians(30))
                .rotateY((float) Math.toRadians(225))
                .scale(0.625f, 0.625f, 0.625f)
                .translate(-0.5f, -0.5f, -0.5f);

        new McModelRenderer().render(model, textures::get, renderer, shader);

        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        frameBuffer.getColorTexture().blit(image);
        ImageIO.write(image, "PNG", new File("minecraft.png"));
    }
}
