/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
**/

package com.eversolve.Medi7;

import com.eversolve.Medi7.datatypes.M7Date;
import com.eversolve.Medi7.datatypes.M7DateTime;
import com.eversolve.Medi7.datatypes.M7PhoneNumber;
import com.eversolve.Medi7.datatypes.M7Time;
import com.eversolve.Medi7.util.M7StringHolder;

/**
 * The M7Field object is used to maintain the value and state of a single field 
 * within a message.  All values are held as a string and the appropriate conversion 
 * is done when a get/set is performed.  None of the methods on M7Field will throw 
 * an exception unless an inappropriate conversion is performed such as retrieving a string as a long.
 * There is currently no testing to determine that your field's value 
 * is really of the type being asked for.  You can query the type if necessary.
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 2.4
 * @com.register ( clsid=E158D7B2-B7E7-11D2-BA43-004005445EAC, typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 */
public class M7Field extends M7Composite
{
    /**
     * Constant value for the HL7 null string "".
     */
	protected static final String HL7_NULL_STRING = "\"\""; //$NON-NLS-1$
    
    protected M7Field( String partName, int compType, M7Composite pParent ) throws M7Exception
    {
        super( partName, compType, pParent );
    	m_state = eFldEmpty;	
    	//{{INIT_CONTROLS
		//}}
    }
    
	/**
	 * Indicates that this object is of type M7Field.
	 * 
	 * @return Always returns true.
	 */
	public boolean isField() 
	{
	    return true;
	}

	/**
	 * Sets the field's value with the string representation from the M7DateTime
	 * parameter.  Currently no checking is done to verify the data type
	 * matches the definition.
	 * 
	 * @param newValue The M7DateTime used to set this field's value.
	 */
	public void setValue( M7DateTime newValue ) 
	{
	    this.setValue( newValue.getStringValue() );
	}
	
	/**
	 * Sets the field's value with the string representation from the M7Date
	 * parameter.  Currently no checking is done to verify the data type
	 * matches the definition.
	 * 
	 * @param newValue The M7Date used to set this field's value.  
	 */
	public void setValue( M7Date newValue ) 
	{
	    this.setValue( newValue.getStringValue() ); 
	}
	
	/**
	 * Sets the field's value with the string representation from the M7time
	 * parameter.  Currently no checking is done to verify the data type
	 * matches the definition.
	 * 
	 * @param newValue The M7Time used to set this field's value.  
	 */
    public void setValue( M7Time newValue ) 
    {
         this.setValue( newValue.getStringValue() );
    }
    
	/**
	 * Sets the field's value with the string representation from the M7PhoneNum
	 * parameter.  Currently no checking is done to verify the data type
	 * matches the definition.
	 * 
	 * @param newValue The M7PhoneNum used to set this field's value.  
	 */
	public void setValue( M7PhoneNumber newValue ) 
	{
	    this.setValue( newValue.getStringValue() ); 
	}
	
	/**
	 * Sets the field's value to the HL7 null value of "".
	 */
	public void setNull()
	{
		m_value = HL7_NULL_STRING;
		m_state = eFldNull;
	}

	/**
	 * Sets the field's value to nothing.
	 */
	public void setEmpty()
	{
		m_value = M7.EMPTY_STRING;
		m_state = eFldEmpty;
	}

	/**
	 * Returns the field's current state.
	 * 
	 * @return One of the following values:
	 * 	 eFldNotExist = 0
	 *   eFldNull     = 1
	 *   eFldEmpty    = 2
	 *   eFldPresent  = 3
	 */
	public int getState() 
	{
	    return m_state;
	}
		
	/**
	 * Returns an indication if the field's current state is null.
	 * 
	 * @return True if the current state is null, otherwise false.
	 */
	public boolean isNull() 
	{	
	    return( m_state == eFldNull );	
	}

	/**
	 * Returns an indication if the field's current state is empty.
	 * 
	 * @return True if the current state is empty, otherwise false.
	 */
	public boolean isEmpty() 
	{ 
	    return( m_state == eFldEmpty ); 
	}

