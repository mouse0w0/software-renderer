package com.github.mouse0w0.softwarerenderer.triangle;

import com.github.mouse0w0.softwarerenderer.framebuffer.DefaultFrameBuffer;
import com.github.mouse0w0.softwarerenderer.texture.RgbaTexture2D;
import com.github.mouse0w0.softwarerenderer.Renderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TriangleTest {
    public static void main(String[] args) throws IOException {
        Renderer<TriangleVertex> renderer = new Renderer<>(TriangleVertex::new);
        TriangleShader shader = new TriangleShader();
        renderer.setShader(shader);
        DefaultFrameBuffer frameBuffer = new DefaultFrameBuffer(new RgbaTexture2D(1024, 1024));
        renderer.setFrameBuffer(frameBuffer);
        renderer.setViewport(0, 0, 1024, 1024);

        // right bottom
        TriangleVertex a = renderer.a();
        a.position.set(0.5f, -0.5f, 0, 1);
        a.color.set(1, 0, 0, 1);
        // left bottom
        TriangleVertex b = renderer.b();
        b.position.set(-0.5f, -0.5f, 0, 1);
        b.color.set(0, 1, 0, 1);
        // top
        TriangleVertex c = renderer.c();
        c.position.set(0, 0.5f, 0, 1);
        c.color.set(0, 0, 1, 1);

        renderer.drawTriangle();

        BufferedImage image = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
        frameBuffer.getColorTexture().blit(image);
        ImageIO.write(image, "PNG", new File("triangle.png"));
    }
}
