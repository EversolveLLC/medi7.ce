/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7;

import java.util.Enumeration;
import java.util.Vector;

import com.eversolve.Medi7.resources.Messages;
import com.eversolve.Medi7.util.M7BooleanHolder;
import com.eversolve.Medi7.util.M7IntegerHolder;
import com.eversolve.Medi7.util.M7LongHolder;
import com.eversolve.Medi7.util.M7StringHolder;

/**
 * Medi7 M7Message definition object. It interprets the message definition
 * information in the message definition file. It holds the definition for all
 * types of message elements.
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code> 
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 2.5
 */
public class M7MessageDefinition {
	/**
	 * @param MsgDefFile
	 * @param structure
	 * @param compType
	 * @param name
	 * @param repeats
	 * @param offset
	 * @throws M7Exception
	 */
	protected M7MessageDefinition(M7DefinitionFile MsgDefFile,
			String structure, int compType, String name, boolean repeats,
			long offset) throws M7Exception {
		structureType = structure;
		m_name = name;
		m_isRepeat = repeats;
		m_compositeType = compType;
		m_offset = offset;

		// continue down the chain building the parts
		m_fieldType = eM7NotAField;
		if (m_compositeType == M7Composite.eM7SegmentGrp) {
			String line = MsgDefFile.GetSegGrpDefLine(structureType);
			_ParseLine(line, MsgDefFile);
		} else if (m_compositeType == M7Composite.eM7Segment) {
			String line = MsgDefFile.GetSegDefLine(structureType);
			_ParseLine(line, MsgDefFile);
		} else if (m_compositeType == M7Composite.eM7CompositeField) {
			String line = MsgDefFile.GetCompFldDefLine(structureType);
			_ParseLine(line, MsgDefFile);
		} else if (m_compositeType == M7Composite.eM7Field) {
			// set the field type based on the structure
			if (structureType.equals(FLD_FRMT_STRING)) {
				m_fieldType = eM7String;
			}
			else if (structureType.equals(FLD_FRMT_LONG)) {
				m_fieldType = eM7Long;
			} else if (structureType.equals(FLD_FRMT_DOUBLE)) {
				m_fieldType = eM7Double;
			} else if (structureType.equals(FLD_FRMT_DATETIME)) {
				m_fieldType = eM7DateTime;
			} else if (structureType.equals(FLD_FRMT_DATE)) {
				m_fieldType = eM7Date;
			} else if (structureType.equals(FLD_FRMT_TIME)) {
				m_fieldType = eM7Time;
			} else if (structureType.equals(FLD_FRMT_PHONENUM)) {

				m_fieldType = eM7PhoneNum;
			}
		}

	}
	

	/**
	 * @param msgType
	 * @param MsgDefFile
	 * @throws M7Exception
	 */
	public M7MessageDefinition(String msgType, M7DefinitionFile MsgDefFile)
			throws M7Exception {
		m_msgType = msgType;
		structureType = msgType;
		m_offset = 0;

		m_compositeType = M7Composite.eM7Message;
		m_isRepeat = false;
		m_fieldType = eM7NotAField;

		String line = MsgDefFile.GetMessageDefLine(m_msgType);

		// this will build all child parts and cascade all the way down
		_ParseLine(line, MsgDefFile);
	}

	/**
	 * Indicates whether the child element indicated, is a repeated element
	 * 
	 * @param absoluteName
	 *            Absolute path name
	 * @return True if the element is repeated
	 * @throws M7Exception
	 *             e absolute name is invalid
	 */
	public boolean isChildRepeat(String absoluteName) throws M7Exception {
		M7MessageDefinition MsgDef = this.getChild(absoluteName);
		if (MsgDef == null)
			_ThrowUnableToLocateChildError(absoluteName);

		return MsgDef.isRepeat();
	}

	/**
	 * Throws an exception if the child element definition cannot be found
	 * 
	 * @param data
	 * @throws M7Exception
	 */
	protected void _ThrowUnableToLocateChildError(String data)
			throws M7Exception {
		String strError = Messages
				.getString("M7MessageDefinition.UNABLE_TO_LOCATE") + data; //$NON-NLS-1$
		System.err.println(strError);
		throw new M7Exception(strError, M7Exception.eUnableToLocateChildDef);
	}

