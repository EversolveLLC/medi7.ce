/**
 /*	(c) Copyright 1998-2006 Eversolve, LLC
 */

package com.eversolve.Medi7;

import java.util.Enumeration;
import java.util.Vector;

import com.eversolve.Medi7.datatypes.M7Date;
import com.eversolve.Medi7.datatypes.M7DateTime;
import com.eversolve.Medi7.datatypes.M7PhoneNumber;
import com.eversolve.Medi7.datatypes.M7Time;
import com.eversolve.Medi7.resources.Messages;
import com.eversolve.Medi7.util.M7BooleanHolder;
import com.eversolve.Medi7.util.M7LongHolder;
import com.eversolve.Medi7.util.M7StringHolder;

/**
 * This is the base class for all objects that represent a part of a message
 * (see object model).
 * 
 * It provides all of the basic functions for getting and setting data from a
 * message as well as traversing the message object graph. This class is not
 * created through its constructor but rather through a parent object by calling
 * Create...() or setFieldValue...() functions. For instance, once a message is
 * created you can call newChild() to get a segment or a segment group that is
 * defined for that message and it will return a reference to an M7Composite. <br/>
 * <code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * 
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 2.3
 * @com.register ( clsid=3ABC3898-5D19-11D4-BBDE-004005445EAC,
 *               typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 */
public class M7Composite {
	/**
	 * Default constructor, it creates a message (type eM7Message)
	 * 
	 * @see #eM7Message
	 */
	protected M7Composite() {
		m_pParent = null;
		m_compType = eM7Message;
		definition = null;
		m_pStreamedData = null;
		m_pDelimiters = null;
	}

	/**
	 * Constructor for creating a specific type of composite
	 * 
	 * @param compType
	 *            Indicates whether the composite element is created as field,
	 *            composite field, segment, segment group, or message. eM7Field,
	 *            eM7CompositeField, eM7Segment, eM7SegmentGrp, eM7Message
	 * @see M7Composite#eM7Message
	 * @see M7Composite#eM7SegmentGrp
	 * @see M7Composite#eM7Segment
	 * @see M7Composite#eM7CompositeField
	 * @see M7Composite#eM7Field
	 */
	protected M7Composite(int compType) {
		m_compType = compType;
		m_pParent = null;
		definition = null;
		m_pStreamedData = null;
		m_pDelimiters = null;
	}

	protected M7Composite(String partName, int compType, M7Composite Parent)
			throws M7Exception {
		m_compType = compType;
		m_pParent = Parent;
		definition = null;
		m_pStreamedData = null;
		m_pDelimiters = null;

		// Add this component into the parent component if there is one.
		if (Parent != null) {
			// set the msg def pointer in this component from the parents
			// for repeats use the parents definition directly, for non repeats
			// get the appropriate child definition
			if (Parent.isRepeat()) {
				definition = Parent._GetMsgDef();
			} else {
				definition = Parent._GetMsgDef().getChild(partName);
			}
		}
	}

	//
	// Composition creation and accessors
	//
	/**
	 * This will return a pointer to a M7Composite. The composite may be a
	 * repeat or a field depending on how it is defined in the configuration
	 * file. You can test the type by using isRepeat or isField methods. You can
	 * also use the CreateRepeat or CreateField methods if you know what type is
	 * created and you do not want to cast the return type. Note that you cannot
	 * call this method on a M7Repeat type of composite.
	 * 
	 * @param absoluteName
	 *            This string specifies the name of the child composite you want
	 *            to create. If the absolute name includes a repeat index(es)
	 *            then the repeat is automatically grown to the appropriate size
	 *            if necessary.
	 * @exception M7Exception
	 *                Thrown if the name specified is not defined in the message
	 *                definition relative to this composite.
	 * @return Child composite
	 * @param h_alreadyExists
	 *            This boolean indicates whether the child may already be
	 *            present
	 */
	public M7Composite newChild(String absoluteName,
			M7BooleanHolder h_alreadyExists) throws M7Exception {
		// validate we can create
		if (!definition.isChild(absoluteName)) {
			// not a valid child of this component so throw exception
			String strError = Messages
					.getString("M7Composite.INVALID_CHILD_ERROR") + absoluteName; //$NON-NLS-1$
			throw new M7Exception(strError, M7Exception.eInvalidChildSpecified);
		}

		M7StringHolder h_absoluteName = new M7StringHolder(absoluteName);

		// parse the name passed in to get the piece identifying my child's name
		M7StringHolder h_nameRemainder = new M7StringHolder(M7.EMPTY_STRING);
		M7LongHolder h_rptIndex = new M7LongHolder(M7.NPOS);
		String myChildName = M7Composite._ParseAbsoluteName(h_absoluteName,
				h_nameRemainder, h_rptIndex);
		String nameRemainder = h_nameRemainder.toString();
		long rptIndex = h_rptIndex.getLongValue();
		boolean isAbsName = false;
		if (nameRemainder.length() > 0) {
			isAbsName = true;
		}

		// if the child to create is a repeat, and we are given an absolute
		// name with no index specified then we have an error
		if (definition.isChildRepeat(myChildName) && rptIndex == M7.NPOS // no
				// position
				// given
				&& isAbsName) {
			String strError = Messages.getString("M7Composite.MISSING_INDEX") + h_absoluteName.toString(); //$NON-NLS-1$
			throw new M7Exception(strError, M7Exception.eMissingIndexOnRepeat);
		}

		// assume we will find it
		if (h_alreadyExists != null) {
			h_alreadyExists.setBoolean(true);
		}

		// see if child named part already exists
		M7Composite pChild = getChild(absoluteName);
		if (pChild != null) {
			return pChild;
		}

		// did not find it
		if (h_alreadyExists != null) {
			h_alreadyExists.setBoolean(false);
		}

		return _NewChild(new M7StringHolder(myChildName), h_nameRemainder,
				rptIndex);

	}

	/**
	 * 
	 * @param h_myChildName
	 * @param h_nameRemainder
	 * @param rptIndex
	 * @return
	 * @throws M7Exception
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 1.0 Change log: Initial Version Feb 2, 2006
	 */
	protected M7Composite _NewChild(M7StringHolder h_myChildName,
			M7StringHolder h_nameRemainder, long rptIndex) throws M7Exception {
		M7Composite pChild = null;

		pChild = getChild(h_myChildName.toString());

		// no child with this name yet so create one of appropriate type
		if (pChild == null) {
			// create it
			int childType = definition.getChildType(h_myChildName.toString());
			if (definition.isChildRepeat(h_myChildName.toString())) {
				pChild = (M7Composite) _CreateRepeat(h_myChildName, childType);
			} else if (definition.isChildField(h_myChildName.toString())) {
				pChild = (M7Composite) _CreateField(h_myChildName, childType);
			} else {
				pChild = (M7Composite) _CreateComposite(h_myChildName,
						childType);
			}
		}

		// if the is a repeat, and we are given an absolute
		// name with no index specified then we have an error
		if (pChild.isRepeat() && rptIndex == M7.NPOS // no position given
				&& h_nameRemainder.length() > 0) {
			String strError = Messages.getString("M7Composite.MISSING_INDEX") + h_myChildName; //$NON-NLS-1$
			throw new M7Exception(strError, M7Exception.eMissingIndexOnRepeat);
		}

		// if we have an index then get the one at the specified position
		if (rptIndex != M7.NPOS) {
			// check that the child type is a repeat
			M7Repeat pRptChild = _ValidateisRepeat(pChild);

			// see if there are enough elements in the repeat,
			// if not then grow it so the index specifed will now be available
			if (rptIndex >= pRptChild.getSize()) {
				pRptChild.grow((rptIndex + 1) - pRptChild.getSize());
			}

			// reassign the child pointer to appropriate instance of the repeat
			pChild = pRptChild.getChildByPosition(rptIndex);
		}

		// if still have some name left then continue creating
		if (h_nameRemainder.length() > 0) {
			M7LongHolder h_rptIndex = new M7LongHolder(M7.NPOS);
			M7StringHolder h_remainder = new M7StringHolder(M7.EMPTY_STRING);
			String myChildName = M7Composite._ParseAbsoluteName(
					h_nameRemainder, h_remainder, h_rptIndex);
			return pChild._NewChild(new M7StringHolder(myChildName),
					h_remainder, h_rptIndex.getLongValue());
		}

		return pChild;
	}

