package merrymanheavyindustries;

/**
 * Represents a control over a block of seats.
 *
 * The seats are all in the same row and adjacent.
 **/
public interface SeatHold {
    /**
     * Gets a unique identifier for this
     * SeatHold.
     *
     * @return a unique identifier
     **/
    int getSeatHoldId();

    /**
     * Gets the date (in seconds)
     * that this SeatHold was created.
     *
     * @return SeatHold creation date
     **/
    long getHoldDate();

    /**
     * Gets the row that this SeatHold controls.
     *
     * @return the row
     **/
    int getRow();

    /**
     * Gets the column of the first seat that
     * this SeatHold controls.
     *
     * @return the column of the first controlled seat
     **/
    int getColumn();

    /**
     * Gets the number of seats that
     * this SeatHold controls.
     *
     * @return the number of seats controlled
     **/
    int getNumSeats();
}
