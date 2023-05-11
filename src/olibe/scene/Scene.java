package olibe.scene;

import java.util.*;

import olibe.core.Log;
import olibe.render.Shader;

/**
 * Represent a scene tree
 */
public class Scene {
    /** Name of this scene */
    protected String name;
    /** Root node of this scene */
    protected SceneNode root;

    /** Function for updating each node */
    protected static final SceneNodeFunction UPDATE_FUNC = new SceneNodeFunction() {
        public <T> void call(SceneNode node, T delta) {
            if(node != null) {
                for(Component comp : node.getComponents()) {
                    comp.onUpdate((float)delta);
                }
            }
        };
    };

    /** Function for rendering each node (world) */
    protected static final SceneNodeFunction RENDER_FUNC = new SceneNodeFunction() {
        public <T> void call(SceneNode node, T extra) {
            if(node != null) {
                for(Component comp : node.getComponents()) {
                    comp.onRender();
                }
            }
        };
    };

    /** Function for rendering each node (screen/ui) */
    protected static SceneNodeFunction RENDER_UI_FUNC = new SceneNodeFunction() {
        public <T> void call(SceneNode node, T extra) {
            if(node != null) {
                for(Component comp : node.getComponents()) {
                    comp.onRenderUI();
                }
            }
        };
    };

    /**
     * Create a scene
     * @param name name of the scene
     */
    public Scene(String name) {
        this.name = name;
        this.root = new SceneNode("root");
    }

    /**
     * Returns a node from a path
     * @param path path to the node (ex: 'MainMenu/PlayButton')
     * @return the resolved node from the path
     */
    public SceneNode getNode(String path) {
        if(path.length() == 0) return root;

        int sep = path.indexOf('/');
        if(sep == -1)
            return root.getChild(path);

        String sceneNode      = path.substring(0, sep);
        String currentPath    = path.substring(sep+1);
        SceneNode currentNode = root.getChild(sceneNode);
        
        while(currentPath.length() > 0 && !currentPath.equals("/")) {
            sep = currentPath.indexOf('/');
            if(sep == -1) sep = currentPath.length();
            sceneNode   = currentPath.substring(0,sep);
            currentPath = currentPath.substring(sep);
        
            currentNode = currentNode.getChild(sceneNode);
            if(currentNode == null) return null;
        }
        return currentNode;
    }

    /**
     * Add a node to another node from a path
     * @param path path to the node you want to add a node to
     * @param node node to add
     * @return this
     */
    public Scene addNode(String path, SceneNode node) {
        SceneNode target = this.getNode(path);
        if(target != null) {
            target.addChild(node);
        } else {
            Log.Get().error("Node not found for ["+path+"]");
        }
        return this;
    }

    /**
     * Apply a node function for each node of this scene using DFS
     * @param <T> extra argument type
     * @param function function to apply for each node
     * @param extra extra argument
     */
    public <T> void applyNode(SceneNodeFunction function, T extra) {
        Queue<SceneNode> toExplore = new ArrayDeque<>();
        toExplore.add(root);
        List<SceneNode> explored = new ArrayList<SceneNode>();

        while(toExplore.size() != 0) {
            SceneNode node = toExplore.remove();
            explored.add(node);

            function.call(node, extra);
            if(node.isActive()) {
                for(SceneNode child : node.getChildren()) {
                    if(!explored.contains(child)) {
                        toExplore.add(child);
                    }
                }
            }
        }
    }

    /**
     * Update this scene
     * @param delta frame delta time (in seconds)
     */
    public void update(float delta) {
        applyNode(UPDATE_FUNC, delta);
    }   

    /**
     * Render this scene (world)
     * @param shader shader to use
     */
    public void render(Shader shader) {
        Shader.makeActive(shader);
        applyNode(RENDER_FUNC, null);
    }

    /**
     * Render this scene (screen/ui)
     */
    public void renderUI() {
        applyNode(RENDER_UI_FUNC, null);
    }

    /**
     * Returns the screen representation of this scene
     * @return the screen representation of this scene
     */
    @Override
    public String toString() {
        return "Scene ["+this.name+"]: \n" + root.toString();
    }
}
