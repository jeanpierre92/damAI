package nl.tue.s2id90.group47;


import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import org10x10.dam.game.Move;

/**
 * A simple draughts player that plays the first moves that comes to mind
 * and values all moves with value 0.
 * @author huub
 */
public class AlphaBetaPlayer extends DraughtsPlayer {

    public AlphaBetaPlayer() {
        super(UninformedPlayer.class.getResource("resources/optimist.png"));
    }
    
    @Override
    /** @return a random move **/
    public Move getMove(DraughtsState s) {
        List<Move> moves = s.getMoves();
        int pieceDifference; //#white pieces - #black pieces
        Move bestMove = moves.get(0);
        int bestMovePieceDifference = -10;  
        for (Move m: moves) {
            s.doMove(m);
            pieceDifference = 0;
     
            int[] allPieces = s.getPieces();
            for (int p : allPieces) {
                if (p == 1 || p == 3) {
                    pieceDifference++;
                } else if (p == 2 || p == 4) {
                    pieceDifference--;
                }
                if (bestMovePieceDifference > bestMovePieceDifference) {
                    bestMovePieceDifference = pieceDifference;
                    bestMove = m;
                }
            }
            s.undoMove(m);
        }
        return bestMove;
    }
    
    @Override
    public Integer getValue() {
        return 0;
    }
}