	/**
	 * Returns the field type specified in the definition
	 * 
	 * @return Integer, field type
	 */
	public int getFieldType() {
		return m_fieldType;
	}

	/**
	 * Indicates if the child element is a field
	 * 
	 * @param absoluteName
	 *            Specifies the absolute path
	 * @return True if the child element is a field
	 * @throws M7Exception
	 *             Throws an exception if the absolute name is invalid
	 */
	public boolean isChildField(String absoluteName) throws M7Exception {
		M7MessageDefinition MsgDef = this.getChild(absoluteName);
		if (MsgDef == null)
			_ThrowUnableToLocateChildError(absoluteName);

		return MsgDef.isField();
	}

	/**
	 * Indicates whether the element specified is a child
	 * 
	 * @param absoluteName
	 *            Absolute path
	 * @return True if the element is a child
	 * @throws M7Exception
	 *             Throws an exception if the absolute name is invalid
	 */
	public boolean isChild(String absoluteName) throws M7Exception {
		M7MessageDefinition pMsgDef = getChild(absoluteName);
		return (pMsgDef != null);
	}

	/**
	 * Returns the type of element
	 * 
	 * @return M7Composite type
	 * @param absoluteName
	 *            Absolute path to the element
	 * @throws M7Exception
	 *             Throws an exception if the absolute name is invalid
	 */
	public int getChildType(String absoluteName) throws M7Exception {
		M7MessageDefinition pMsgDef = getChild(absoluteName);
		if (pMsgDef == null)
			_ThrowUnableToLocateChildError(absoluteName);

		return pMsgDef.getType();
	}

	/**
	 * Returns the number of child element definitions
	 * 
	 * @return long, number of message definitions in the list
	 */
	public long getChildCount() {
		return m_msgDefList.size();
	}

	/**
	 * Returns the offset, HL7 and ASTM are positionally defined
	 * 
	 * @return long, ofset for the current definition
	 */
	public long getOffset() {
		return m_offset;
	}

	/**
	 * Returns the definition of the child element specified
	 * 
	 * @return M7Composite type
	 * @param absoluteName
	 *            Absolute path to the element
	 * @throws M7Exception
	 *             Throws an exception if the absolute name is invalid
	 */
	public M7MessageDefinition getChild(String absoluteName) throws M7Exception {
		// parse the name passed in
		M7StringHolder h_absoluteName = new M7StringHolder(absoluteName);
		M7LongHolder h_rptIndex = new M7LongHolder(0);
		M7StringHolder h_nameRemainder = new M7StringHolder(M7.EMPTY_STRING);

		String childName = M7Composite._ParseAbsoluteName(h_absoluteName,
				h_nameRemainder, h_rptIndex);
		// System.out.println("Child name: " + childName);
		// look for the name in my collection
		for (Enumeration<M7MessageDefinition> e = m_msgDefList.elements(); e.hasMoreElements();) {
			M7MessageDefinition MsgDef = (M7MessageDefinition) e.nextElement();
			String name = MsgDef.getName();
			if (childName.equalsIgnoreCase(name)) {
				// found but may need to continue if we still have a name
				// remiander
				if (h_nameRemainder.length() > 0) {
					return MsgDef.getChild(h_nameRemainder.toString());
				} else {
					return MsgDef;
				}
			}
		}

		return null;
	}

	/**
	 * Returns the child element definition based on offset
	 * 
	 * @param position
	 *            Element offset or position within its parent element
	 * @return M7MessageDefinition for the child at the specified location
	 */
	public M7MessageDefinition getChild(long position) {
		if (position >= 0 && position < m_msgDefList.size())
			return (M7MessageDefinition) m_msgDefList.elementAt((int) position);

		return null;
	}