	/**
	 * Indicates whether this object is of type M7Repeat.
	 * 
	 * @return True if this object is an M7Repeat, false if not.
	 */
	public boolean isRepeat() {
		return false;
	}

	/**
	 * Indicates whether this object is of type M7Field.
	 * 
	 * @return True if this object is an M7Field, false if not.
	 */
	public boolean isField() {
		return false;
	}

	/**
	 * Returns the Parent M7Composite object.
	 * 
	 * @return M7Composite object that may be of type M7Repeat or M7Field.
	 *         Returns null if called on the M7Message object since the
	 *         M7Message object has no parent
	 */
	public M7Composite getParent() {
		return m_pParent;
	}

	/**
	 * The number of child elements allowed under this composite, this does not
	 * include the number of descendants below each direct child. If this is a
	 * repeat then it is the number of elements that exist in the repeat.
	 * 
	 * @return The number of child elements allowed under this composite.
	 */
	public long getChildCount() {
		return definition.getChildCount();
	}

	protected M7MessageDefinition _GetMsgDef() {
		return definition;
	}

	protected M7Repeat _CreateRepeat(M7StringHolder h_partName, int childType)
			throws M7Exception {
		M7Repeat pRep = new M7Repeat(h_partName, childType, this);
		_AddChild(pRep);
		return pRep;
	}

	protected M7Composite _CreateComposite(M7StringHolder h_partName,
			int childType) throws M7Exception {
		M7Composite pComp = new M7Composite(h_partName.toString(), childType,
				this);
		_AddChild(pComp);
		return pComp;
	}

	protected M7Field _CreateField(M7StringHolder h_partName, int childType)
			throws M7Exception {
		M7Field pFld = new M7Field(h_partName.toString(), childType, this);
		_AddChild(pFld);
		return pFld;
	}

	/**
	 * This will return a M7Composite if the one specified in the absoluteName
	 * already exists, otherwise it returns null (which may also occur if the
	 * name passed in is not a valid child under this composite) . You can test
	 * the returned M7Composite by using isRepeat or isField methods. You can
	 * also use the GetRepeat or GetField methods if you know what type is to be
	 * returned and you do not want to cast the return type.
	 * 
	 * @param absoluteName
	 *            This string specifies the name of the child composite you want
	 *            to retrieve.
	 * @return null or an M7Composite refrerence of type M7Composite, M7Repeat,
	 *         or M7Field.
	 * @exception M7Exception
	 *                Thrown if the absoluteName is not formed correctly.
	 * 
	 * @see M7Composite#isRepeat
	 * @see M7Composite#isField
	 * @see M7Composite#GetRepeat
	 * @see M7Composite#GetField
	 */
	public M7Composite getChild(String absoluteName) throws M7Exception {
		// someone is asking for a child component and if we have a
		// piece of the stream that has not been parsed then unparse it now
		if (m_pStreamedData != null) {
			_UnparseTheSegment();
		}

		// the name could be an absoulte name so break it down
		M7StringHolder h_absoluteName = new M7StringHolder(absoluteName);
		M7StringHolder h_nameRemainder = new M7StringHolder(M7.EMPTY_STRING); //$NON-NLS-1$
		M7LongHolder h_rptIndex = new M7LongHolder(M7.NPOS);

		String myChildName = M7Composite._ParseAbsoluteName(h_absoluteName,
				h_nameRemainder, h_rptIndex);
		String nameRemainder = h_nameRemainder.toString();
		long rptIndex = h_rptIndex.getLongValue();

		boolean isAbsName = false;
		if (nameRemainder.length() > 0) {
			isAbsName = true;
		}

		// look for child by name in my collection
		for (Enumeration<M7Composite> e = m_partList.elements(); e
				.hasMoreElements();) {
			M7Composite pChild = (M7Composite) e.nextElement();

			if (pChild == null) {
				continue;
			}

			String name = pChild.getName();
			if (myChildName.equalsIgnoreCase(name)) {
				// does the name have an index, if so then reassign the child to
				// the appropriate instance,
				// assuming that the index is in range
				if (rptIndex != M7.NPOS) {
					if (rptIndex < pChild.m_partList.size() && rptIndex >= 0) {
						pChild = pChild.getChildByPosition(rptIndex);
					} else {
						break;
					}
				}

				// found but may need to continue if we still have a name
				// remiander
				if (isAbsName) {
					return pChild.getChild(h_nameRemainder.toString());
				} else {
					return pChild;
				}
			}
		}

		// never found a match
		return null;
	}

	/**
	 * This will return the name of the M7Composite. All composite objects in
	 * the message graph are identified by name. The name comes from the message
	 * definition file.
	 * 
	 * @return Name of the composite
	 */
	public String getName() {
		return definition.getName();
	}

	/**
	 * This will return a pointer to a M7Composite if the one specified by the
	 * position already exists, otherwise it returns null. You can test the
	 * returned M7Composite by using isRepeat or isField methods. You can also
	 * use the GetRepeat or GetField methods if you know what type is to be
	 * returned and you do not want to cast the return type.
	 * 
	 * @param position
	 *            Specifies the position of the child composite you want to
	 *            retrieve.
	 * @return null or an M7Composite reference of type M7Composite, M7Repeat,
	 *         or M7Field.
	 * @exception M7Exception
	 *                Thrown if the position is out of range.
	 * 
	 * @see M7Composite#isRepeat
	 * @see M7Composite#isField
	 * @see M7Composite#GetRepeat
	 * @see M7Composite#GetField
	 */
	public M7Composite getChildByPosition(long position) throws M7Exception {
		// someone is aksing for a child component and if we have a
		// piece of the stream that has not been parsed then unparse it now
		if (m_pStreamedData != null) {
			_UnparseTheSegment();
		}

		// the part list may never have been initialized yet
		// so that means there are no child objects
		if (m_partList.size() == 0) {
			return null;
		}

		if (position < getChildCount() && position >= 0) {
			return (M7Composite) m_partList.elementAt((int) position);
		}

		String strError = Messages.getString("M7Composite.OUT_OF_RANGE_1") + getChildCount() + Messages.getString("M7Composite.OUT_OF_RANGE_2") + position; //$NON-NLS-1$ //$NON-NLS-2$
		throw new M7Exception(strError, M7Exception.eOutOfRange);
	}

	protected void _AddChild(M7Composite pMsgComponent) throws M7Exception {
		// first check to see if we have initialized our child collection
		if (m_partList.size() == 0) {
			// load child collection with null pointers
			m_partList.setSize((int) definition.getChildCount());
		}

		// System.out.println( "Adding: " + pMsgComponent.getName() + " to pos:"
		// + pMsgComponent.m_pMsgDef.getOffset() );

		// if the offset we need to place this component into is larger
		// than the number of children or if we try adding the child to
		// the same position already used then this is an exception
		if (pMsgComponent.definition.getOffset() > this.getChildCount()
				|| m_partList.elementAt((int) pMsgComponent.definition
						.getOffset()) != null) {
			String strError = Messages
					.getString("M7Composite.CANNOT_ADD_CHILD") + pMsgComponent.getName() + Messages.getString("M7Composite.AT_OFFSET") + pMsgComponent.definition.getOffset(); //$NON-NLS-1$ //$NON-NLS-2$
			throw new M7Exception(strError, M7Exception.eOffsetError);
		}

		// put the new child in the correct spot based on his offset
		m_partList.setElementAt(pMsgComponent, (int) pMsgComponent.definition
				.getOffset());
	}

	protected Vector<M7Composite> _GetChildList() {
		return m_partList;
	}

