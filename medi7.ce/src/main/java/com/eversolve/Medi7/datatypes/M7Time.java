/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7.datatypes;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import com.eversolve.Medi7.M7;
import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.resources.Messages;

/**
 * The M7Time class is used to format the <b>TM</b> datatype in HL7 which has
 * the form HHMM[SS[.S[S[S[S]]]]][+/-ZZZZ]. <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * 
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * @com.register ( clsid=E158D7BD-B7E7-11D2-BA43-004005445EAC,
 *               typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 */
public class M7Time {
	/**
	 * Default construtor initializes all parts of the time to 0
	 */
	public M7Time() {
		m_hour = 0;
		m_minutes = 0;
		m_seconds = 0;
		m_fractional_seconds = 0.0;
		m_offset = 0;
	}

	/**
	 * Constructor which sets the hours, minutes, and seconds from the
	 * java.util.Date object.
	 * 
	 * @deprecated
	 */
	public M7Time(Date dt) {
		m_hour = dt.getHours();
		m_minutes = dt.getMinutes();
		m_seconds = dt.getSeconds();
	}

	/**
	 * Constructor which sets the hours minutes and seconds from the
	 * java.util.Calendar object.
	 * 
	 */
	public M7Time(Calendar dt) {
		m_hour = dt.get(java.util.Calendar.HOUR_OF_DAY);
		m_minutes = dt.get(java.util.Calendar.MINUTE);
		m_seconds = dt.get(java.util.Calendar.SECOND);
		m_offset = dt.get(java.util.Calendar.ZONE_OFFSET)/36000;
	}

	/**
	 * Sets the hours, minutes, and seconds from the values provided, all other
	 * values are left unchanged.
	 * 
	 */
	public void setHMS(int hours, int minutes, int seconds) {
		m_hour = hours;
		m_minutes = minutes;
		m_seconds = seconds;
	}

	/**
	 * Sets all of the elements of the time from the values provided
	 */
	public void setAll(int hours, int minutes, int seconds,
			double fractionalSeconds, int offset) {
		m_hour = hours;
		m_minutes = minutes;
		m_seconds = seconds;
		m_fractional_seconds = fractionalSeconds;
		m_offset = offset;
	}

	/**
	 * Sets the hour portion of the time
	 */
	public void setHours(int newValue) {
		m_hour = newValue;
	}

	/**
	 * Sets the minutes portion of the time
	 */
	public void setMinutes(int newValue) {
		m_minutes = newValue;
	}

	/**
	 * Sets the seconds portion of the time
	 */
	public void setSeconds(int newValue) {
		m_seconds = newValue;
	}

	/**
	 * Sets the fractional seconds portion of the time, only the 4 digits after
	 * the decimal are used any values in front of the decimal are truncated.
	 */
	public void setFractionalSeconds(double newValue) {
		m_fractional_seconds = newValue;
	}

	/**
	 * Sets the offset portion of the time. This should be a value in the range
	 * of -2359 to +2359 - No validation is done to ensure this.
	 * 
	 */
	public void setOffset(int newValue) {
		m_offset = newValue;
	}

	/**
	 * Returns the hour portion of the time.
	 */
	public int getHours() {
		return m_hour;
	}

	/**
	 * Returns the minutes portion of the time.
	 */
	public int getMinutes() {
		return m_minutes;
	}

	/**
	 * Returns the seconds portion of the time.
	 */
	public int getSeconds() {
		return m_seconds;
	}

	/**
	 * Returns the fractional seconds portion of the time.
	 */
	public double getFractionalSeconds() {
		return m_fractional_seconds;
	}

	/**
	 * Returns the offset portion of the time.
	 */
	public int getOffset() {
		return m_offset;
	}

	/**
	 * Constructor that sets the time values from the string provided that must
	 * be in the form of HHMM[SS[.S[S[S[S]]]]][+/-ZZZZ] or SQL timestamp. It
	 * also accepts SQL date/time formatted strings <br/> <code>
	 if (dbValue.matches(M7Date.regExSQL)) {<br/>
	 msg.setFieldValue(field, new M7Date(dbValue));<br/>
	 } 
	 else if (dbValue.matches(M7DateTime.regExSQL)) <br/>
	 {<br/>
	 msg.setFieldValue(field, new M7DateTime(dbValue));<br/>
	 } else {<br/>
	 msg.setFieldValue(field, dbValue);<br/>
	 }<br/>
	 }</code>
	 * </p>
	 * 
	 * @param sTime
	 *            string containing the time
	 * @exception M7Exception
	 *                if the string is not parsable as an HL7 or SQL timestamp
	 */
	public M7Time(String sTime) throws M7Exception {
		fromString(sTime);
	}