	/**
	 * Returns the static profile for the element
	 * 
	 * @param profileId
	 *            Profile identifier as it appears in the file
	 * @param pMsgDefFile
	 *            M7Message definition file containing the static profile
	 * @return M7Profile definition object
	 * @throws M7Exception
	 *             Throws an exception if the profile cannot be found
	 */
	public M7Profile getProfile(String profileId, M7DefinitionFile pMsgDefFile)
			throws M7Exception {
		// look for the profile in the collection of profiles for this message
		for (Enumeration<M7Profile> e = m_profileList.elements(); e.hasMoreElements();) {
			M7Profile p = (M7Profile) e.nextElement();
			if (p.m_profileId.equals(profileId))
				return p;
		}

		// not found so load it
		return _LoadProfile(profileId, pMsgDefFile);
	}

	/**
	 * Indicates if the current element is repeated
	 * 
	 * @return True if it is repeatable
	 */
	public boolean isRepeat() {
		return m_isRepeat;
	}

	/**
	 * Indicates whether the element is a field
	 * 
	 * @return True if it is a field
	 */
	public boolean isField() {
		return (m_compositeType == M7Composite.eM7Field);
	}

	/**
	 * Returns the name of the element
	 * 
	 * @return Element name
	 */
	public String getName() {
		if (m_name.length() == 0)
			return getStructureType();

		return m_name;
	}

	/**
	 * Returns the element type as specified in the message definition file
	 * 
	 * @return Integer, element/composite type
	 */
	public int getType() {
		return m_compositeType;
	}

	/**
	 * Structure type (segment, group, field, etc.)
	 * 
	 * @return String as it appears in the definition file
	 */
	public String getStructureType() {
		return structureType;
	}

	/**
	 * Parses one line of definition file
	 * 
	 * @param line
	 * @param MsgDefFile
	 * @throws M7Exception
	 */
	protected void _ParseLine(String line, M7DefinitionFile MsgDefFile)
			throws M7Exception {
		// System.out.println("Parsing: "+line);
		// strip off up to the equal sign
		int pos = M7.NPOS;

		pos = line.indexOf(M7DefinitionFile.KEY_DELIM);
		if (pos == M7.NPOS)
			_ThrowParsingError(line);

		// nothing in the string past the = sign
		if ((pos + 1) == line.length())
			return;

		// reset the line so upto and including the = sign are gone
		line = line.substring(pos + 1);

		// loop through the entire line creating child definitions for each part
		String part = null;
		long offset = 0;
		while (true) {
			pos = line.indexOf(PART_DELIM);
			if (pos == M7.NPOS) {
				pos = line.indexOf(PART_DELIM_1);
			}
			if (pos == M7.NPOS) {
				part = line.substring(0);
				// part = part.substring( 0, part.length() - 1 );
			} else {
				part = line.substring(0, pos);
			}

			if (part.length() == 0)
				break;

			// parse the part into components
			M7StringHolder h_structure = new M7StringHolder(M7.EMPTY_STRING);
			M7IntegerHolder h_compType = new M7IntegerHolder(
					M7Composite.eM7Message);
			M7StringHolder h_name = new M7StringHolder(M7.EMPTY_STRING);
			M7BooleanHolder h_repeats = new M7BooleanHolder(false);
			_ParseDefParts(part, h_structure, h_compType, h_name, h_repeats);

			String structure = h_structure.toString();
			int compType = h_compType.m_iInt;
			String name = h_name.toString();
			boolean repeats = h_repeats.m_bBool;

			// creating a new child def component
			M7MessageDefinition MsgDef = new M7MessageDefinition(MsgDefFile,
					structure, compType, name, repeats, offset);
			m_msgDefList.addElement(MsgDef);
			if (pos == M7.NPOS)
				break;

			// strip off the part from the line and continue
			line = line.substring(part.length() + 1);

			offset++;
		}
	}

