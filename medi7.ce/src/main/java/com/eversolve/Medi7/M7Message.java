/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import com.eversolve.Medi7.resources.Messages;
import com.eversolve.Medi7.util.M7StringHolder;

/**
 * This class is the starting point for either constructing a message or for
 * parsing a message either from stream or file. A message is one single HL7
 * message and must begin with an MSH segment. All methods to either construct a
 * new message or to parse a message requires a reference to an M7DefinitionFile
 * object. By not making the M7DefinitionFile object global (static) multiple
 * instances of M7DefinitionFile can be used in the implementation. <br/>
 * <code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * 
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 2.3
 * @com.register ( clsid=E158D7B5-B7E7-11D2-BA43-004005445EAC,
 *               typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 */
public class M7Message extends M7Composite {
	/**
	 * Default constructor. You must call CreateNewMsg, ParseMsg, or
	 * LoadPersistedMessage after calling this constructor.
	 * 
	 * @see M7Message#CreateNewMsg
	 * @see M7Message#ParseMsg
	 * @see M7Message#LoadPersistedMessage
	 */
	public M7Message() {
		super(M7Composite.eM7Message);
	}

	/**
	 * This constructs a new instance of a message where all of the values in
	 * the message are empty. It calls CreateNewMsg.
	 * 
	 * @param msgDefFile
	 *            A reference to an initialized message definition file object.
	 * @param msgType
	 *            A string that corresponds to a message that exists in the
	 *            message definition file. This is the string that appears
	 *            before the "=" sign in the [MESSAGES] section of a message
	 *            definition file.
	 * 
	 * @exception M7Exception
	 *                Thrown if the msgType cannot be located in the message
	 *                definition file or if an error occurs while loading the
	 *                message definition.
	 * 
	 * @see M7Message#CreateNewMsg
	 */
	public M7Message(M7DefinitionFile msgDefFile, String msgType)
			throws M7Exception {
		super(
				Messages.getString("M7Message.M7MESSAGE"), M7Composite.eM7Message, null); //$NON-NLS-1$
		//ASTM fix to automatically detect standard type
		if(msgType.contains(ASTM_STD))
		{
			this.setStandard(ASTM_STD);
		}
		createMessage(msgDefFile, msgType);
		this.setMessgeDefinitionFile(msgDefFile);
	}

	/**
	 * Constructor that sets the profile id
	 * 
	 * @param msgDefFile
	 * @param msgType
	 * @param messageProfileId
	 * @throws M7Exception
	 */
	public M7Message(M7DefinitionFile msgDefFile, String msgType,
			String messageProfileId) throws M7Exception {
		this(msgDefFile, msgType);
		this.setMessageProfileId(messageProfileId);

	}

	/**
	 * This will remove all values that currently exist in the message and
	 * reinitialize this message object for the msgType specified.
	 * 
	 * @param msgDefFile
	 *            A reference to an initialized message definition file object.
	 * @param msgType
	 *            A string that corresponds to a message that exists in the
	 *            message definition file. This is the string that appears
	 *            before the "=" sign in the [MESSAGES] section of a message
	 *            definition file.
	 * 
	 * @exception M7Exception
	 *                Thrown if the msgType cannot be located in the message
	 *                definition file or if an error occurs while loading the
	 *                message definition.
	 */
	public void createMessage(M7DefinitionFile msgDefFile, String msgType)
			throws M7Exception {

		checkEvaluationCounter();

		// cleanup in case msg object is reused
		deleteAllChildren();
		// set the message type for this message instance
		m_msgType = msgType;
		// load a msg def
		definition = msgDefFile.getDefinition(m_msgType);
		this.setMessgeDefinitionFile(msgDefFile);
	}

	/**
	 * Use this constructor to parse a message from a stream. This constructor
	 * calls ParseMessage.
	 * 
	 * @param msgStream
	 *            The message stream to parse.
	 * @param msgDefFile
	 *            A reference to an initialized message definition file object.
	 * 
	 * @exception M7Exception
	 *                Thrown if a parsing error occurs or if there is no message
	 *                definition in the message definition file that corresponds
	 *                to the message type in the message header.
	 * 
	 * @see M7Message#ParseMsg
	 */
	public M7Message(String msgStream, M7DefinitionFile msgDefFile)
			throws M7Exception {
		super(
				Messages.getString("M7Message.M7MESSAGE"), M7Composite.eM7Message, null); //$NON-NLS-1$

		parseMessage(msgStream.trim() + M7Message.MSG_TERMINATE_CHAR,
				msgDefFile);
		this.setMessgeDefinitionFile(msgDefFile);
	}

