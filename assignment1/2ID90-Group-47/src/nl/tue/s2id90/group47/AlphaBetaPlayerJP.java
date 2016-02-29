package nl.tue.s2id90.group47;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
public class AlphaBetaPlayerJP extends DraughtsPlayer {

    private boolean stopped;
    private int[] allPieces;
    private int totalScore;
    
    public AlphaBetaPlayerJP() {
        super(AlphaBetaPlayer.class.getResource("resources/smiley.png"));
        stopped = false;
        allPieces = null;
        totalScore = 0;
    }
    
    @Override
    /** @return an illegal move **/
    public Move getMove(DraughtsState s) {
        //System.err.println("AlphaBetaPlayer");
       Node topNode = new Node(s);
       Move bestMove=null;
       int value;
        try {
            for (int maxDepth = 5; maxDepth < 100; maxDepth++) {
                if (s.isWhiteToMove()) {
                    value = alphaBetaMax(topNode, -100, 100, 0, maxDepth);
                } else {
                    value = alphaBetaMin(topNode, -100, 100, 0, maxDepth);
                }
                bestMove = topNode.getBestMove();
                //System.out.println("New best move, old: " + bestMove + ". new: " + value + ". depth: " + maxDepth);
            }
        } catch (AIStoppedException ex) {
            //System.out.println("TIME IS UP");
        }
       return bestMove;
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
            //System.out.println("MAX: MAX DEPTH");
            return evalFunction(node);
        }
        
        // get all possible moves of this node
        List<Move> moves = draughtsState.getMoves();
        
        //performs all the possible moves this node can do
        for (Move move : moves) {
            draughtsState.doMove(move);
            int newNodeValue = alphaBetaMin(new Node(draughtsState), alpha, beta, currentDepth + 1, maxDepth); 
            draughtsState.undoMove(move);
            
            if (newNodeValue > alpha) {
//                System.out.println("MAX: New best move, old: " + alpha + ". new: " + newNodeValue + ". depth: " + currentDepth);
                alpha = newNodeValue;
                node.setBestMove(move);
            }
 
            if (alpha >= beta) {
                // the minimizer already has a better move, dont calc rest of the children
                return beta;
            }
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
            //System.out.println("MIN: MAX DEPTH");
            return evalFunction(node);
        }
        
        // get all possible moves of this node
        List<Move> moves = draughtsState.getMoves();
        