	/**
	 * Parses the absolute path expression
	 * 
	 * @param absoluteName
	 * @param remainder
	 * @param repeatIndex
	 * @return
	 * @throws M7Exception
	 */
	protected static String _ParseAbsoluteName(M7StringHolder absoluteName,
			M7StringHolder remainder, M7LongHolder repeatIndex)
			throws M7Exception {
		// this method returns the first piece parsed out, a value in the index,
		// a

		int pos = M7.NPOS;
		String parsedName = absoluteName.toString();
		pos = parsedName.indexOf(ABS_NAME_DELM);
		if (pos != M7.NPOS) // found a delimiter
		{
			remainder.setString(parsedName.toString().substring(pos + 1));
			parsedName = parsedName.substring(0, pos);
		} else {
			// no delimiter so remainder is empty
			remainder.setString(M7.EMPTY_STRING);
		}

		// does the parsedName have an index, if so then get the index value
		repeatIndex.setLong(M7.NPOS);
		pos = parsedName.indexOf(ABS_NAME_REPEAT_START_DELIM);
		if (pos != M7.NPOS) // found it
		{
			// copy the parsed name to work with it
			String sIndex = parsedName;

			// strip the index off the parsed name
			parsedName = parsedName.substring(0, pos);

			// strip off the name and the opening bracket
			sIndex = sIndex.substring(pos + 1);

			// look for the closing bracket
			pos = sIndex.indexOf(ABS_NAME_REPEAT_END_DELIM);
			if (pos == M7.NPOS) {
				String strError = Messages
						.getString("M7Composite.MISSING_CLOSING_BRACKET") + absoluteName; //$NON-NLS-1$
				System.err.println(strError);
				throw new M7Exception(strError,
						M7Exception.eAbsNameParsingError);
			}

			// strip off the closing bracket, should only have a number left now
			sIndex = sIndex.substring(0, pos);

			// convert index to long
			repeatIndex.setLong(Long.valueOf(sIndex).longValue());
		}

		return parsedName;
	}

	protected void _UnparseTheSegment() throws M7Exception {
		// parse the segment until we hit the segment end char
		long cnt = 0;
		int pos = 0;
		while (m_pStreamedData.length() > 0) {
			// strip off the field delimiter from the msg stream
			m_pStreamedData = m_pStreamedData.substring(1);

			// get the next part as long as not an escaped delimeter
			pos = 0;
			while (true) {
				pos = m_pStreamedData.indexOf(
						m_pDelimiters.getFieldDelimiter(), pos);

				// either found delimiter immediately, or not at all
				if (pos == 0 || pos == M7.NPOS) {
					break;
				}

				// see if it is an escaped delimiter value

				if (m_pStreamedData.charAt(pos - 1) == m_pDelimiters
						.getEscapeDelimiter()) {
					// ASTM fix
					// used to be simply:
					// pos++;
					if ((this.getName().equals(M7Message.H_SEG_NAME))
							&& (cnt == 0)) {
						break;
					} else {
						pos++;
					}
				} else
					break;
			}

			String segPart = null;

			if (pos == M7.NPOS) {
				segPart = m_pStreamedData.substring(0);
			} else {
				segPart = m_pStreamedData.substring(0, pos);
			}

			if (segPart.length() > 0) {
				// get the child def
				M7MessageDefinition pChildDef = definition.getChild(cnt);

				if (pChildDef != null) {
					// create the appropriate type of child part
					M7Composite pComp = null;
					if (pChildDef.isRepeat()) {
						pComp = _CreateRepeat(new M7StringHolder(pChildDef
								.getName()), pChildDef.getType());
					} else if (pChildDef.isField()) {
						pComp = _CreateField(new M7StringHolder(pChildDef
								.getName()), pChildDef.getType());
					} else {
						pComp = _CreateComposite(new M7StringHolder(pChildDef
								.getName()), pChildDef.getType());
					}

					// unstream the child part
					M7StringHolder h_segPart = new M7StringHolder(segPart);
					pComp._Unstream(h_segPart, m_pDelimiters);
					segPart = h_segPart.toString();
				}
			}

			// are we done yet
			if (pos == M7.NPOS) {
				break;
			}

			// strip off the message stream upto but not including the delimiter
			// found
			m_pStreamedData = m_pStreamedData.substring(pos);
			cnt++;
		}

		// these are no longer needed
		m_pStreamedData = null;
		m_pDelimiters = null;
	}

	protected M7Repeat _ValidateisRepeat(M7Composite pComposite)
			throws M7Exception {
		if (pComposite == null) {
			return null;
		}

		if (!pComposite.isRepeat()) {
			String strError = Messages
					.getString("M7Composite.COMPONENT_NOT_REPEATED") + pComposite.getAbsoluteName(); //$NON-NLS-1$
			throw new M7Exception(strError, M7Exception.eNotARepeat);
		}

		return (M7Repeat) pComposite;
	}

	/**
	 * This returns the full name of the composite relative to the top of the
	 * object graph (the message object) including the appropriate repeat
	 * instances (i.e., "PID.PatientName.FamilyName" or
	 * "PID.IntPatID[0].PatientID").
	 * 
	 * @return String containing the fully qualified absolute name for this
	 *         object.
	 */
	public String getAbsoluteName() {
		M7StringHolder h_absoluteName = new M7StringHolder(new String());

		_BuildAbsoluteName(h_absoluteName, this);

		return h_absoluteName.toString();
	}

	protected boolean _BuildAbsoluteName(M7StringHolder h_absoluteName,
			M7Composite pChildComp) {
		M7Composite pParent = getParent();
		if (pParent != null) {
			if (pParent.isRepeat()) {
				return pParent._BuildAbsoluteName(h_absoluteName, this);
			} else {
				if (pParent._BuildAbsoluteName(h_absoluteName, this)) {
					h_absoluteName.append(ABS_NAME_DELM);
				}

				h_absoluteName.append(this.getName());

				return true;
			}
		}

		return false;
	}

	/**
	 * This method will return the appropriate value that indicates what kind of
	 * composite this is.
	 * 
	 * @return An int that is equal to one of the following:
	 *         M7Composite.eM7Message, M7Composite.eM7Segment,
	 *         M7Composite.eM7SegmentGrp, M7Composite.eM7Field,
	 *         M7Composite.eM7CompositeField
	 */
	public int getType() {
		return m_compType;
	}

	protected void _Stream(M7StringHolder h_msgStream, M7Delimiters delimiters,
			M7StringHolder h_delimiterStack) throws M7Exception {
		if (m_compType == M7Composite.eM7Segment) {
			h_delimiterStack.clear();
			if (h_msgStream.length() > 0) {
				h_msgStream.append(M7.SEGMENT_END_CHAR);
			}
			h_msgStream.append(definition.getStructureType());
			// System.out.println( m_pMsgDef.GetStructureType() );

			h_msgStream.append(delimiters.getFieldDelimiter());
		}

		// we have a piece of the stream that has not been parsed
		// so just append that to the stream and do not bother looking
		// at child components because none should exist
		if (m_pStreamedData != null) {
			h_msgStream.append(h_delimiterStack.toString());
			h_msgStream.append(m_pStreamedData.substring(1));
			return;
		}

		for (Enumeration<M7Composite> e = m_partList.elements(); e
				.hasMoreElements();) {
			M7Composite c = (M7Composite) e.nextElement();
			if (c != null) {
				// hold the old size to see if it changed
				int priorSize = h_msgStream.length();

				// stream the child
				c._Stream(h_msgStream, delimiters, h_delimiterStack);

				// check for a change to see if we should reset the delimiter
				// stack
				if (priorSize != h_msgStream.length()) {
					h_delimiterStack.clear();
				}
			}

			// append the appropriate delimeter even if part not found
			if (m_compType == M7Composite.eM7Segment) {
				h_delimiterStack.append(delimiters.getFieldDelimiter());
			} else if (this._IsSubComposite()) {
				h_delimiterStack.append(delimiters.getSubComponentDelimiter());
			} else {
				h_delimiterStack.append(delimiters.getComponentDelimiter());
			}
		}
	}

	protected void _Stream_CRLF(M7StringHolder h_msgStream,
			M7Delimiters delimiters, M7StringHolder h_delimiterStack)
			throws M7Exception {
		if (m_compType == M7Composite.eM7Segment) {
			h_delimiterStack.clear();
			if (h_msgStream.length() > 0) {
				h_msgStream.append(M7.SEGMENT_END_CHAR_CRLF);
			}
			h_msgStream.append(definition.getStructureType());
			// System.out.println( m_pMsgDef.GetStructureType() ));

			h_msgStream.append(delimiters.getFieldDelimiter());
		}

		// we have a piece of the stream that has not been parsed
		// so just append that to the stream and do not bother looking
		// at child components because none should exist
		if (m_pStreamedData != null) {
			h_msgStream.append(h_delimiterStack.toString());
			h_msgStream.append(m_pStreamedData.substring(1));
			return;
		}

		for (Enumeration<M7Composite> e = m_partList.elements(); e
				.hasMoreElements();) {
			M7Composite c = (M7Composite) e.nextElement();
			if (c != null) {
				// hold the old size to see if it changed
				int priorSize = h_msgStream.length();

				// stream the child
				c._Stream_CRLF(h_msgStream, delimiters, h_delimiterStack);

				// check for a change to see if we should reset the delimiter
				// stack
				if (priorSize != h_msgStream.length()) {
					h_delimiterStack.clear();
				}
			}

			// append the appropriate delimiter even if part not found
			if (m_compType == M7Composite.eM7Segment) {
				h_delimiterStack.append(delimiters.getFieldDelimiter());
			} else if (this._IsSubComposite()) {
				h_delimiterStack.append(delimiters.getSubComponentDelimiter());
			} else {
				h_delimiterStack.append(delimiters.getComponentDelimiter());
			}
		}
	}