	/**
	 * <p>
	 * Use this method to parse a message from a message stream and build an
	 * object graph of the message that can then be validated and traversed
	 * using absolute names. The message is parsed using the delimiters
	 * specified in the MSH segment. The MSH segment must be the first segment.
	 * In order to correlate the stream to a message definition in the message
	 * definition file, the message type code, event type code, and any
	 * additional fields in the message type composite field are strung together
	 * with colon (:) delimiters. This concatenated value is used as the message
	 * type to look up in the message definition file.
	 * </p>
	 * <p>
	 * For instance:
	 * <ul>
	 * <li>...|ADT^A01|...becomes ADT:A01</li>
	 * <li>...|ORM^001^DIET|...becomes ORM:001:DIET</li>
	 * </ul>
	 * </p>
	 * <p>
	 * If the stream has the Minimal Low Level Protocol delimiters at the
	 * beginning and/or end of the message, then they are removed before parsing
	 * the message.
	 * </p>
	 * <p>
	 * The parser will use the delimiters identified in the pre-defined segment
	 * locations.
	 * </p>
	 * 
	 * @param stream
	 *            The message stream to parse.
	 * @param msgDefFile
	 *            A reference to an initialized message definition file object.
	 * 
	 * @exception M7Exception
	 *                Thrown if a parsing error occurs or if there is no message
	 *                definition in the message definition file that corresponds
	 *                to the message type in the message header.
	 *                <p>
	 *                Change log:
	 *                <ul>
	 *                <li>Strip off message framing for MLLP if it exist</li>
	 *                </ul>
	 *                </p>
	 * @version 2.0
	 * 
	 * 
	 */
	public void parseMessage(String stream, M7DefinitionFile msgDefFile)
			throws M7Exception {

		checkEvaluationCounter();

		// cleanup in case msg object is reused
		deleteAllChildren();

		String msgStream = stream.trim() + MSG_TERMINATE_CHAR;

		// strip off message delimiters if they exist
		if ((msgStream.length() > 0) && (msgStream.charAt(0) == MSG_BEGIN_CHAR))
			msgStream = msgStream.substring(1);
		if ((msgStream.length() > 1)
				&& (msgStream.charAt(msgStream.length() - 1) == MSG_TERMINATE_CHAR)) {
			if ((msgStream.length() > 0)
					&& (msgStream.charAt(msgStream.length() - 2) == MSG_END_CHAR))
				msgStream = msgStream.substring(0, msgStream.length() - 2);
		}

		// validate that the first 3 characters are the MSH
		int pos = msgStream.indexOf(MSH_SEG_NAME);
		if (pos != 0) {
			pos = msgStream.indexOf(H_SEG_NAME);

			if (pos != 0) {
				_ThrowUnstreamError(stream, Messages
						.getString("M7Message.MISSING_HEADER")); //$NON-NLS-1$
			} else
				this.setStandard(ASTM_STD);

		}

		// get the delimiters
		// MSH|^~\&
		if (msgStream.length() < 8)
			_ThrowUnstreamError(stream, Messages
					.getString("M7Message.MISSING_DELIMS")); //$NON-NLS-1$

		String delimStr = null;

		M7Delimiters delimiters = new M7Delimiters();
		if (isHL7()) // ASTM fix
		{
			delimStr = msgStream.substring(3, 8);
			delimiters.setFieldDelimiter(delimStr.charAt(0));
			delimiters.setComponentDelimiter(delimStr.charAt(1));
			delimiters.setRepetitionDelimiter(delimStr.charAt(2));
			delimiters.setEscapeDelimiter(delimStr.charAt(3));
			delimiters.setSubComponentDelimiter(delimStr.charAt(4));
		} else // ASTM fix
		{
			delimStr = msgStream.substring(1, 5);
			delimiters.setFieldDelimiter(delimStr.charAt(0));
			delimiters.setRepetitionDelimiter(delimStr.charAt(1));
			delimiters.setComponentDelimiter(delimStr.charAt(2));
			delimiters.setEscapeDelimiter(delimStr.charAt(3));
		}

		// get the message type, eighth field in the MSH so look past 8
		// delimiters
		pos = M7.NPOS;
		if (isHL7()) // ASTM fix
		{
			for (int i = 0; i < 8; i++) {
				pos = msgStream.indexOf(delimiters.getFieldDelimiter(), pos);
				pos++; // increase so our next find moves past the one we just
				// found
				if (pos == M7.NPOS)
					_ThrowUnstreamError(stream, Messages
							.getString("M7Message.MISSING_MESSAGE_TYPE")); //$NON-NLS-1$
			}

			m_msgType = msgStream.substring(pos);
			// look for the next field delim and strip off anything past
			m_msgType = m_msgType.substring(0, m_msgType.indexOf(delimiters
					.getFieldDelimiter()));
		} else {
			m_msgType = ASTM_BATCH; // ASTM fix

		}
		// replace the component delimeters with the key field delimieter ":"
		pos = 0;

		StringTokenizer st = new StringTokenizer(m_msgType, String
				.valueOf(delimiters.getComponentDelimiter()));
		int i = 0;
		String tempMsgType = M7.EMPTY_STRING;
		while ((st.hasMoreTokens()) && (i < 2)) {
			tempMsgType += st.nextToken();
			i++;
			if (i < 2)// pick up only the first two components
			{
				tempMsgType += String
						.valueOf(M7MessageDefinition.MSG_TYPE_FLD_DELIM);
			}
		}
		// StringBuffer b = new StringBuffer(m_msgType);
		// while( true )
		// {
		// pos = m_msgType.indexOf( delimiters.componentDelim, pos );
		// if( pos == M7.NPOS )
		// break;
		//
		// b.setCharAt(pos , M7MessageDefinition.MSG_TYPE_FLD_DELIM );
		// pos++;
		// }

		// m_msgType = new String( b );
		m_msgType = tempMsgType;

		// create the message def for this message type
		definition = msgDefFile.getDefinition(m_msgType);

		// now unstream the message
		M7StringHolder h_msgStream = new M7StringHolder(msgStream);

		_Unstream(h_msgStream, delimiters);

		if (h_msgStream.length() > 0) {
			_ThrowUnstreamError(msgStream.toString());
		}
	}

