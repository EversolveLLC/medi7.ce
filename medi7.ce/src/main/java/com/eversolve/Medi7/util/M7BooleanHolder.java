/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
**/
package com.eversolve.Medi7.util;

/**
 * This a wrapper class is used when a boolean is an out or in/out parameter. 
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * @com.register ( clsid=E158D7AD-B7E7-11D2-BA43-004005445EAC, typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 * 
**/
/**
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * <p>Class: M7BooleanHolder</p>
 * 
 */
public class M7BooleanHolder
{
    /** Holds the boolean value
     */    
	public boolean m_bBool = false; 
 
	/**
	 * Default constructor that will initialize the boolean to false
	 */
	public M7BooleanHolder()
	{}
	
	/** Constructor that will initialize the boolean to the value provided
         * @param bBool Value to be held by the boolean holder
         */
	public M7BooleanHolder( boolean bBool )
	{
		m_bBool = bBool;
	}
 
	/** Sets the boolean's value
         * @param bBool Value to be held by the boolean holder
         */
	public void setBoolean( boolean bBool )
	{
		m_bBool = bBool;  
	}

	/** Returns the boolean's value
         * @return Value held by the boolean holder
         */
	public boolean getValue()
	{
		return m_bBool;
	}
}
