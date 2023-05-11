package olibe.scene;

/**
 * Function to apply at a node
 */
public interface SceneNodeFunction {
    /**
     * Function applied to a node
     * @param <T> arbitrary and optional type used for extra arguments
     * @param node current node
     * @param extra extra arguments
     */
    public <T> void call(SceneNode node, T extra);
}