        //performs all the possible moves this node can do
        for (Move move : moves) {
            draughtsState.doMove(move);
            int newNodeValue = alphaBetaMax(new Node(draughtsState), alpha, beta, currentDepth + 1, maxDepth);
            draughtsState.undoMove(move);
            
            if (newNodeValue < beta) {
//                System.out.println("MIN: New best move, old: " + beta + ". new: " + newNodeValue + ". depth: " + currentDepth);
                beta = newNodeValue;
                node.setBestMove(move);
            }
 
            if (beta <= alpha) {
                // the maximiser already has a better move, dont calc rest of the children
                return alpha;
            }
        }
        return beta;
    }

    private int evalFunction(Node node) {
        /**
 * <blockquote><pre>
 *   col  0  1  2  3  4  5  6  7  8  9
 *  row ------------------------------
 *   0  |    21    21    21    21    21
 *      |
 *   1  | 18    19    19    19    18
 *      |
 *   2  |    14    15    15    15    14
 *      |
 *   3  | 13    14    14    14    13
 *      |
 *   4  |    12    13    13    13    12
 *      |
 *   5  | 11    12    12    12    11
 *      |
 *   6  |    10    10    10    10    10
 *      |
 *   7  | 10    10    10    10    10
 *      |
 *   8  |    10    10    10    10    10
 *      |
 *   9  | 10    10    10    10    10
 * </pre></blockquote>
 * @author huub
 */
        // evaluation function for the while player.
        // WHITEKING is always +22
        // BLACKKING is always -22
        // The rest depends on the score above
        int score = 0;
        allPieces = node.getGameState().getPieces();
        
        // row 0      
        score += scorePerPosition(1, 21, 22, 10, 22);
        score += scorePerPosition(2, 21, 22, 10, 22);
        score += scorePerPosition(3, 21, 22, 10, 22);
        score += scorePerPosition(4, 21, 22, 10, 22);
        score += scorePerPosition(5, 21, 22, 10, 22);
        
        // row 1
        score += scorePerPosition(6, 18, 22, 10, 22);
        score += scorePerPosition(7, 19, 22, 10, 22);
        score += scorePerPosition(8, 19, 22, 10, 22);
        score += scorePerPosition(9, 19, 22, 10, 22);
        score += scorePerPosition(10, 18, 22, 10, 22);
        
        // row 2
        score += scorePerPosition(11, 14, 22, 10, 22);
        score += scorePerPosition(12, 15, 22, 10, 22);
        score += scorePerPosition(13, 15, 22, 10, 22);
        score += scorePerPosition(14, 15, 22, 10, 22);
        score += scorePerPosition(15, 14, 22, 10, 22);
        
        // row 3
        score += scorePerPosition(16, 13, 22, 10, 22);
        score += scorePerPosition(17, 14, 22, 10, 22);
        score += scorePerPosition(18, 14, 22, 10, 22);
        score += scorePerPosition(19, 14, 22, 10, 22);
        score += scorePerPosition(20, 13, 22, 10, 22);
        
        // row 4
        score += scorePerPosition(21, 12, 22, 11, 22);
        score += scorePerPosition(22, 13, 22, 12, 22);
        score += scorePerPosition(23, 13, 22, 12, 22);
        score += scorePerPosition(24, 13, 22, 12, 22);
        score += scorePerPosition(25, 12, 22, 11, 22);
        
        // row 5
        score += scorePerPosition(26, 11, 22, 12, 22);
        score += scorePerPosition(27, 12, 22, 13, 22);
        score += scorePerPosition(28, 12, 22, 13, 22);
        score += scorePerPosition(29, 12, 22, 13, 22);
        score += scorePerPosition(30, 11, 22, 12, 22);
        
        // row 6
        score += scorePerPosition(31, 10, 22, 13, 22);
        score += scorePerPosition(32, 10, 22, 14, 22);
        score += scorePerPosition(33, 10, 22, 14, 22);
        score += scorePerPosition(34, 10, 22, 14, 22);
        score += scorePerPosition(35, 10, 22, 13, 22);
        
        // row 7
        score += scorePerPosition(36, 10, 22, 14, 22);
        score += scorePerPosition(37, 10, 22, 15, 22);
        score += scorePerPosition(38, 10, 22, 15, 22);
        score += scorePerPosition(39, 10, 22, 15, 22);
        score += scorePerPosition(40, 10, 22, 14, 22);
        
        // row 8
        score += scorePerPosition(41, 10, 22, 18, 22);
        score += scorePerPosition(42, 10, 22, 19, 22);
        score += scorePerPosition(43, 10, 22, 19, 22);
        score += scorePerPosition(44, 10, 22, 19, 22);
        score += scorePerPosition(45, 10, 22, 18, 22);
        
        // row 9
        score += scorePerPosition(46, 10, 22, 21, 22);
        score += scorePerPosition(47, 10, 22, 21, 22);
        score += scorePerPosition(48, 10, 22, 21, 22);
        score += scorePerPosition(49, 10, 22, 21, 22);
        score += scorePerPosition(50, 10, 22, 21, 22);
        
        return score;
    }
    
    private int scorePerPosition(int position, int score1, int score2, int score3, int score4) {
        totalScore = 0;
        if (allPieces[position] == DraughtsState.WHITEPIECE) {totalScore += score1;}
        else if (allPieces[position] == DraughtsState.WHITEKING) {totalScore += score2;}
        else if (allPieces[position] == DraughtsState.BLACKPIECE) {totalScore -= score3;}
        else if (allPieces[position] == DraughtsState.BLACKKING) {totalScore -= score4;}
        return totalScore;
    }
    
    public void stop() {
        stopped = true;
    }

    private static class AIStoppedException extends Exception {

        public AIStoppedException() {
        }
    }
}
