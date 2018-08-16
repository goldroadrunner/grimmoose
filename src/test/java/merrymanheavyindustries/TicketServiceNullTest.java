package merrymanheavyindustries;

import org.junit.Before;
import org.junit.Test;

public class TicketServiceNullTest {
    private TicketService ticketService;

    @Before
    public void setUp() {
	ticketService = new TicketServiceImpl(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNumSeatsAvailable() {
	ticketService.numSeatsAvailable();
    }

    @Test(expected = NullPointerException.class)
    public void testFindAndHoldSeats() {
	ticketService.findAndHoldSeats(1, "emory.merryman@gmail.com");
    }

    @Test(expected = RuntimeException.class)
    public void testReserveSeats() {
	ticketService.reserveSeats(0, "emory.merryman@gmail.com");
    }
}
