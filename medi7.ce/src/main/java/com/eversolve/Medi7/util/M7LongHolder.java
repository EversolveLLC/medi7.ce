/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
**/
package com.eversolve.Medi7.util;

/**
 * This a wrapper class is used when a long is an in-out parameter
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * 
**/
public class M7LongHolder 
{
    /** Long value
     */    
 public long m_lLong = 0; 
 
 /** Constructor
  * @param lLong Value to be held by the wrapper object
  */ 
 public M7LongHolder( long lLong )
 {
    m_lLong = lLong;
 }
 
 /** Sets the value of the object
  * @param lLong Value to be held by the wrapper object
  */ 
 public void setLong( long lLong )
 {
    m_lLong = lLong;  
 }
 
 /** Returns the value of the object
  * @return Long value of the object
  */ 
 public long getLongValue()
 {
    return m_lLong;
 }
 
 /** Returns the string value of the object
  * @return String value of the object
  */ 
 public String toString()
 {
    return String.valueOf(m_lLong);
 } 
	//{{DECLARE_CONTROLS
	//}}
}
