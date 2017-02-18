/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.eversolve.Medi7.resources.Messages;

/**
 * This class is the starting point in working with the Medi7 parser. You must
 * have a message definition file with valid message configurations in order to
 * use the parser. A reference to this class is passed in when you create a
 * message and when you parse a message from a stream. This approach allows you
 * to use multiple message definition files.
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @version Medi7 Version 2.0
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * 
 * @com.register ( clsid=E158D7B7-B7E7-11D2-BA43-004005445EAC,
 *               typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 */
public class M7DefinitionFile {
	/**
	 * Default constructor. If used then you must also call Init before using
	 * this object.
	 * 
	 * @see M7DefinitionFile#Init
	 */
	public M7DefinitionFile() {
	}

	/**
	 * Initializes or re-initializes this object with the message definition
	 * file specified "0" is the default start position for message definition
	 * scan. Change log: <br/>
	 * <ul>
	 * <li>Modified this entry from <code>-1</code> to <code>0</code>.
	 * This way the file is searched for each key.</li>
	 * </ul>
	 * 
	 * @param fileName
	 *            the name of the message definition file to open.
	 * 
	 * @exception M7Exception
	 * @exception java.io.IOException
	 *                Thrown if the file cannot be opened.
	 */
	public void initialize(String fileName) throws M7Exception,
			java.io.IOException {
		_checkTimeBomb();
		m_fileName = fileName;
		if ((m_fileName == null) || (m_fileName.length() == 0)) {
			throw new M7Exception(Messages.getString("M7DefinitionFile.MISSING_FILE_NAME")); //$NON-NLS-1$
		}
		m_beginMsgSectionPos = 0;
		m_beginSegGrpSectionPos = 0;
		m_beginSegSectionPos = 0;
		m_beginCompFldSectionPos = 0;
		m_beginMsgProfileSectionPos = 0;
		m_beginSegGrpProfileSectionPos = 0;
		m_beginSegProfileSectionPos = 0;
		m_beginCompFldProfileSectionPos = 0;

		// open the file
		m_file = new RandomAccessFile(m_fileName, Messages
				.getString("M7DefinitionFile.READ_ONLY")); //$NON-NLS-1$

		// find positions for various sections
		boolean bContinue = true;
		long lCurPos = 0;

		boolean msgSectionIsCurrent = false;
		boolean msgProfileSectionIsCurrent = false;

		while (bContinue) {
			// find my msg def
			String strLine = m_file.readLine();

			if (strLine == null) {
				bContinue = false;
				continue;
			}
			strLine = strLine.trim();
			if (strLine.length() == 0)
				continue;

			// System_out_println( "Read: " + strLine );

			if (strLine.startsWith(MSG_SECTION)) {
				m_beginMsgSectionPos = m_file.getFilePointer();
				lCurPos = m_beginMsgSectionPos;
				msgSectionIsCurrent = true;
				msgProfileSectionIsCurrent = false;
				continue;
			}

			if (strLine.startsWith(SEG_GRP_SECTION)) {
				m_beginSegGrpSectionPos = m_file.getFilePointer();
				lCurPos = m_beginSegGrpSectionPos;
				msgSectionIsCurrent = false;
				msgProfileSectionIsCurrent = false;
				continue;
			}

			if (strLine.startsWith(SEG_SECTION)) {
				m_beginSegSectionPos = m_file.getFilePointer();
				lCurPos = m_beginSegSectionPos;
				msgSectionIsCurrent = false;
				msgProfileSectionIsCurrent = false;
				continue;
			}

			if (strLine.startsWith(COMP_FIELD_SECTION)) {
				m_beginCompFldSectionPos = m_file.getFilePointer();
				lCurPos = m_beginCompFldSectionPos;
				msgSectionIsCurrent = false;
				msgProfileSectionIsCurrent = false;
				continue;
			}

			if (strLine.startsWith(MSG_PROFILES_SECTION)) {
				m_beginMsgProfileSectionPos = m_file.getFilePointer();
				lCurPos = m_beginMsgProfileSectionPos;
				msgSectionIsCurrent = false;
				msgProfileSectionIsCurrent = true;
				continue;
			}

			if (strLine.startsWith(SEG_GRP_PROFILES_SECTION)) {
				m_beginSegGrpProfileSectionPos = m_file.getFilePointer();
				lCurPos = m_beginSegGrpProfileSectionPos;
				msgSectionIsCurrent = false;
				msgProfileSectionIsCurrent = false;
				continue;
			}

			if (strLine.startsWith(SEG_PROFILES_SECTION)) {
				m_beginSegProfileSectionPos = m_file.getFilePointer();
				lCurPos = m_beginSegProfileSectionPos;
				msgSectionIsCurrent = false;
				msgProfileSectionIsCurrent = false;
				continue;
			}

			if (strLine.startsWith(COMP_FIELD_PROFILES_SECTION)) {
				m_beginCompFldProfileSectionPos = m_file.getFilePointer();
				lCurPos = m_beginCompFldProfileSectionPos;
				msgSectionIsCurrent = false;
				msgProfileSectionIsCurrent = false;
				continue;
			}

			int pos = strLine.indexOf(KEY_DELIM);
			if (pos != -1) {
				String sKey = String.valueOf(lCurPos)
						+ Messages.getString("M7DefinitionFile.COLON") + strLine.substring(0, pos); //$NON-NLS-1$
				hKeyCache.put(sKey, strLine);
				String originalKey = strLine.substring(0, pos).trim();
				if ((msgSectionIsCurrent)
						|| (originalKey.matches("^[A-Z]{3}:[A-Z][0-9]{2}$"))) { //$NON-NLS-1$

					m_msgTypeList.addElement(originalKey);
				}

				else if ((msgProfileSectionIsCurrent)
						|| (originalKey.matches("^[A-Z]{3}:[A-Z][0-9]{2}:.*$"))) { //$NON-NLS-1$

					m_profileTypeList.addElement(originalKey);
				}

			}
		}
	}