	/**
	 * Parses each message part Change log: <br/>Supports both
	 * <code>&lt; ... &gt;</code> and <code>( ... )</code> definition part
	 * delimiters
	 * 
	 * @param part
	 * @param h_structure
	 * @param h_compType
	 * @param h_name
	 * @param h_repeats
	 * @throws M7Exception
	 */
	protected void _ParseDefParts(String part, M7StringHolder h_structure,
			M7IntegerHolder h_compType, M7StringHolder h_name,
			M7BooleanHolder h_repeats) throws M7Exception {
		// parse the def string into component pieces

		try {
			// System.out.println("Parse part:"+part);
			String strDef = part;
			int pos;
			// get the structure
			pos = strDef.indexOf(TYPE_BEGIN_DELIM);
			if (pos == M7.NPOS) {
				pos = strDef.indexOf(TYPE_BEGIN_DELIM_1);
			}
			// System.out.println(strDef);
			h_structure.setString(strDef.substring(0, pos));
			strDef = strDef.substring(pos + 1);

			// get the type
			pos = strDef.indexOf(TYPE_END_DELIM);
			if (pos == M7.NPOS) {
				pos = strDef.indexOf(TYPE_END_DELIM_1);
			}
			String type = strDef.substring(0, pos);

			// the name is whatever is left
			h_name.setString(strDef.substring(pos + 1));

			// determine if it is a repeat
			h_repeats.m_bBool = false;
			if (type.charAt(type.length() - 1) == REPEAT_CHAR) {
				h_repeats.m_bBool = true;
				// strip off the R
				type = type.substring(0, type.length() - 1);
			}

			if (type.equalsIgnoreCase(SEG_TYPE))
				h_compType.m_iInt = M7Composite.eM7Segment;
			else if (type.equalsIgnoreCase(SEG_GRP_TYPE))
				h_compType.m_iInt = M7Composite.eM7SegmentGrp;
			else if (type.equalsIgnoreCase(FLD_TYPE))
				h_compType.m_iInt = M7Composite.eM7Field;
			else if (type.equalsIgnoreCase(COMP_FLD_TYPE))
				h_compType.m_iInt = M7Composite.eM7CompositeField;
			else
				_ThrowParsingError(type);
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
			_ThrowParsingError(part);
		}
	}

	/**
	 * Throws a message definition parsing error
	 * 
	 * @param data
	 * @throws M7Exception
	 */
	protected void _ThrowParsingError(String data) throws M7Exception {
		String error = Messages
				.getString("M7MessageDefinition.PARSING_ERROR_MESSAGE") + data; //$NON-NLS-1$
		System.err.println(error);
		throw new M7Exception(error, M7Exception.eMsgDefParsingError);
	}

	/**
	 * Loads the message profile from the definition file
	 * 
	 * @param profileId
	 * @param MsgDefFile
	 * @return
	 * @throws M7Exception
	 */
	protected M7Profile _LoadProfile(String profileId,
			M7DefinitionFile MsgDefFile) throws M7Exception {
		M7Profile Prof = new M7Profile(profileId, this, MsgDefFile);
		m_profileList.addElement(Prof);
		return Prof;
	}

	/**
	 * Validates the composite value against the profile specified in the
	 * definition file
	 * 
	 * @param profileId
	 * @param pMsgDefFile
	 * @param pComp
	 * @param h_errors
	 * @return true if valid according to the profile
	 * @throws M7Exception
	 */
	protected boolean _Validate(String profileId, M7DefinitionFile pMsgDefFile,
			M7Composite pComp, M7StringHolder h_errors) throws M7Exception {
		// look for the profile in the collection of profiles for this message
		boolean bFound = false;
		for (Enumeration<M7Profile> e = m_profileList.elements(); e.hasMoreElements();) {
			M7Profile p = (M7Profile) e.nextElement();
			if (p.m_profileId.equals(profileId)) {
				bFound = true;
				break;
			}
		}

		// not found so load it
		if (bFound == false)
			_LoadProfile(profileId, pMsgDefFile);

		this._ApplyProfile(profileId, pComp, h_errors);

		// we have errors
		if (h_errors.length() > 0)
			return false;

		return true;

	}

	/**
	 * Applies a profile for validation
	 */
	protected void _ApplyProfile(String profileId, M7Composite pComp,
			M7StringHolder h_errors) throws M7Exception {
		// look for the profile in the collection of profiles for this message
		for (Enumeration<M7Profile> e = m_profileList.elements(); e.hasMoreElements();) {
			M7Profile p = (M7Profile) e.nextElement();
			if (p.m_profileId.equals(profileId)) {
				p._ApplyRules(this, pComp, h_errors); // apply the rules
				return;
			}
		}

		String strError = Messages
				.getString("M7MessageDefinition.MISSING_PROFILE") + profileId; //$NON-NLS-1$
		throw new M7Exception(strError, M7Exception.eProfileNotFound);
	}

