package reversi.alexvashurov.reversi;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by AlexVashurov on 24.03.16.
 */
public class Board implements Cloneable {
    private final static String TAG = "REVERSI_DEBUG";

    private final static Shift shiftDown = new Shift() {
        @Override
        public long shift(long position) {
            return position >>> 8;
        }
    };
    private final static Shift shiftDownLeft = new Shift() {
        @Override
        public long shift(long position) {
            long dlShift = position >>> 7;
            return dlShift & ~RIGHT_MASK;
        }
    };
    private final static Shift shiftDownRight = new Shift() {
        @Override
        public long shift(long position) {
            long drShift = position >>> 9;
            return drShift & ~LEFT_MASK;
        }
    };
    private final static Shift shiftLeft = new Shift() {
        @Override
        public long shift(long position) {
            long lShift = position << 1;
            return lShift & ~RIGHT_MASK;
        }
    };
    private final static Shift shiftRight = new Shift() {
        @Override
        public long shift(long position) {
            long rShift = position >>> 1;
            return rShift & ~LEFT_MASK;
        }
    };
    private final static Shift shiftUp = new Shift() {
        @Override
        public long shift(long position) {
            return position << 8;

        }
    };
    private final static Shift shiftUpLeft = new Shift() {
        @Override
        public long shift(long position) {
            long ulShift = position << 9;
            return ulShift & ~RIGHT_MASK;
        }
    };
    private final static Shift shiftUpRight = new Shift() {
        @Override
        public long shift(long position) {
            long urShift = position << 7L;
            return urShift & ~LEFT_MASK;
        }
    };
    private long blackBoard;
    private long whiteBoard;
    private Player currentPlayer;

    private Board(long blackBoard, long whiteBoard) {
        this.whiteBoard = whiteBoard;
        this.blackBoard = blackBoard;
        this.currentPlayer = Player.WHITE;
    }

    private Board(long blackBoard, long whiteBoard, Player currentPlayer) {
        this.blackBoard = blackBoard;
        this.whiteBoard = whiteBoard;
        this.currentPlayer = currentPlayer;
    }

    public static Board initialize() {
        return new Board(68853694464L, 34628173824L);
    }

