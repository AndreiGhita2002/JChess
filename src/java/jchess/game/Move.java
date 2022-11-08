package jchess.game;

public abstract class Move {
    Condition condition;
    
    Move(Condition condition) {
        this.condition = condition;
    }
}