	/**
	 * Returns the field's value as a string.
	 * 
	 * @return A string representation of the field's current value.
	 */
	public String getValue() 
	{
	    return m_value;
	};

	/**
	 * Returns the field's value as a long.  May cause a Java numeric exception
	 * if the field's value cannot be converted to a number.
	 * 
	 * @return A numeric representation of the field's current value.
	 */
	public long getValueAsLong()
	{
		return Long.valueOf( m_value).longValue();
	}

	/**
	 * Returns the field's value as a long.  May cause a Java numeric exception
	 * if the field's value cannot be converted to a decimal number.
	 * 
	 * @return A decimal numeric representation of the field's current value.
	 */
	public double getValueAsDouble()
	{
		return Double.valueOf( m_value ).doubleValue();
    }
    

	/**
	 * Sets the field's value to the string provided.  If the value is equal to "" then 
	 * the field's state is set to eFldNull.  If the value has a string with no length
	 * then the field's state is set to eFldEmpty.  Otherwise it is set to eFldPresent
	 * and the field's value is set appropriately.
	 * 
	 * @param newValue New value for this field.
	 */
	public void setValue( String newValue )
    {
    	m_value = newValue;
    	if( m_value.length() > 0  )
    		if( m_value.equals( HL7_NULL_STRING ) )
    			this.setNull();
    		else
    			m_state = eFldPresent;
    	else
    		m_state = eFldEmpty;
    }

	/**
	 * Sets the field's value using the value of the long provided.
	 * Currently no checking is done to verify the data type
	 * matches the definition.
	 * 
	 * @param newValue New value for this field.
	 */
    public void setValue( long newValue )
    {
    	this.setValue( String.valueOf( newValue ) );
    }

	/**
	 * Sets the field's value using the value of the double provided.
	 * Currently no checking is done to verify the data type
	 * matches the definition.
	 * 
	 * @param newValue New value for this field.
	 */
    public void SetValue( double newValue )
    {	
    	this.setValue( String.valueOf( newValue ) );
    }

    protected void _Stream( M7StringHolder h_msgStream, M7Delimiters delimeters, M7StringHolder h_delimiterStack  )
    {
    	//add the value
    	if( m_state == eFldPresent )
    	{
    		h_msgStream.append(h_delimiterStack.toString());
    		h_msgStream.append( m_value);
    		return;
    	}
    	else
    	//null field in HL7 is passed as empty quotes
    	if( m_state == eFldNull )
    	{
    		h_msgStream.append( h_delimiterStack.toString());
    		h_msgStream.append(HL7_NULL_STRING);
    		return;
    	}

    }

    protected void _Stream_CRLF( M7StringHolder h_msgStream, M7Delimiters delimeters, M7StringHolder h_delimiterStack  )
    {
    	//add the value
    	if( m_state == eFldPresent )
    	{
    		h_msgStream.append( h_delimiterStack.toString());
    		h_msgStream.append( m_value);
    		return;
    	}
    	else
    	//null field in HL7 is passed as empty quotes
    	if( m_state == eFldNull )
    	{
    		h_msgStream.append( h_delimiterStack.toString());
    		h_msgStream.append( HL7_NULL_STRING);
    		return;
    	}

    }

    protected boolean _Unstream( M7StringHolder h_msgStream, M7Delimiters delimiters )
    {
    	//when we get to this point the stream only contains the field value

    	//set the value and state appropriately
    	this.setValue( h_msgStream.toString() );
    	
    	//if this is empty then return false
    	if( this.isEmpty() )
    		return false;

    	return true;
    }

	/**
	 * Returns the field's value as an M7DateTime object.  
	 * Currently no checking is done to verify the data type
	 * matches the definition.
	 * 
	 * @return The value represented in an M7DateTime object
	 * @exception M7Exception is the value is not parsable
	 */
	public M7DateTime getValueAsDateTime() throws M7Exception
	{
		M7DateTime dttm = new M7DateTime( m_value );
		return dttm;
	}

