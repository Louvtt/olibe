package olibe.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import olibe.core.Log;
import olibe.exception.LWJGLException;
import olibe.ui.BitmapFont;
import olibe.ui.Font;

/** Group all fonts and load them */
public class FontLibrary {
    /** List of all loaded fonts */
    protected Map<String, Font> fonts;
    /** Unique instance of the Font library (Singleton) */
    protected static FontLibrary instance = null;

    /** Creates a Font library */
    protected FontLibrary() {
        fonts = new HashMap<>();
    }
    /**
     * Returns the current Font Library instance  
     * @return the current Font Library instance
     */
    public static FontLibrary Get() {
        if(instance == null) {
            instance = new FontLibrary();
        }
        return instance;
    }

    /**
     * Load a bitmap font and register it in this library
     * @param path path to the bitmap font image atlas 
     * @param charsize size of each grid cell for each char
     * @param charset present characters in the font (from left to right and top to bottom) 
     * @return the loaded bitmap font or null if an error occured
     */
    public Font loadBitmapFont(String path, Vector2f charsize, String charset) {
        try {
            Font loadedFont = new BitmapFont(path, charsize, charset);
            fonts.put(loadedFont.getName(), loadedFont);
            return loadedFont;
        } catch (LWJGLException | IOException e) { 
            Log.Get().error("Error while loading bitmap font ["+path+"]");
            return null;
        }
    }

    /**
     * Returns the corresponding font in this library
     * @param fontName the font to get
     * @return the corresponding font in this library
     */
    public Font getFont(String fontName) {
        return fonts.get(fontName);
    }
}
