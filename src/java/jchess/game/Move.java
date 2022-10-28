package jchess.game;

import jchess.util.Vec2;
import org.json.JSONObject;

public class Move {
    Vec2 displacement;
    Condition condition;

    public String toPrintString() {
        String s = "Move: {\n";
        s += "  displacement: " + displacement.toString() + "\n";
        s += "  " + condition.toPrintString().indent(2);
        s += "}";
        return s;
    }

    public String toString() {
        JSONObject j = new JSONObject();
        j.append("displacement", displacement.toString());
        j.append("condition", condition.toString());
        return j.toString();
    }

    Move(String json) {
        JSONObject j = new JSONObject(json);
        this.displacement =  new Vec2(j.getString("displacement"));
        this.condition = new Condition(j.getString("condition"));
    }

    public Move(Vec2 displacement, Condition condition) {
        this.displacement = displacement;
        this.condition = condition;
    }
}
