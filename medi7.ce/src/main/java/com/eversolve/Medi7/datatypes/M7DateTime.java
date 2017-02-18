/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7.datatypes;

import java.util.Calendar;
import java.util.StringTokenizer;

import com.eversolve.Medi7.M7Exception;

/**
 * The M7DateTime is used to represent the datetime <b>TS</b> portion of the
 * Timestamp (TS) HL7 datatype. This has the format
 * YYYY[MM[DD[HHMM[SS[.S[S[S[S]]]]]]]][+/-ZZZZ]. The M7DateTime class is merely
 * a helper that aggregates a M7Date and M7Time class; refer to the descriptions
 * for the M7Date and M7Time classes for detailed information. <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * 
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * @com.register ( clsid=E158D7B0-B7E7-11D2-BA43-004005445EAC,
 *               typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 */
public class M7DateTime {
	/**
	 * Default constructor
	 * <p>
	 * Change log:</br>
	 * <ul>
	 * <li>Instead of initializing to '0', initialize to today's timestamp
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 2.0
	 */
	public M7DateTime() {
		this(Calendar.getInstance());
	}

	/**
	 * Date constructor
	 * 
	 * @param dt
	 *            Date object
	 */
	public M7DateTime(Calendar dt) {

		m_date = new M7Date(dt);
		m_time = new M7Time(dt);
	}

	/**
	 * Constructs an M7Datetime from the strValue in the format of
	 * YYYY[MM[DD[HHMM[SS[.S[S[S[S]]]]]]]][+/-ZZZZ].
	 * <p>
	 * It also accepts SQL date/time formatted strings <br/> <code>
	 if (dbValue.matches(M7Date.regExSQL)) {<br/>
	 msg.setFieldValue(field, new M7Date(dbValue));<br/>
	 } 
	 else if (dbValue.matches(M7DateTime.regExSQL)) <br/>
	 {<br/>
	 msg.setFieldValue(field, new M7DateTime(dbValue));<br/>
	 } else {<br/>
	 msg.setFieldValue(field, dbValue);<br/>
	 }<br/>
	 }</code></p>
	 * 
	 * @param strValue
	 *            String representation of date/time
	 */
	public M7DateTime(String strValue) throws M7Exception {
		fromString(strValue);
	}

	/**
	 * Constructs an M7DateTime from M7Date and M7Time objects
	 * 
	 * @param date
	 *            M7Date object
	 * @param time
	 *            M7Time object
	 */
	public M7DateTime(M7Date date, M7Time time) {
		fromDateAndTime(date, time);
	}

	/**
	 * Constructs an M7DateTime from an M7DateTime object
	 * 
	 * @param dttm
	 *            M7DateTime object
	 */
	public M7DateTime(M7DateTime dttm) {
		fromDateTime(dttm);
	}

	/**
	 * Sets the values for the M7Datetime from the strValue in the format of
	 * YYYY[MM[DD[HHMM[SS[.S[S[S[S]]]]]]]][+/-ZZZZ].
	 * 
	 * @param strValue
	 *            String representation of date/time
	 */
	public void fromString(String strValue) throws M7Exception {

		if (strValue == null) {
			throw new M7Exception("Attempting to parse a null string");
		}
		if (strValue.trim().matches(regExSQL)) {
			StringTokenizer st = new StringTokenizer(strValue);
			int i = 0;
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				switch (i++) {
				case 0:
					m_date = new M7Date(token);
					break;
				case 1:
					m_time = new M7Time(token);
					break;

				}

			}

		} else {
			// if we have at least 8 characters then we have the date
			if (strValue.length() >= 8) {
				m_date = new M7Date(strValue.substring(0, 8));
			}

			// the rest is the time
			if (strValue.length() > 8) {
				m_time = new M7Time(strValue.substring(8));
			}
		}
	}

	/**
	 * Sets the values from an M7DateTime object
	 * 
	 * @param dttm
	 *            M7Time object
	 */
	public void fromDateTime(M7DateTime dttm) {
		m_date = dttm.m_date;
		m_time = dttm.m_time;
	}

	/**
	 * Sets the values from M7Date and M7Time objects
	 * 
	 * @param date
	 *            M7Date object
	 * @param time
	 *            M7Time object
	 */
	public void fromDateAndTime(M7Date date, M7Time time) {
		m_date = date;
		m_time = time;
	}

	/**
	 * Returns the M7Date portion of the M7DateTime
	 * 
	 * @return M7Date representation of date/time
	 */
	public M7Date getDate() {
		return m_date;
	}

	/**
	 * Returns the M7time portion of the M7DateTime
	 * 
	 * @return M7Time representation of date/time
	 */
	public M7Time getTime() {
		return m_time;
	}

	/**
	 * Returns the string representation in the form of
	 * YYYY[MM[DD[HHMM[SS[.S[S[S[S]]]]]]]][+/-ZZZZ].
	 * 
	 * @return String representation of date/time
	 */
	public String getStringValue() {
		String strDateTime = m_date.getStringValue();
		if (strDateTime.length() == 8) // we have a year, month, and day
			strDateTime += m_time.getStringValue();

		return strDateTime;
	}

	/**
	 * Returns the string representation in the form of
	 * YYYY[MM[DD[HHMM[SS[.S[S[S[S]]]]]]]][+/-ZZZZ].
	 * 
	 * @return String representation of date/time
	 */
	public String toString() {
		return getStringValue();
	}

	/**
	 * Return a string like "2000-11-25 15:00:11" from an M7DateTime
	 * 
	 * @param date
	 *            is of M7DateTime type
	 * @return String representing the MSSql date
	 */

	public String getSQLFormat() {

		return this.getDate().getSQLFormat() + delim
				+ this.getTime().getSQLFormat();
	}

	/**
	 * Returns a string like "2000-11-25 15:00:11" from an HL date like
	 * "20001125200307-0800"
	 * 
	 * @param date
	 *            String representing an HL7 time stamp
	 * @return String representing the MSSql date
	 */
	public static String parseSQLString(String date) {
		if (date == null)
			return null;
		String year = date.substring(0, 4);

		String mon = date.substring(4, 6);
		if (mon.length() == 1)
			mon = "0" + mon;

		String day = date.substring(6, 8);
		if (day.length() == 1)
			day = "0" + day;

		String hour = "00";
		String min = "00";
		String sec = "00";

		hour = date.substring(8, 10);
		min = date.substring(10, 12);
		sec = date.substring(12, 14);

		return year + "-" + mon + "-" + day + " " + hour + ":" + min + ":"
				+ sec;
	}

	// Member variables
	/**
	 * SQL format delimiter
	 */
	char delim = ' ';

	/**
	 * Holds the date value
	 */
	protected M7Date m_date = new M7Date();

	/**
	 * Holds the time value
	 */
	protected M7Time m_time = new M7Time();

	public static final String regExSQL = "^[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9]([.][0-9]{1,})?$";

}
