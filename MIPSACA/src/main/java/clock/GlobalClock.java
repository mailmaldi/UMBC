package clock;

import java.util.concurrent.atomic.AtomicInteger;

public class GlobalClock
{
    public static AtomicInteger CLOCK = new AtomicInteger(0);

}
