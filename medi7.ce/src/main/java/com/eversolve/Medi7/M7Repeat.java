/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7;

import java.util.Enumeration;
import java.util.Vector;

import com.eversolve.Medi7.datatypes.M7Date;
import com.eversolve.Medi7.datatypes.M7DateTime;
import com.eversolve.Medi7.datatypes.M7PhoneNumber;
import com.eversolve.Medi7.datatypes.M7Time;
import com.eversolve.Medi7.resources.Messages;
import com.eversolve.Medi7.util.M7BooleanHolder;
import com.eversolve.Medi7.util.M7StringHolder;

/**
 * The repeat class acts as a container for multiple instances of the same
 * message object type. This can be a Segment Group, a Segment, a M7Composite
 * Field or a Field. The size of the repeat will grow automatically if necessary
 * when certain methods are called like setFieldValue. Any methods in M7Repeat
 * always use a zero-based index. Repeats are always put into the message stream
 * in the order in which they appear in the repeat.
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code> 
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 2.3
 * @com.register ( clsid=E158D7BA-B7E7-11D2-BA43-004005445EAC,
 *               typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 */
public class M7Repeat extends M7Composite {
	protected M7Repeat(M7StringHolder h_partName, int compType,
			M7Composite pParent) throws M7Exception {
		super(h_partName.toString(), compType, pParent);
	}

	//override from base class
	/**
	 * This is overridden from the base class and ALWAYS throws an exception
	 * because this method cannot be called when the M7Composite is a repeat.
	 * You must call Add or Grow to create an instance of a repeat item. Also,
	 * when calling any method that will set a field's values (i.e.
	 * M7Compisite::setFieldValue) and the absolute name contains an index for a
	 * repeat item then the repeat will automatically grow to accommodate that
	 * index.
	 * 
	 * @exception M7Exception
	 *                Thrown because this method cannot be called on a M7Repeat.
	 */
	public M7Composite newChild(String absoluteName,
			M7BooleanHolder h_alreadyExists) throws M7Exception {
		//throw exception because this cannot be called on a repeat, you must
		// call add, grow, or insert
		String strError = Messages
				.getString("M7Repeat.INVALID_CREATECHILD_INVOCATION"); //$NON-NLS-1$
		throw new M7Exception(strError,
				M7Exception.eCannotCallCreateChildOnRepeat);
	}

	//override from base class
	/**
	 * Overridden from base class and indicates that this object is of type
	 * M7Repeat.
	 * 
	 * @return Always returns true because this object is a M7Repeat.
	 */
	public boolean isRepeat() {
		return true;
	}

	/**
	 * Appends an additional instance to the end of this repeat.
	 * 
	 * @return Returns a reference to the newly added M7Composite which may be
	 *         of type M7Composite or M7Field.
	 * 
	 * @exception M7Exception
	 *                Thrown if an element cannot be added to the repeat.
	 */
	public M7Composite add() throws M7Exception {
		//add is essentially grow by one then return the last instance
		this.grow(1);
		return this.getItemAt(this.getSize() - 1);
	}

	//overidden from base class
	/**
	 * Overridden from the base class because the behavior needed to delete an
	 * object from a repeat is different than that of an M7Composite.
	 * 
	 * @param msgComponent
	 *            The composite object to remove from the repeat.
	 * @return True indicates that the object to be deleted was removed from the
	 *         repeat, otherwise false is returned.
	 */
	public boolean deleteChild(M7Composite msgComponent) {
		//this will delete the item pointed to in the vector and
		//rather than setting the vector item to null, actually remove
		//that vector item from the collection
		for (int i = 0; i < m_partList.size(); i++) {
			Object o = m_partList.elementAt(i);

			if (o == null)
				continue;

			if (o == msgComponent) {
				m_partList.removeElementAt(i);
				return true;
			}
		}

		return false;
	}

	/**
	 * Indicates the number of elements in the repeat.
	 * 
	 * @return The number of elements in the repeat.
	 */
	public long getSize() {
		return m_partList.size();
	}

	/**
	 * Overridden from base class and indicates the number of elements in the
	 * repeat. Returns the same value as GetSize().
	 * 
	 * @return The number of elements in the repeat.
	 */
	public long getChildCount() {
		return getSize();
	}

