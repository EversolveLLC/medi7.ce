/**
*	(c) Copyright 1998-2006 Eversolve, LLC
*/

package com.eversolve.Medi7;

import com.eversolve.Medi7.resources.Messages;

/**
 * This class is used for exception handling in the Medi7 library.  
 * Following are the error codes:
 * <ul>
 * <li>	eUndefined = 0; </li>
 * <li> eInvalidChildSpecified = 1; </li>
 * <li> eMissingIndexOnRepeat = 2; </li>
 * <li> eOutOfRange = 3; </li>
 * <li> eAbsNameParsingError = 4; </li>
 * <li>	eNotARepeat = 5; </li>
 * <li> eNotAField = 6; </li>
 * <li> eFieldNotFound = 7; </li>
 * <li> eUnstreamParsingError = 8; </li>
 * <li> eInvalidEncodingChars = 9; </li>
 * <li> eUnableToLocateChildDef = 10; </li>
 * <li> eMsgDefParsingError = 11; </li>
 * <li> eUnableToOpenDefFile = 12; </li>
 * <li> eUnableToFindKeyInDefFile = 13; </li>
 * <li>	eCannotCallCreateChildOnRepeat = 14; </li>
 * <li>	eRepeatBeyondMax = 15; </li>
 * <li>	eOffsetError = 16; </li>
 * <li>	eProfileNotFound = 17; </li>
 * <li>	eEvalCopyExpired = 18; </li>
 *	</ul>
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 2.4
 * @com.register ( clsid=6C077E74-BEE6-11D2-BA49-004005445EAC, typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 */
public class M7Exception extends Exception {
	/**
	 * <b>serialVersionUID</b> set to default: 1
	 */
	private static final long serialVersionUID = 1L;
	
	/** Creates an exception with an error message indicated by the string
	 * @param sMessage Error message
	 * @param iCode Integer, error code
	 */
	public M7Exception(String sMessage) {
		super(sMessage);
		reason = sMessage;
		code = eUndefined;
	}


	/** Creates an exception of the type specified by the integer, with an error message indicated by the string
	 * @param sMessage Error message
	 * @param iCode Integer, error code
	 */
	public M7Exception(String sMessage, int iCode) {
		super(sMessage);
		reason = sMessage;
		code = iCode;
	}

