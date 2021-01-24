/* Copyright Rupert Smith, 2005 to 2008, all rights reserved. */
package com.thesett.rttest;

import javolution.context.StackContext;
import javolution.lang.ValueType;

/**
 * <p/><pre><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class FFTStackImpl implements FFTCalculator
{
    public void fft(Complex[] a, final Complex[] A)
    {
        StackContext.enter(); // Enters new allocation stack.

        try
        {
            for (int i = 0; i < Constants.N; ++i)
            {
                A[reverseBits(i)] = a[i];
            }

            for (int s = 1; s <= Constants.LOG2_N; ++s)
            {
                int m = 1 << s;
                Complex w = ComplexStackImpl.valueOf(1, 0);
                Complex wm = ComplexStackImpl.valueOf(Math.cos(2 * Math.PI / m), Math.sin(2 * Math.PI / m));

                for (int j = 0; j < (m / 2); ++j)
                {
                    for (int k = j; k < Constants.N; k += m)
                    {
                        Complex t = w.times(A[k + (m / 2)]);
                        Complex u = A[k];
                        A[k] = u.plus(t);
                        A[k + (m / 2)] = u.minus(t);
                    }

                    w = w.times(wm);
                }
            }
        }
        finally
        {
            StackContext.outerCopy((ValueType[]) A); // Exports array results
            StackContext.exit(); // Resets stack.
        }
    }

    private int reverseBits(int x)
    {
        int n = 0;

        for (int i = 0; i < Constants.LOG2_N; i++)
        {
            n <<= 1;
            n |= (x & 1);
            x >>= 1;
        }

        return n;
    }
}
