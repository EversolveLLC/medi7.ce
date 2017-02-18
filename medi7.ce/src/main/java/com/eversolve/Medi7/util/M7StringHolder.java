/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
**/
package com.eversolve.Medi7.util;

/**
 * This wrapper class is used for situations where a string is used as an in-out parameter
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * 
 * @com.register ( clsid=E158D7BC-B7E7-11D2-BA43-004005445EAC, typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
**/
public class M7StringHolder 
{
    /** Value held by this wrapper object
     */    
	private StringBuffer m_sString; 
	 
	/**
	 * Default constructor sets the string buffer to an empty value
	 */
	public M7StringHolder()
	{
		m_sString = new StringBuffer();;
	}
 
	/** Constructor which initializes the string buffer to the value provided
         * @param sString Object value
         */
	public M7StringHolder( String sString )
	{
	   m_sString = new StringBuffer(sString);
	}
 
	/** Sets the strings value with the value provided
         * @param sString Object value
         */
	public void setString( String sString )
	{
	   m_sString = new StringBuffer (sString);
	}
 
	/** Returns the length of the string buffer.
         * @return String length
         */
	public int length()
	{
	   return m_sString.length();  
	}
 
 
	/** Returns the string's value.
         * @return Object value
         */
	public String getStringValue()
	{
		 return m_sString.toString();
	}
	
	/**
	 * Append string
	 * @param string
	 */
	public void append(String string)
	{
		m_sString.append( string);
	}
	
	/**
	 * Append character
	 * @param ch
	 */
	public void append(char ch)
	{
		m_sString.append( ch);
	}
	public void append(int i)
	{
		m_sString.append( String.valueOf(i));
	}
	public void append(double i)
	{
		m_sString.append( String.valueOf(i));
	}
	
	public void clear()
	{
		m_sString.setLength(0);
	}
 
	public char charAt(int i)
	{
		return m_sString.charAt(i);
	}
	
	public String substring(int start)
	{
		return m_sString.substring(start);
	}
	
	public String substring(int start,int end)
	{
		return m_sString.substring(start,end);
	}
	
	public int indexOf(String str)
	{
		return m_sString.indexOf(str);
	}
	
	public int indexOf(String str, int pos)
	{
		return m_sString.indexOf(str, pos);
	}
	
	public int indexOf(char ch, int pos)
	{
		return m_sString.indexOf(String.valueOf(ch), pos);
	}
	public int indexOf(char ch)
	{
		return m_sString.indexOf(String.valueOf(ch));
	}
	/** Returns the string's value.
         * @return String value
         */
	public String toString()
	{
	   return m_sString.toString();
	}
}
