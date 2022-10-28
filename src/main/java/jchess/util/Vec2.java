package jchess.util;

import org.json.JSONObject;

public class Vec2 {
    public int x;
    public int y;
    
    public Vec2 add(Vec2 v1, Vec2 v2) {
        return new Vec2(v1.x + v2.x, v1.y +v2.y);
    }

    @Override
    public String toString() {
        JSONObject j = new JSONObject();
        j.append("x", x);
        j.append("y", y);
        return j.toString();
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
    
    public Vec2(String json) {
        JSONObject j = new JSONObject(json);
        this.x = (int) j.get("x");
        this.y = (int) j.get("y");
    }
    
    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
