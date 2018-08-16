package merrymanheavyindustries;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Stream;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

public class TicketServiceNotEmptyTest {
    private TicketService ticketService;

    @Before
    public void setUp() {
	final boolean [][] seats = new boolean[10][10];
	for (int r = 0; r < seats.length; r++) {
	    for (int c = 0; c < seats[ r ].length; c++) {
		seats[ r ][ c ] = true;
	    }
	}
	ticketService = new TicketServiceImpl(seats);
    }

    @Test
    public void testNumSeatsAvailable() {
	assertEquals("The number of tickets available on an empty stage is obviously 100.", 100, ticketService.numSeatsAvailable());
    }

    @Test
    public void testSeatHoldZeroIsNull(){
	SeatHold seatHold = ticketService.findAndHoldSeats(0, "emory.merryman@gmail.com");
	assertNull("This is a nonsense request b/c holding zero seats makes no sense.  We will return a null SeatHold", seatHold);
    }

    @Test
    public void testSeatHoldNegativeIsNull(){
	SeatHold seatHold = ticketService.findAndHoldSeats(-1, "emory.merryman@gmail.com");
	assertNull("This is a nonsense request b/c a negative numSeats makes no sense.  We will return a null SeatHold", seatHold);
    }

    @Test
    public void testSeatHoldPositiveIsNotNull(){
	SeatHold seatHold = ticketService.findAndHoldSeats(1, "emory.merryman@gmail.com");
	assertNotNull("We should process this request.", seatHold);
	assertEquals("This is the first hold.", 0, seatHold.getSeatHoldId());
	assertEquals("We had 100 seats and we put one on hold, so we should have 99 now.", 99, ticketService.numSeatsAvailable());
	assertEquals("We should be getting front row seats", 0, seatHold.getRow());
	assertEquals("We should be getting a seat in the far left.", 0, seatHold.getColumn());
	assertEquals("We should be getting one seat.", 1, seatHold.getNumSeats());
    }

    @Test
    public void testMultipleHolds(){
	SeatHold hold1 = ticketService.findAndHoldSeats(6, "emory.merryman@gmail.com");
	SeatHold hold2 = ticketService.findAndHoldSeats(6, "emory.merryman@gmail.com");
	assertEquals("This is the first hold", 0, hold1.getSeatHoldId());
	assertEquals("This is the second hold", 1, hold2.getSeatHoldId());
	assertEquals("We had 100 seats and we put 12 on hold.  We whould have 88 now.", 88, ticketService.numSeatsAvailable());
	assertEquals("The first hold should get front row seats", 0, hold1.getRow());
	assertEquals("The first hold should get the left most seats", 0, hold1.getColumn());
	assertEquals("The first hold should get 6 seats", 6, hold1.getNumSeats());
	assertEquals("The second hold should get the left most seats", 0, hold2.getColumn());
	assertEquals("The second hold should get seats behind front row", 1, hold2.getRow());
	assertEquals("The second hold should get 6 seats", 6, hold2.getNumSeats());
    }

    @Test
    public void testFillARow(){
	SeatHold hold = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	assertNotNull("You can fill a row", hold);
	assertEquals("This is the first hold", 0, hold.getSeatHoldId());
	assertEquals("We should get front row seats", 0, hold.getRow());
	assertEquals("We should get a left seat", 0, hold.getColumn());
	assertEquals("We should get 10 seats", 10, hold.getNumSeats());
    }

    @Test
    public void testTooManySeats(){
	SeatHold hold = ticketService.findAndHoldSeats(11, "emory.merryman@gmail.com");
	assertNull("You can not ask for more than 10 seats in a single reservation", hold);
    }

    @Test
    public void testWayTooManySeats(){
	SeatHold hold0 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold hold1 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold hold2 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold hold3 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold hold4 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold hold5 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold hold6 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold hold7 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold hold8 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold hold9 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold holdA = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	assertNotNull("Row 1", hold0);
	assertNotNull("Row 2", hold1);
	assertNotNull("Row 3", hold2);
	assertNotNull("Row 4", hold3);
	assertNotNull("Row 5", hold4);
	assertNotNull("Row 6", hold5);
	assertNotNull("Row 7", hold6);
	assertNotNull("Row 8", hold7);
	assertNotNull("Row 9", hold8);
	assertNotNull("Row 10", hold9);
	assertNull("Service Refused b/c out of seats", holdA);
    }

    @Test
    public void testHoldsAreSelfClearing() throws InterruptedException{
	SeatHold hold1 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	assertEquals("We had 100 seats and held 10.  We should have 90 now.", 90, ticketService.numSeatsAvailable());
	sleep(3000);
	assertNotNull("The request should have been honored.", hold1);
	assertEquals("We should get front row seats.", 0, hold1.getRow());
	assertEquals("We should get a left seat", 0, hold1.getColumn());
	assertEquals("We should get 10 seats", 10, hold1.getNumSeats());
	assertEquals("We had 100 seats and held 1 but let it lapse.  We should have 100 now.", 100, ticketService.numSeatsAvailable());
	SeatHold hold2 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	assertNotNull("The request should have been honored.", hold2);
	assertEquals("We should get front row seats because the first hold got cleared.", 0, hold2.getRow());
	assertEquals("We should get a left seat", 0, hold2.getColumn());
	assertEquals("We should get 10 seats", 10, hold2.getNumSeats());
    }