	protected boolean _Unstream(M7StringHolder h_msgStream,
			M7Delimiters delimiters) throws M7Exception {
		// this method is over-ridden by field and repeat
		if (h_msgStream.length() == 0) {
			return false;
		}

		switch (m_compType) {
		case M7Composite.eM7Segment: {
			return this._UnstreamSegment(h_msgStream, delimiters);
		}

		case M7Composite.eM7SegmentGrp: {
			return this._UnstreamSegmentGrp(h_msgStream, delimiters);
		}

		case M7Composite.eM7CompositeField: // this also handles subcomposite
			// fields
		{
			return this._UnstreamCompositeField(h_msgStream, delimiters);
		}

		default: {
			return this._UnstreamMsg(h_msgStream, delimiters);
		}
		}

		// return false;
	}

	protected boolean _UnstreamSegment(M7StringHolder streamStr,
			M7Delimiters delimiters) throws M7Exception {
		// build a string of delims to look for
		// String delims;
		// delims += delimiters.fieldDelim;
		// delims += M7.SEGMENT_END_CHAR;

		// get the next segment name off the stream

		int pos = streamStr.indexOf(delimiters.getFieldDelimiter());
		if (pos == M7.NPOS) {
			pos = streamStr.indexOf(M7.SEGMENT_END_CHAR);
			if (pos == M7.NPOS) {
				_ThrowUnstreamError(streamStr.toString(), Messages
						.getString("M7Composite.MISSING_FIRST_FIELD_DELIM")); //$NON-NLS-1$}
				// cannot find first field delimiter or end of segment to get
				// the
				// segment name
			}
		}

		// it is not our segment
		String segName = streamStr.substring(0, pos);
		if (!segName.equals(definition.getStructureType())) {

			return false;
		}
		// pull the segment name off the stream
		streamStr.setString(streamStr.substring(pos));

		// create the pointer to hold onto the stream for the segment
		// that we unparse later when asked for
		m_pStreamedData = new String();
		pos = streamStr.indexOf(M7.SEGMENT_END_CHAR);
		if (pos == M7.NPOS) {
			_ThrowUnstreamError(streamStr.toString(), Messages
					.getString("M7Composite.MISSING_SEGMENT_TERMINATOR") + //$NON-NLS-1$
					":" + streamStr.toString());//$NON-NLS-1$
			// cannot find segment terminator
		}
		m_pStreamedData = streamStr.substring(0, pos);
		// strip off the segment from the stream
		streamStr.setString(streamStr.substring(pos + 1));

		// CR/LF fix
		if ((streamStr.length() > 0) && (streamStr.charAt(0) == 0x0A)) {
			streamStr.setString(streamStr.substring(1));
		}

		// hold the delimiters, these are used for unparsing the segment later
		// on demand
		m_pDelimiters = new M7Delimiters();
		m_pDelimiters.setFieldDelimiter(delimiters.getFieldDelimiter());
		m_pDelimiters
				.setComponentDelimiter((delimiters.getComponentDelimiter()));
		m_pDelimiters.setRepetitionDelimiter((delimiters
				.getRepetitionDelimiter()));
		m_pDelimiters.setEscapeDelimiter((delimiters.getEscapeDelimiter()));
		m_pDelimiters.setSubComponentDelimiter((delimiters
				.getSubComponentDelimiter()));

		return true;
	}

	protected boolean _UnstreamSegmentGrp(M7StringHolder h_msgStream,
			M7Delimiters delimiters) throws M7Exception {
		long defCnt = definition.getChildCount();
		boolean addedComponents = false;

		for (long i = 0; i < defCnt; i++) {
			M7MessageDefinition pChildDef = definition.getChild(i);

			// create a child composite, can only be a repeat or a composite
			M7Composite pComp = null;
			if (pChildDef.isRepeat()) {
				pComp = this._CreateRepeat(new M7StringHolder(pChildDef
						.getName()), pChildDef.getType());
			} else {
				pComp = this._CreateComposite(new M7StringHolder(pChildDef
						.getName()), pChildDef.getType());
			}

			// TBD - could optimize by create not automatically adding to
			// collection and then we have to go back and delete
			// nothing found to parse for this component so get rid of it from
			// our collection
			// (the act of creating automatically added to our collection)
			if (!pComp._Unstream(h_msgStream, delimiters)) {
				deleteChildWithComposite(pComp);
			} else {
				addedComponents = true;
			}

			if (h_msgStream.length() == 0) {
				break;
			}

		}

		return addedComponents;
	}

	protected boolean _UnstreamCompositeField(M7StringHolder streamStr,
			M7Delimiters delimiters) throws M7Exception {
		// when parsing a composite field all that is passed in is the composite
		// field and
		// not the terminating field delimiter or a segment delimiter

		// build a string of delimiter to look for
		char delim;

		if (this._IsSubComposite()) {
			delim = delimiters.getSubComponentDelimiter();
		} else {
			delim = delimiters.getComponentDelimiter();
		}

		long cnt = 0;
		int pos = 0;
		while (true) {
			// get the next part as long as not an escaped delimiter
			pos = 0;
			while (true) {
				pos = streamStr.indexOf(delim, pos);
				// either found delimiter immediately, or not at all
				if (pos == 0 || pos == M7.NPOS) {
					break;
				}

				// see if it is an escaped delimiter value
				if (streamStr.charAt(pos - 1) == delimiters
						.getEscapeDelimiter()) {
					pos++;
				} else {
					break;
				}
			}

			String compPart = new String();

			if (pos == M7.NPOS) {
				compPart = streamStr.substring(0);
			} else {
				compPart = streamStr.substring(0, pos);
			}

			if (compPart.length() > 0) {
				// get the child def
				M7MessageDefinition pChildDef = definition.getChild(cnt);

				if (pChildDef != null) {
					// create the appropriate type of child part
					M7Composite pComp = null;
					if (pChildDef.isRepeat()) {
						pComp = this._CreateRepeat(new M7StringHolder(pChildDef
								.getName()), pChildDef.getType());
					} else if (pChildDef.isField()) {
						pComp = this._CreateField(new M7StringHolder(pChildDef
								.getName()), pChildDef.getType());
					} else {
						pComp = this._CreateComposite(new M7StringHolder(
								pChildDef.getName()), pChildDef.getType());
					}

					// unstream the child part
					M7StringHolder h_compPart = new M7StringHolder(compPart);
					pComp._Unstream(h_compPart, delimiters);
					compPart = h_compPart.toString();
				}
			}

			// we hit the end of the string
			if (pos == M7.NPOS) {
				break;
			}

			// strip off the message stream the piece we just processed
			streamStr.setString(streamStr.substring(pos + 1));
			cnt++;
		}

		// we added child components in the above loop so return true
		if (m_partList.size() > 0) {
			return true;
		}

		return false;
	}

	protected boolean _IsSubComposite() throws M7Exception {
		if (m_compType == M7Composite.eM7CompositeField) {
			if (m_pParent.getType() == M7Composite.eM7CompositeField) // parent
			{// is also
				// composite
				// but we
				// must
				// continue
				if (m_pParent.isRepeat()) {// if a repeat is the parent then we
					// must check it's parent because the
					// repeat just acts as a container
					if (m_pParent.m_pParent.getType() == M7Composite.eM7CompositeField) // we
					// really
					// are
					// a
					// subcomponent
					// buried
					// under
					// a
					// repeating
					// composite
					// field
					{
						return true;
					} else {
						// we are just a repeating composite field
						return false;
					}
				} else {
					// we are a subcomponent under a real composite field and
					// not a repeat container
					return true;
				}
			}
		} else { // we are just a simple composite field
			return false;
		}

		return false;
	}

