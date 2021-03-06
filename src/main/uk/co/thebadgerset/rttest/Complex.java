/* Copyright Rupert Smith, 2005 to 2008, all rights reserved. */
package com.thesett.rttest;

/**
 * <p/><pre><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities
 * </table></pre>
 *
 * @author Rupert Smith
 */
public interface Complex
{
    Complex plus(Complex that);

    Complex minus(Complex that);

    Complex times(Complex that);
}
