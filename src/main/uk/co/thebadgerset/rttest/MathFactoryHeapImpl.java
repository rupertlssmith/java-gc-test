/* Copyright Rupert Smith, 2005 to 2008, all rights reserved. */
package com.thesett.rttest;

/**
 * <p/><pre><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class MathFactoryHeapImpl implements MathFactory
{
    public FFTCalculator getFFTCalculator()
    {
        return new FFTHeapImpl();
    }

    public Complex getRandomComplex()
    {
        return new ComplexHeapImpl(Math.random(), Math.random());
    }

    public Complex[][] createComplexArray(int x, int y)
    {
        return new ComplexHeapImpl[x][y];
    }
}
