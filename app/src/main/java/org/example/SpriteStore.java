package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * A resource manager for sprites in the game.
 */
public class SpriteStore {
    /**
     * The single instance of the SpriteStore
     */
    public static final SpriteStore SINGLE_STORE = new SpriteStore();

    public static SpriteStore get() {
        return SINGLE_STORE;
    }

    /**
     * The cached sprite map, from reference to sprite instance
     */
    public HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    public SpriteStore() {
    }

    /**
     * Retrieve a sprite from the store
     *
     * @param ref The reference to the image to use for the sprite
     * @return A sprite instance containing an accelerate image of the request reference
     */
    public Sprite getSprite(String ref) {
        // if the sprite is in the cache, return the existing version
        if (sprites.get(ref) != null) {
            return sprites.get(ref);
        }

        // otherwise grab the sprite from the resource loader
        BufferedImage sourceImage = null;

        try {
            // The ClassLoader.getResource() ensures we get the sprite
            // from the appropriate place, this helps with deploying the game
            // with things like webstart. You could equally do a file look
            // up here.
            URL url = this.getClass().getClassLoader().getResource(ref);

            if (url == null) {
                fail("Can't find ref: " + ref);
            }

            // use ImageIO to read the image in
            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            fail("Failed to load: " + ref);
        }

        // create an accelerated image of the right size to store our sprite in
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

        // draw our source image into the accelerated image
        image.getGraphics().drawImage(sourceImage, 0, 0, null);

        // create a sprite, add it the cache then return it
        Sprite sprite = new Sprite(image);
        sprites.put(ref, sprite);

        return sprite;
    }

    /**
     * Utility method to handle resource loading failure
     */
    public void fail(String message) {
        // if a resource isn't available
        // we dump the message and exit the game
        System.err.println(message);
        System.exit(0);
    }
}