	protected boolean _UnstreamMsg(M7StringHolder h_msgStream,
			M7Delimiters delimiters) throws M7Exception {
		// all other types (message) we just walk the message def list
		long defCnt = definition.getChildCount();
		boolean addedComponents = false;
		boolean usedit = true;
		long holdPos = 0;
		for (long i = 0; i < defCnt; i++) {
			// hold the current position when parsing the message in case we
			// have to skip over an unrecognized segment
			if (usedit == true) {
				holdPos = i;
				usedit = false;
			}

			M7MessageDefinition pChildDef = definition.getChild(i);

			// create a child composite, can only be a repeat or a composite
			M7Composite pComp = null;
			if (pChildDef.isRepeat()) {
				pComp = this._CreateRepeat(new M7StringHolder(pChildDef
						.getName()), pChildDef.getType());
			} else {
				pComp = this._CreateComposite(new M7StringHolder(pChildDef
						.getName()), pChildDef.getType());
			}

			// TBD - could optimize by create not automatically adding to
			// collection and then we have to go back and delete
			// nothing found to parse for this component so get rid of it from
			// our collection
			// (the act of creating automatically added to our collection)
			if (!pComp._Unstream(h_msgStream, delimiters)) {
				// System.out.println("Unable to
				// parse:"+pChildDef.getStructureType());
				deleteChildWithComposite(pComp);
			} else {
				addedComponents = true;
				usedit = true;
			}

			if (h_msgStream.length() == 0) {
				break;
			}

			// we are going to terminate the loop, if we did not use it then
			// reset the loop and skip over the segment
			if ((i + 1) == defCnt) {
				if (usedit == false) {
					// strip off the segment and reset i
					int pos = h_msgStream.indexOf(M7.SEGMENT_END_CHAR);
					if (pos == M7.NPOS) {
						_ThrowUnstreamError(
								h_msgStream.toString(),
								Messages
										.getString("M7Composite.MISSING_SEGMENT_TERMINATOR")); //$NON-NLS-1$
					}
					// cannot find segment terminator
					h_msgStream.setString(h_msgStream.substring(pos + 1)); // strip
					// off
					// the
					// segment
					// from
					// the stream

					// CR/LF fix
					if ((h_msgStream.length() > 0)
							&& (h_msgStream.charAt(0) == 0x0A)) {
						h_msgStream.setString(h_msgStream.substring(1));
					}

					// there is nothing left, unknown segment at end of stream
					if (h_msgStream.length() == 0) {
						break;
					}

					usedit = true;
					i = holdPos - 1;
				}
			}
		}

		return addedComponents;
	}

	protected void _ThrowUnstreamError(String msgStream) throws M7Exception {
		String strError = Messages.getString("M7Composite.PARSING_ERROR") + msgStream; //$NON-NLS-1$
		throw new M7Exception(strError, M7Exception.eUnstreamParsingError);
	}

	protected void _ThrowUnstreamError(String msgStream, String additionalInfo)
			throws M7Exception {
		String strError = Messages.getString("M7Composite.PARSING_ERROR") + msgStream + Messages.getString("M7Composite.CR"); //$NON-NLS-1$ //$NON-NLS-2$
		strError += Messages.getString("M7Composite.REASON") + additionalInfo; //$NON-NLS-1$

		throw new M7Exception(strError, M7Exception.eUnstreamParsingError);
	}

	//
	// Removal of children from composite
	//
	/**
	 * This will delete the specified child composite object and all descendants
	 * of all child objects under it. It returns false if the child object was
	 * not found and true if it was found and deleted.
	 * 
	 * @param pMsgComponent
	 *            The component you wish to delete. This must be a direct child
	 *            object under this composite.
	 * @return Returns false if not found to delete
	 */
	public boolean deleteChildWithComposite(M7Composite pMsgComponent) {
		// this will delete the item pointed to in the vector and set the vector
		// item to null
		for (int i = 0; i < m_partList.size(); i++) {
			Object o = m_partList.elementAt(i);

			if (o == null) {
				continue;
			}

			if (o == pMsgComponent) {
				m_partList.setElementAt(null, i);
				return true;
			}
		}

		return false;
	}

	/**
	 * This will delete the specified child composite object and all descendants
	 * of all child objects under it. It returns false if the child object was
	 * not found and true if it was found and deleted.
	 * 
	 * @param absoluteName
	 *            The name of the component you wish to delete.
	 * @return Returns false if not found to delete.
	 */
	public boolean deleteChild(String absoluteName) throws M7Exception {
		M7Composite pComp = getChild(absoluteName);
		if (pComp != null) {
			return pComp.getParent().deleteChildWithComposite(pComp);
		}

		// not found
		return false;
	}

	// these allow you to transcend through the composite tree to set or get a
	// value directly
	// rather than traversing through each part individually, if setting many
	// values then
	// you will likely get better performance using the other methods.
	/**
	 * Will automatically create the component in the object graph if it does
	 * not already exist. If the absolute name includes a repeat index then the
	 * repeat is automatically grown to the appropriate size if necessary. This
	 * allows for very simple usage of the Medi7 parser.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @param newValue
	 *            The value for the field
	 * 
	 * @exception M7Exception
	 *                Thrown if there is no component in the message definition
	 *                that corresponds to the absoluteName, or if the element
	 *                specified by the name is not a field.
	 */
	public void setFieldValue(String absoluteName, String newValue)
			throws M7Exception {
		// when setting a value it also creates the one needed if necessary
		M7Field pField = this.createField(absoluteName, null);
		pField.setValue(newValue);
	}

	protected M7Field _CreateField(String partName, int childType)
			throws M7Exception {
		M7Field pFld = new M7Field(partName, childType, this);
		_AddChild(pFld);
		return pFld;
	}

	/**
	 * This method is identical to CreateComposite but ensures that the object
	 * to be created is of type M7Field.
	 * 
	 * @param absoluteName
	 *            This string specifies the name of the child field you want to
	 *            create. If the absolute name includes a repeat index(es) then
	 *            the repeat is automatically grown to the appropriate size if
	 *            necessary.
	 * @param h_alreadyExists
	 *            A pointer to a boolean value. Null is allowed but if provided
	 *            this will return true if object being asked for already exists
	 *            and false if a new object was created and false if it already
	 *            existed.
	 * @return An M7Field
	 * 
	 * @exception M7Exception
	 *                An exception is thrown if the name specified is not
	 *                defined in the message definition relative to this
	 *                composite or the name does not correspond to a field.
	 * 
	 * @see M7Composite#CreateChild
	 */
	public M7Field createField(String absoluteName,
			M7BooleanHolder h_alreadyExists) throws M7Exception {
		return _ValidateisField(this.newChild(absoluteName, h_alreadyExists));
	}

	/**
	 * This method is identical to GetChild but ensures that the requested
	 * object is of type M7Field.
	 * 
	 * @param absoluteName
	 *            This string specifies the name of the child field you want to
	 *            retrieve.
	 * @return null or an M7Field.
	 * @exception M7Exception
	 *                Thrown if the absoluteName is not formed correctly.
	 * 
	 * @see M7Composite#GetRepeat
	 * @see M7Composite#GetChild
	 */
	public M7Field getField(String absoluteName) throws M7Exception {
		return _ValidateisField(this.getChild(absoluteName));
	}

	//
	// Repeat Creation and Accessors
	//
	/**
	 * This method is identical to CreateComposite but ensures that the
	 * requested object to be created is of type M7Repeat.
	 * 
	 * @param absoluteName
	 *            This string specifies the name of the child repeat you want to
	 *            create. If the absolute name includes a repeat index(es) then
	 *            the repeat is automatically grown to the appropriate size if
	 *            necessary.
	 * @param h_alreadyExists
	 *            A pointer to a boolean value. Null is allowed but if provided
	 *            this will return true if object being asked for already exists
	 *            and false if a new object was created and false if it already
	 *            existed.
	 * @return An M7Repeat
	 * 
	 * @exception M7Exception
	 *                An exception is thrown if the name specified is not
	 *                defined in the message definition relative to this
	 *                composite or the name does not correspond to a repeat.
	 * 
	 * @see M7Composite#CreateChild
	 * @see M7Composite#CreateField
	 */
	public M7Repeat createRepeat(String absoluteName,
			M7BooleanHolder h_alreadyExists) throws M7Exception {
		return _ValidateisRepeat(newChild(absoluteName, h_alreadyExists));
	}

