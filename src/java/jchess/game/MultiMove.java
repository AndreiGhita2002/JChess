package jchess.game;

import org.json.JSONObject;

import java.util.ArrayList;

public class MultiMove extends Move {
    /*
    The first condition needs to define what piece is allowed to start this
    TODO figure this one out, after the other moves are implemented
     */
    ArrayList<PieceType> allowedPieceTypes = new ArrayList<>();
    boolean valid = true;
    
    @Override
    public String toString() {
        JSONObject j = new JSONObject();
        j.append("condition", condition.toString());
        return j.toString();
    }
    
    MultiMove(Condition condition) {
        super(condition);
        
    }
}
