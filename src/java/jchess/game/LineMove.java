package jchess.game;

import jchess.util.Vec2;
import org.json.JSONObject;

public class LineMove extends Move {
    Vec2 direction;
    
    @Override
    public String toString() {
        JSONObject j = new JSONObject();
        j.append("direction", direction.toString());
        j.append("condition", condition.toString());
        return j.toString();
    }
    
    LineMove(Vec2 direction, Condition condition) {
        super(condition);
        this.direction = direction;
    }
}