	/**
	 * This method is identical to GetChild but ensures that the requested
	 * object is of type M7Repeat.
	 * 
	 * @param absoluteName
	 *            This string specifies the name of the child repeat you want to
	 *            retrieve.
	 * @return null or an M7Repeat.
	 * @exception M7Exception
	 *                Thrown if the absoluteName is not formed correctly.
	 * 
	 * @see M7Composite#GetField
	 * @see M7Composite#GetChild
	 */
	public M7Repeat getRepeat(String absoluteName) throws M7Exception {
		return _ValidateisRepeat(getChild(absoluteName));
	}

	/**
	 * Identical to setFieldValue(String absoluteName, String newValue) but sets
	 * the field's value with the long provided.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @param newValue
	 *            The value for the field
	 * 
	 * @exception M7Exception
	 *                Thrown if there is no component in the message definition
	 *                that corresponds to the absoluteName, or if the element
	 *                specified by the name is not a field.
	 */
	public void setFieldValue(String absoluteName, long newValue)
			throws M7Exception {
		// convert the long to a string and call that version of set field value
		this.setFieldValue(absoluteName, String.valueOf(newValue));
	}

	/**
	 * Identical to setFieldValue(String absoluteName, String newValue) but sets
	 * the field's value with the double provided.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @param newValue
	 *            The value for the field
	 * 
	 * @exception M7Exception
	 *                Thrown if there is no component in the message definition
	 *                that corresponds to the absoluteName, or if the element
	 *                specified by the name is not a field.
	 */
	public void setFieldValue(String absoluteName, double newValue)
			throws M7Exception {
		// convert the double to a string and call that version of set field
		// value
		this.setFieldValue(absoluteName, String.valueOf(newValue));
	}

	/**
	 * Identical to setFieldValue(String absoluteName, String newValue) but sets
	 * the field's value with the string value of the M7DateTime provided.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @param newValue
	 *            The value for the field
	 * 
	 * @exception M7Exception
	 *                Thrown if there is no component in the message definition
	 *                that corresponds to the absoluteName, or if the element
	 *                specified by the name is not a field.
	 */
	public void setFieldValue(String absoluteName, M7DateTime newValue)
			throws M7Exception {
		// convert the datetime to a string and call that version of set field
		// value
		this.setFieldValue(absoluteName, newValue.getStringValue());
	}

	/**
	 * Identical to setFieldValue(String absoluteName, String newValue) but sets
	 * the field's value with the string value of the M7Date provided.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @param newValue
	 *            The value for the field
	 * 
	 * @exception M7Exception
	 *                Thrown if there is no component in the message definition
	 *                that corresponds to the absoluteName, or if the element
	 *                specified by the name is not a field.
	 */
	public void setFieldValue(String absoluteName, M7Date newValue)
			throws M7Exception {
		// convert the date to a string and call that version of set field value
		this.setFieldValue(absoluteName, newValue.getStringValue());
	}

	/**
	 * Identical to setFieldValue(String absoluteName, String newValue) but sets
	 * the field's value with the string value of the M7Time provided.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @param newValue
	 *            The value for the field
	 * 
	 * @exception M7Exception
	 *                Thrown if there is no component in the message definition
	 *                that corresponds to the absoluteName, or if the element
	 *                specified by the name is not a field.
	 */
	public void setFieldValue(String absoluteName, M7Time newValue)
			throws M7Exception {
		// convert the datetime to a string and call that version of set field
		// value
		this.setFieldValue(absoluteName, newValue.getStringValue());
	}

	/**
	 * Identical to setFieldValue(String absoluteName, String newValue) but sets
	 * the field's value with the string value of the M7PhoneNum provided.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @param newValue
	 *            The value for the field
	 * 
	 * @exception M7Exception
	 *                Thrown if there is no component in the message definition
	 *                that corresponds to the absoluteName, or if the element
	 *                specified by the name is not a field.
	 */
	public void setFieldValue(String absoluteName, M7PhoneNumber newValue)
			throws M7Exception {
		// convert the phone num to a string and call that version of set field
		// value
		this.setFieldValue(absoluteName, newValue.getStringValue());
	}

	/**
	 * Sets the specified field to nothing. This equates to an HL7 empty field.
	 * The field object is not automatically created if it does not exist
	 * because a non-existent field in the object graph always equates to an
	 * empty HL7 field.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * 
	 * @exception M7Exception
	 *                Thrown if there is no component in the message definition
	 *                that corresponds to the absoluteName, or if the element
	 *                specified by the name is not a field.
	 */
	public void setFieldEmpty(String absoluteName) throws M7Exception {
		// call GetChild instead of CreateChild because there is no sense
		// setting it to empty
		// if it does not exist
		M7Composite pComp = this.getChild(absoluteName);
		if (pComp != null) {
			M7Field pField = _ValidateisField(pComp);
			pField.setEmpty();
		}
	}

	/**
	 * Sets the specified field to the HL7 null value (i.e. ""). Will
	 * automatically create the component in the object graph if it does not
	 * already exist. If the absolute name includes a repeat index then the
	 * repeat is automatically grown to the appropriate size if necessary.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * 
	 * @exception M7Exception
	 *                Thrown if there is no component in the message definition
	 *                that corresponds to the absoluteName, or if the element
	 *                specified by the name is not a field.
	 */
	public void setFieldNull(String absoluteName) throws M7Exception {
		// call CreateChild because Null is considered a value in HL7, as
		// opposed to empty
		// which means not provided, therefore we should create the field to
		// hold the value
		M7Field pField = this.createField(absoluteName, (M7BooleanHolder) null);
		pField.setNull();
	}

	/**
	 * This will return the state of the field specified. If the field has not
	 * yet been created then it will return eFldNotExist.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @return An int that corresponds to one of the following:
	 *         M7Field.eFldNotExist M7Field.eFldNull M7Field.eFldEmpty
	 *         M7Field.eFldPresent
	 * 
	 * @exception M7Exception
	 *                Thrown if there is no component in the message definition
	 *                that corresponds to the absoluteName, or if the element
	 *                specified by the name is not a field.
	 */
	public int getFieldState(String absoluteName) throws M7Exception {
		M7Field pField = null;
		M7MessageDefinition fieldDef = null;
		fieldDef = definition.getChild(absoluteName);
		if (fieldDef == null) {
			return M7Field.eFldNotExist;
		}
		pField = this.getField(absoluteName);
		if (pField == null) {
			return M7Field.eFldNull;
		}

		return pField.getState();
	}

	/**
	 * All of the getFieldValue methods will return the contents of the field
	 * specified by the absolute name. If there is a possibility that the field
	 * may not exist in the message then you should first check to see if it
	 * exists by calling GetField and ensure that it does not return a null
	 * pointer, or call GetFieldState which returns an indication of whether or
	 * not the field exists.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @return The value of the field as a String.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field objects values being asked for does
	 *                not exist, or the absolute name does not correspond to a
	 *                M7Field.
	 */
	public String getFieldValue(String absoluteName) throws M7Exception {
		M7Field pField = this.getField(absoluteName);
		// not found so throw exception, should check the state first to avoid
		// the exception
		if (pField == null)
			this._ThrowBadFieldException(absoluteName);

		return pField.getValue();
	}

	/**
	 * Identical to getFieldValue but converts the field's value and returns it
	 * as a long.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @return The value of the field as a long.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field objects values being asked for does
	 *                not exist, or the absolute name does not correspond to a
	 *                M7Field.
	 */
	public long getFieldValueAsLong(String absoluteName) throws M7Exception {
		M7Field pField = this.getField(absoluteName);
		// not found so throw exception, should check the state first to avoid
		// the exception
		if (pField == null) {
			this._ThrowBadFieldException(absoluteName);
		}

		return pField.getValueAsLong();
	}

	/**
	 * Identical to getFieldValue but converts the field's value and returns it
	 * as a double.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @return The value of the field as a double.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field objects values being asked for does
	 *                not exist, or the absolute name does not correspond to a
	 *                M7Field.
	 */
	public double getFieldValueAsDouble(String absoluteName) throws M7Exception {
		M7Field pField = this.getField(absoluteName);
		// not found so throw exception, should check the state first to avoid
		// the exception
		if (pField == null) {
			this._ThrowBadFieldException(absoluteName);
		}

		return pField.getValueAsDouble();
	}