	/**
	 * Retrieves the element of the repeat at the indicated position
	 * (zero-based).
	 * 
	 * @param position
	 *            The index of the element in the repeat to return.
	 * @return A M7Composite which may be of type M7Composite or M7Field.
	 * @exception M7Exception
	 *                Thrown if the position is out of range.
	 */
	public M7Composite getItemAt(long position) throws M7Exception {
		return this.getChildByPosition(position);
	} //exception if pos > size

	/**
	 * Adds the desired number of instances to the end of the repeat.
	 * 
	 * @param howMany
	 *            the number of elements to add to the repeat.
	 * 
	 * @exception M7Exception
	 *                Thrown if the repeat cannot grow to the desired size.
	 */
	public void grow(long howMany) throws M7Exception {
		int myType = this.getType();
		//add the number desired to the repeat
		for (int i = 0; i < howMany; i++) {
			if (myType == M7Composite.eM7Field)
				_CreateField(new M7StringHolder(this.getName()), myType);
			else
				_CreateComposite(new M7StringHolder(this.getName()), myType);
		}
	}

	//
	// These versions of get/set field value are useful if the repeat points to
	// a composite
	//
	/**
	 * All of the SetRepCompositesFieldValue methods are useful to use if the
	 * repeat contains elements of type M7Composite. For instance, if this is a
	 * repeat for NK1 segments and you had a reference to this repeat, you could
	 * set the SetId field as follows:
	 * 
	 * nk1Rep.SetRepCompositesFieldValue( 2, "SetId", 1L );
	 * 
	 * or to set the FamilyName field of the PersonName composite field would
	 * be:
	 * 
	 * nk1Rep.SetRepCompositesFieldValue( 2, "Name.FamilyName", "MASSIE" );
	 * 
	 * If the position value is greater than the current size of the repeat, the
	 * repeat will automatically grow to accommodate the position.
	 * 
	 * @param position
	 *            The index of the element of the repeat you wish to set the
	 *            value for.
	 * @param absoluteName
	 *            The full path name of the field relative to this repeat for
	 *            which you wish to set the value.
	 * @param newValue
	 *            The value to set the field.
	 * 
	 * @exception M7Exception
	 *                Thrown if the absoluteName does not specify a field.
	 */
	public void setRepeatedCompositesValue(long position, String absoluteName,
			String newValue) throws M7Exception {
		//check the range and if out of range grow the repeat so it will be in
		// range
		if (position >= this.getSize())
			this.grow((position + 1) - this.getSize());

		//get the child at the specified position,
		//throws exception if out of range but this should not happen since we
		// just made sure of the size
		M7Composite pComp = this.getItemAt(position);

		//call the version on M7Composite
		pComp.setFieldValue(absoluteName, newValue);
	}

	/**
	 * Identical to #setRepeatedCompositesValue( long, String, String ) but sets
	 * the field's value with the long provided.
	 * 
	 * @exception M7Exception
	 *                Thrown if the absoluteName does not specify a field.
	 * @param position
	 *            Field offset
	 * @param absoluteName
	 *            Absolute path name
	 * @param newValue
	 *            Element value
	 */
	public void setRepeatedCompositesValue(long position, String absoluteName,
			long newValue) throws M7Exception {
		//convert the data then call the string version
		this.setRepeatedCompositesValue(position, absoluteName, String
				.valueOf(newValue));
	}

	/**
	 * Identical to #setRepeatedCompositesValue( long, String, String ) but sets
	 * the field's value with the double provided.
	 * 
	 * @param position
	 *            Field offset
	 * @param absoluteName
	 *            Absolute path name
	 * @param newValue
	 *            Element value
	 * @exception M7Exception
	 *                Thrown if the absoluteName does not specify a field.
	 */
	public void setRepeatedCompositesValue(long position, String absoluteName,
			double newValue) throws M7Exception {
		//convert the data then call the string version
		this.setRepeatedCompositesValue(position, absoluteName, String
				.valueOf(newValue));
	}