	/**
	 * This method detects the encoding characters at runtime
	 * 
	 * @return a string containing the encoding characters
	 * @throws M7Exception
	 */
	private String getEncodingCharacters() throws M7Exception {
		String delimStr = null;
		if (isHL7()) {
			delimStr = MSH_ENC_CHARS;

		} else // ASTM fix
		{
			delimStr = ASTM_ENC_CHARS;
		}

		try {
			M7Composite comp = this.getChildByPosition(0);
			if (comp != null) {
				M7Field fld = (M7Field) comp.getChildByPosition(0); // first
				// field in "H" or "MSH"
				if (fld != null)
					if (fld.getState() == M7Field.eFldPresent)
						delimStr = fld.getValue();
					else
						fld.setValue(MSH_ENC_CHARS);
			}
		} catch (M7Exception ex) {
		}
		return delimStr;
	}

	/**
	 * This method can be used to validate a message against a given message
	 * profile.
	 * 
	 * @param profileId
	 *            An id to a message profile value that can be found in the
	 *            message definition file in the [MESSAGE_PROFILES] section.
	 * @param msgDefFile
	 *            A reference to an initialized M7MSgDefFile object.
	 * @param h_errors
	 *            A holder for any validation error messages. Each error is
	 *            separated by a newline.
	 * @return false if errors were found, otherwise true meaning it is valid.
	 * 
	 * @exception M7Exception
	 *                Thrown if the profile specified is not found in the
	 *                message definition file.
	 */
	public boolean validate(String profileId, M7DefinitionFile msgDefFile,
			M7StringHolder h_errors) throws M7Exception {
		if (this.getChildCount() == 0) {
			return false;
		}
		StringBuffer errBuf = new StringBuffer();
		if (profileId == null) {
			errBuf
					.append(Messages.getString("M7Message.MISSING_PROFILE_ID")).append('\n'); //$NON-NLS-1$
		}
		if (msgDefFile == null) {
			errBuf
					.append(
							Messages
									.getString("M7Message.MISSING_DEFINITION_FILE")).append('\n'); //$NON-NLS-1$
		}
		if (errBuf.length() > 0) {
			throw new M7Exception(errBuf.toString().trim());
		}

		String strProfileId = profileId;
		return definition._Validate(strProfileId, msgDefFile, this, h_errors);
	}

	/**
	 * Validate the message using the message definition and profile id
	 * referenced when the message was instantiated.
	 * 
	 * @param errors
	 * @return
	 * @throws M7Exception
	 */
	public boolean validate(StringBuffer errors) throws M7Exception {
		M7StringHolder h_errors = new M7StringHolder();
		boolean retval = false;
		this.validate(this.getMessageProfileId(), this
				.getMessgeDefinitionFile(), h_errors);
		errors.append(h_errors);
		return retval;

	}

