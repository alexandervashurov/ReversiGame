package reversi.alexvashurov.reversi;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by AlexVashurov on 24.03.16.
 */

public class BoardTest {

    @Test
    public void testMove() {
        Board board = Board.initialize();
        Assert.assertEquals(4, board.getAllPossibleMoves().size());
    }

    @Test
    public void testMove1() {
        Board board = Board.initialize();
        List<Move> moves = board.getAllPossibleMoves();
        board.makeMove(moves.get(0));
        Assert.assertEquals(3, board.getAllPossibleMoves().size());
    }

    @Test
    public void testMove2() {
        Board board = Board.initialize();
        List<Move> moves = board.getAllPossibleMoves();
        board.makeMove(moves.get(1));
        Assert.assertNotEquals(6, board.getAllPossibleMoves().size());
    }

    @Test
    public void testMove3() {
        Board board = Board.initialize();
        List<Move> moves = board.getAllPossibleMoves();
        board.makeMove(moves.get(0));
        board.makeMove(moves.get(1));
        board.makeMove(moves.get(2));
        List<Board.Stone> stones = board.getBoardStones();
        Assert.assertEquals(7, stones.size());
    }

    @Test
    public void testMove4() {
        Board board = Board.initialize();
        List<Move> moves = board.getAllPossibleMoves();
        List<Board.Stone> stones = board.getBoardStones();
        board.makeMove(moves.get(0));
        Assert.assertEquals(4, stones.size());
    }

    @Test
    public void testAlgorithm() {
        Board board = Board.initialize();
        Algorithm.setDEPTH(3);
        for (int i = 0; i < 10; i++) {
            Move move = Algorithm.getBetterMove(board);
            System.out.println(move);
            board.makeMove(move);
        }
        Assert.assertEquals(12, board.getAllPossibleMoves().size());
        Assert.assertEquals(new Move(board.getPosition(2, 2)), Algorithm.getBetterMove(board));
    }



}