	/**
	 * Identical to #setRepeatedCompositesValue( long, String, String ) but sets
	 * the field's value with the string value of the M7DateTime provided.
	 * 
	 * @param position
	 *            Field offset
	 * @param absoluteName
	 *            Absolute path name
	 * @param newValue
	 *            Element value
	 * @exception M7Exception
	 *                Thrown if the absoluteName does not specify a field.
	 */
	public void setRepeatedCompositesValue(long position, String absoluteName,
			M7DateTime newValue) throws M7Exception {
		//convert the data then call the string version
		this.setRepeatedCompositesValue(position, absoluteName, newValue
				.getStringValue());
	}

	/**
	 * Identical to #setRepeatedCompositesValue( long, String, String ) but sets
	 * the field's value with the string value of the M7Date provided.
	 * 
	 * @param position
	 *            Field offset
	 * @param absoluteName
	 *            Absolute path name
	 * @param newValue
	 *            Element value
	 * @exception M7Exception
	 *                Thrown if the absoluteName does not specify a field.
	 */
	public void setRepeatedCompositesValue(long position, String absoluteName,
			M7Date newValue) throws M7Exception {
		//convert the data then call the string version
		this.setRepeatedCompositesValue(position, absoluteName, newValue
				.getStringValue());
	}

	/**
	 * Identical to SetRepCompositesFieldValue( long, String, String ) but sets
	 * the field's value with the string value of the M7Time provided.
	 * 
	 * @param position
	 *            Field offset
	 * @param absoluteName
	 *            Absolute path name
	 * @param newValue
	 *            Element value
	 * @exception M7Exception
	 *                Thrown if the absoluteName does not specify a field.
	 */
	public void setRepeatedCompositesValue(long position, String absoluteName,
			M7Time newValue) throws M7Exception {
		//convert the data then call the string version
		this.setRepeatedCompositesValue(position, absoluteName, newValue
				.getStringValue());
	}

	/**
	 * Identical to #setRepeatedCompositesValue( long, String, String ) but sets
	 * the field's value with the string value of the M7PhoneNum provided.
	 * 
	 * @param position
	 *            Field offset
	 * @param absoluteName
	 *            Absolute path name
	 * @param newValue
	 *            Element value
	 * @exception M7Exception
	 *                Thrown if the absoluteName does not specify a field.
	 */
	public void setRepeatedCompositesValue(long position, String absoluteName,
			M7PhoneNumber newValue) throws M7Exception {
		//convert the data then call the string version
		this.setRepeatedCompositesValue(position, absoluteName, newValue
				.getStringValue());
	}

	/**
	 * Identical to SetRepCompositesFieldValue( long, String, String ) but sets
	 * the field's value to the HL7 empty state.
	 * 
	 * @param position
	 *            Field offset
	 * @param absoluteName
	 *            Absolute path name
	 * @exception M7Exception
	 *                Thrown if the absoluteName does not specify a field.
	 */
	public void setRepeatedCompositesEmpty(long position, String absoluteName)
			throws M7Exception {
		//check the range and if out of range grow the repeat so it will be in
		// range
		if (position >= this.getSize())
			this.grow((position + 1) - this.getSize());

		//must be in range so get the one at the position
		//throws exception if out of range but this should not happen since we
		// just made sure of the size
		M7Composite pComp = this.getItemAt(position);

		pComp.setFieldEmpty(absoluteName);
	}

	/**
	 * Identical to SetRepCompositesFieldValue( long, String, String ) but sets
	 * the field's value with the HL7 null value - a pair of double quotes ("").
	 * 
	 * @param position
	 *            Field offset
	 * @param absoluteName
	 *            Absolute path name
	 * @exception M7Exception
	 *                Thrown if the absoluteName does not specify a field.
	 */
	public void setRepeatedCompositesNull(long position, String absoluteName)
			throws M7Exception {
		//check the range and if out of range grow the repeat so it will be in
		// range
		if (position >= this.getSize())
			this.grow((position + 1) - this.getSize());

		//get the child at the specified position,
		//throws exception if out of range but this should not happen since we
		// just made sure of the size
		M7Composite pComp = this.getItemAt(position);

		//call the version on M7Composite
		pComp.setFieldNull(absoluteName);
	}

