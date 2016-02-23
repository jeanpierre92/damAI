package nl.tue.s2id90.group47;


import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;

/**
 * A simple draughts player that plays random moves
 * and values all moves with value 0.S
 * @author huub
 */
public class AlphaBetaPlayer extends DraughtsPlayer {

    private final int maxTreeLevel;
    
    public AlphaBetaPlayer() {
        super(AlphaBetaPlayer.class.getResource("resources/smiley.png"));
        maxTreeLevel = 10;
    }
    
    @Override
    /** @return an illegal move **/
    public Move getMove(DraughtsState s) {
       Node topNode = new Node(s);
       alphaBetaMax(topNode, -100, 100, 0);
       System.out.println("ik kom hier");
       return topNode.getBestMove();
    }

    @Override
    public Integer getValue() {
        return 0;
    }

    
    int alphaBetaMax(Node node, int alpha, int beta, int depth) {
        DraughtsState draughtsState = node.getGameState();
        if (depth >= maxTreeLevel || draughtsState.isEndState()) {
            System.out.println("MAX: MAX DEPTH");
            return evalFunction(node, draughtsState.isWhiteToMove());
        }
        
        // get all possible moves of this node
        List<Move> moves = draughtsState.getMoves();
        
        //performs all the possible moves this node can do
        for (Move move : moves) {
            draughtsState.doMove(move);
                        
            int newNodeValue = alphaBetaMin(new Node(draughtsState), alpha, beta, depth + 1);            
            if (newNodeValue > alpha) {
                System.out.println("MAX: New best move, old: " + alpha + ". new: " + newNodeValue + ". depth: " + depth);
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
    
    int alphaBetaMin(Node node, int alpha, int beta, int depth) {
        DraughtsState draughtsState = node.getGameState();
        if (depth >= maxTreeLevel || draughtsState.isEndState()) {
            System.out.println("MIN: MAX DEPTH");
            return evalFunction(node, draughtsState.isWhiteToMove());
        }
        
        // get all possible moves of this node
        List<Move> moves = draughtsState.getMoves();
        
        //performs all the possible moves this node can do
        for (Move move : moves) {
            draughtsState.doMove(move);
                        
            int newNodeValue = alphaBetaMax(new Node(draughtsState), alpha, beta, depth + 1);            
            if (newNodeValue < beta) {
                System.out.println("MIN: New best move, old: " + beta + ". new: " + newNodeValue + ". depth: " + depth);
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
                pieceDifference++;
            } else if ((p == 1 || p == 3) && !player) {
                pieceDifference--;
            } else if ((p == 2 || p == 4) && player) {
                pieceDifference++;
            } else if ((p == 2 || p == 4) && !player) {
                pieceDifference--;
            }
        }
        return pieceDifference;
    }
}
