package olibe.ui;

import java.io.IOException;

import org.joml.Vector2f;

import olibe.core.Log;
import olibe.exception.LWJGLException;
import olibe.render.ImageTexture;

/**
 * Bitmap image font
 */
public class BitmapFont extends Font {

    /**
     * Load and create bitmap font
     * @param filepath path to the bitmap font image atlas 
     * @param charSize size of each grid cell for each char
     * @param charset present characters in the font (from left to right and top to bottom) 
     * @exception LWJGLException failed to create the opengl texture atlas 
     * @exception IOException failed to load the font image atlas
     */
    public BitmapFont(String filepath, Vector2f charSize, String charset) 
    throws LWJGLException, IOException {
        super(filepath.substring(filepath.lastIndexOf("/")+1, filepath.indexOf('.')));

        // load file
        this.atlas = new ImageTexture(filepath, false);
        // build charset
        Vector2f uvCharSize = new Vector2f(charSize).div(this.atlas.getSize().x(), this.atlas.getSize().y());
        Vector2f pos = new Vector2f();
        for(char c : charset.toCharArray()) {
            this.charactersUVs.put(c, new CharInfo(
                new Rect(pos.x(), pos.y(), uvCharSize.x(), uvCharSize.y()),
                new Rect(0, 0, charSize.x(), charSize.y()),
                charSize.x()
            ));
            pos.x += uvCharSize.x();
            // greater than last one that can possibly fit 
            if(pos.x > 1f - uvCharSize.x()) {
                pos.x = 0f;
                pos.y += uvCharSize.y();
            }
        }


        this.lineHeight = charSize.y();
        this.spaceWidth = charSize.x();


        Log.Get().debug("Loaded bitmap font ["+this.getName()+"]");
    }
}
