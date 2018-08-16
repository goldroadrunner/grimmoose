# TicketService

## Getting Started

### My Setup
I use the fedora 28 operating system.
I installed maven with the command `sudo dnf install --assumeyes maven`.  I think this implicitly installed all the other necessary software.

My setup is not a prerequisite, but a guide.
I trust the user has a similar system with the necessary software (or can figure out how to install it).

### Usage Guide
This repository is a software library and not an application.
Execute `mvn clean cobertura:cobertura test org.pitest:pitest-maven:mutationCoverage checkstyle:checkstyle package`

#### Testing
Unit test results are stored in ${PROJECT_DIR}/target/surefire-reports/.
We expect to see all passing tests.
If there are failing tests, then the previous mvn command will terminate early and show the error messages.

#### Coverage
Coverage reports are stored in ${PROJECT_DIR}/target/site/cobertura/index.html.
We expect to see 100% coverage, but less than 100% coverage will not terminate the mvn command early.

#### Mutation Testing
Mutation testing reports are stored in ${PROJECT_DIR}/target/pit-reports.
We expect to see 100% of mutants killed, but less than 100% will not terminate the mvn command early.

#### Static Testing
Static testing reports are store in ${PROJECT_DIR}/target/site/checkstyle.html.
We expect to see no violations.

### Discussion

#### Assumptions
* I assume that the best seats are
  1. Adjacent -
     I am thinking of a family group with minor children.
       It is necessary to sit together.
       For this purpose, adjacent means on the same row.
       Consequently, the system is unable to handle requests for more seats than a row is long.
  2. As close to the front as possible.
* I assume that either the user will never make nonsense request (or the response does not matter much):
  1. Requesting a negative number of seats:
     I chose the easiest response to nonsense requests and tested for that.
     Perhaps a more appropriate response would be to throw an Exception or return a SeatHold that does not hold any seats.
  2. Reserving seats after the find has expired (or reserving seats with a bogus seatHoldId)
     I chose to silently fail.
     Perhaps it would be more appropriate to throw an exception or
     instead of returning an email we could return a status object
* I assume that the customerEmail address is not needed.
  I completely ignore it - other than to use it once as a return value.
  I do not attempt to validate that the address used for findAndHold matches reserveSeats.

#### Observations

##### Mutation Testing

  I chose to use the PIT Mutation Testing System (http://pitest.org/).
  I think this will obviate more traditional code coverage tools.
  The Mutation Testing System creates "code mutants".
  An example of a mutant is taking the existing program and modifying the 'getHoldDate' method of 'merrymanheavyindustries/TicketServiceImpl' from `return holdDate;` to `return holdDate+1;`
  The Mutation Testing System runs all the tests.
  If at least one test fails then we say the tests "killed the mutant."
  However, if all tests pass then we say the "mutant survived."

  In my initial implementation I stored the date as number of milliseconds and I tested it with a margin of 1 second.
  Changing the return value by 1 millisecond had no effect on the tests and the mutant "survived".
  In response, I stored the date as number of seconds.
  This meant that a "+1" mutation had a much greater effect and the mutant died.

  In a hypothetical future iteration, I can address this issue.
  However, the important point I wanted to highlight is that I would not have seen this issue except for mutation testing.

  A common complaint against focus on coverage is that it is relatively easy to write tests that achieve 100% coverage without actually verifying anything.
  A good example of how mutation testing addresses this is the 'customerEmail'.
  Initial tests did not use or verify in anyway the 'customerEmail' address returned by the `reserveSeats` method.
  One mutant created replaced that string and all my tests continued to pass - the mutant survived.

##### Performance
The performance of the implementation is not good.
This is probably because for each 'findAndHold', the service
scans each seat from the first row until it finds an acceptable
set of seats.

In the hypothetical next iteration, I would work on improving performance.
I would use the suite of tests, to insure that any optimizations would not
impact correctness.