    @Test
    public void testReservationsAreHonored() throws InterruptedException {
	SeatHold hold1 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	assertEquals("We had 100 seats and held 10.  We should have 90 now.", 90, ticketService.numSeatsAvailable());
	ticketService.reserveSeats(0, "emory.merryman@gmail.com");
	sleep(3000);
	assertNotNull("The request should have been honored.", hold1);
	assertEquals("We should get front row seats.", 0, hold1.getRow());
	assertEquals("We should get left seat", 0, hold1.getColumn());
	assertEquals("We should get 10 seats", 10, hold1.getNumSeats());
	assertEquals("We had 100 seats and held 1 and reserved it before it could lapse.  We should have 90 now.", 90, ticketService.numSeatsAvailable());
	SeatHold hold2 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	assertNotNull("The request should have been honored.", hold2);
	assertEquals("We should get behind front row seats because the first hold got cleared.", 1, hold2.getRow());
    }

    @Test
    public void testFragTheStadium() throws InterruptedException {
	SeatHold hold1 = ticketService.findAndHoldSeats(5, "emory.merryman@gmail.com");
	SeatHold hold2 = ticketService.findAndHoldSeats(5, "emory.merryman@gmail.com");
	assertEquals(1, hold2.getSeatHoldId());
	ticketService.reserveSeats(1, "emory.merryman@gmail.com");
	sleep(3000);
	SeatHold hold3 = ticketService.findAndHoldSeats(6, "emory.merryman@gmail.com");
	assertEquals("We should get behind front row seats", 1, hold3.getRow());
    }

    @Test
    public void testReservationsAreHonoredButOthersNot() throws InterruptedException {
	SeatHold hold1 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	assertEquals("We had 100 seats and held 10.  We should have 90 now.", 90, ticketService.numSeatsAvailable());
	assertNotNull("The request should have been honored.", hold1);
	assertEquals("We should get front row seats.", 0, hold1.getRow());
	ticketService.reserveSeats(0, "emory.merryman@gmail.com");
	SeatHold hold2 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	assertNotNull("The request should have been honored.", hold2);
	assertEquals("We should get behind front row seats because the first hold got cleared.", 1, hold2.getRow());
	assertEquals(0, hold1.getSeatHoldId());
	assertEquals(1, hold2.getSeatHoldId());
	ticketService.reserveSeats(1, "emory.merryman@gmail.com");
    }

    @Test
    public void testReserveDoesNotScuttleOthers(){
	SeatHold hold1 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	SeatHold hold2 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	ticketService.reserveSeats(1, "emory.merryman@gmail.com");
	SeatHold hold3 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	assertEquals("We should be seated in the 3rd row", 2, hold3.getRow());
    }

    @Test
    public void testEmail() {
	SeatHold hold = ticketService.findAndHoldSeats(1, "976d2aba-7ef9-4d20-a20b-6839fd9619b6");
	assertNotNull("It should be honored", hold);
	assertEquals("We are the first hold", 0, hold.getSeatHoldId());
	assertEquals("54c387a5-0ff8-4ab0-a91f-4e077f7e4ad3", ticketService.reserveSeats(0, "54c387a5-0ff8-4ab0-a91f-4e077f7e4ad3"));
    }

    @Test
    public void testCleanIsNotRetained() throws InterruptedException {
	SeatHold hold = ticketService.findAndHoldSeats(1, "emory.merryman@gmail.com");
	sleep(1000);
	assertEquals("The hold should be retained at least one second.  Because of rounding, the hold might be released anytime between one and two seconds.", 99, ticketService.numSeatsAvailable());
	sleep(1000);
	assertEquals("The hold should not be retained after two seconds.", 100, ticketService.numSeatsAvailable());
	assertEquals("We should have gotten the first hold", 0, hold.getSeatHoldId());
    }

    @Test(expected = RuntimeException.class)
    public void testCleanHoldsList() throws InterruptedException {
	SeatHold hold = ticketService.findAndHoldSeats(1, "emory.merryman@gmail.com");
	sleep(1000);
	assertEquals("The hold should be retained at least one second.  Because of rounding, the hold might be released anytime between one and two seconds.", 99, ticketService.numSeatsAvailable());
	sleep(1000);
	assertEquals("The hold should not be retained after two seconds.", 100, ticketService.numSeatsAvailable());
	assertEquals("We should have gotten the first hold", 0, hold.getSeatHoldId());
	ticketService.reserveSeats(0, "emory.merryman@gmail.com");
    }

    @Test
    public void testCleanNecessaryForFindAndHoldSeats() throws InterruptedException {
	SeatHold hold0 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	sleep(3000);
	SeatHold hold1 = ticketService.findAndHoldSeats(10, "emory.merryman@gmail.com");
	assertEquals("We should get front row seats", 0, hold1.getRow());
    }

    @Test
    public void testColumns() {
	SeatHold hold0 = ticketService.findAndHoldSeats(5, "emory.merryman@gmail.com");
	SeatHold hold1 = ticketService.findAndHoldSeats(4, "emory.merryman@gmail.com");
	assertNotNull(hold0);
	assertNotNull(hold1);
	assertEquals(0, hold0.getColumn());
	assertEquals(5, hold1.getColumn());
	assertEquals(5, hold0.getNumSeats());
	assertEquals(4, hold1.getNumSeats());
    }
}
