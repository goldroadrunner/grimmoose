package merrymanheavyindustries;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TicketServiceOneEmptyRowTest {
    private TicketService ticketService;
    
    @Before
    public void setUp() {
	final boolean [][] seats = new boolean[1][0];
	ticketService = new TicketServiceImpl(seats);
    }

    @Test
    public void testNumSeatsAvailable() {
	assertEquals("The number of tickets available on an empty stage is obviously zero.", 0, ticketService.numSeatsAvailable());
    }

    @Test
    public void testSeatHoldZeroIsNull(){
	SeatHold seatHold = ticketService.findAndHoldSeats(0, "emory.merryman@gmail.com");
	assertNull("This is a nonsense request b/c the arena is empty and holding zero seats makes no sense.  We will return a null SeatHold", seatHold);
    }

    @Test
    public void testSeatHoldNegativeIsNull(){
	SeatHold seatHold = ticketService.findAndHoldSeats(-1, "emory.merryman@gmail.com");
	assertNull("This is a nonsense request b/c the arena is empty and holding a negative number of seats makes no sense.  We will return a null SeatHold", seatHold);
    }

    @Test
    public void testSeatHoldPositiveIsNull(){
	SeatHold seatHold = ticketService.findAndHoldSeats(1, "emory.merryman@gmail.com");
	assertNull("This is a nonsense request b/c the arena is empty.  We will return a null SeatHold.", seatHold);
    }
}
