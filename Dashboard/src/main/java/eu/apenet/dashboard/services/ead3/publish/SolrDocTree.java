/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3.publish;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 *
 * @author mahbub
 */
public class SolrDocTree implements Iterator {

    private final Stack<SolrDocNode> stack = new Stack<>();
    private final SolrDocNode root;

    public SolrDocTree(SolrDocNode argRoot) {
        root = argRoot;
        SolrDocNode node = root;
        while (node != null) {
            stack.push(node);
            node = node.getChild();
        }
    }

    public SolrDocNode getRoot() {
        return root;
    }

    @Override
    public SolrDocNode next() {
        if (stack.isEmpty()) {
            throw new NoSuchElementException();
        }
        SolrDocNode node = stack.pop();
        SolrDocNode result = node;
        
        if (node.getSibling() != null) {
            node = node.getSibling();
            while (node != null) {
                stack.push(node);
                node = node.getChild();
            }
        }
        return result;
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }
}
