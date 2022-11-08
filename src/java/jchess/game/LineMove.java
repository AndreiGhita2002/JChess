package jchess.game;

import jchess.util.Vec2;
import org.json.JSONObject;

public class LineMove extends Move {
    Vec2 direction;

    public String toPrintString() {
        String s = "LineMove: {\n";
        s += "  direction: " + direction.toString() + "\n";
        s += "  " + condition.toPrintString().indent(2);
        s += "}";
        return s;
    }

    public String toString() {
        JSONObject j = new JSONObject();
        j.append("direction", direction.toString());
        j.append("condition", condition.toString());
        return j.toString();
    }

    LineMove(String json) {
        super(json);
        JSONObject j = new JSONObject(json);
        this.direction =  new Vec2(j.getString("direction"));
        this.condition = new Condition(j.getString("condition"));
    }

    public LineMove(Vec2 direction, Condition condition) {
        super(condition);
        this.direction = direction;
    }
}