	/**
	 * This will return the state of the field specified. If the field has not
	 * yet been created then it will return eFldNotExist.
	 * 
	 * @param position
	 *            The index of the element of the repeat that you want to
	 *            retreive the field's state.
	 * @param absoluteName
	 *            Must reference a field in the message that is relative to this
	 *            repeat.
	 * @return An int that corresponds to one of the following:
	 *         M7Field.eFldNotExist = 0 this occurs if position is out of range
	 *         M7Field.eFldNull = 1 M7Field.eFldEmpty = 2 M7Field.eFldPresent =
	 *         3
	 * 
	 * @exception M7Exception
	 *                Thrown if the element specified by the name is not a
	 *                field.
	 */
	public int getRepeatedCompositesState(long position, String absoluteName)
			throws M7Exception {
		//check the range and if out of range then does not exist
		if (position >= this.getSize())
			return M7Field.eFldNotExist;

		M7Composite pComp = this.getItemAt(position);

		//call the version on M7Composite
		return pComp.getFieldState(absoluteName);
	}

	/**
	 * All of the GetRepCompositesFieldValue methods will return the contents of
	 * the field specified by the absolute name. If there is a possibility that
	 * the field may not exist in the message then you should first check to see
	 * if it exists by calling GetField and ensure that it does not return a
	 * null pointer, or call GetFieldState which returns an indication of
	 * whether or not the field exists.
	 * 
	 * @position The index of the repeat element for which you wish to retreive
	 *           the field's value.
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @return The value of the field as a String.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field object being asked for does not exist,
	 *                the position is out of range, or the absoluteName does not
	 *                correspond to a M7Field.
	 */
	public String getRepeatedCompositesValue(long position, String absoluteName)
			throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//call the version of this function on M7Composite
		return pComp.getFieldValue(absoluteName);
	}

	/**
	 * This is identical to M7Repeat.GetRepCompositesFieldValue but returns the
	 * field's value as a long. No datatype checking is currently done to ensure
	 * that the conversion can occur, a Java conversion exception may be thrown
	 * in this case.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field object being asked for does not exist,
	 *                the position is out of range, or the absoluteName does not
	 *                correspond to a M7Field.
	 */
	public long getRepeatedCompositesValueAsLong(long position,
			String absoluteName) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//call the version of this function on M7Composite
		return pComp.getFieldValueAsLong(absoluteName);
	}

	/**
	 * This is identical to M7Repeat.GetRepCompositesFieldValue but returns the
	 * field's value as a double. No datatype checking is currently done to
	 * ensure that the conversion can occur, a Java conversion exception may be
	 * thrown in this case.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field object being asked for does not exist,
	 *                the position is out of range, or the absoluteName does not
	 *                correspond to a M7Field.
	 */
	public double getRepeatedCompositesValueAsDouble(long position,
			String absoluteName) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//call the version of this function on M7Composite
		return pComp.getFieldValueAsDouble(absoluteName);
	}

	/**
	 * This is identical to M7Repeat.GetRepCompositesFieldValue but returns the
	 * field's value as a M7DateTime. No datatype checking is currently done to
	 * ensure that the conversion can occur, you may get an erroneous datetime
	 * if the field's string value is not in the proper HL7 format.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field object being asked for does not exist,
	 *                the position is out of range, or the absoluteName does not
	 *                correspond to a M7Field.
	 */
	public M7DateTime getRepeatedCompositesValueAsDateTime(long position,
			String absoluteName) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//call the version of this function on M7Composite
		return pComp.getFieldValueAsDateTime(absoluteName);
	}

	/**
	 * This is identical to M7Repeat.GetRepCompositesFieldValue but returns the
	 * field's value as a M7Date. No datatype checking is currently done to
	 * ensure that the conversion can occur, you may get an erroneous date if
	 * the field's string value is not in the proper HL7 format.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field object being asked for does not exist,
	 *                the position is out of range, or the absoluteName does not
	 *                correspond to a M7Field.
	 */
	public M7Date getRepeatedCompositesValueAsDate(long position,
			String absoluteName) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//call the version of this function on M7Composite
		return pComp.getFieldValueAsDate(absoluteName);
	}

	/**
	 * This is identical to M7Repeat.GetRepCompositesFieldValue but returns the
	 * field's value as a M7Time. No datatype checking is currently done to
	 * ensure that the conversion can occur, you may get an erroneous date if
	 * the field's string value is not in the proper HL7 format.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field object being asked for does not exist,
	 *                the position is out of range, or the absoluteName does not
	 *                correspond to a M7Field.
	 */
	public M7Time getRepeatedCompositesValueAsTime(long position,
			String absoluteName) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//call the version of this function on M7Composite
		return pComp.getFieldValueAsTime(absoluteName);
	}

	/**
	 * This is identical to M7Repeat.GetRepCompositesFieldValue but returns the
	 * field's value as a M7PhoneNum. No datatype checking is currently done to
	 * ensure that the conversion can occur, you may get an erroneous date if
	 * the field's string value is not in the proper HL7 format.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field object being asked for does not exist,
	 *                the position is out of range, or the absoluteName does not
	 *                correspond to a M7Field.
	 */
	public M7PhoneNumber getRepeatedCompositesValueAsPhoneNumber(long position,
			String absoluteName) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//call the version of this function on M7Composite
		return pComp.getFieldValueAsPhoneNumber(absoluteName);
	}

	//
	// These versions of set field value are used when the repeat contains a
	// repeating field
	// (note: position is always 0 based)
	//
	/**
	 * All of the SetRepFieldValue methods are useful to use if the repeat
	 * contains elements of type M7Field. For instance, if this is a repeat for
	 * PID.HomePhone and you had a reference to this repeat, you could set the
	 * home phone numbers as follows:
	 * 
	 * pidHomePhoneRep.SetRepFieldValue( 0, "(860)555-1111" );
	 * pidHomePhoneRep.SetRepFieldValue( 1, "(860)555-2222" );
	 * 
	 * If the position value is greater than the current size of the repeat, the
	 * repeat will automatically grow to accommodate the position.
	 * 
	 * @param position
	 *            The index of the element of the repeat you wish to set the
	 *            value for.
	 * @param newValue
	 *            The value to set the field.
	 * @exception M7Exception
	 *                Thrown if the repeat is not for a field
	 */
	public void setRepeatedValue(long position, String newValue)
			throws M7Exception {
		//check the range and if out of range grow the repeat so it will be in
		// range
		if (position >= this.getSize())
			this.grow((position + 1) - this.getSize());

		//get the child at the specified position,
		//throws exception if out of range but this should not happen since we
		// just made sure of the size
		M7Composite pComp = this.getItemAt(position);

		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);

		pField.setValue(newValue);
	}

	/**
	 * Identical to SetRepFieldValue( long, String ) but sets the field's value
	 * with the long provided.
	 * 
	 * @exception M7Exception
	 *                Thrown if the repeat is not for a field
	 */
	public void setRepeatedValue(long position, long newValue)
			throws M7Exception {
		//convert the data then call the string version
		this.setRepeatedValue(position, String.valueOf(newValue));
	}

	/**
	 * Identical to SetRepFieldValue( long, String ) but sets the field's value
	 * with the double provided.
	 * 
	 * @exception M7Exception
	 *                Thrown if the repeat is not for a field
	 */
	public void setRepeatedValue(long position, double newValue)
			throws M7Exception {
		//convert the data then call the string version
		this.setRepeatedValue(position, String.valueOf(newValue));
	}

	/**
	 * Identical to SetRepFieldValue( long, String ) but sets the field's value
	 * with the string representation of the M7DateTime provided.
	 * 
	 * @exception M7Exception
	 *                Thrown if the repeat is not for a field
	 */
	public void setRepeatedValue(long position, M7DateTime newValue)
			throws M7Exception {
		this.setRepeatedValue(position, newValue.getStringValue());
	}

	/**
	 * Identical to SetRepFieldValue( long, String ) but sets the field's value
	 * with the string representation of the M7Date provided.
	 * 
	 * @exception M7Exception
	 *                Thrown if the repeat is not for a field
	 */
	public void setRepeatedValue(long position, M7Date newValue)
			throws M7Exception {
		this.setRepeatedValue(position, newValue.getStringValue());
	}

	/**
	 * Identical to SetRepFieldValue( long, String ) but sets the field's value
	 * with the string representation of the M7Time provided.
	 * 
	 * @exception M7Exception
	 *                Thrown if the repeat is not for a field
	 */
	public void setRepeatedValue(long position, M7Time newValue)
			throws M7Exception {
		this.setRepeatedValue(position, newValue.getStringValue());
	}

	/**
	 * Identical to SetRepFieldValue( long, String ) but sets the field's value
	 * with the string representation of the M7PhoneNum provided.
	 * 
	 * @exception M7Exception
	 *                Thrown if the repeat is not for a field
	 */
	public void setRepeatedValue(long position, M7PhoneNumber newValue)
			throws M7Exception {
		this.setRepeatedValue(position, newValue.getStringValue());
	}

	/**
	 * Identical to SetRepFieldValue( long, String ) but sets the field's value
	 * to the HL7 empty state.
	 * 
	 * @exception M7Exception
	 *                Thrown if the repeat is not for a field
	 */
	public void setRepeatedEmpty(long position) throws M7Exception {
		//check the range and if out of range grow the repeat so it will be in
		// range
		if (position >= this.getSize())
			this.grow((position + 1) - this.getSize());

		//must be in range so get the one at the position
		//throws exception if out of range but this should not happen since we
		// just made sure of the size
		M7Composite pComp = this.getItemAt(position);

		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);

		pField.setEmpty();
	}

	/**
	 * Identical to SetRepFieldValue( long, String ) but sets the field's value
	 * with the HL7 null value - a pair of double quotes ("").
	 * 
	 * @exception M7Exception
	 *                Thrown if the repeat is not for a field
	 */
	public void setRepeatedNull(long position) throws M7Exception {
		//check the range and if out of range grow the repeat so it will be in
		// range
		if (position >= this.getSize())
			this.grow((position + 1) - this.getSize());

		//get the child at the specified position,
		//throws exception if out of range but this should not happen since we
		// just made sure of the size
		M7Composite pComp = this.getItemAt(position);

		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);

		pField.setNull();
	}

	/**
	 * This will return the state of the field at the specified position.
	 * 
	 * @param position
	 *            The index of the element of the repeat that you want to
	 *            retreive the field's state.
	 * @return An int that corresponds to one of the following:
	 *         M7Field.eFldNotExist = 0 returned if position is out of range
	 *         M7Field.eFldNull = 1 M7Field.eFldEmpty = 2 M7Field.eFldPresent =
	 *         3
	 * 
	 * @exception M7Exception
	 *                Thrown if the repeat is not for a field.
	 */
	public int getRepeatedState(long position) throws M7Exception {
		//check the range and if out of range then does not exist
		if (position >= this.getSize())
			return M7Field.eFldNotExist;

		M7Composite pComp = this.getItemAt(position);

		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);

		return pField.getState();
	}

	/**
	 * All of the GetRepFieldValue methods will return the contents of the field
	 * specified by the absolute name. If there is a possibility that the field
	 * may not exist in the message then you should first check to see if it
	 * exists by calling GetField and ensure that it does not return a null
	 * pointer, or call GetFieldState which returns an indication of whether or
	 * not the field exists.
	 * 
	 * @position The index of the repeat element for which you wish to retreive
	 *           the field's value.
	 * @return The value of the field as a String.
	 * 
	 * @exception M7Exception
	 *                Thrown if the position is out of range, or repeat does not
	 *                contain fields.
	 */
	public String getRepeatedValue(long position) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);

		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);

		//get the value from the field
		return pField.getValue();
	}

	/**
	 * This is identical to M7Repeat.GetRepFieldValue but returns the field's
	 * value as a long. No datatype checking is currently done to ensure that
	 * the conversion can occur, a java conversion exception may be thrown in
	 * this case.
	 * 
	 * @exception M7Exception
	 *                Thrown if the position is out of range, or repeat does not
	 *                contain fields.
	 * @param position
	 *            Indicates the field position/offset
	 */
	public long getRepeatedValueAsLong(long position) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);
		//get the value from the field
		return pField.getValueAsLong();
	}

	/**
	 * This is identical to M7Repeat.GetRepFieldValue but returns the field's
	 * value as a double. No datatype checking is currently done to ensure that
	 * the conversion can occur, a Java conversion exception may be thrown in
	 * this case.
	 * 
	 * @exception M7Exception
	 *                Thrown if the position is out of range, or repeat does not
	 *                contain fields.
	 * @param position
	 *            Indicates the field position/offset
	 */
	public double getRepeatedValueAsDouble(long position) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);
		//get the value from the field
		return pField.getValueAsDouble();
	}

	/**
	 * This is identical to M7Repeat.GetRepFieldValue but returns the field's
	 * value as a M7DateTime. No datatype checking is currently done to ensure
	 * that the conversion can occur, you may get an erroneous datetime if the
	 * field's string value is not in the proper HL7 format.
	 * 
	 * @exception M7Exception
	 *                Thrown if the position is out of range, or repeat does not
	 *                contain fields.
	 * @param position
	 *            Indicates the field position/offset
	 * @return M7DateTime
	 */
	public M7DateTime getRepeatedValueAsDateTime(long position)
			throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);
		//get the value from the field
		return pField.getValueAsDateTime();
	}

	/**
	 * This is identical to M7Repeat.GetRepFieldValue but returns the field's
	 * value as a M7Date. No datatype checking is currently done to ensure that
	 * the conversion can occur, you may get an erroneous datetime if the
	 * field's string value is not in the proper HL7 format.
	 * 
	 * @exception M7Exception
	 *                Thrown if the position is out of range, or repeat does not
	 *                contain fields.
	 * @param position
	 *            Indicates the field position/offset
	 * @return M7Date object
	 */
	public M7Date getRepeatedValueAsDate(long position) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);
		//get the value from the field
		return pField.getValueAsDate();
	}

	/**
	 * This is identical to M7Repeat.GetRepFieldValue but returns the field's
	 * value as a M7Time. No datatype checking is currently done to ensure that
	 * the conversion can occur, you may get an erroneous datetime if the
	 * field's string value is not in the proper HL7 format.
	 * 
	 * @exception M7Exception
	 *                Thrown if the position is out of range, or repeat does not
	 *                contain fields.
	 * @return M7Time object
	 * @param position
	 *            Indicates the field position/offset
	 */
	public M7Time getRepeatedValueAsTime(long position) throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);
		//get the value from the field
		return pField.getValueAsTime();
	}

	/**
	 * This is identical to M7Repeat.GetRepFieldValue but returns the field's
	 * value as a M7PhoneNum. No datatype checking is currently done to ensure
	 * that the conversion can occur, you may get an erroneous datetime if the
	 * field's string value is not in the proper HL7 format.
	 * 
	 * @exception M7Exception
	 *                Thrown if the position is out of range, or repeat does not
	 *                contain fields.
	 */
	public M7PhoneNumber getRepeatedValueAsPhoneNumber(long position)
			throws M7Exception {
		//call GetChild on M7Composite which throws exception if out of range
		M7Composite pComp = this.getChildByPosition(position);
		//ensure this is a field
		M7Field pField = _ValidateisField(pComp);
		//get the value from the field
		return pField.getValueAsPhoneNumber();
	}

	protected void _Stream(M7StringHolder h_msgStream,
			M7Delimiters delimiters, M7StringHolder h_delimiterStack)
			throws M7Exception {
		//repeats just stream parts in order
		Vector<M7Composite> pPartList = this._GetChildList();
		for (Enumeration<M7Composite> e = pPartList.elements(); e.hasMoreElements();) {
			//hold the old size to see if it changed
			int priorSize = h_msgStream.length();

			M7Composite c = (M7Composite) e.nextElement();

			c._Stream(h_msgStream, delimiters, h_delimiterStack);

			//check for a change to see if we should reset the delimiter stack
			if (priorSize != h_msgStream.length()) {
				h_delimiterStack.clear();
			}
			//append the repeat delimiter unconditionally
			//if this is a repeat segment or segment group then the
			// delim stack is reset anyway.
			h_delimiterStack.append( delimiters.getRepetitionDelimiter());
		}
	}

	protected void _Stream_CRLF(M7StringHolder h_msgStream,
			M7Delimiters delimiters, M7StringHolder h_delimiterStack)
			throws M7Exception {
		//repeats just stream parts in order
		Vector<M7Composite> pPartList = this._GetChildList();
		for (Enumeration<M7Composite> e = pPartList.elements(); e.hasMoreElements();) {
			//hold the old size to see if it changed
			int priorSize = h_msgStream.length();

			M7Composite c = (M7Composite) e.nextElement();

			c._Stream_CRLF(h_msgStream, delimiters, h_delimiterStack);

			//check for a change to see if we should reset the delimiter stack
			if (priorSize != h_msgStream.length())
				h_delimiterStack.clear();

			//append the repeat delimiter unconditionally
			//if this is a repeat segment or segment group then the
			// delim stack is reset anyway.
			h_delimiterStack.append( delimiters.getRepetitionDelimiter());
		}
	}

	protected boolean _Unstream(M7StringHolder h_msgStream,
			M7Delimiters delimiters) throws M7Exception {
		switch (this.getType()) {
		case M7Composite.eM7SegmentGrp:
		case M7Composite.eM7Segment:
			return _UnstreamSegRpt(h_msgStream, delimiters);

		case M7Composite.eM7CompositeField:
		case M7Composite.eM7Field:
			return _UnstreamFldRpt(h_msgStream, delimiters);

		default:
			_ThrowUnstreamError(h_msgStream.toString(), Messages
					.getString("M7Repeat.INCORRECT_TYPE")); //$NON-NLS-1$
		}

		return false; //Keep compiler happy
	}

	protected boolean _UnstreamSegRpt(M7StringHolder h_msgStream,
			M7Delimiters delimiters) throws M7Exception {
		if (h_msgStream.length() == 0)
			return false;

		//just create repeats of this type until it is no longer needed
		while (true) {
			M7Composite pComp = this.add();
			if (!pComp._Unstream(h_msgStream, delimiters)) {
				//clean up the last child we created
				this.deleteChild(pComp);
				break;
			}
		}
		if (this.getSize() > 0)
			return true;

		return false;
	}

	protected boolean _UnstreamFldRpt(M7StringHolder h_msgStream,
			M7Delimiters delimiters) throws M7Exception {
		//build a string of delims to look for
		char delim;
		delim = delimiters.getRepetitionDelimiter();

		//parse through the string looking for a repeat delimiter
		//for each one found, create an instance of the repeat type
		//and ask it to unstream
		int escapedIndex = -1;
		int pos = 0;
		String temp = M7.EMPTY_STRING;
		while (true) {
			//get the next part
			if (escapedIndex == -1) {
				pos = h_msgStream.indexOf(delim);
			} else {
				temp = h_msgStream.substring(escapedIndex + 1,
						h_msgStream.length());
				pos = temp.indexOf(delim);
			}

			//see if it is an escaped delimiter value
			if (pos != 0 && pos != M7.NPOS)
				if (h_msgStream.charAt(pos - 1) == delimiters
						.getEscapeDelimiter()) {
					//remember the index
					escapedIndex = pos;
					continue;
				}

			if (escapedIndex != -1)
				pos = pos + escapedIndex + 1;
			String compPart =null;

			if (pos == M7.NPOS) {
				compPart = h_msgStream.substring(0);
			} else {

				compPart = h_msgStream.substring(0, pos);
			}

			if (compPart.length() > 0) {
				M7Composite pComp = this.add();
				M7StringHolder h_compPart = new M7StringHolder(compPart);
				if (!pComp._Unstream(h_compPart, delimiters)) {
					//clean up the last child we created
					this.deleteChild(pComp);
					break;
				}
			}

			//we hit the end of the string
			if (pos == M7.NPOS)
				break;

			//strip off the message stream the piece we just processed
			h_msgStream.setString( h_msgStream.substring(pos + 1));
			escapedIndex = -1;
		}

		if (this.getSize() > 0)
			return true;

		return false;
	}

	protected void _AddChild(M7Composite pMsgComponent) {
		m_partList.addElement(pMsgComponent);
	}

	//override from base class so we add the subscript
	protected boolean _BuildAbsoluteName(M7StringHolder h_absoluteName,
			M7Composite pChildComp) {
		//do the base class version
		super._BuildAbsoluteName(h_absoluteName, pChildComp);

		//tack on the subscript
		if (pChildComp != null) {
			h_absoluteName.append(Messages
					.getString("M7Repeat.OPEN_BRACKET")); //$NON-NLS-1$
			for (int i = 0; i < m_partList.size(); i++) {
				if (m_partList.elementAt(i) == pChildComp) {
					h_absoluteName.append(i);
					break;
				}
			}
			h_absoluteName.append(Messages
					.getString("M7Repeat.CLOSE_BRACKET")); //$NON-NLS-1$
		}

		return true;
	}
}