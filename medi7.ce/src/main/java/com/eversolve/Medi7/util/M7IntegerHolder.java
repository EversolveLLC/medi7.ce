/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7.util;

/**
 * This a wrapper class is used when integer is an in-out parameter <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * 
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 */
public class M7IntegerHolder {
	/**
	 * Integer value
	 */
	public int m_iInt = 0;

	/**
	 * Constructor, initializes the integer holder to the parameter
	 * 
	 * @param iInt
	 *            Integer value
	 */
	public M7IntegerHolder(int iInt) {
		m_iInt = iInt;
	}

	/**
	 * Sets the value of the object
	 * 
	 * @param iInt
	 *            Integer value
	 */
	public void setInt(int iInt) {
		m_iInt = iInt;
	}

	/**
	 * Returns the value of the integer
	 * 
	 * @return Integer value
	 */
	public long getIntValue() {
		return m_iInt;
	}

	/**
	 * Returns the string value of the integer
	 * 
	 * @return String representation of the integer
	 */
	public String toString() {
		return String.valueOf(m_iInt);
	}
	// {{DECLARE_CONTROLS
	// }}
}
