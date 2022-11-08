package jchess.util;

import jchess.game.*;

public class JSONGenerator {
    public static void main(String[] args) {
        PieceType.doGraphics = false;
        Game game = new Game(new Scenario("classic"));
        Vec2 oneF = new Vec2(0, 1);
        Condition cond = new Condition(PieceType.getPieceType("any"), new Vec2(0, 0));
        DirectMove m = new DirectMove(oneF, cond);

        System.out.println(cond.toPrintString());
        System.out.println();
        System.out.println(m.toPrintString());
        System.out.println();
        System.out.println(cond);
        System.out.println();
        System.out.println(m);
    }
}
