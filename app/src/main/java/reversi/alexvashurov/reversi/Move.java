package reversi.alexvashurov.reversi;

import java.util.List;

/**
 * Created by AlexVashurov on 24.03.16.
 */
public class Move {
    private final long position;

    Move(long move) {
        position = move;
    }


    public long getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        return position == move.position;

    }

    public int getRank() {
        int index = Long.numberOfLeadingZeros(position);
        return (index / 8);
    }

    public int getFile() {
        int index = Long.numberOfLeadingZeros(position);
        return (index % 8);
    }

    @Override
    public int hashCode() {
        return (int) (position ^ (position >>> 32));
    }


    @Override
    public String toString() {

        int rank = getRank();
        int file = getFile();

        return ("Rank " + rank + " File " + file);

    }
}
