package olibe.scene;

/**
 * Component of a node
 */
public interface Component {
    /** World render method */
    default public void onRender(){}
    /**
     * Update render method
     * @param delta frame delta time (in seconds)
     */
    default public void onUpdate(double delta){}
    /** Screen/UI render method */
    default public void onRenderUI(){}

    /** Delete the component */
    public void delete();
}