	/**
	 * This method will convert the message objects into an HL7 ASCII-encoded
	 * stream. It will use the delimiters from the encoding characters field in
	 * the MSH segment if they are present, else it will use the default HL7
	 * recommended encoding characters. Note that this function separates HL7
	 * segments with a single carriage return character.
	 * 
	 * @exception M7Exception
	 *                Thrown if an error occurs during the streaming process.
	 * @return String representation of the object, uses CR as segment delimiter
	 */
	public String stream() throws M7Exception {
		String delimiterStack = new String();
		M7Delimiters delimiters = new M7Delimiters();

		String delimStr = getEncodingCharacters();
		String strError = Messages
				.getString("M7Message.INVALID_ENCODING_CHARS") + " '" + delimStr + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		if (isHL7()) // ASTM fix
		{

			if (delimStr.length() < 4) {

				throw new M7Exception(strError,
						M7Exception.eInvalidEncodingChars);
			} else if (delimStr.length() < 3) {
				throw new M7Exception(strError,
						M7Exception.eInvalidEncodingChars);
			}

			delimiters.setFieldDelimiter(m_streamFieldDelim);
			delimiters.setComponentDelimiter(delimStr.charAt(0));
			delimiters.setRepetitionDelimiter(delimStr.charAt(1));
			delimiters.setEscapeDelimiter(delimStr.charAt(2));
			delimiters.setSubComponentDelimiter(delimStr.charAt(3));
		} else if (this.isASTM()) {//ASTM fix

			if (delimStr.length() < 3) {

				throw new M7Exception(strError,
						M7Exception.eInvalidEncodingChars);
			} else if (delimStr.length() < 3) {
				throw new M7Exception(strError,
						M7Exception.eInvalidEncodingChars);
			}
			delimiters.setFieldDelimiter(m_streamFieldDelim);
			delimiters.setRepetitionDelimiter(delimStr.charAt(0));
			delimiters.setComponentDelimiter(delimStr.charAt(1));
			delimiters.setEscapeDelimiter(delimStr.charAt(2));
		} 
		
		M7StringHolder h_msgStream = new M7StringHolder();
		_Stream(h_msgStream, delimiters, new M7StringHolder(delimiterStack));
		h_msgStream.append(M7.SEGMENT_END_CHAR);
		return h_msgStream.toString();
	}

	/**
	 * This method will convert the message objects into an HL7 ASCII-encoded
	 * stream. It will use the delimiters from the encoding characters field in
	 * the MSH segment if they are present, else it will use the default HL7
	 * recommended encoding characters. Note that this function separates HL7
	 * segments with a carriage return/line feed combination.
	 * 
	 * @exception M7Exception
	 *                Thrown if an error occurs during the streaming process.
	 * @return String representation of the object, uses CR, LF as segment
	 *         delimiters
	 */
	public String stream_CRLF() throws M7Exception {
		String delimiterStack = new String();
		M7Delimiters delimiters = new M7Delimiters();

		String delimStr = getEncodingCharacters();

		
			String strError = Messages
					.getString("M7Message.INVALID_ENCODING_CHARS") + delimStr; //$NON-NLS-1$
		

		if (isHL7()) // ASTM fix
		{
			if (delimStr.length() < 4) {
				throw new M7Exception(strError,
						M7Exception.eInvalidEncodingChars);
			} else if (delimStr.length() < 3) {
				throw new M7Exception(strError,
						M7Exception.eInvalidEncodingChars);
			}
			delimiters.setFieldDelimiter(m_streamFieldDelim);
			delimiters.setComponentDelimiter(delimStr.charAt(0));
			delimiters.setRepetitionDelimiter(delimStr.charAt(1));
			delimiters.setEscapeDelimiter(delimStr.charAt(2));
			delimiters.setSubComponentDelimiter(delimStr.charAt(3));
		} else if(isASTM())// ASTM fix
		{
			delimiters.setFieldDelimiter(m_streamFieldDelim);
			delimiters.setRepetitionDelimiter(delimStr.charAt(0));
			delimiters.setComponentDelimiter(delimStr.charAt(1));
			delimiters.setEscapeDelimiter(delimStr.charAt(2));
			if (delimStr.length() < 3) {
				throw new M7Exception(strError,
						M7Exception.eInvalidEncodingChars);
			} else if (delimStr.length() < 3) {
				throw new M7Exception(strError,
						M7Exception.eInvalidEncodingChars);
			}
		}

		M7StringHolder h_msgStream = new M7StringHolder();
		_Stream_CRLF(h_msgStream, delimiters,
				new M7StringHolder(delimiterStack));

		h_msgStream.append(M7.SEGMENT_END_CHAR_CRLF);

		return h_msgStream.toString();
	}

