package olibe.scene;

import java.util.*;

/**
 * Represent a Node in a scene
 */
public class SceneNode {
    /** Name of this node */
    protected String name;
    /** List of its children */
    protected List<SceneNode> children;
    /** Transform of this node */
    protected Transform transform;
    /** Components of this node */
    protected List<Component> components;
    /** The node is active or not */
    protected boolean isActive;
    /** Indentation string for toString formatting */
    protected static final String INDENT_STRING = "  ";

    /**
     * Creates a node
     * @param name name of the node
     */
    public SceneNode(String name) {
        this.name = name;
        this.children = new ArrayList<SceneNode>();
        this.transform = new Transform();
        this.components = new ArrayList<>();
        this.isActive = true;
    }

    /**
     * Change the node active state
     * @param isActive new active state
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Returns true if the node is active, false otherwise
     * @return true if the node is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Add a component to this node
     * @param component component to add
     * @return this
     */
    public SceneNode addComponent(Component component) {
        if(component == null) return this; // don't add null components :(
        this.components.add(component);
        return this;
    }

    /**
     * Add a child in this node
     * @param child child to add
     * @return this
     */
    public SceneNode addChild(SceneNode child) {
        this.children.add(child);
        return this;
    }

    /**
     * Returns the string representation of this node and its children
     * @return the string representation of this node and its children
     */
    public String toString() {
        return this.toStringInternal("");        
    }

    /**
     * Returns null or the child with the same name as wanted
     * @param name name of the child
     * @return null or the child with the same name as wanted
     */
    public SceneNode getChild(String name) {
        for(SceneNode child : this.children) {
            if(child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Returns the name of this node
     * @return the name of this node
     */
    public String getName() {
        return name;
    }

    /**
     * Auxiliary method for toString
     * @param indent indentation string
     * @return the concatenated children string representations
     */
    protected String toStringInternal(String indent) {
        String res = indent + this.name + ((this.children.size()>0)?":\n":"\n");
        for(SceneNode child : this.children) {
            res += child.toStringInternal(indent+INDENT_STRING);
        }
        return res;
    }

    /**
     * Returns the transform of this node
     * @return the transform of this node
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Returns the children of this node
     * @return the children of this node
     */
    public List<SceneNode> getChildren() {
        return children;
    }

    /**
     * Remove and clear all childrens
     */
    public void clearChildren() {
        for(SceneNode child : this.children) {
            child.delete();
        }
        this.children.clear();
    }

    /**
     * Returns the components of this node 
     * @return the components of this node
     */
    public List<Component> getComponents() {
        return components;
    }

    /**
     * Delete this node and its children
     */
    public void delete() {
        this.clearChildren();

        for(Component c : this.components) {
            c.delete();
        }
        this.components.clear();
    }
}
