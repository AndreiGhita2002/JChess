package jchess.game;

import org.json.JSONObject;
public class MultiMove extends Move{

    // TODO implement MultiMove
    
    @Override
    public JSONObject toJSON() {
        return null;
    }
    
    MultiMove(Condition condition) {
        super(condition);
    }

    MultiMove(String json) {
        super(json);
    }
}