	/**
	 * Checks whether this is an evaluation installation
	 * 
	 * @throws M7Exception
	 *             Throws an exception if the evaluation exceeded the number of
	 *             messages supported.
	 */
	protected void checkEvaluationCounter() throws M7Exception {
		//if (M7.EVALUATION == true) {
		//	if (messageEvaluationCounter > MAX_EVAL) {
		//		throw new M7Exception(
		//				Messages.getString("M7Message.EVALUATION_LIMIT") + MAX_EVAL, M7Exception.eEvalCopyExpired); //$NON-NLS-1$
		//	}
		//	messageEvaluationCounter++;
		//}
	}

	/**
	 * Use this method to persist a message in HL7 ASCII-encoded form to a file.
	 * The message will be streamed and then written to the file.
	 * 
	 * @param fileName
	 *            The name of the file to persist the message to. Any existing
	 *            file is overwritten.
	 * 
	 * @exception M7Exception
	 *                Thrown if an error occurs during the streaming process.
	 * @exception IOException
	 *                Thrown if an I/O error occurs.
	 * @exception FileNotFoundException
	 *                Thrown if the filename is invalid.
	 * 
	 */
	public void persistMessage(String fileName) throws FileNotFoundException,
			IOException, M7Exception {
		// attempt to open the file
		FileOutputStream file = new FileOutputStream(fileName);
		String stream = this.stream();
		file.write(stream.getBytes());
		file.close();
	}

	/**
	 * This method will retrieve a message from a file and parses the contents
	 * of the fiel according to the message definition.
	 * 
	 * @param fileName
	 *            the name of the file that contains a single HL7 message.
	 * @param msgDefFile
	 *            A reference to an initialized M7MSgDefFile object.
	 * 
	 * @exception M7Exception
	 *                Thrown if a parsing error occurs or if there is no message
	 *                definition in the message definition file that corresponds
	 *                to the message type in the message header.
	 * @exception java.io.IOException
	 *                Thrown if an I/O error occurs.
	 * 
	 * @see M7Message#ParseMsg
	 */
	public void loadPersistedMessage(String fileName,
			M7DefinitionFile msgDefFile) throws M7Exception, IOException {
		// create message tree
		parseMessage(readMessage(fileName), msgDefFile);
		this.setMessgeDefinitionFile(msgDefFile);

	}

	/**
	 * Read the message out of a file
	 * 
	 * @param fileName
	 * @return String containing the
	 * @throws M7Exception
	 * @throws IOException
	 */
	public static String readMessage(String fileName) throws M7Exception,
			IOException {
		// attempt to open the file
		FileInputStream file = new FileInputStream(fileName);

		// read in file
		String msg = new String();
		while (true) {
			byte[] buf = new byte[2048];
			int cnt = file.read(buf);
			if (cnt == -1)
				break;
			msg += new String(buf, 0, cnt);
		}
		file.close();
		return msg;
	}

	/**
	 * Standard HL7 encoding characters
	 * 
	 * @return Concatenate HL7 delimiters
	 */
	public String STANDARD_ENCODING_CHARS() {
		return MSH_ENC_CHARS;
	}

	/**
	 * Indicates whether this message is ASTM
	 * 
	 * @return Boolean, "true" if ASTM
	 */
	public boolean isASTM() {
		return this.m_Standard.equals(ASTM_STD);
	}
	


	/**
	 * Indicates the standard supported by the 
	 * @see 
	 * @return String standard value
	 */
	public String getStandard() {
		return m_Standard;
	}



	/**
	 * Indicates whether this message is HL7
	 * 
	 * @return Boolean, "true" if HL7
	 */
	public boolean isHL7() {
		return this.m_Standard.equals(HL7_STD);
	}