	protected void _ThrowBadFieldException(String absoluteName)
			throws M7Exception {
		String absName = this.getAbsoluteName();
		absName += ABS_NAME_DELM;
		absName += absoluteName;
		String strError = Messages.getString("M7Composite.MISSING_FIELD") + absName; //$NON-NLS-1$
		throw new M7Exception(strError, M7Exception.eFieldNotFound);
	}

	/**
	 * Identical to getFieldValue but converts the field's value and returns it
	 * as an M7DateTime.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @return The value of the field as an M7DateTime.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field objects values being asked for does
	 *                not exist, or the absolute name does not correspond to a
	 *                M7Field.
	 */
	public M7DateTime getFieldValueAsDateTime(String absoluteName)
			throws M7Exception {
		M7Field pField = this.getField(absoluteName);
		// not found so throw exception, should check the state first to avoid
		// the exception
		if (pField == null) {
			this._ThrowBadFieldException(absoluteName);
		}

		return pField.getValueAsDateTime();
	}

	/**
	 * Identical to getFieldValue but converts the field's value and returns it
	 * as an M7Date.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @return The value of the field as an M7Date.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field objects values being asked for does
	 *                not exist, or the absolute name does not correspond to a
	 *                M7Field.
	 */
	public M7Date getFieldValueAsDate(String absoluteName) throws M7Exception {
		M7Field pField = this.getField(absoluteName);
		// not found so throw exception, should check the state first to avoid
		// the exception
		if (pField == null) {
			this._ThrowBadFieldException(absoluteName);
		}

		return pField.getValueAsDate();
	}

	/**
	 * Identical to getFieldValue but converts the field's value and returns it
	 * as an M7Time.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @return The value of the field as an M7Time.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field objects values being asked for does
	 *                not exist, or the absolute name does not correspond to a
	 *                M7Field.
	 */
	public M7Time getFieldValueAsTime(String absoluteName) throws M7Exception {
		M7Field pField = this.getField(absoluteName);
		// not found so throw exception, should check the state first to avoid
		// the exception
		if (pField == null) {
			this._ThrowBadFieldException(absoluteName);
		}

		return pField.getValueAsTime();
	}

	/**
	 * Identical to getFieldValue but converts the field's value and returns it
	 * as an M7PhoneNum.
	 * 
	 * @param absoluteName
	 *            Must reference a field in the message as defined in the
	 *            message definition.
	 * @return The value of the field as an M7PhoneNum.
	 * 
	 * @exception M7Exception
	 *                Thrown if the field objects values being asked for does
	 *                not exist, or the absolute name does not correspond to an
	 *                M7Field.
	 */
	public M7PhoneNumber getFieldValueAsPhoneNumber(String absoluteName)
			throws M7Exception {
		M7Field pField = this.getField(absoluteName);
		// not found so throw exception, should check the state first to avoid
		// the exception
		if (pField == null) {
			this._ThrowBadFieldException(absoluteName);
		}

		return pField.getValueAsPhoneNumber();
	}

	// static method
	protected static M7Field _ValidateisField(M7Composite pComposite)
			throws M7Exception {
		if (pComposite == null) {
			return null;
		}

		if (!pComposite.isField()) {
			String strError = Messages.getString("M7Composite.NOT_FIELD") + pComposite.getAbsoluteName(); //$NON-NLS-1$
			throw new M7Exception(strError, M7Exception.eNotAField);
		}

		return (M7Field) pComposite;
	}

	/**
	 * This will delete all the child composite objects and all descendants of
	 * all child objects under this composite.
	 */
	public void deleteAllChildren() {
		// just declare a new vector and let the gc do the rest
		m_partList = new Vector<M7Composite>();
		System.gc();
	}

	//
	// composite types added for COM build
	//
	/**
	 * Constant accessor for use in COM clients
	 * 
	 * @return 0.
	 */
	public int COMP_TYPE_MSG() {
		return M7Composite.eM7Message;
	}

	/**
	 * Constant accessor for use in COM clients
	 * 
	 * @return 1.
	 */
	public int COMP_TYPE_SEG() {
		return M7Composite.eM7Segment;
	}

	/**
	 * Constant accessor for use in COM clients
	 * 
	 * @return 2.
	 */
	public int COMP_TYPE_SEGGRP() {
		return M7Composite.eM7SegmentGrp;
	}

	/**
	 * Constant accessor for use in COM clients
	 * 
	 * @return 3.
	 */
	public int COMP_TYPE_FLD() {
		return M7Composite.eM7Field;
	}

	/**
	 * Constant accessor for use in COM clients
	 * 
	 * @return 4.
	 */
	public int COMP_TYPE_COMPFLD() {
		return M7Composite.eM7CompositeField;
	}

	/**
	 * Debugging function used to display the content of the message as:<br/>
	 * <code>
	 <p><code><b>&lt;absolute path/name of filed&gt;<font color="red">=</font>&lt;value of field&gt;</b></code>
	 * </p>
	 * 
	 * <p>
	 * <code>
	 MSH.EncodingCharacters<font color="red">=</font>^~\&<br/>
	 MSH.SendingApplication.namespaceID<font color="red">=</font>LAB\S\ICU<br/>
	 MSH.ReceivingFacility.namespaceID<font color="red">=</font>MER\F\RCY<br/>
	 MSH.DateTimeOfMessage.DateTime<font color="red">=</font>20050810103400<br/>
	 MSH.MessageType.messagetype<font color="red">=</font>ORU<br/>
	 MSH.MessageType.triggerevent<font color="red">=</font>R01<br/>
	 MSH.MessageControlID<font color="red">=</font>200508151034000001<br/>
	 MSH.ProcessingID.processingID<font color="red">=</font>P<br/>
	 MSH.VersionID.versionID<font color="red">=</font>2.3<br/>
	 MSH.AcceptAcknowledgmentTyp<font color="red">=</font>NE<br/>
	 MSH.ApplicationAcknowledgme<font color="red">=</font>AL<br/>
	 PID.PatientIdentifierList.assigningauthority.namespaceID<font color="red">=</font>IDMS<br/>
	 PID.PatientAccountNumber.ID<font color="red">=</font>H00000002353<br/>
	 ORC.OrderControl<font color="red">=</font>RE<br/>
	 ORC.DateTimeofTransaction.DateTime<font color="red">=</font>20050715403400<br/>
	 ORC.EnteredBy.IDnumberST<font color="red">=</font>MMWINTEC<br/>
	 ORC.EnteredBy.familyname.surname<font color="red">=</font>JONES<br/>
	 ORC.EnteredBy.givenname<font color="red">=</font>CATHY<br/>
	 ORC.EnteredBy.secondandfurthergivenna<font color="red">=</font>J<br/>
	 ORC.EnteringDevice.identifier<font color="red">=</font>23468<br/>
	 ORC.EnteringDevice.alternateidentifier<font color="red">=</font>33332<br/>
	 OBR.PlacerOrderNumber.entityidentifier<font color="red">=</font>111<br/></code>
	 * </p>
	 * 
	 * @return String contents of the message
	 */
	public String debug() {
		StringBuffer msgBuffer = new StringBuffer();

		try {
			debug(this, msgBuffer);
		} catch (M7Exception e) {
			e.printStackTrace();
			msgBuffer.append(e.getLocalizedMessage());
		}
		return msgBuffer.toString();
	}

	/**
	 * Debug method that is invoked recursively
	 * 
	 * @param root
	 * @param msgBuffer
	 * @throws M7Exception
	 */
	private void debug(M7Composite root, StringBuffer msgBuffer)
			throws M7Exception {
		if (root == null) // empty message
		{
			return;
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			M7Composite child = root.getChildByPosition(i);
			if (child == null) {
				// throw new M7Exception("Element '"+root.getAbsoluteName()+"'
				// has null child element at ["+i+']');
			} else {
				if (child.isField()) {
					msgBuffer.append(
							child.getAbsoluteName() + "="
									+ ((M7Field) child).getValue())
							.append('\n');
				} else {
					debug(child, msgBuffer);
				}
			}
		}
	}

