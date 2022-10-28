package jchess.game;


import jchess.util.Vec2;
import org.json.JSONObject;

public class Move {
    Vec2 displacement;
    Condition condition;
    boolean canTake;

    public String toString() {
        JSONObject j = new JSONObject();
        j.append("displacement", displacement.toString());
        j.append("condition", condition.toString());
        j.append("canTake", canTake);
        return j.toString();
    }

    Move(String json) {
        JSONObject j = new JSONObject(json);
        this.displacement =  new Vec2(j.getString("displacement"));
        this.condition = new Condition(j.getString("condition"));
        this.canTake = j.getBoolean("canTake");
    }

    Move(Vec2 displacement, boolean canTake, Condition condition) {
        this.displacement = displacement;
        this.canTake = canTake;
        this.condition = condition;
    }
}
