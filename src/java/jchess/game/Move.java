package jchess.game;

import org.json.JSONObject;
public abstract class Move {
    public Condition condition;

    Move(Condition condition) {
        this.condition = condition;
    }

    Move(String json) {
        // TODO is this the proper way of doing this??
        //   look up proper java way of implementing this
    }
    
    public abstract JSONObject toJSON();
}