	/**
	 * Sets the standard as ASTM or HL7 as the format is discovered at runtime.
	 * 
	 * @see M7Message#ASTM_STD
	 * @see M7Message#HL7_STD
	 * @param value
	 *            Standard supported.
	 * @throws M7Exception if the standard is not supported
	 */
	private void setStandard(String value)throws M7Exception {
		if (value.equals(ASTM_STD))
		{
			m_Standard = ASTM_STD;
		}
		else if (value.equals(HL7_STD))
		{
			m_Standard = HL7_STD;
		}
		else
		{
			throw new M7Exception(Messages.getString("M7Message.STANDARD_NOT_SUPPORTED_1")+value+Messages.getString("M7Message.STANDARD_NOT_SUPPORTED_2"),M7Exception.eStandardNotSupported); //$NON-NLS-1$ //$NON-NLS-2$
		}
		// else undetermined, X12 in the future
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString() Segments are separated by CR LF
	 */
	public String toString() {
		String retval = Messages.getString("M7Message.INVALID_MESSAGE"); //$NON-NLS-1$
		try {
			retval = stream_CRLF();
		} catch (M7Exception e) {
			retval += Messages.getString("M7Message.COLON") + e.toString(); //$NON-NLS-1$
		}
		return retval;
	}

	/**
	 * Formats the message for sending over MLLP
	 * 
	 * @see M7Message#MSG_BEGIN_CHAR
	 * @see M7Message#MSG_END_CHAR
	 * @return message framed by MLLP framing characters
	 * @exception if the message is not parsable
	 */
	public String getMLLPFrame() throws M7Exception {
		return MSG_BEGIN_CHAR + stream() + MSG_END_CHAR;
	}

	// Member variables
	/**
	 * Static counter of processed messages
	 */
	protected static int messageEvaluationCounter = 0;

	/**
	 * <code>MAX_EVAL</code> indicates the maximum number of messages that could
	 * parsed using the evaluation library
	 */
	public static int MAX_EVAL = 50;

	/**
	 * Constant that holds the name of the HL7 message header segment. Default:
	 * "MSH"
	 */
	public static final String MSH_SEG_NAME = Messages
			.getString("M7Message.MESSAGE_HEADER_SEGMENT"); //$NON-NLS-1$

	/**
	 * Constant that holds the name of the ASTM message header segment. Default:
	 * "H"
	 */
	public static final String H_SEG_NAME = Messages
			.getString("M7Message.ASTM_HEADER"); //$NON-NLS-1$

	private static final char HL7_FIELD_DELIM = '|';

	/**
	 * This variable will default to a "|" (vertical bar) but can be changed for
	 * any instance of a message. This will be used as the field delimiter when
	 * a message is streamed.
	 */
	public char m_streamFieldDelim = HL7_FIELD_DELIM;

	public static final char MSG_BEGIN_CHAR = 0x0B;

	public static final char MSG_END_CHAR = 0x1C;

	public static final char MSG_TERMINATE_CHAR = 0x0D;

	/**
	 * Standard HL7 encoding characters "^~\\&".
	 */
	public static final String MSH_ENC_CHARS = Messages
			.getString("M7Message.DEFAULT_ENCODING_CHARS"); //$NON-NLS-1$

	/**
	 * Standard ASTM encoding characters "\\^&"
	 */
	public static final String ASTM_ENC_CHARS = Messages
			.getString("M7Message.ASTM_ENCODING_CHARS"); //$NON-NLS-1$

	/**
	 * Standard ASTM value
	 */
	public static final String ASTM_STD = Messages
			.getString("M7Message.ASTM_STANDARD"); //$NON-NLS-1$

	/**
	 * Standard HL7 value
	 */
	public static final String HL7_STD = Messages
			.getString("M7Message.HL7_STANDARD"); //$NON-NLS-1$

	/**
	 * Default ASTM batch value
	 */
	public static final String ASTM_BATCH = Messages
			.getString("M7Message.ASTM_BATCH"); //$NON-NLS-1$

	/**
	 * Returns message profile id
	 * 
	 * @return
	 */
	public String getMessageProfileId() {
		return messageProfileId;
	}

	/**
	 * Sets the messge profile id for this message
	 * 
	 * @param messageProfileId
	 */
	public void setMessageProfileId(String messageProfileId) {
		this.messageProfileId = messageProfileId;
	}

	public M7DefinitionFile getMessgeDefinitionFile() {
		return messgeDefinitionFile;
	}

	public void setMessgeDefinitionFile(M7DefinitionFile messgeDefinitionFile) {
		this.messgeDefinitionFile = messgeDefinitionFile;
	}

	/**
	 * M7Message type
	 */
	protected String m_msgType;

	/**
	 * M7Message profile id
	 */
	private String messageProfileId;

	String m_Standard = HL7_STD;

	M7DefinitionFile messgeDefinitionFile;

	// {{DECLARE_CONTROLS
	// }}
}
