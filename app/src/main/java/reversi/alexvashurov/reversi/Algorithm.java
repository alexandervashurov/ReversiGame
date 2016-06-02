package reversi.alexvashurov.reversi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by AlexVashurov on 30.03.16.
 */
class Algorithm {
    private final static String TAG = "REVERSI_DEBUG";
    private static final int[][] arrayPositionValues = new int[8][8];
    private static int DEPTH = 7;

    static {
        arrayPositionValues[0][0] = 99;
        arrayPositionValues[0][1] = -8;
        arrayPositionValues[0][2] = 8;
        arrayPositionValues[0][3] = 6;

        arrayPositionValues[1][0] = -8;
        arrayPositionValues[1][1] = -24;
        arrayPositionValues[1][2] = -4;
        arrayPositionValues[1][3] = -3;

        arrayPositionValues[2][0] = 8;
        arrayPositionValues[2][1] = -4;
        arrayPositionValues[2][2] = 7;
        arrayPositionValues[2][3] = 4;

        arrayPositionValues[3][0] = 6;
        arrayPositionValues[3][1] = -3;
        arrayPositionValues[3][2] = 4;
        arrayPositionValues[3][3] = 1;

        for (int j = 0; j <= 3; j++) {

            for (int i = 0; i <= 3; i++) {
                arrayPositionValues[7 - i][j] = arrayPositionValues[i][j];
            }
        }

        for (int i = 0; i <= 7; i++) {

            for (int j = 0; j <= 3; j++) {
                arrayPositionValues[i][7 - j] = arrayPositionValues[i][j];
            }
        }

    }

    public static void setDEPTH(int depth) {
        DEPTH = depth;
    }

    public static Move getBetterMove(Board board) {

        Board workBoard = board.clone();
        Board originalBoard = workBoard.clone();

        List<Move> possibleMoves = workBoard.getAllPossibleMoves();
        List<Pair<Integer, Move>> scoredMoves = new LinkedList<>();


        for (Move move : possibleMoves) {
            workBoard.makeMove(move);

            int score = alphaBetaMax(Integer.MIN_VALUE, Integer.MAX_VALUE, DEPTH, workBoard);

            scoredMoves.add(new Pair<>(score, move));


            workBoard = originalBoard;

        }



        List<Move> bestMoves = new LinkedList<>();
        int maxScore = 0;

        for (Pair<Integer, Move> scoredMove : scoredMoves)
            if (scoredMove.first > maxScore)
                maxScore = scoredMove.first;

        for (Pair<Integer, Move> scoredMove : scoredMoves)
            if (scoredMove.first == maxScore)
                bestMoves.add(scoredMove.second);


        if (bestMoves.size() == 0) {
            return null;
        }
        return bestMoves.get((int) (Math.random() * bestMoves.size()));
    }

    private static int alphaBetaMax(int alpha, int beta, int depthLeft, Board board) {
        Board originalBoard = board.clone();

        List<Move> possibleMoves = originalBoard.getAllPossibleMoves();
        if (depthLeft == 0 || possibleMoves.isEmpty()) {

            return score(originalBoard);
        }


        for (Move move : possibleMoves) {


            board.makeMove(move);
            int score = alphaBetaMin(alpha, beta, depthLeft - 1, board);
            board = originalBoard;

            if (score >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, score);
        }

        return alpha;
    }

    private static int alphaBetaMin(int alpha, int beta, int depthLeft, Board board) {


        Board originalBoard = board.clone();

        List<Move> possibleMoves = originalBoard.getAllPossibleMoves();
        if (depthLeft == 0 || possibleMoves.isEmpty()) {

            return -score(originalBoard);
        }


        for (Move move : possibleMoves) {


            board.makeMove(move);
            int score = alphaBetaMax(alpha, beta, depthLeft - 1, board);
            board = originalBoard;

            if (score <= alpha) {
                return alpha;
            }
            beta = Math.min(beta, score);
        }

        return beta;
    }

    private static int score(Board board) {
        List<Board.Stone> stones = board.getBoardStones();
        return evalPositions(stones, board.getCurrentPlayer());
    }

    private static int evalPositions(List<Board.Stone> stones, Board.Player curentPlayer) {

        int score = 0;
        for (Board.Stone stone : stones) {
            if (stone.getPlayer() == curentPlayer)
                score += arrayPositionValues[stone.getX()][stone.getY()];
            else
                score -= arrayPositionValues[stone.getX()][stone.getY()];
        }
        return score;
    }

    public static class Pair<F, S> {
        public final F first;
        public final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair<?, ?> pair = (Pair<?, ?>) o;

            return first != null ? first.equals(pair.first) : pair.first == null &&
                    (second != null ? second.equals(pair.second) : pair.second == null);

        }

        @Override
        public String toString() {
            return "first: " + first.toString() +
                    ", second: " + second.toString() +
                    '\n';
        }

        @Override
        public int hashCode() {
            int result = first != null ? first.hashCode() : 0;
            result = 31 * result + (second != null ? second.hashCode() : 0);
            return result;
        }
    }

}



