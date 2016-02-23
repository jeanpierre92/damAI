/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group47;

import nl.tue.s2id90.draughts.DraughtsState;
import org10x10.dam.game.Move;

/**
 *
 * @author Jeanpierre
 */
public class Node {
    
    DraughtsState state;
    Move bestMove;
    
    Node(DraughtsState state) {
        this.state = state;
    }
    
    public DraughtsState getGameState() {
        return state;
    }
        
    public void setBestMove(Move move) {
        bestMove = move;
    }
    
    Move getBestMove() {
        return bestMove;
    }
}
