//
//	(c) Copyright 1998-2006 Eversolve, LLC
//
package com.eversolve.Medi7.datatypes;

import com.eversolve.Medi7.M7;


/**
 * The M7PhoneNum class is used to format the telephone number <b>TN</b> datatype in HL7 
 * which has the form [NN] [(999)]999-9999[X99999][B99999][C any text].
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * @com.register ( clsid=E158D7B8-B7E7-11D2-BA43-004005445EAC, typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 */
public class M7PhoneNumber
{
    /**
     * Default constructor that set all pieces of the phone number to blank
     */
	public M7PhoneNumber() 
	{
	}
    
	/**
	 * Sets the pieces of the phone number from the string that must have the 
	 * format of [NN] [(999)]999-9999[X99999][B99999][C any text].
	 */
	public void fromString( String strValue ) 
	{
    	String strPhone = strValue;
    	int pos;

    	//search for a 'C' for the comments
    	pos = strPhone.indexOf( COMMENT_DELIM );
    	if( pos != M7.NPOS ) //found it
    	{
    		if( (pos + 1) < strPhone.length() ) //not at the end of the string
    			m_comments = strPhone.substring( pos + 1 );
    		
    		strPhone = strPhone.substring( 0, pos );
    	}

    	//search for a 'B' for the beeper
    	pos = strPhone.indexOf( BEEPER_DELIM );
    	if( pos != M7.NPOS ) //found it
    	{
    		if( (pos + 1) < strPhone.length() ) //not at the end of the string
    			m_beeper = strPhone.substring( pos  + 1 );
    		strPhone = strPhone.substring( 0, pos );
    	}

    	//search for a 'X' for the extension
    	pos = strPhone.indexOf( EXTENSION_DELIM );
    	if( pos != M7.NPOS ) //found it
    	{
    		if( (pos + 1) < strPhone.length() ) //not at the end of the string
    			m_extension = strPhone.substring( pos  + 1 );
    		strPhone = strPhone.substring( 0, pos );
    	}

    	//the trailing 8 characters should be the phone number 999-9999
    	if( strPhone.length() >= 8 )
    	{
    		m_phoneNumber = strPhone.substring( strPhone.length() - 8 );
    		strPhone = strPhone.substring( 0, strPhone.length() - 8 );
    	}
    	
    	//search for a '(' for an area code
    	pos = strPhone.indexOf( AREACODE_OPEN_PAREN );
    	if( pos != M7.NPOS ) //found it
    	{
    		if( (pos + 1) < strPhone.length() ) //not at the end of the string
    		{
    			m_areaCode = strPhone.substring( pos + 1 );
    			
    			//find the closing paren
    			int pos1 = m_areaCode.indexOf( AREACODE_CLOSE_PAREN );
    			if( pos1 != M7.NPOS ) //found it
    				m_areaCode = m_areaCode.substring( 0, pos1 );	
    		}
    		
    		strPhone = strPhone.substring( 0, pos );
    	}

    	//anything left is the country code
    	if( strPhone.length() >= 2 )
    		m_countryCode = strPhone.substring( 0, 2 );
    }
    
	/**
	 * Constructor that sets the pieces of the phone number from the string that must 
	 * have the format of [NN] [(999)]999-9999[X99999][B99999][C any text].
	 */
	public M7PhoneNumber( String strValue ) 
    {
		fromString( strValue );
	}


	/**
	 * Sets the country code of the phone number
	 */
	public void setCountryCode( String countryCode )
	{
		m_countryCode = countryCode;
	}
	
	/**
	 * Sets the area code of the phone number, the parenthesis may be omitted
	 */
	public void setAreaCode( String newValue )
	{
		m_areaCode = newValue;
	}
	
	/**
	 * Sets the phone number portion of the phone number and must be in the form 
	 * 999-9999
	 */
	public void setPhoneNumber( String newValue )
	{
		m_phoneNumber = newValue;
	}
	
	/**
	 * Sets the extension of the phone number, the "X" may be omitted
	 */
	public void setExtension( String newValue )
	{
		m_extension = newValue;
	}
	
	/**
	 * Sets the beeper portion of the phone number, the "B" may be omitted
	 */
	public void setBeeper( String newValue )
	{
		m_beeper = newValue;
	}
	
