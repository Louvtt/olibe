package olibe.ui;

import java.util.*;

import olibe.render.Texture;    


/**
 * Generic Font class
 */
public abstract class Font {
    /** List of Character kerning informations */
    protected Map<Character,CharInfo> charactersUVs;
    /** Name of the font */
    private String name;
    /** Texture atlas of the font */
    protected Texture atlas;
    /** Line height of this font (max char height) */
    protected float lineHeight;
    /** Space width of this font */
    protected float spaceWidth;

    /** Characted kerning information */
    public class CharInfo {
        /** Uv rect inside the texture */
        public Rect source;
        /** Destination properties */
        public Rect dest;
        /** Space from dest.x to next char */
        public float advance;

        /**
         * Create a character kerning information
         * @param source source rect in the atlas
         * @param dest dest rect in pixels
         * @param advance number of pixels before next char
         */
        public CharInfo(Rect source, Rect dest, float advance) {
            this.source  = source;
            this.dest    = dest;
            this.advance = advance;
        }
    }

    /**
     * Create a font
     * @param name name of the font
     */
    protected Font(String name) {
        this.name = name;
        this.charactersUVs = new HashMap<Character, CharInfo>();
        this.lineHeight = 1;
        this.spaceWidth = 1;
    }

    /**
     * Returns the kerning informations about the letter in this font
     * @param letter letter
     * @return the kerning informations about the letter in this font
     */
    public CharInfo getCharInfo(char letter) {
        return this.charactersUVs.get(letter);
    }

    /**
     * Returns the texture font atlas of this font
     * @return the texture font atlas of this font
     */
    public Texture getAtlas() {
        return atlas;
    }

    /**
     * Returns the name of this font
     * @return the name of this font
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the line height of this font
     * @return the line height of this font
     */
    public float getLineHeight() {
        return lineHeight;
    }   

    /**
     * Returns the space width of this font
     * @return the space width of this font
     */
    public float getSpaceWidth() {
        return spaceWidth;
    }
}