    @Override
    protected Board clone() {
        return new Board(blackBoard, whiteBoard, currentPlayer);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Stone> getBoardStones() {
        List<Stone> stones = new ArrayList<>();
        try {
            long whiteStones = whiteBoard;
            long blackStones = blackBoard;


            while (whiteStones != 0L) {
                long stone = Long.highestOneBit(whiteStones);
                whiteStones ^= stone;
                int x = getFile(stone);
                int y = getRank(stone);

                stones.add(new Stone(Player.WHITE, x, y));

            }

            while (blackStones != 0L) {
                long stone = Long.highestOneBit(blackStones);
                blackStones ^= stone;
                int x = getFile(stone);
                int y = getRank(stone);

                stones.add(new Stone(Player.BLACK, x, y));

            }
        } catch (Exception e) {
            Log.i(TAG, "get stones " + e.toString());
        }
        return stones;
    }

    private int getRank(long position) {
        int index = Long.numberOfLeadingZeros(position);
        return (index / 8);
    }

    private int getFile(long position) {
        int index = Long.numberOfLeadingZeros(position);
        return (index % 8);
    }

    public long getPosition(int rank, int file) {
        int shift = rank * 8 + file;
        return 1L << shift;
    }

    private long getEnemyBoard(Player player) {
        if (player == Player.WHITE)
            return blackBoard;
        else
            return whiteBoard;
    }

    private long getPlayerBoard(Player player) {
        if (player == Player.BLACK)
            return blackBoard;
        else
            return whiteBoard;
    }

    public long doubleBoard() {
        return (blackBoard | whiteBoard);
    }

    public long freeSquares() {
        return ~doubleBoard();
    }

    private void changePieceAtPosition(Player player, long position) {

        if (player == Player.WHITE) {
            blackBoard ^= position;
            whiteBoard |= position;
        } else {
            blackBoard |= position;
            whiteBoard ^= position;
        }
    }

    private void setPieceAtPosition(Player player, long position) {
        if (player == Player.WHITE) {
            whiteBoard |= position;
        } else {
            blackBoard |= position;
        }
    }

    public void makeMove(Move move) {
        makeMove(currentPlayer, move);
        currentPlayer = changePlayer();
    }

    private void makeMove(Player player, Move move) {


        List<Move> piecesToTurn = new LinkedList<>();
        List<Move> endPoints = getAllPiecesToChange(player, move);
        piecesToTurn.addAll(endPoints);
        for (Move m : piecesToTurn) {
            changePieceAtPosition(player, m.getPosition());
        }
        setPieceAtPosition(player, move.getPosition());


    }

    public List<Move> getAllPossibleMoves() {
        return getAllPossibleMoves(currentPlayer);
    }

    public List<Move> getAllPossibleMoves(Player player) {

        List<Move> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(moves(player, shiftDown));
        possibleMoves.addAll(moves(player, shiftDownLeft));
        possibleMoves.addAll(moves(player, shiftDownRight));
        possibleMoves.addAll(moves(player, shiftLeft));
        possibleMoves.addAll(moves(player, shiftRight));
        possibleMoves.addAll(moves(player, shiftUp));
        possibleMoves.addAll(moves(player, shiftUpLeft));
        possibleMoves.addAll(moves(player, shiftUpRight));


        return possibleMoves;
    }

    private List<Move> moves(Player player, Shift shift) {
        List<Move> upRightMoves = new LinkedList<>();
        long potentialMoves = shift.shift(getPlayerBoard(player)) & getEnemyBoard(player);
        long emptyBoard = freeSquares();
        while (potentialMoves != 0) {
            long legalMoves = shift.shift(potentialMoves) & emptyBoard;
            upRightMoves.addAll(getLongCoordinates(legalMoves));
            potentialMoves = shift.shift(potentialMoves) & getEnemyBoard(player);
        }
        return upRightMoves;
    }

    private List<Move> getLongCoordinates(long legalMoves) {
        List<Move> possibleMoves = new ArrayList<>();
        while (legalMoves != 0L) {

            long move = Long.highestOneBit(legalMoves);

            legalMoves ^= move;


            possibleMoves.add(new Move(move));

        }

        return possibleMoves;
    }

    private List<Move> getAllPiecesToChange(Player player, Move moveFrom) {
        List<Move> piecesToChange = new ArrayList<>();


        piecesToChange.addAll(getEndPoint(player, moveFrom, shiftUpLeft));
        piecesToChange.addAll(getEndPoint(player, moveFrom, shiftUp));
        piecesToChange.addAll(getEndPoint(player, moveFrom, shiftUpRight));
        piecesToChange.addAll(getEndPoint(player, moveFrom, shiftLeft));
        piecesToChange.addAll(getEndPoint(player, moveFrom, shiftRight));
        piecesToChange.addAll(getEndPoint(player, moveFrom, shiftDownLeft));
        piecesToChange.addAll(getEndPoint(player, moveFrom, shiftDown));
        piecesToChange.addAll(getEndPoint(player, moveFrom, shiftDownRight));


        return piecesToChange;
    }

    private boolean validEndPoint(Player player, long potentialEndPoint) {
        return (potentialEndPoint & getPlayerBoard(player)) == potentialEndPoint;
    }

    private List<Move> getEndPoint(Player color, Move moveFrom, Shift shift) {
        long startPoint = moveFrom.getPosition();
        long potentialEndPoint = shift.shift(startPoint);

        List<Move> potential = new ArrayList<>();

        while (potentialEndPoint != 0) {
            if (validEndPoint(color, potentialEndPoint)) {
                return potential;
            }
            if ((potentialEndPoint & doubleBoard()) == 0L)
                return new ArrayList<>();

            potential.add(new Move(potentialEndPoint));
            potentialEndPoint = shift.shift(potentialEndPoint);
        }
        return new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Black\n" +
                Long.toBinaryString(blackBoard) +
                "\n" +
                "White\n" +
                Long.toBinaryString(whiteBoard) +
                "\n";
    }

    public void changeCurrentPlayer() {
        if (currentPlayer == Player.WHITE)
            currentPlayer = Player.BLACK;
        else
            currentPlayer = Player.WHITE;
    }

    private Player changePlayer() {
        if (currentPlayer == Player.WHITE) return Player.BLACK;
        return Player.WHITE;
    }

    public enum Player {
        WHITE,
        BLACK;
    }

    private interface Shift {
        long LEFT_MASK = -9187201950435737472L;
        long RIGHT_MASK = 72340172838076673L;

        long shift(long position);
    }

    public class Stone {
        private final Player player;
        private final int x;
        private final int y;

        public Stone(Player player, int x, int y) {
            this.player = player;
            this.x = x;
            this.y = y;
        }

        public Player getPlayer() {
            return player;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Stone stone = (Stone) o;

            return x == stone.x && y == stone.y && player == stone.player;

        }

        @Override
        public int hashCode() {
            int result = player.hashCode();
            result = 31 * result + x;
            result = 31 * result + y;
            return result;
        }
    }

}
