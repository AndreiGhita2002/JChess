package jchess.game;

import jchess.util.Vec2;
import org.json.JSONObject;

public class DirectMove extends Move {
    Vec2 displacement;

    public String toPrintString() {
        String s = "DirectMove: {\n";
        s += "  displacement: " + displacement.toString() + "\n";
        s += "  " + condition.toPrintString().indent(2);
        s += "}";
        return s;
    }

    public String toString() {
        return toJSON().toString();
    }
    
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("displacement", displacement.toJSON());
        j.put("condition", condition.toJSON());
        return j;
    }

    DirectMove(String json) {
        super(json);
        JSONObject j = new JSONObject(json);
        this.displacement = new Vec2(j.getJSONObject("displacement"));
        this.condition = new Condition(j.getJSONObject("condition"));
    }

    public DirectMove(Vec2 displacement, Condition condition) {
        super(condition);
        this.displacement = displacement;
    }
}
