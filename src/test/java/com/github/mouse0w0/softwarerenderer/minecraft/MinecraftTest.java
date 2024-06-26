package com.github.mouse0w0.softwarerenderer.minecraft;

import com.github.mouse0w0.softwarerenderer.BlendMode;
import com.github.mouse0w0.softwarerenderer.CullMode;
import com.github.mouse0w0.softwarerenderer.Renderer;
import com.github.mouse0w0.softwarerenderer.framebuffer.DefaultFrameBuffer;
import com.github.mouse0w0.softwarerenderer.minecraft.model.Model;
import com.github.mouse0w0.softwarerenderer.minecraft.model.ModelLoader;
import com.github.mouse0w0.softwarerenderer.sampler.WrapMode;
import com.github.mouse0w0.softwarerenderer.texture.FloatTexture2D;
import com.github.mouse0w0.softwarerenderer.texture.RgbaTexture2D;
import com.github.mouse0w0.softwarerenderer.texture.Texture2D;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
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

        Model model = ModelLoader.load(MinecraftTest.class.getResourceAsStream("/model.json"));
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

        new ModelRenderer().render(model, textures::get, ModelRenderer.TintGetter.DEFAULT, false, renderer, shader);

        ImageIO.write(frameBuffer.getColorTexture().toBufferedImage(), "PNG", new File("minecraft.png"));
    }
}