	/** Returns a string representation of the error
	 *  This method is replaced by @see #getMessage()
	 * @return String representation of the error
	 * @deprecated
	 */
	public String getStringValue() {
		return toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage()
	{
		return reason;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage()
	{
		return getMessage();
	}
	/** Returns the error code
	 * @return Integer, error code
	 */
	public int getErrorCode() {
		return code;
	}

	/** Returns a string representation of the error
	 * @return String representation of the error
	 */
	public String toString() {
		return Messages.getString("M7Exception.ERROR_PREFIX") + code + Messages.getString("M7Exception.PREFIX_DELIM") + reason; //$NON-NLS-1$ //$NON-NLS-2$
	}

	//
	// error codes, instance based to be accessed from COM client
	//
	/**
	 * Constant accessor for use in COM clients
	 * @return 0.
	 */
	public int ERR_UNDEF() {
		return M7Exception.eUndefined;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 1.
	 */
	public int ERR_INVALID_CHILD_SPECIFIED() {
		return M7Exception.eInvalidChildSpecified;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 2.
	 */
	public int ERR_MISSING_RPT_INDEX() {
		return M7Exception.eMissingIndexOnRepeat;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 3.
	 */
	public int ERR_OUT_OF_RANGE() {
		return M7Exception.eOutOfRange;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 4.
	 */
	public int ERR_ABS_NAME_PARSE_ERR() {
		return M7Exception.eAbsNameParsingError;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 5.
	 */
	public int ERR_NOT_A_REPEAT() {
		return M7Exception.eNotARepeat;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 6.
	 */
	public int ERR_NOT_A_FIELD() {
		return M7Exception.eNotAField;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 7.
	 */
	public int ERR_FIELD_NOT_FOUND() {
		return M7Exception.eFieldNotFound;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 8.
	 */
	public int ERR_MSG_PARSING_ERROR() {
		return M7Exception.eUnstreamParsingError;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 9.
	 */
	public int ERR_INVALID_ENCODING_CHARS() {
		return M7Exception.eInvalidEncodingChars;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 10.
	 */
	public int ERR_INVALID_CHILD_DEF() {
		return M7Exception.eUnableToLocateChildDef;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 11.
	 */
	public int ERR_MSG_DEF_PARSING_ERROR() {
		return M7Exception.eMsgDefParsingError;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 12.
	 */
	public int ERR_UNABLE_TO_OPEN_DEF_FILE() {
		return M7Exception.eUnableToOpenDefFile;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 13.
	 */
	public int ERR_UNABLE_TO_FIND_KEY_IN_DEF_FILE() {
		return M7Exception.eUnableToFindKeyInDefFile;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 14.
	 */
	public int ERR_CANNOT_CALL_CREATE_CHILD_ON_REPEAT() {
		return M7Exception.eCannotCallCreateChildOnRepeat;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 15.
	 */
	public int ERR_REPEAT_BEYOND_MAX() {
		return M7Exception.eRepeatBeyondMax;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 16.
	 */
	public int ERR_OFFSET_ERROR() {
		return M7Exception.eOffsetError;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 17.
	 */
	public int ERR_PROFILE_NOT_FOUND() {
		return M7Exception.eProfileNotFound;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 18.
	 */
	public int ERR_EVAL_EXPIRED() {
		return M7Exception.eEvalCopyExpired;
	}
	
	/**
	 * Constant accessor for use in COM clients
	 * @return 18.
	 */
	public int ERR_STD_NOT_SUPPORTED() {
		return M7Exception.eEvalCopyExpired;
	}

	/** Reason for this error
	 */
	private String reason;

	/** Error code
	 */
	private int code;

	/** Element undefined
	 */
	public static final int eUndefined = 0;

	/** Invalid child element
	 */
	public static final int eInvalidChildSpecified = 1;

	/** invalid or missing message index for repeated elements
	 */
	public static final int eMissingIndexOnRepeat = 2;

	/** Index out of range
	 */
	public static final int eOutOfRange = 3;

	/** Unable to parse the absolute name of an element
	 */
	public static final int eAbsNameParsingError = 4;

	/** The element is not repeated
	 */
	public static final int eNotARepeat = 5;

	/** The element is not a field
	 */
	public static final int eNotAField = 6;

	/** The field could not be found
	 */
	public static final int eFieldNotFound = 7;

	/** Parsing error due to invalid or missing delimiters
	 */
	public static final int eUnstreamParsingError = 8;

	/** Parsing error due to invalid delimiters
	 */
	public static final int eInvalidEncodingChars = 9;

	/** Missing a child element definition
	 */
	public static final int eUnableToLocateChildDef = 10;

	/** M7Message definition file could not be parsed
	 */
	public static final int eMsgDefParsingError = 11;

	/** The message definition file could not be open
	 */
	public static final int eUnableToOpenDefFile = 12;

	/** Unable to find a key in the definition file for a specific element
	 */
	public static final int eUnableToFindKeyInDefFile = 13;

	/** Unable to create a repeated child element
	 */
	public static final int eCannotCallCreateChildOnRepeat = 14;

	/** Invalid (overflow) index
	 */
	public static final int eRepeatBeyondMax = 15;

	/** Invalid element offset value
	 */
	public static final int eOffsetError = 16;

	/** M7Message profile definition for this element could not be found
	 */
	public static final int eProfileNotFound = 17;

	/** Evaluation copy expired
	 */
	public static final int eEvalCopyExpired = 18;

	/** Evaluation copy expired
	 */
	public static final int eStandardNotSupported = 19;
	/**
	 * Use #getMessage() instead
	 * @return Returns the reason for the error.
	 * @deprecated Use #getMessage() instead
	 */
	public String getReason() {
		return reason;
	}
}