	/**
	 * Returns the field's value as an M7Date object.  
	 * Currently no checking is done to verify the data type
	 * matches the definition.
	 * 
	 * @return The value represented in an M7Date object
	 */
	public M7Date getValueAsDate() throws M7Exception
	{
		M7Date dt = null;
		dt = new M7Date( m_value );
		return dt;
	}

	/**
	 * Returns the field's value as an M7Time object.  
	 * Currently no checking is done to verify the data type
	 * matches the definition.
	 * 
	 * @return The value represented in an M7Time object
	 * @exception if the string is not parsable
	 */
	public M7Time getValueAsTime()throws M7Exception
	{
		M7Time t = new M7Time( m_value );
		return t;
	}

	/**
	 * Returns the field's value as an M7PhoneNum object.  
	 * Currently no checking is done to verify the data type
	 * matches the definition.
	 * 
	 * @return The value represented in an M7PhoneNum object
	 */
	public M7PhoneNumber getValueAsPhoneNumber()
	{
		M7PhoneNumber pn = new M7PhoneNumber( m_value );
		return pn;
	}
	
	/**
	 * Returns the field's data type as configured in the message definition file
	 * 
	 * @return One of the following values:
	 * 	eM7String = 1
	 *  eM7Long = 2
	 *  eM7Double = 3
	 *  eM7DateTime = 4
	 *  eM7Date = 5
	 *  eM7Time = 6
	 *  eM7PhoneNum = 7
	 */
	public int getFieldType()
	{
		return definition.getFieldType();
	}


	/**
	 * Constant accessor for use in COM clients
	 * @return String containing a pari of double quotes ("").
	 */
	public String HL7_NULL_STRING()
	{
		return HL7_NULL_STRING;
	}
	
	//
	// field data types
	//
	/**
	 * Constant accessor for use in COM clients
	 * @return 0
	 */
	public int FLD_TYPE_NOT_FLD()
	{
		return M7MessageDefinition.eM7NotAField;
	}
	
	/**
	 * Constant accessor for use in COM clients
	 * @return 1
	 */
	public int FLD_TYPE_STRING()
	{
		return M7MessageDefinition.eM7String;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 2
	 */
	public int FLD_TYPE_LONG()
	{
		return M7MessageDefinition.eM7Long;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 3
	 */
	public int FLD_TYPE_DOUBLE()
	{
		return M7MessageDefinition.eM7Double;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 4
	 */
	public int FLD_TYPE_DATETIME()
	{
		return M7MessageDefinition.eM7DateTime;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 5
	 */
	public int FLD_TYPE_DATE()
	{
		return M7MessageDefinition.eM7Date;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 6
	 */
	public int FLD_TYPE_TIME()
	{
		return M7MessageDefinition.eM7Time;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 7
	 */
	public int FLD_TYPE_PHONE_NUM()
	{
		return M7MessageDefinition.eM7PhoneNum;
	}	
	
	//
	// field states added for COM build
	//
	/**
	 * Constant accessor for use in COM clients
	 * @return 0
	 */
	public int FLD_STATE_NOT_EXIST()
	{
		return eFldNotExist;
	}
	
	/**
	 * Constant accessor for use in COM clients
	 * @return 1
	 */
	public int FLD_STATE_NULL()
	{
		return eFldNull;
	}
	
	/**
	 * Constant accessor for use in COM clients
	 * @return 2
	 */
	public int FLD_STATE_EMPTY()
	{
		return eFldEmpty;
	}

	/**
	 * Constant accessor for use in COM clients
	 * @return 3
	 */
	public int FLD_STATE_PRESENT()
	{
		return eFldPresent;
	}

	//Member variables
    String m_value ;
	int m_state;

	public final static int eFldNotExist       = 0;
	public final static int eFldNull           = 1;
	public final static int eFldEmpty          = 2;
	public final static int eFldPresent        = 3;
    
}