	/**
	 * Sets the comments portion of the phone number, the "C" may be omitted
	 */
	public void setComments( String newValue )
	{
		m_comments = newValue;
	}

    
	/**
	 * Returns the country code portion of the phone number
	 */
	public String getCountryCode()
	{
		return m_countryCode;
	}
	
	/**
	 * Returns the area code portion of the phone number
	 */
	public String getAreaCode()
	{
		return m_areaCode;
	}
	
	/**
	 * Returns the number portion of the phone number
	 */
	public String getPhoneNumber()
	{
		return m_phoneNumber;
	}
	
	/**
	 * Returns the extension portion of the phone number
	 */
	public String getExtension()
	{
		return m_extension;
	}
	
	/**
	 * Returns the beeper portion of the phone number
	 */
	public String getBeeper()
	{
		return m_beeper;
	}
	
	/**
	 * Returns the comments portion of the phone number
	 */
	public String getComments()
	{
		return m_comments;
	}

	
	/**
	 * Returns the HL7 string formatted representation of the phone number
	 * the country code portion of the phone number in the format 
	 * [NN] [(999)]999-9999[X99999][B99999][C any text].
	 */
	public String getStringValue()
    {
    	String strPhone;
    	int pos;

    	strPhone = m_countryCode;
    	
    	//there is an area code but if no parans than add them
    	if( m_areaCode.length() > 0 )
    	{
    		pos = m_areaCode.indexOf( AREACODE_OPEN_PAREN );
    		if( pos == M7.NPOS ) //not found so add it
    			strPhone += AREACODE_OPEN_PAREN;
    	
    		strPhone += m_areaCode;

    		//make sure there is closing paren
    		pos = m_areaCode.indexOf( AREACODE_CLOSE_PAREN );
    		if( pos == M7.NPOS ) //not found so add it
    			strPhone += AREACODE_CLOSE_PAREN;			
    	}
    	
    	strPhone += m_phoneNumber;

    	//there is an extension but no X to mark where it starts,
    	//so insert an X and add the extension
    	if( m_extension.length() > 0 ) 
    	{
    		if( m_extension.charAt(0) != EXTENSION_DELIM )
    			strPhone += EXTENSION_DELIM;

    		strPhone += m_extension;		
    	}

    	//there is a beeper but no B to mark where it starts,
    	//so insert a B and add the beeper
    	if( m_beeper.length() > 0 ) 
    	{
    		if( m_beeper.charAt(0) != BEEPER_DELIM )
    			strPhone += BEEPER_DELIM;

    		strPhone += m_beeper;		
    	}

    	
    	//there is a comment but no C to mark where it starts,
    	//so insert a C and add the comment.  Note that if the comment
    	//starts with a "C" we check the next letter and if it is also a C then
    	//we do nothing.  One of the leading C's will be stripped off later
    	//  "CCall after 3:00" would become "Call after 3:00"
    	//  "Call after 3:00" would become "Call after 3:00"
    	//  "Will not be home" would become "CWill not be home"
    	//We strip out the leading C when we unstream into parts
    	if( m_comments.length() > 0 ) 
    	{
    		if( m_comments.charAt(0) != COMMENT_DELIM ) //just append because first char is not a C
    			strPhone += COMMENT_DELIM;
    		else	//the first char is a C so check the next and if not a C then append
    			if( m_comments.length() >= 2 ) 
    				if( m_comments.charAt(1) != COMMENT_DELIM ) //the second char is not a C
    					strPhone += COMMENT_DELIM;
    			
    		strPhone += m_comments;
    	}

    	return strPhone;
    }
	
	/**
	 * Returns the HL7 string formatted representation of the phone number
	 * the country code portion of the phone number in the format 
	 * [NN] [(999)]999-9999[X99999][B99999][C any text].
	 */
	public String toString()
	{
		return getStringValue();
	}

    //Member variables
    protected String  m_countryCode;
	protected String  m_areaCode ;
	protected String  m_phoneNumber ;
	protected String  m_extension ;
	protected String  m_beeper ;
	protected String  m_comments ;
	
	protected static char AREACODE_OPEN_PAREN = '(';
    protected static char AREACODE_CLOSE_PAREN = ')'; 
    protected static char EXTENSION_DELIM = 'X';
    protected static char BEEPER_DELIM = 'B';
    protected static char COMMENT_DELIM = 'C';

	//{{DECLARE_CONTROLS
	//}}
}