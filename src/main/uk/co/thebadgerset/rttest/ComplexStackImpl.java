/* Copyright Rupert Smith, 2005 to 2008, all rights reserved. */
package com.thesett.rttest;

import javolution.context.ObjectFactory;
import javolution.lang.ValueType;

/**
 * <p/><pre><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * </table></pre>
 *
 * @author Rupert Smith
 */
class ComplexStackImpl implements ValueType, Complex
{
    static final ObjectFactory<ComplexStackImpl> FACTORY =
        new ObjectFactory<ComplexStackImpl>()
        {
            protected ComplexStackImpl create()
            {
                return new ComplexStackImpl();
            }
        };

    double real;

    double imag;

    public ComplexStackImpl copy()
    {
        return ComplexStackImpl.valueOf(real, imag);
    }

    public ComplexStackImpl plus(Complex that)
    {
        ComplexStackImpl operand = (ComplexStackImpl) that;

        return ComplexStackImpl.valueOf(this.real + operand.real, this.imag + operand.real);
    }

    public ComplexStackImpl minus(Complex that)
    {
        ComplexStackImpl operand = (ComplexStackImpl) that;

        return ComplexStackImpl.valueOf(this.real - operand.real, this.imag - operand.real);
    }

    public ComplexStackImpl times(Complex that)
    {
        ComplexStackImpl operand = (ComplexStackImpl) that;

        return ComplexStackImpl.valueOf((this.real * operand.real) - (this.imag * operand.imag),
            (this.real * operand.imag) + (this.imag * operand.real));
    }

    static ComplexStackImpl valueOf(double real, double imag)
    {
        ComplexStackImpl complex = FACTORY.object();
        complex.real = real;
        complex.imag = imag;

        return complex;
    }
}
