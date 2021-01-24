/* Copyright Rupert Smith, 2005 to 2008, all rights reserved. */
package com.thesett.rttest;

/**
 * <p/><pre><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class ComplexHeapImpl implements Complex
{
    double _real, _imag;

    public ComplexHeapImpl(double real, double imag)
    {
        _real = real;
        _imag = imag;
    }

    public ComplexHeapImpl plus(Complex that)
    {
        ComplexHeapImpl operand = (ComplexHeapImpl) that;

        return new ComplexHeapImpl(this._real + operand._real, this._imag + operand._real);
    }

    public ComplexHeapImpl minus(Complex that)
    {
        ComplexHeapImpl operand = (ComplexHeapImpl) that;

        return new ComplexHeapImpl(this._real - operand._real, this._imag - operand._real);
    }

    public ComplexHeapImpl times(Complex that)
    {
        ComplexHeapImpl operand = (ComplexHeapImpl) that;

        return new ComplexHeapImpl((this._real * operand._real) - (this._imag * operand._imag),
            (this._real * operand._imag) + (this._imag * operand._real));
    }
}