	/**
	 * Sets the time values from the string provided that must be in the form of
	 * HHMM[SS[.S[S[S[S]]]]][+/-ZZZZ]. If some of the values in the string are
	 * not provided then those values are set to 0.
	 * 
	 * @param sTime
	 *            string represing HL7 or SQL time
	 * @exception M7Exception
	 *                if the string is not parsable
	 */
	public void fromString(String sTime) throws M7Exception {
		if (sTime == null) {
			throw new M7Exception("Attempting to parse a null string");
		}
		try {
			m_hour = 0;
			m_minutes = 0;
			m_seconds = 0;
			m_fractional_seconds = 0;
			m_offset = 0;

			String part = null;
			int pos;
			sTime = sTime.trim();
			if (sTime.matches(regExSQL)) {
				StringTokenizer st = new StringTokenizer(sTime, ":");
				int i = 0;
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					switch (i) {
					case 0:
						this.setHours(Integer.parseInt(token));
						break;
					case 1:
						this.setMinutes(Integer.parseInt(token));
						break;
					case 2:
						if (token.contains(new StringBuffer('.'))) {
							this
									.setFractionalSeconds(Double
											.parseDouble(token));
						} else {
							this.setSeconds(Integer.parseInt(token));
						}
						break;
					}
					i++;
				}
			} else {

				// Search for a + sign or a - sign to see if a time offset was
				// sent we have to monkey with the + sign because we must strip
				// that
				// off the part or else a java lang number exception is thrown,
				// but
				// for negatives we have to leave it
				String plus_minus = Messages.getString("M7Time.PLUS"); //$NON-NLS-1$
				pos = sTime.indexOf(plus_minus);
				boolean foundPlusSign = true;
				if (pos == M7.NPOS) // did not find a +
				{
					foundPlusSign = false;
					plus_minus = Messages.getString("M7Time.MINUS"); //$NON-NLS-1$
					pos = sTime.indexOf(plus_minus);
				}

				if (pos != M7.NPOS) // found a delimiter
				{
					if (foundPlusSign)
						pos++;

					if ((pos + 1) < sTime.length()) // not at the end of the
					// string
					{
						part = sTime.substring(pos);
						m_offset = Integer.valueOf(part).intValue();
					}

					if (foundPlusSign)
						pos--;

					sTime = sTime.substring(0, pos);
				}

				// search for a decimal point to see if fractional seconds were
				// sent
				pos = sTime
						.indexOf(Messages.getString("M7Time.PERIOD_DECIMAL")); //$NON-NLS-1$
				if (pos != M7.NPOS) // found a delimiter
				{
					part = sTime.substring(pos);
					if (part.length() > 1)
						m_fractional_seconds = Double.valueOf(part)
								.doubleValue();

					sTime = sTime.substring(0, pos);
				}

				// see if we have 6 bytes left which would mean that we have the
				// seconds passed in
				if (sTime.length() >= 6) {
					part = sTime.substring(4, 6);
					m_seconds = Integer.valueOf(part).intValue();
					sTime = sTime.substring(0, 4);
				}

				// see if we have 4 bytes left which would mean that we have the
				// minutes passed in
				if (sTime.length() >= 4) {
					part = sTime.substring(2, 4);
					m_minutes = Integer.valueOf(part).intValue();
					sTime = sTime.substring(0, 2);
				}

				// see if we have 2 bytes left which would mean that we have the
				// hour passed in
				if (sTime.length() >= 2) {
					part = sTime.substring(0, 2);
					m_hour = Integer.valueOf(part).intValue();
				}
			}
		} catch (NumberFormatException e) {

			e.printStackTrace();
			throw new M7Exception("Unable to parse time '" + sTime
					+ "' as an HL7 time");
		}
	}

	/**
	 * Constructs an M7Time object from the time portion of the M7DateTime
	 * provided.
	 */
	public M7Time(M7DateTime dttm) {
		fromDateTime(dttm);
	}

	/**
	 * Sets the values in this M7Time object from the time portion of the
	 * M7DateTime provided.
	 */
	public void fromDateTime(M7DateTime dttm) {
		m_hour = dttm.m_time.m_hour;
		m_minutes = dttm.m_time.m_minutes;
		m_seconds = dttm.m_time.m_seconds;
		m_fractional_seconds = dttm.m_time.m_fractional_seconds;
		m_offset = dttm.m_time.m_offset;
	}

	/**
	 * Sets the values in this M7Time object from the M7Time provided.
	 */
	public M7Time(M7Time theTime) {
		fromTime(theTime);
	}

	/**
	 * Sets the values in this M7Time object from the M7Time provided.
	 */
	public void fromTime(M7Time theTime) {
		m_hour = theTime.m_hour;
		m_minutes = theTime.m_minutes;
		m_seconds = theTime.m_seconds;
		m_fractional_seconds = theTime.m_fractional_seconds;
		m_offset = theTime.m_offset;
	}

	/**
	 * Returns the appropriate HL7 string representation of the phone number
	 * values int the format HHMM[SS[.S[S[S[S]]]]][+/-ZZZZ].
	 */
	public String getStringValue() {
		// format the time per HL7 with leading 0's if necessary.
		String strTime = new String();

		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(0, 0, 0, m_hour, m_minutes, m_seconds);

		DecimalFormat decFormat = new DecimalFormat(Messages
				.getString("M7Time.DECIMAL_FORMAT_SHORT")); //$NON-NLS-1$
		strTime += decFormat.format(m_hour);
		strTime += decFormat.format(m_minutes);
		if (m_fractional_seconds > 0 || m_seconds > 0) {
			strTime += decFormat.format(m_seconds);
		}

		// format the fractional seconds and add it to the string by
		// only using what is after the decimal point
		if (m_fractional_seconds > 0) {
			String strFract = String.valueOf(m_fractional_seconds);

			// only take what is past the decimal point
			int pos = strFract.indexOf(Messages
					.getString("M7Time.PERIOD_DECIMAL")); //$NON-NLS-1$
			if (pos != M7.NPOS) // found a delimiter
			{
				strFract = strFract.substring(pos);
				if (strFract.length() > 5)
					strTime += strFract.substring(0, 5);
				else
					strTime += strFract;
			}
		}
		// format the offset and add it to the string
		if (m_offset != 0) {
			decFormat = new DecimalFormat(Messages
					.getString("M7Time.DECIMAL_FORMAT_LONG")); //$NON-NLS-1$
			if (m_offset > 0)
				strTime += Messages.getString("M7Time.PLUS"); //$NON-NLS-1$
			strTime += decFormat.format(m_offset);
		}

		return strTime;
	}

	/**
	 * Returns the appropriate HL7 string representation of the phone number
	 * values int the format HHMM[SS[.S[S[S[S]]]]][+/-ZZZZ].
	 */
	public String toString() {
		return getStringValue();
	}

	/**
	 * Return the date as <code>12:11:57.0</code>
	 * 
	 * @return String
	 */
	public String getSQLFormat() {
		StringBuffer returnValue = new StringBuffer();
		String hour = new Integer(getHours()).toString();
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		returnValue.append(hour);
		String min = new Integer(getMinutes()).toString();
		if (min.length() == 1) {
			min = "0" + min;
		}
		returnValue.append(min);
		String sec = "00.0";
		if (getSeconds() != 0) {
			sec = new Integer(getSeconds()).toString();
			if (sec.length() == 1) {
				sec = "0" + sec;
			}
			sec += ".0";
		} else if (this.getFractionalSeconds() != 0D) {
			sec = new Double(this.getFractionalSeconds()).toString();
			if (sec.length() == 3) {
				sec = "0" + sec;
			}
		}

		return hour + delim + min + delim + sec;
	}

	// Member variables
	/**
	 * SQL format delimiter
	 */
	char delim = ':';

	protected int m_hour = 0;

	protected int m_minutes = 0;

	protected int m_seconds = 0;

	protected double m_fractional_seconds = 0; // only use what is after the decimal point

	protected int m_offset = 0; // can only be +2359 to -2359

	public static final String regExSQL = "^[0-2][0-9]:[0-5][0-9]:[0-5][0-9]([.][0-9])?$";

}