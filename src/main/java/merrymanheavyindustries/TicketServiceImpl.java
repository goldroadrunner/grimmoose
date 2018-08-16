package merrymanheavyindustries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @{InheritDoc}.
 **/
final class TicketServiceImpl implements TicketService {
    /**
     * Holds not reserved should expire after this
     * number of seconds.
     **/
    private static final long EXPIRY = 2;

    /**
     * A matrix that shows seat availability.
     **/
    private final boolean[][] seats;

    /**
     * A list of recently issued SeatHolds.
     **/
    private List<SeatHold> seatHolds = new ArrayList<SeatHold>();

    /**
     * Used to generate unique ids for SeatHolds.
     **/
    private int seatHoldCounter = 0;

    /**
     * Construct a TicketServiceImpl based on the specified
     * boolean seat matrix.
     *
     * Each element is a flag that indicates whether the
     * associated seat is available.
     *
     * @param xseats the boolean seat matrix
     **/
    TicketServiceImpl(final boolean[][] xseats) {
        this.seats = xseats;
    }

    /**
     * Iterate over the list of seatHolds
     * and for all seatHolds over 2 seconds old.
     * <OL>
     *      <LI> Reverse the seat flag
     *      <LI> discard it
     * </OL>
     **/
    private void clean() {
        final long now = new Date().getTime() / 1000;
        seatHolds.stream()
            .filter(seatHold -> now - seatHold.getHoldDate() >= EXPIRY)
            .forEach(seatHold -> {
                    for (int i = 0; i < seatHold.getNumSeats(); i++) {
                        seats[ seatHold.getRow() ][ seatHold.getColumn() + i ]
                            = true;
                    }
                });
        seatHolds.removeIf(seatHold -> now - seatHold.getHoldDate() >= EXPIRY);
    }

    /**
     * @InheritDoc.
     *
     * @return @{InheritDoc}
     **/
    @Override
    public int numSeatsAvailable() {
        int numSeatsAvailable = 0;
        synchronized (seatHolds) { // 8b48bc9d
            clean();
            for (boolean[] ss: seats) {
                for (boolean s: ss) {
                    if (s) {
                        numSeatsAvailable++;
                    }
                }
            }
        }
        return numSeatsAvailable;
    }

    /**
     * @{InheritDoc}.
     *
     * @param numSeats @{InheritDoc}
     * @param customerEmail @{InheritDoc}
     * @return @{InheritDoc}
     **/
    @Override
    public SeatHold
        findAndHoldSeats(final int numSeats, final String customerEmail) {
        if (numSeats > 0) {
            synchronized (seatHolds) { // c9e7eee1
                clean();
                for (int r = 0; r < seats.length; r++) {
                    for (int c = 0; c < seats[ r ].length - numSeats + 1; c++) {
                        boolean match = true;
                        for (int i = 0; i < numSeats; i++) {
                            match = match && seats[ r ][ c + i ];
                        }
                        if (match) {
                            for (int i = 0; i < numSeats; i++) {
                                seats[ r ][ c + i ] = false;
                            }
                            SeatHold seatHold =
                                new SeatHoldImpl(
                                                 seatHoldCounter++,
                                                 r,
                                                 c,
                                                 numSeats);
                            seatHolds.add(seatHold);
                            return seatHold;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @{InheritDoc}.
     *
     * @param seatHoldId @{InheritDoc}
     * @param customerEmail @{InheritDoc}
     * @return @{InheritDoc}
     **/
    @Override
    public String
        reserveSeats(final int seatHoldId, final String customerEmail) {
        if (!seatHolds
            .removeIf(seatHold -> seatHold.getSeatHoldId() == seatHoldId)
            ) {
            throw new RuntimeException("No Reservation.");
        }
        return customerEmail;
    }
}