	/**
	 * Initializes a new instance of this object with the message definition
	 * file specified
	 * 
	 * @param fileName
	 *            the name of the message definition file to open.
	 * @exception M7Exception
	 * @exception java.io.IOException
	 *                Thrown if the file cannot be opened.
	 * 
	 */
	public M7DefinitionFile(String fileName) throws M7Exception,
			java.io.IOException {
		initialize(fileName);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() {
		try {
			m_file.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Retrieves the M7MessageDefinition object. This may be retrieved from
	 * cache or built the first time if it is not already in the cache.
	 * 
	 * @param msgType
	 *            A string that corresponds to a message that exists in the
	 *            message definition file. This is the string that appears
	 *            before the "=" sign in the [MESSAGES] section of a message
	 *            definition file.
	 * 
	 * @exception M7Exception
	 *                Thrown if the msgType cannot be located in the message
	 *                definition file or if an error occurs while loading and
	 *                parsing the message definition.
	 * 
	 */
	public synchronized M7MessageDefinition getDefinition(String msgType)
			throws M7Exception {

		// first look in collection for a def of the same message type
		for (Enumeration<M7MessageDefinition> e = m_msgDefList.elements(); e.hasMoreElements();) {
			M7MessageDefinition msgDef = (M7MessageDefinition) e.nextElement();
			if (msgDef.m_msgType.equalsIgnoreCase(msgType))
				return msgDef;
		}

		// did not find a match, create it and add to collection
		M7MessageDefinition msgDef = new M7MessageDefinition(msgType, this);
		m_msgDefList.addElement(msgDef);

		return msgDef;
	}

	protected String GetMessageDefLine(String msgName) throws M7Exception {
		return _LookForKeyInSection(msgName, m_beginMsgSectionPos);
	}

	/**
	 * Returns a list of all the message types contained within the definition
	 * file used to construct or initialize this object. A COM client can
	 * construct the following code using the returned IDispatch:
	 * 
	 * Dim list, mdf As Object Set mdf = New M7DefinitionFile mdf.Init
	 * "M7MsgDf.dat" Set list = mdf.GetMsgTypeList For i = 0 To list.Size() - 1
	 * Debug.Print list.elementAt(i) Next
	 * 
	 * @return A Vector of strings, one string for each message type in the
	 *         message definition file.
	 */
	public Vector<String> GetMsgTypeList() {
		return m_msgTypeList;
	}

	/**
	 * Returns a list of all the message types contained within the definition
	 * file used to construct or initialize this object. A COM client can
	 * construct the following code using the returned IDispatch:
	 * 
	 * Dim list, mdf As Object Set mdf = New M7DefinitionFile mdf.Init
	 * "M7MsgDf.dat" Set list = mdf.GetMsgTypesEnum While (list.hasMoreElements)
	 * Debug.Print list.nextElement() Wend
	 * 
	 * @return An enumeration of String objects.
	 */
	public Enumeration<String> GetMsgTypesEnum() {
		return m_msgTypeList.elements();
	}

	/**
	 * Returns a list of all the message profile types contained within the
	 * definition file used to construct or initialize this object. A COM client
	 * can construct the following code using the returned IDispatch:
	 * 
	 * Dim list, mdf As Object Set mdf = New M7DefinitionFile mdf.Init
	 * "M7MsgDf.dat" Set list = mdf.GetMsgProfileTypesEnum While
	 * (list.hasMoreElements) Debug.Print list.nextElement() Wend
	 * 
	 * @return An enumeration of String objects.
	 */
	public Enumeration<String> GetMsgProfileTypesEnum() {
		return m_profileTypeList.elements();
	}

	// M7MessageDefinition GetSegGrpDef( String grpName ) throws M7Exception
	// {
	// return _LookupInMasterList( m_masterSegGrpList, grpName );
	// }

	// M7MessageDefinition GetSegDef( String segName ) throws M7Exception
	// {
	// return _LookupInMasterList( m_masterSegList, segName );
	// }

	protected String GetSegGrpDefLine(String grpName) throws M7Exception {
		return _LookForKeyInSection(grpName, m_beginSegGrpSectionPos);
	}

	protected String GetSegDefLine(String segName) throws M7Exception {
		return _LookForKeyInSection(segName, m_beginSegSectionPos);
	}

	protected String GetCompFldDefLine(String compFldName) throws M7Exception {
		return _LookForKeyInSection(compFldName, m_beginCompFldSectionPos);
	}

	protected String GetMessageProfileLine(String profileName)
			throws M7Exception {
		return _LookForKeyInSection(profileName, m_beginMsgProfileSectionPos);
	}

	protected String GetSegGrpProfileLine(String profileName)
			throws M7Exception {
		return _LookForKeyInSection(profileName, m_beginSegGrpProfileSectionPos);
	}

	protected String GetSegProfileLine(String profileName) throws M7Exception {
		return _LookForKeyInSection(profileName, m_beginSegProfileSectionPos);
	}

	protected String GetCompFldProfileLine(String profileName)
			throws M7Exception {
		return _LookForKeyInSection(profileName,
				m_beginCompFldProfileSectionPos);
	}

	protected String _LookForKeyInSection(String key, long startPos)
			throws M7Exception {
		String sHashKey = String.valueOf(startPos)
				+ Messages.getString("M7DefinitionFile.COLON") + key; //$NON-NLS-1$
		String sKeyLine = (String) hKeyCache.get(sHashKey);
		// System.out.println(sHashKey);

		if (sKeyLine == null) {
			String sError = Messages.getString("M7DefinitionFile.MISSING_KEY") + '"' + key + '"' + Messages.getString("M7DefinitionFile.IN_FILE") + m_fileName; //$NON-NLS-1$ //$NON-NLS-2$
			M7Exception e = new M7Exception(sError,
					M7Exception.eUnableToFindKeyInDefFile);
			e.printStackTrace();
			throw e;
		}

		return sKeyLine;
	}

	protected String _LookForKeyInSectionx(String key, long startPos)
			throws M7Exception {
		// start at beginning
		// bool b = m_file.fail(); ???

		// System.out
		// .println(Messages.getString("M7DefinitionFile.LOOKING_FOR_KEY") +
		// key); //$NON-NLS-1$

		try {
			m_file.seek(startPos);

			if (key.length() == 0) {
				String sMessage = Messages
						.getString("M7DefinitionFile.MISSING_KEY_IN_FILE") + m_fileName; //$NON-NLS-1$
				System.err.println(sMessage);
				throw new M7Exception(sMessage,
						M7Exception.eUnableToFindKeyInDefFile);
			}

			key += KEY_DELIM;

			String strLine = null;
			boolean bContinue = true;
			while (bContinue) {
				strLine = m_file.readLine().trim();
				if (strLine == null) {
					bContinue = false;
					continue;
				}
				strLine = strLine.trim();
				if (strLine.length() == 0)
					continue;

				if (strLine.indexOf(key) != -1) {
					// System.out
					// .println(Messages.getString("M7DefinitionFile.FOUND") +
					// strLine); //$NON-NLS-1$
					return strLine;
				}

				// we hit the beginning of a new section so we are outta here
				if (strLine.charAt(0) == SECTION_BEGIN_DELIM) {
					break;
				}
			}
		} catch (IOException e) {
			String sError = Messages
					.getString("M7DefinitionFile.UNABLE_TO_OPEN") + m_fileName; //$NON-NLS-1$
			// System.out.println(sError);
			throw new M7Exception(sError, M7Exception.eUnableToFindKeyInDefFile);
		}

		// not found
		String sError = Messages.getString("M7DefinitionFile.MISSING_KEY") + key + Messages.getString("M7DefinitionFile.IN_FILE") + m_fileName; //$NON-NLS-1$ //$NON-NLS-2$
		throw new M7Exception(sError, M7Exception.eUnableToFindKeyInDefFile);
	}

	protected M7MessageDefinition _LookupInMasterList(Vector<M7MessageDefinition> deflist,
			String name) {
		for (Enumeration<M7MessageDefinition> e = deflist.elements(); e.hasMoreElements();) {
			M7MessageDefinition msgDef = (M7MessageDefinition) e.nextElement();

			if (name.equalsIgnoreCase(msgDef.getName()))
				return msgDef;
		}

		return null;
	}

	/**
	 * Check the flag and time bomb for evaluation
	 * 
	 * @throws M7Exception
	 * @deprecated
	 */
	protected void _checkTimeBomb() throws M7Exception {
		if (M7.EVALUATION) {
			java.util.Date dtNow = new Date();
			java.util.Date dtEndEval = new Date((2006 - 1900), 1, 30); // 0
			// based
			// month

			if (dtNow.after(dtEndEval))
				throw new M7Exception(
						Messages
								.getString("M7DefinitionFile.EVALUATION_EXPIRED"), M7Exception.eEvalCopyExpired); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the current version of the Medi7 Parser
	 */
	public String GetVersion() {
		return Messages.getString("M7DefinitionFile.PRODUCT_VERSION"); //$NON-NLS-1$
	}

	protected static final String MSG_SECTION = Messages
			.getString("M7DefinitionFile.MESSAGE_SECTION"); //$NON-NLS-1$

	protected static final String SEG_GRP_SECTION = Messages
			.getString("M7DefinitionFile.GROUP_SECTION"); //$NON-NLS-1$

	protected static final String SEG_SECTION = Messages
			.getString("M7DefinitionFile.SEGMENT_SECTION"); //$NON-NLS-1$

	protected static final String COMP_FIELD_SECTION = Messages
			.getString("M7DefinitionFile.COMPOSITE_SECTION"); //$NON-NLS-1$

	protected static final String MSG_PROFILES_SECTION = Messages
			.getString("M7DefinitionFile.MESSAGE_PROFILE"); //$NON-NLS-1$

	protected static final String SEG_GRP_PROFILES_SECTION = Messages
			.getString("M7DefinitionFile.GROUP_PROFILE"); //$NON-NLS-1$

	protected static final String SEG_PROFILES_SECTION = Messages
			.getString("M7DefinitionFile.SEGMENT_PROFILE"); //$NON-NLS-1$

	protected static final String COMP_FIELD_PROFILES_SECTION = Messages
			.getString("M7DefinitionFile.COMPOSITE_PROFILE"); //$NON-NLS-1$

	protected static final String DEFAULT_FILE_NAME = Messages
			.getString("M7DefinitionFile.DEFAULT_FILE"); //$NON-NLS-1$

	protected static final char KEY_DELIM = '=';

	protected static final char SECTION_BEGIN_DELIM = '[';

	protected static final long MAX_LINE_LENGTH = 4096;

	protected String m_fileName;

	protected RandomAccessFile m_file;

	protected long m_beginMsgSectionPos;

	protected long m_beginSegGrpSectionPos;

	protected long m_beginSegSectionPos;

	protected long m_beginCompFldSectionPos;

	protected long m_beginMsgProfileSectionPos;

	protected long m_beginSegGrpProfileSectionPos;

	protected long m_beginSegProfileSectionPos;

	protected long m_beginCompFldProfileSectionPos;

	protected Vector<M7MessageDefinition> m_msgDefList = new Vector<M7MessageDefinition>();

	protected Vector<String> m_masterSegGrpList = new Vector<String>();

	protected Vector<String> m_masterSegList = new Vector<String>();

	protected Vector<String> m_pProfileList = new Vector<String>();

	protected Hashtable<String, String> hKeyCache = new Hashtable<String, String>();

	protected Vector<String> m_msgTypeList = new Vector<String>();

	protected Vector<String> m_profileTypeList = new Vector<String>();
}