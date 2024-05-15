package com.github.mouse0w0.softwarerenderer.texture;

import com.github.mouse0w0.softwarerenderer.Renderer;
import com.github.mouse0w0.softwarerenderer.framebuffer.DefaultFrameBuffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureTest {
    public static void main(String[] args) throws IOException {
        Renderer<TextureVertex> renderer = new Renderer<>(TextureVertex::new);
        TextureShader shader = new TextureShader();
        shader.sampler.setTexture(new RgbaTexture2D(ImageIO.read(new File("resource\\texture2.png"))));
        renderer.setShader(shader);
        DefaultFrameBuffer frameBuffer = new DefaultFrameBuffer(new RgbaTexture2D(1024, 1024));
        renderer.setFrameBuffer(frameBuffer);
        renderer.setViewport(0, 0, 1024, 1024);

        // right bottom
        TextureVertex a = renderer.a();
        a.position.set(0.5f, -0.5f, 0, 1);
        a.color.set(1);
        a.texCoord.set(1, 1);
        // left bottom
        TextureVertex b = renderer.b();
        b.position.set(-0.5f, -0.5f, 0, 1);
        b.color.set(1);
        b.texCoord.set(0, 1);
        // top
        TextureVertex c = renderer.c();
        c.position.set(0, 0.5f, 0, 1);
        c.color.set(1);
        c.texCoord.set(0.5, 0);

        renderer.drawTriangle();

        BufferedImage image = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
        frameBuffer.getColorTexture().blit(image);
        ImageIO.write(image, "PNG", new File("texture.png"));
    }
}
