/**
*	(c) Copyright 1998-2006 Eversolve, LLC
*/ 
package com.eversolve.Medi7;

import com.eversolve.Medi7.resources.Messages;

/**
 * Class M7 contains constants used throughout the package
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.1
 */
public class M7
{    
	/**
	 *  <code>NPOS</code> 
	 */
	public static final int NPOS = -1;
	/**
	 * Default <code>EMPTY_STRING</code> used for empty fields, etc.
	 */
	public static String EMPTY_STRING = ""; //$NON-NLS-1$
	/**
	 * HL7 syntax specific <code>SEGMENT_END_CHAR</code>
	 */
	public static char SEGMENT_END_CHAR = 0x0D;
	
	/** Added for compatibility with systems that expect CR/LF combinations
	*  as end-of-line delimiters (i.e., Windows systems)
	**/
	public static String SEGMENT_END_CHAR_CRLF = Messages.getString("M7.SEGMENT_END"); //$NON-NLS-1$
	
	/**
	 * '-1' indicates that there are no repetitions for repeated element <code>NO_MAX_RPT</code>
	 */
	public static final long NO_MAX_RPT = -1;	
	
	/** 
	 * Set following boolean to "true" for evaluation versions, false
	 * otherwise */
	protected static final boolean EVALUATION = false;
}