	/**
	 * Copies the element from the source to the target
	 * 
	 * @param source
	 * @param target
	 * @throws M7Exception
	 */
	public static void copy(M7Composite source, M7Composite target)
			throws M7Exception {
		if (source == null) // empty message
		{
			throw new M7Exception("Source element is null.");
		}
		if (target == null) {
			throw new M7Exception("Target element is null.");
		}
		for (int i = 0; i < source.getChildCount(); i++) {
			M7Composite child = source.getChildByPosition(i);
			if (child == null) {
				// throw new
				// M7Exception("Element '"+source.getAbsoluteName()+"'has null child element at ["+i+']');
			} else {
				String childName = child.getName();
				if (child.isField()) {
					if (!((M7Field) child).isNull()) {
						target.setFieldValue(((M7Field) child).getName(),
								((M7Field) child).getValue());
					}
				} else {
					if (child.getName().equals(target.getName())) {
						copy(child, target);
					} else {
						if (target.getChild(childName) == null) {
							// add child if missing
							if (source.getChild(childName).isField()) {
								target.createField(childName,
										new M7BooleanHolder(false));
							} else if (source.getChild(childName).isRepeat()) {
								target.createRepeat(childName,
										new M7BooleanHolder(false));
							} else {
								target.newChild(childName, new M7BooleanHolder(
										false));
							}							
						}
						copy(child, target.getChild(child.getName()));
					}
				}
			}
		}
	}

	/**
	 * Copies the current object to the target object
	 * 
	 * @param target
	 * @throws M7Exception
	 */
	public void copy(M7Composite target) throws M7Exception {

		copy(this, target);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer msgBuffer = new StringBuffer();
		try {
			getString(this, msgBuffer, this.getParent(), 0L);
		} catch (M7Exception e) {
			msgBuffer.append(e.getLocalizedMessage());
		}
		return msgBuffer.toString();
	}

	/**
	 * This method is used to render the element from the native syntax (e.g.
	 * HL7 ASCII Encoding Rules, ASTM rules) to XML.
	 * <p>
	 * <u>M7Message Example:</u>
	 * </p>
	 * <p>
	 * <b>&lt;ORU_R01&gt;</b> <blockquote>
	 * <p>
	 * &lt;MSH&gt; <blockquote>
	 * <p>
	 * &lt;EncodingCharacters>^&amp;~/&lt;/EncodingCharacters&gt;
	 * </p>
	 * <p>
	 * &lt;SendingApplication> <blockquote>
	 * <p>
	 * &lt;namespaceID&gt;EVERSOLVE&lt;/namespaceID&gt;
	 * </p>
	 * <p>
	 * &lt;universalID&gt;Damon&lt;/universalID&gt;
	 * </p>
	 * </blockquote> &lt;/SendingApplication&gt; </p>
	 * <p>
	 * &lt;DateTimeOfMessage&gt; <blockquote>
	 * <p>
	 * &lt;DateTime&gt;20050102000831&lt;/DateTime&gt; </blockquote>
	 * </p>
	 * <p>
	 * &lt;/DateTimeOfMessage&gt;
	 * </p>
	 * </p> ... </blockquote> </blockquote>
	 * 
	 * @return string representation of the message as an XML document
	 */

	public String toXML() throws M7Exception {
		StringBuffer msgBuffer = new StringBuffer();
		getXMLstring(this, msgBuffer, this.getParent(), 0L);
		return msgBuffer.toString();
	}

	/**
	 * This function will walk the message and fill in the element with the
	 * field names of all fields that are present and are not set to empty or
	 * null. This method recursively navigates the message tree
	 * 
	 * @param node
	 * @param elements
	 * @throws M7Exception
	 */
	protected void getXMLstring(M7Composite node, StringBuffer elements,
			M7Composite parent, long sequence) throws M7Exception {
		if (node == null) // empty message
		{
			return;
		}
		if (node.isField()) // tree leaf reached
		{
			M7Field field = (M7Field) node;
			if (field.getState() == M7Field.eFldPresent) {

				elements.append("<" + escape(field.getName()) + ">"
						+ escape(field.getValue()) + "</"
						+ escape(field.getName()) + ">");
			}
		} else {
			String suffix = "";
			if (node.isRepeat()) {
				suffix += "_SET";
			}
			elements.append("<" + escape(node.getName()) + suffix + ">");
			// elements.append("<"+node.getName()+">");
			for (int i = 0; i < node.getChildCount(); i++) {
				M7Composite branch = node.getChildByPosition(i);
				if (branch != null) {
					getXMLstring(branch, elements, node, i);
				}

			}
			elements.append("</" + escape(node.getName()) + suffix + ">");

		}
		// elements.append( "</"+node.getName()+">");
	}

	/*
	 * This function will walk the message and fill in the element with the
	 * field names of all fields that are present and are not set to empty or
	 * null. This method recursively navigates the message tree and creates an
	 * HL7-conformant XML string.
	 * 
	 * @param node
	 * 
	 * @param elements
	 * 
	 * @throws M7Exception
	 */
	protected void getHL7XMLstring(M7Composite node, StringBuffer elements,
			M7Composite parent, long sequence) throws M7Exception {
		if (node == null) // empty message
		{
			return;
		}
		if (node.isField()) // tree leaf reached
		{
			M7Field field = (M7Field) node;
			if (field.getState() == M7Field.eFldPresent) {

				elements.append("<" + escape(field.getName()) + ">"
						+ escape(field.getValue()) + "</"
						+ escape(field.getName()) + ">");
			}
		} else {
			String suffix = "";
			if (node.isRepeat()) {
				suffix += "_SET";
			}
			elements.append("<" + escape(node.getName()) + suffix + ">");
			// elements.append("<"+node.getName()+">");
			for (int i = 0; i < node.getChildCount(); i++) {
				M7Composite branch = node.getChildByPosition(i);
				if (branch != null) {
					getXMLstring(branch, elements, node, i);
				}
			}
			elements.append("</" + escape(node.getName()) + suffix + ">");
		}
		// elements.append( "</"+node.getName()+">");
	}

	/**
	 * This function will walk the message and create a string of the contents
	 * of the message
	 * 
	 * @param node
	 * @param elements
	 * @throws M7Exception
	 */
	protected void getString(M7Composite node, StringBuffer elements,
			M7Composite parent, long sequence) throws M7Exception {
		if (node == null) // empty message
		{
			return;
		}
		if (node.isField()) // tree leaf reached
		{
			M7Field field = (M7Field) node;
			if (field.getState() == M7Field.eFldPresent) {
				if (elements.length() > 0) {
					elements.append(", ");
				}
				elements.append(field.getValue());
			}
		} else {

			for (int i = 0; i < node.getChildCount(); i++) {
				M7Composite branch = node.getChildByPosition(i);
				if (branch != null) {

					getString(branch, elements, node, i);

				}

			}

		}
	}

	protected String escape(String value) {
		value = value.replaceAll("&", "&amp;");
		return value.replaceAll(":", "_").replaceAll(">", "&gt;").replaceAll(
				"<", "&lt;").replaceAll("\"", "&quot;").replaceAll("'",
				"$quot;").replaceAll("#", "");

	}

	// Member variables

	/**
	 * Indicates that the composite is a message
	 */
	public final static int eM7Message = 0;

	/**
	 * Indicates that the composite is a segment
	 */
	public final static int eM7Segment = 1;

	/**
	 * Indicates that the composite is a segment group
	 */
	public final static int eM7SegmentGrp = 2;

	/**
	 * Indicates that the composite is a simple field
	 */
	public final static int eM7Field = 3;

	/**
	 * Indicates that the composite is a composite field
	 */
	public final static int eM7CompositeField = 4;

	/**
	 * References the underlying definition for this element
	 */
	protected M7MessageDefinition definition;

	/**
	 * Delimiter for the absolute name (Default, ".")
	 */
	public static char ABS_NAME_DELM = '.';

	/**
	 * Delimiter for the start of repetion delimiter (Default, "[")
	 */
	public static char ABS_NAME_REPEAT_START_DELIM = '[';

	/**
	 * Delimiter for the end of repetion delimiter (Default, "]")
	 */
	public static char ABS_NAME_REPEAT_END_DELIM = ']';

	/**
	 * Holds the child elements of this composite
	 */
	protected Vector<M7Composite> m_partList = new Vector<M7Composite>();

	private M7Composite m_pParent = null;

	/**
	 * Holds the message delimiter characters: field, component, sub-component,
	 * repetition, escape
	 */
	protected M7Delimiters m_pDelimiters;

	/**
	 * Holds the streamed composite element
	 */
	protected String m_pStreamedData = new String();

	/**
	 * Specifies the composite type
	 */
	protected int m_compType;

	// {{DECLARE_CONTROLS
	// }}

	public M7MessageDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(M7MessageDefinition definition) {
		this.definition = definition;
	}
}
