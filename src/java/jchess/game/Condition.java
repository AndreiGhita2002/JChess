package jchess.game;

import jchess.util.Vec2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Condition {
    // returns true if other piece is in place (relative to the current piece)
    // checked in Game
    String otherPiece;
    Vec2 place;
    Condition next;
    public ArrayList<String> tags; // TODO you sure do love tags (see if necessary)

    public String toPrintString() {
        String s = "Condition: {\n";
        s += "  otherPiece: " + otherPiece + "\n";
        s += "  place: " + place.toString() + "\n";
        if (next == null) {
            s += "  next:  null\n";
        } else {
            s += "  next:  " + next.toPrintString().indent(2) + "\n";
        }
        s += "}";
        return s;
    }

    public String toString() {
        return toJSON().toString();
    }
    
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("otherPiece", otherPiece);
        j.put("place", place.toJSON());
        if (next != null) {
            j.put("nextCondition", next.toJSON());
        } else {
            j.put("nextCondition", "null");
        }
        j.put("tags", tags);
        return j;
    }
    
    public Condition(String otherPiece, Vec2 place) {
        this.otherPiece = otherPiece;
        this.place = place;
        tags = new ArrayList<>();
        next = null;
    }

    Condition(String otherPiece, Vec2 place, Condition nextCondition) {
        this.otherPiece = otherPiece;
        this.place = place;
        next = nextCondition;
        tags = new ArrayList<>();
    }

    Condition(JSONObject j) {
        this.otherPiece = j.getString("otherPiece");
        this.place = new Vec2(j.getJSONObject("place"));
        // tags
        JSONArray tagsJSON = j.getJSONArray("tags");
        tags = new ArrayList<>();
        for (Object o : tagsJSON) {
            tags.add(o.toString());
        }
        // next
        Object s = j.get("nextCondition");
        if (s.toString().equals("null")) {
            next = null;
        } else {
            next = new Condition((JSONObject) s);
        }
    }
}
