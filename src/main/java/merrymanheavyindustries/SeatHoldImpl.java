package merrymanheavyindustries;

import java.util.Date;

/**
 * @{InheritDoc}.
 **/
final class SeatHoldImpl implements SeatHold {
    /**
     * A unique identifier.
     **/
    private final int seatHoldId;

    /**
     * The date (in seconds since 1970) that this
     * SeatHold was created.
     * This can be used to expire unreserved SeatHolds.
     **/
    private final long holdDate = new Date().getTime() / 1000;

    /**
     * The row that this SeatHold controls.
     **/
    private final int row;

    /**
     * The column of the first seat this SeatHold controls.
     **/
    private final int column;

    /**
     * The number of seats this SeatHold controls.
     **/
    private final int numSeats;

    /**
     * Constructs a SeatHoldImpl based on
     * <OL>
     *      <LI> the specified id
     *      <LI> the specified row
     *      <LI> the specified column
     *      <LI> the specified number of seats
     * </OL>
     *
     * Basically this seat hold holds the seats in the
     * specified row starting from the specified column
     * and continuing to the right for the specified
     * number of seats.
     *
     * @param xseatHoldId the specified id
     * @param xrow the specified row
     * @param xcolumn the specified column
     * @param xnumSeats the specified number of seats
     **/
    SeatHoldImpl(
                 final int xseatHoldId,
                 final int xrow,
                 final int xcolumn,
                 final int xnumSeats) {
        this.seatHoldId = xseatHoldId;
        this.row = xrow;
        this.column = xcolumn;
        this.numSeats = xnumSeats;
    }

    /**
     * @{InheritDoc}
     *
     * @return @{InheritDoc}
     **/
    @Override
    public int getSeatHoldId() {
        return seatHoldId;
    }

    /**
     * @{InheritDoc}.
     *
     * @return @{InheritDoc}
     **/
    @Override
    public long getHoldDate() {
        return holdDate;
    }

    /**
     * @{InheritDoc}.
     *
     * @return @{InheritDoc}
     **/
    @Override
    public int getRow() {
        return row;
    }

    /**
     * @{InheritDoc}.
     *
     * @return @{InheritDoc}
     **/
    @Override
    public int getColumn() {
        return column;
    }

    /**
     * @{InheritDoc}.
     *
     * @return @{InheritDoc}
     **/
    @Override
    public int getNumSeats() {
        return numSeats;
    }
}
