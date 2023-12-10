package dev.lone.fastnbt_benchmark.tests;

/**
 * Notes:
 * https://stackoverflow.com/questions/19052316/why-is-system-nanotime-way-slower-in-performance-than-system-currenttimemill
 * https://stackoverflow.com/questions/3654848/how-to-get-a-meaningful-result-from-subtracting-2-nanotime-objects
 */
public class Benchmark
{
    protected long nano = System.nanoTime();
    protected long nanoLast = System.nanoTime();

    long saved;

    public void init()
    {
        nano = System.nanoTime();
        nanoLast = System.nanoTime();
    }

    public long elapsed()
    {
        long l = enlapsed0(nano);
        nanoLast = System.nanoTime();
        return l;
    }

    public long elapsedLast()
    {
        long l = enlapsed0(nanoLast);
        nanoLast = System.nanoTime();
        return l;
    }

    long enlapsed0(long ms)
    {
        return (System.nanoTime() - ms) / 1_000_000;
    }

    public static long ms()
    {
        return System.nanoTime();
    }

    public void save()
    {
        saved = elapsed();
    }

    public long getSaved()
    {
        return saved;
    }
}
