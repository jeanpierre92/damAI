package nl.tue.s2id90.group47;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;

/**
 * A simple draughts player that plays random moves
 * and values all moves with value 0.S
 * @author huub
 */
public class AlphaBetaPlayer extends DraughtsPlayer {

    private boolean stopped;
    
    public AlphaBetaPlayer() {
        super(AlphaBetaPlayer.class.getResource("resources/smiley.png"));
        stopped = false;
    }
    
    @Override
    public Move getMove(DraughtsState s) {
       Node topNode = new Node(s);
        try {
            for (int maxDepth = 5; maxDepth < 100; maxDepth++) {
                alphaBetaMax(topNode, -100, 100, 0, maxDepth);
            }
        } catch (AIStoppedException ex) {
            System.out.println("TIME IS UP");
            return topNode.getBestMove();
        }
       return topNode.getBestMove();
    }

    @Override
    public Integer getValue() {
        return 0;
    }
    
    int alphaBetaMax(Node node, int alpha, int beta, int currentDepth, int maxDepth) throws AIStoppedException {
        if (stopped) {
            stopped = false;
            throw new AIStoppedException();
        }
        
        DraughtsState draughtsState = node.getGameState();
        if (currentDepth >= maxDepth || draughtsState.isEndState()) {
            System.out.println("MAX: MAX DEPTH");
            return evalFunction(node, draughtsState.isWhiteToMove());
        }
        
        // get all possible moves of this node
        List<Move> moves = draughtsState.getMoves();
        
        //performs all the possible moves this node can do
        for (Move move : moves) {
            draughtsState.doMove(move);
                        
            int newNodeValue = alphaBetaMin(new Node(draughtsState), alpha, beta, currentDepth + 1, maxDepth);            
            if (newNodeValue > alpha) {
                System.out.println("MAX: New best move, old: " + alpha + ". new: " + newNodeValue + ". depth: " + currentDepth);
                alpha = newNodeValue;
                node.setBestMove(move);
            }
 
            if (alpha >= beta) {
                // the minimizer already has a better move, dont calc rest of the children
                return beta;
            }
            
            draughtsState.undoMove(move);
        }
        return alpha;
    }
    
    int alphaBetaMin(Node node, int alpha, int beta, int currentDepth, int maxDepth) throws AIStoppedException {
        if (stopped) {
            stopped = false;
            throw new AIStoppedException();
        }
        
        DraughtsState draughtsState = node.getGameState();
        if (currentDepth >= maxDepth || draughtsState.isEndState()) {
            System.out.println("MIN: MAX DEPTH");
            return evalFunction(node, draughtsState.isWhiteToMove());
        }
        
        // get all possible moves of this node
        List<Move> moves = draughtsState.getMoves();
        
        //performs all the possible moves this node can do
        for (Move move : moves) {
            draughtsState.doMove(move);
                        
            int newNodeValue = alphaBetaMax(new Node(draughtsState), alpha, beta, currentDepth + 1, maxDepth);            
            if (newNodeValue < beta) {
                System.out.println("MIN: New best move, old: " + beta + ". new: " + newNodeValue + ". depth: " + currentDepth);
                beta = newNodeValue;
                node.setBestMove(move);
            }
 
            if (beta <= alpha) {
                // the maximiser already has a better move, dont calc rest of the children
                return alpha;
            }
            
            draughtsState.undoMove(move);
        }
        return beta;
    }

    private int evalFunction(Node node, boolean player) {
        // write evaluation function here
        // this returns an integer value about how many/more or less white
        // pieces has than black. eg: if returns 4 then white has 4 more pieces
        // than black
        int pieceDifference = 0;
        int[] AllPieces = node.getGameState().getPieces();
        for (int p : AllPieces) {
            if ((p == 1 || p == 3) && player) {
                pieceDifference += 1;
            } else if ((p == 1 || p == 3) && !player) {
                pieceDifference -= 1;
            } else if ((p == 2 || p == 4) && player) {
                pieceDifference += 1;
            } else if ((p == 2 || p == 4) && !player) {
                pieceDifference -= 1;
            }
        }
        return pieceDifference;
    }
    
    public void stop() {
        stopped = true;
    }

    private static class AIStoppedException extends Exception {

        public AIStoppedException() {
        }
    }
}
