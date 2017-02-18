/**
/*	(c) Copyright 1998-2002 Eversolve, LLC
*/  

package com.eversolve.Medi7;

/**
 * Class M7Delimiters is used to store the message delimiters - hardcoded by the standard (ASTM, X12, etc.)  or detected at runtime (e.g. HL7)
 * HL7 provides that the delimiters may be set at runtime in MSH.2
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @value 1.0
 */
public class M7Delimiters
{
	/**
	 * Separates fields
	 */
	private char fieldDelimiter;
	/**
	 * Separates components of fields
	 */
	private char componentDelimiter;
	/**
	 * Separates repetitions of the the same field
	 */
	private char componentRepetitionDelimiter;
	/**
	 * Used to build escape sequences such as:
	 * <ul>
	 * <li> \F\	field separator</li> 
	 * <li> \S\	component separator</li> 
	 * <li> \T\	subcomponent separator</li> 
	 * <li> \R\	repetition separator</li> 
	 * <li> \E\	escape character</li> 
	 * </ul>
	 */
	private char escapeDelimiter;
	/**
	 * Separates sub-components
	 */
	private char subComponentDelimiter;
	/**
	 * @return Returns the componentDelimiter.
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0
	 * Change log: Initial Version Feb 2, 2006
	 */
	public char getComponentDelimiter() {
		return componentDelimiter;
	}
	/**
	 * @param componentDelimiter The componentDelimiter to set.
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0
	 * Change log: Initial Version Feb 2, 2006
	 */
	public void setComponentDelimiter(char componentDelimiter) {
		this.componentDelimiter = componentDelimiter;
	}
	/**
	 * @return Returns the componentRepetitionDelimiter.
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0
	 * Change log: Initial Version Feb 2, 2006
	 */
	public char getRepetitionDelimiter() {
		return componentRepetitionDelimiter;
	}
	/**
	 * @param componentRepetitionDelimiter The componentRepetitionDelimiter to set.
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0
	 * Change log: Initial Version Feb 2, 2006
	 */
	public void setRepetitionDelimiter(
			char componentRepetitionDelimiter) {
		this.componentRepetitionDelimiter = componentRepetitionDelimiter;
	}
	/**
	 * 	Provides the escape charaters to build escape sequences such as:
	 * <ul>
	 * <li> \F\	field separator</li> 
	 * <li> \S\	component separator</li> 
	 * <li> \T\	subcomponent separator</li> 
	 * <li> \R\	repetition separator</li> 
	 * <li> \E\	escape character</li> 
	 * </ul>
	 * @return Returns the escapeDelimiter.
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0
	 * Change log: Initial Version Feb 2, 2006
	 */
	public char getEscapeDelimiter() {
		return escapeDelimiter;
	}
	/**
	 * @param escapeDelimiter The escapeDelimiter to set.
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0
	 * Change log: Initial Version Feb 2, 2006
	 */
	public void setEscapeDelimiter(char escapeDelimiter) {
		this.escapeDelimiter = escapeDelimiter;
	}
	/**
	 * @return Returns the fieldDelimiter.
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0
	 * Change log: Initial Version Feb 2, 2006
	 */
	public char getFieldDelimiter() {
		return fieldDelimiter;
	}
	/**
	 * @param fieldDelimiter The fieldDelimiter to set.
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0
	 * Change log: Initial Version Feb 2, 2006
	 */
	public void setFieldDelimiter(char fieldDelimiter) {
		this.fieldDelimiter = fieldDelimiter;
	}
	/**
	 * @return Returns the subComponentDelimiter.
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0
	 * Change log: Initial Version Feb 2, 2006
	 */
	public char getSubComponentDelimiter() {
		return subComponentDelimiter;
	}
	/**
	 * @param subComponentDelimiter The subComponentDelimiter to set.
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0
	 * Change log: Initial Version Feb 2, 2006
	 */
	public void setSubComponentDelimiter(char subComponentDelimiter) {
		this.subComponentDelimiter = subComponentDelimiter;
	}
}

