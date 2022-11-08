package jchess.game;

import jchess.util.Vec2;
import org.json.JSONObject;

public class DirectMove extends Move {
    Vec2 displacement;
//    boolean canTake; //TODO see if this is necessary

    @Override
    public String toString() {
        JSONObject j = new JSONObject();
        j.append("displacement", displacement.toString());
        j.append("condition", condition.toString());
        return j.toString();
    }

    DirectMove(String json) {
        super(null);
        JSONObject j = new JSONObject(json);
        this.displacement =  new Vec2(j.getString("displacement"));
        this.condition = new Condition(j.getString("condition"));
    }

    DirectMove(Vec2 displacement, Condition condition) {
        super(condition);
        this.displacement = displacement;
    }
}
