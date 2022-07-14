package student_player;

import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

import java.util.ArrayList;
import java.util.List;
import java.math.*;

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }

    // Generate a minimax tree from given game state and return the root of the tree
    public static Node minimaxGenerate(PentagoBoardState boardState) {
        // clone the current board state for further usage
        PentagoBoardState currentState = (PentagoBoardState) boardState.clone();

        List<PentagoMove> allMoves = currentState.getAllLegalMoves();

        // build the minimax tree from current board state
        Node root = new Node(currentState, null, true);

        // First level min nodes
        for (PentagoMove move : allMoves) {
            PentagoBoardState tempState = (PentagoBoardState) currentState.clone();
            tempState.processMove(move);
            // cut off
            if(heu(tempState) >= -1000000){
                Node temp = new Node(tempState, move, false);
                root.addChild(temp);
            }
        }

        // Second level max nodes
        for (Node child : root.getChildren()) {
            // get all legal moves from the child
            List<PentagoMove> Moves = child.getData().getAllLegalMoves();

            // produce max nodes from legal moves
            for (PentagoMove move : Moves) {
                PentagoBoardState tempState = (PentagoBoardState) child.getData().clone();
                tempState.processMove(move);
                Node temp = new Node(tempState, move, true);
                temp.setHeuristic(heu(temp.getData()));
                // cut off
                if (temp.getHeuristic() > -1000000) {
                    child.addChild(temp);
                }

            }
        }

        return root;
    }

    // Generate heuristic for board states
    public static double heu(PentagoBoardState bs) {
        int turnPlayer = bs.getTurnPlayer();

        PentagoBoardState.Piece[][] board = bs.getBoard();

        PentagoBoardState.Piece currentPiece = PentagoBoardState.Piece.EMPTY;
        PentagoBoardState.Piece opponentPiece = PentagoBoardState.Piece.EMPTY;

        double MAX = Math.pow(50, 8);
        double fin = 0.0;
        double mvalue = 0.0;
        double ovalue = 0.0;
        double scoreM = 10;
        double scoreO = 10;
        int fiveM = 0;
        int fiveO = 0;

        if (turnPlayer == 0) {
            currentPiece = PentagoBoardState.Piece.WHITE;
            opponentPiece = PentagoBoardState.Piece.BLACK;
        } else {
            currentPiece = PentagoBoardState.Piece.BLACK;
            opponentPiece = PentagoBoardState.Piece.WHITE;
        }

        // horizontal check
        for (int i = 0; i < board.length; i++) {
            fiveM = 0;
            fiveO = 0;
            mvalue = Math.max(mvalue, scoreM);
            ovalue = Math.max(ovalue, scoreO);
            scoreM = 5;
            scoreO = 5;
            for (int j = 0; j < board[i].length; j++) {
                if (fiveO == 5) {
                    return MAX;
                }
                if (fiveM == 5) {
                    return MAX;
                }
                if (board[i][j] == currentPiece) {
                    fiveM++;
                    fiveO = 0;
                    scoreM = scoreM * scoreM;
                    if (fiveO >= 3){
                        scoreO = Math.sqrt(scoreO);
                    }else if(fiveO != 0){
                        scoreO = scoreO/5.0;
                    }else{}
                } else if (board[i][j] == opponentPiece) {
                    fiveO++;
                    fiveM = 0;
                    scoreO = scoreO * scoreO;
                    if (fiveM >= 3){
                        scoreM = Math.sqrt(scoreO);
                    }else if(fiveM != 0){
                        scoreM = scoreM/5.0;
                    }else{}
                } else {
                    if (fiveM != 0){
                        fiveM++;
                        fiveO = 0;
                    }else if(fiveO != 0){
                        fiveO++;
                        fiveM = 0;
                    }else{
                        fiveM = 0;
                        fiveO = 0;
                    }
                }
            }
        }

        mvalue = Math.max(mvalue, scoreM);
        ovalue = Math.max(scoreO, ovalue);
        if (mvalue > ovalue){
            fin = Math.max(mvalue, Math.abs(fin));
        }else {
            fin = Math.max(ovalue, Math.abs(fin)) * -1;
        }
        mvalue = 0;
        ovalue = 0;
        fiveM = 0;
        fiveO = 0;
        scoreM = 1;
        scoreO = 1;

        // vertical check
        for (int j = 0; j < board.length; j++) {
            fiveM = 0;
            fiveO = 0;
            mvalue = Math.max(mvalue, scoreM);
            ovalue = Math.max(ovalue, scoreO);
            scoreM = 1;
            scoreO = 1;
            for (int i = 0; i < board[j].length; i++) {
                if (fiveO == 5) {
                    return MAX;
                }
                if (fiveM == 5) {
                    return MAX;
                }
                if (board[i][j] == currentPiece) {
                    fiveM++;
                    fiveO = 0;
                    scoreM = scoreM * scoreM;
                    if (fiveO >= 3){
                        scoreO = Math.sqrt(scoreO);
                    }else if(fiveO != 0){
                        scoreO = scoreO/10.0;
                    }else{}
                } else if (board[i][j] == opponentPiece) {
                    fiveO++;
                    fiveM = 0;
                    scoreO = scoreO * scoreO;
                    if (fiveM >= 3){
                        scoreM = Math.sqrt(scoreO);
                    }else if(fiveM != 0){
                        scoreM = scoreM/10.0;
                    }else{}
                } else {
                    if (fiveM != 0){
                        fiveM++;
                        fiveO = 0;
                    }else if(fiveO != 0){
                        fiveO++;
                        fiveM = 0;
                    }else{
                        fiveM = 0;
                        fiveO = 0;
                    }
                }
            }
        }

        mvalue = Math.max(mvalue, scoreM);
        ovalue = Math.max(scoreO, ovalue);
        if (mvalue > ovalue){
            fin = Math.max(mvalue, Math.abs(fin));
        }else {
            fin = Math.max(ovalue, Math.abs(fin)) * -1;
        }
        mvalue = 0;
        ovalue = 0;
        fiveM = 0;
        fiveO = 0;
        scoreM = 1;
        scoreO = 1;

        // L To R diagonal check
        for (int i = 0; i < board.length; i++) {
            fiveM = 0;
            fiveO = 0;
            mvalue = Math.max(mvalue, scoreM);
            ovalue = Math.max(ovalue, scoreO);
            scoreM = 1;
            scoreO = 1;
            if (fiveO == 5) {
                return MAX;
            }
            if (fiveM == 5) {
                return MAX;
            }
            int x = i;
            int y = 0;
            while (y != i) {
                if (board[x][y] == currentPiece) {
                    fiveM++;
                    fiveO = 0;
                    scoreM = scoreM * scoreM;
                    if (fiveO >= 3){
                        scoreO = Math.sqrt(scoreO);
                    }else if(fiveO != 0){
                        scoreO = scoreO/10.0;
                    }else{}
                } else if (board[x][y] == opponentPiece) {
                    fiveO++;
                    fiveM = 0;
                    scoreO = scoreO * scoreO;
                    if (fiveM >= 3){
                        scoreM = Math.sqrt(scoreO);
                    }else if(fiveM != 0){
                        scoreM = scoreM/10.0;
                    }else{}
                } else {
                    if (fiveM != 0){
                        fiveM++;
                        fiveO = 0;
                    }else if(fiveO != 0){
                        fiveO++;
                        fiveM = 0;
                    }else{
                        fiveM = 0;
                        fiveO = 0;
                    }
                }
                y++;
                x--;
            }
        }

        mvalue = Math.max(mvalue, scoreM);
        ovalue = Math.max(scoreO, ovalue);
        if (mvalue > ovalue){
            fin = Math.max(mvalue, Math.abs(fin));
        }else {
            fin = Math.max(ovalue, Math.abs(fin)) * -1;
        }
        mvalue = 0;
        ovalue = 0;
        fiveM = 0;
        fiveO = 0;
        scoreM = 1;
        scoreO = 1;

        // R to L diagonal check
        for (int i = board.length - 1; i >= 0; i--) {
            fiveM = 0;
            fiveO = 0;
            mvalue = Math.max(mvalue, scoreM);
            ovalue = Math.max(ovalue, scoreO);
            scoreM = 1;
            scoreO = 1;
            if (fiveO == 5) {
                return MAX;
            }
            if (fiveM == 5) {
                return MAX;
            }
            int x = i;
            int y = 0;
            while (y != i) {
                if (board[x][y] == currentPiece) {
                    fiveM++;
                    fiveO = 0;
                    scoreM = scoreM * scoreM;
                    if (fiveO >= 3){
                        scoreO = Math.sqrt(scoreO);
                    }else if(fiveO != 0){
                        scoreO = scoreO/10.0;
                    }else{}
                } else if (board[x][y] == opponentPiece) {
                    fiveO++;
                    fiveM = 0;
                    scoreO = scoreO * scoreO;
                    if (fiveM >= 3){
                        scoreM = Math.sqrt(scoreO);
                    }else if(fiveM != 0){
                        scoreM = scoreM/10.0;
                    }else{}
                } else {
                    if (fiveM != 0){
                        fiveM++;
                        fiveO = 0;
                    }else if(fiveO != 0){
                        fiveO++;
                        fiveM = 0;
                    }else{
                        fiveM = 0;
                        fiveO = 0;
                    }
                }
                y++;
                x--;
            }
        }

        mvalue = Math.max(mvalue, scoreM);
        ovalue = Math.max(scoreO, ovalue);
        if (mvalue > ovalue){
            fin = Math.max(mvalue, Math.abs(fin));
        }else {
            fin = Math.max(ovalue, Math.abs(fin)) * -1;
        }

        return fin;
    }

    // minimax with alpha-beta pruning reference from https://www.youtube.com/watch?v=l-hh51ncgDI&ab_channel=SebastianLague
    public static double minimaxpruned(Node n, double alpha, double beta) {
        if (n.getChildren() == null || n.getChildren().size() == 0) {
            return n.getHeuristic();
        }

        if (n.isMax()) {
            double maxEval = -(Math.pow(50, 8) - 1.0) ;
            for (Node child :
                    n.getChildren()) {
                double eval = minimaxpruned(child, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                n.setHeuristic(maxEval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            double minEval = Double.MAX_VALUE;
            for (Node child :
                    n.getChildren()) {
                double eval = minimaxpruned(child, alpha, beta);
                minEval = Math.min(minEval, eval);
                n.setHeuristic(minEval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }

            return minEval;
        }
    }
}

class Node {
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