	// protected constructor used for building child parts
	// M7MessageDefinition( M7DefinitionFile* pMsgDefFile, M7String& structure,
	// M7Composite::eM7CompositeType& compType, M7String& name, bool& repeats,
	// long offset );

	// Member variables
	public static final int eM7NotAField = 0;

	public static final int eM7String = 1;

	public static final int eM7Long = 2;

	public static final int eM7Double = 3;

	public static final int eM7DateTime = 4;

	public static final int eM7Date = 5;

	public static final int eM7Time = 6;

	public static final int eM7PhoneNum = 7;

	/**
	 * The static <code>TYPE_BEGIN_DELIM</code> is not final because the value
	 * may be either <code>'&lt;'</code> or <code>'('</code>
	 */
	public static char TYPE_BEGIN_DELIM = '(';

	public static char TYPE_BEGIN_DELIM_1 = '<';

	/**
	 * The static <code>TYPE_BEGIN_DELIM</code> is not final because the value
	 * may be either <code>'&gt;'</code> or <code>')'</code>
	 */
	public static char TYPE_END_DELIM = ')';

	public static char TYPE_END_DELIM_1 = '>';

	public final static char PART_DELIM = '~';

	public final static char PART_DELIM_1 = '|';

	public final static char REPEAT_CHAR = 'R';

	public final static String SEG_TYPE = Messages
			.getString("M7MessageDefinition.SEGMENT_MNEMONIC"); //$NON-NLS-1$

	public final static String SEG_GRP_TYPE = Messages
			.getString("M7MessageDefinition.SEGMENT_GROUP_MNEMONIC"); //$NON-NLS-1$

	public final static String FLD_TYPE = Messages
			.getString("M7MessageDefinition.FIELD_MNEMONIC"); //$NON-NLS-1$

	public final static String COMP_FLD_TYPE = Messages
			.getString("M7MessageDefinition.COMPOSITE_FIELD_MNEMONIC"); //$NON-NLS-1$

	public final static String FLD_FRMT_STRING = Messages
			.getString("M7MessageDefinition.DT_ST"); //$NON-NLS-1$

	public final static String FLD_FRMT_LONG = Messages
			.getString("M7MessageDefinition.DT_LONG"); //$NON-NLS-1$

	public final static String FLD_FRMT_DOUBLE = Messages
			.getString("M7MessageDefinition.DT_DOUBLE"); //$NON-NLS-1$

	public final static String FLD_FRMT_DATETIME = Messages
			.getString("M7MessageDefinition.DATETIME"); //$NON-NLS-1$

	public final static String FLD_FRMT_DATE = Messages
			.getString("M7MessageDefinition.DATE"); //$NON-NLS-1$

	public final static String FLD_FRMT_TIME = Messages
			.getString("M7MessageDefinition.TIME"); //$NON-NLS-1$

	public final static String FLD_FRMT_PHONENUM = Messages
			.getString("M7MessageDefinition.TEL_NUM"); //$NON-NLS-1$

	public final static char MSG_TYPE_FLD_DELIM = ':';

	/**
	 * Messge type
	 */
	protected String m_msgType;

	/**
	 * Holds the message definition list
	 */
	protected Vector<M7MessageDefinition> m_msgDefList = new Vector<M7MessageDefinition>();

	/**
	 * Holds the message profile list
	 */
	protected Vector<M7Profile> m_profileList = new Vector<M7Profile>();

	/**
	 * Element type
	 */
	protected int m_compositeType;

	/**
	 * Primitive structure types such as segment, group, field, composite,
	 * date/time, string, phone. These appear in the message definition file as
	 * S, SG, CM, DT, DM, TN, ST, etc.
	 */
	protected String structureType = new String();

	/**
	 * Element name
	 */
	protected String m_name = new String();

	/**
	 * Repeatable flag
	 */
	protected boolean m_isRepeat = false;

	/**
	 * Field type
	 */
	protected int m_fieldType;

	/**
	 * Position within its parent element
	 */
	protected long m_offset;
	// {{DECLARE_CONTROLS
	// }}
}