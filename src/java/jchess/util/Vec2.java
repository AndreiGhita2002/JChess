package jchess.util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Vec2 {
    public int x;
    public int y;

    public static Vec2 add(Vec2 v1, Vec2 v2) {
        return new Vec2(v1.x + v2.x, v1.y +v2.y);
    }

    public Vec2 add(Vec2 v2) {
        return new Vec2(this.x + v2.x, this.y + v2.y);
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() == this.getClass())
            return this.x == ((Vec2) other).x && this.y == ((Vec2) other).y;
        return false;
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    public Vec2 getInverse() {
        return new Vec2(x, -y);
    }

    public static List<Vec2> invertList(List<Vec2> vectorList) {
        ArrayList<Vec2> out = new ArrayList<>();
        for (Vec2 v : vectorList) {
            out.add(v.getInverse());
        }
        return out;
    }

    public Vec2(JSONObject json) {
        this.x = (int) json.get("x");
        this.y = (int) json.get("y");
    }

    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("x", x);
        j.put("y", y);
        return j;
    }
}
