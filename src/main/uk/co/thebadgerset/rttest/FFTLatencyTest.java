/* Copyright Rupert Smith, 2005 to 2008, all rights reserved. */
package com.thesett.rttest;

import com.thesett.common.properties.ParsedProperties;
import com.thesett.common.throttle.SleepThrottle;
import com.thesett.common.throttle.Throttle;
import com.thesett.junit.extensions.AsymptoticTestCase;
import com.thesett.junit.extensions.TestThreadAware;
import com.thesett.junit.extensions.TimingController;
import com.thesett.junit.extensions.TimingControllerAware;
import com.thesett.junit.extensions.util.TestContextProperties;
import static com.thesett.rttest.Constants.K;
import static com.thesett.rttest.Constants.N;

/**
 * FFTLatencyTest runs reasonably lengthy FFT calculations, in order to time how long such a calculation takes, and
 * particularly to examine the variance in the time taken under different conditions.
 *
 * <p/>The test can run one of two implementations of complex numbers and an FFT calculation. One creates many
 * {@link Complex} numbers on the heap, the other creates many {@link Complex} numbers on the stack, using the stack
 * allocation capabilities of the Javolution library.
 *
 * <p/>The tests should be run many times, and the latencies examined, under different configurations of the JVM
 * wrt garbage collection. The idea is to demonstrate that by either running the tests slowly, and using an incremental
 * garbage collector, or by using stack allocation techniques to avoid creating garbage in the first place, that
 * the variance in latency can be minimized.
 *
 * <p/><pre><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Calculate a large FFT over a vector of complex numbers.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class FFTLatencyTest extends AsymptoticTestCase implements TestThreadAware, TimingControllerAware
{
    /** The FFT implementation property name. */
    private static final String FFT_IMPL_PROPNAME = "fftImpl";

    /** The default FFT implementation property name. */
    private static final String FFT_IMPL_DEFAULT = "heap";

    /** The throttle property name. */
    private static final String THROTTLE_PROPNAME = "throttle";

    /** The default throttle rate. */
    private static final int THROTTLE_DEFAULT = 30;

    /** Holds the factory to create the complex math implementations under test with. */
    private MathFactory mathFactory;

    /** Holds the FFT calculator instance to test. */
    private FFTCalculator calculator;

    /** Holds some data vectors to calculate FFTs over. */
    private Complex[][] frames;

    /** Holds some data vectors to place the test results into. */
    private Complex[][] results;

    /** Used to count the test iterations, to cycle the data set under test with. */
    private int testIterations;

    /** Used to throttle the test to keep it running below the saturation load. */
    private Throttle throttle = new SleepThrottle();

    /** Used to read the tests configurable properties through. */
    private ParsedProperties testProps;

    /** Holds the timing controller to allow the test to set its own wall clock boundaries. */
    private TimingController timingController;

    /**
     * Creates the named test case.
     *
     * @param name The name of the test to run.
     */
    public FFTLatencyTest(String name)
    {
        super(name);

        // Set up the default test properties.
        ParsedProperties defaults = new ParsedProperties();
        defaults.setPropertyIfNull(FFT_IMPL_PROPNAME, FFT_IMPL_DEFAULT);
        defaults.setPropertyIfNull(THROTTLE_PROPNAME, THROTTLE_DEFAULT);

        testProps = TestContextProperties.getInstance(defaults);
    }

    /**
     * Sets up the complex math implementation to test, along with some random data sets.
     */
    public void threadSetUp()
    {
        // Set up the test throttle.
        float throttleRate = testProps.getPropertyAsInteger(THROTTLE_PROPNAME);
        throttle.setRate(throttleRate);

        // Select the FFT implementation to use for the test.
        String fftImplName = testProps.getProperty(FFT_IMPL_PROPNAME);

        if ("heap".equals(fftImplName))
        {
            mathFactory = new MathFactoryHeapImpl();
            System.out.println("Using heap implementation.");
        }
        else if ("stack".equals(fftImplName))
        {
            mathFactory = new MathFactoryStackImpl();
            System.out.println("Using stack implementation.");
        }
        else
        {
            throw new IllegalArgumentException("Not a valid FFT implementation, must be 'heap' or 'stack'.");
        }

        // Set up the calculator and the test data.
        calculator = mathFactory.getFFTCalculator();

        frames = mathFactory.createComplexArray(K, N);
        results = mathFactory.createComplexArray(K, N);

        for (int i = 0; i < K; i++)
        {
            for (int j = 0; j < N; j++)
            {
                frames[i][j] = mathFactory.getRandomComplex();
            }
        }
    }

    /** Does nothing. */
    public void threadTearDown()
    {
    }

    /** Runs a reasonably lengthy FFT calculation. */
    public void testLatency() throws Exception
    {
        try
        {
            TimingController tc = timingController.getControllerForCurrentThread();

            testIterations++;

            throttle.throttle();

            // Start the test timing from this point only to remove the throttling from the test time.
            tc.restart();
            calculator.fft(frames[testIterations % K], results[testIterations % K]);
            tc.completeTest(true);
        }
        catch (InterruptedException e)
        {
            // Interrupted for shutdown, keep the interrupt flag set, ignore exception but return immediately.
            Thread.currentThread().interrupt();
            e = null;

            return;
        }
    }

    /** {@inheritDoc} */
    public void setTimingController(TimingController timingController)
    {
        this.timingController = timingController;
    }
}
