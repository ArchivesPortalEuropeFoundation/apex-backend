package eu.archivesportaleurope.harvester.oaipmh.portugal.objects;

import java.util.LinkedList;
import java.util.List;

/**
 * User: yoannmoranville
 * Date: 07/02/14
 *
 * @author yoannmoranville
 */
public class Node {
    public String id;
    public List<Node> children;
    public Node parent;

    public Node(Node parent, String id){
        this.parent = parent;
        this.id = id;
        this.children = null;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setChildren(List<Node> children){
        this.children = children;
    }
    public List<Node> getChildren(){
        if(children == null)
            return new LinkedList<Node>();
        return children;
    }

    public void addChild(Node child){
        if(children == null)
            children = new LinkedList<Node>();
        children.add(child);
    }

    public Node getParent() {
        return parent;
    }
    public void setParent(Node parent) {
        this.parent = parent;
    }


    public int sizeWithoutRoot() {
        int size = loopOver(this) - 1; //-1 because we should not count the root node
        return size;
    }
    private int loopOver(Node node) {
        int size = 0;
        size++;
        for(Node child : node.getChildren()) {
            size += loopOver(child);
        }
        return size;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        loopOver(buffer, 0, this);

        return buffer.toString();
    }

    private void loopOver(StringBuffer buffer, int level, Node node) {
        buffer.append("\n").append(getLevel(level++)).append(node.getId());
        for(Node child : node.getChildren()) {
            loopOver(buffer, level, child);
        }
    }

    private String getLevel(int level) {
        StringBuffer buffer = new StringBuffer();
        for(int i = 1; i <= level; i++) {
            buffer.append("-");
        }
        return buffer.toString();
    }
}
