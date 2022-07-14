package student_player;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private PentagoBoardState data = null;

    private PentagoMove move = null;

    private boolean max = false;

    private double heuristic = Double.MIN_VALUE;

    private List<Node> children = new ArrayList<>();

    private Node parent = null;

    public Node(PentagoBoardState data, PentagoMove move, boolean max){
        this.data = data;
        this.move = move;
        this.max = max;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    public void setMax(boolean max) {
        this.max = max;
    }

    public boolean isMax() {
        return max;
    }

    public Node addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<Node> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    public List<Node> getChildren() {
        return children;
    }

    public PentagoBoardState getData() {
        return data;
    }

    public PentagoMove getMove() {
        return move;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }
}
