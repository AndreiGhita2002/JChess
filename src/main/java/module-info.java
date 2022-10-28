module chess.jchess {
    requires javafx.controls;
    requires org.controlsfx.controls;
    requires org.reflections;
    requires org.json;

    opens pieces;
    opens graphics;
    opens scenarios;
    exports jchess.ux;
    exports jchess.util;
    exports jchess.game;
}