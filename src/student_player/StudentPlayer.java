package student_player;

import boardgame.Move;

import pentago_twist.PentagoMove;
import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;

import java.util.List;

/**
 * A player file submitted by a student.
 */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260793546");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
        MyTools.getSomething();

        // generate the minimax tree for current boardState
        Node root = MyTools.minimaxGenerate(boardState);

        // find the best path
        MyTools.minimaxpruned(root, Integer.MIN_VALUE, Integer.MAX_VALUE);

        // early strategy
        if (boardState.getPieceAt(1, 1) == PentagoBoardState.Piece.EMPTY && boardState.getTurnNumber() <= 2) {
            return new PentagoMove(1, 1, 0, 0, boardState.getTurnPlayer());
        } else if (boardState.getPieceAt(1, 4) == PentagoBoardState.Piece.EMPTY && boardState.getTurnNumber() <= 2) {
            return new PentagoMove(1, 4, 0, 0, boardState.getTurnPlayer());
        } else if (boardState.getPieceAt(4, 1) == PentagoBoardState.Piece.EMPTY && boardState.getTurnNumber() <= 2) {
            return new PentagoMove(4, 1, 0, 0, boardState.getTurnPlayer());
        } else if (boardState.getPieceAt(4, 4) == PentagoBoardState.Piece.EMPTY && boardState.getTurnNumber() <= 2) {
            return new PentagoMove(4, 4, 0, 0, boardState.getTurnPlayer());
        }

        // heuristic strategy
        for (Node child :
                root.getChildren()) {
            if (child.getHeuristic() == root.getHeuristic()) {
                return child.getMove();
            }
        }

        // Back-up solution
        Move myMove = boardState.getRandomMove();

        // Return your move to be processed by the server.
        return myMove;
    }
}