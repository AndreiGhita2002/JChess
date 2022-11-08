package jchess.game;

import jchess.util.Vec2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Condition {
    // returns true if other piece is in place (relative to the current piece)
    // checked in Game
    PieceType otherPiece;
    Vec2 place;
    Condition next;
    ArrayList<String> tags; // TODO you sure do love tags (see if necessary)

    public String toPrintString() {
        String s = "Condition: {\n";
        s += "  otherPiece: " + otherPiece.getTypeName() + "\n";
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
        JSONObject j = new JSONObject();
        j.append("otherPiece", otherPiece.getTypeName());
        j.append("place", place.toString());
        if (next != null) {
            j.append("nextCondition", next.toString());
        } else {
            j.append("nextCondition", "null");
        }
        j.append("tags", tags);
        return j.toString();
    }

    public Condition(PieceType otherPiece, Vec2 place) {
        this.otherPiece = otherPiece;
        this.place = place;
        next = null;
    }

    Condition(PieceType otherPiece, Vec2 place, Condition nextCondition) {
        this.otherPiece = otherPiece;
        this.place = place;
        next = nextCondition;
    }

    Condition(String json) {
        JSONObject j = new JSONObject(json);
        this.otherPiece = PieceType.getType(j.getString("otherPiece"));
        this.place = new Vec2(j.getString("place"));
        // tags
        JSONArray tagsJSON = j.getJSONArray("tags");
        tags = new ArrayList<>();
        for (Object o : tagsJSON) {
            tags.add(o.toString());
        }
        // next
        String s = j.getString("nextCondition");
        if (s.equals("null")) {
            next = null;
        } else {
            next = new Condition(s);
        }
    }
}
