package jchess.game;

import jchess.util.Vec2;
import org.json.JSONObject;

public class Condition {
    PieceType otherPiece;
    Vec2 place;
    Condition next;

    public String toString() {
        JSONObject j = new JSONObject();
        j.append("otherPiece", otherPiece.toString());
        j.append("place", place.toString());
        j.append("nextCondition", next.toString());
        return j.toString();
    }

    Condition(PieceType otherPiece, Vec2 place) {
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
        String s = j.getString("next");
        if (s.equals("null")) { //TODO test this
            next = null;
        } else {
            next = new Condition(s);
        }
    }
}
