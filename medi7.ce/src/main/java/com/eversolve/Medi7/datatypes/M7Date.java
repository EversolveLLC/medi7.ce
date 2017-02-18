/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7.datatypes;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.resources.Messages;

/**
 * The M7Date class is used to format the <b>DT</b> datatype in HL7 which has
 * the form YYYY[MM[DD]].
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * @com.register ( clsid=E158D7AF-B7E7-11D2-BA43-004005445EAC,
 *               typelib=E158D7A1-B7E7-11D2-BA43-004005445EAC )
 */
public class M7Date extends BaseDate{
	/**
	 * Default constructor that sets the year, month, and day to today's date.
	 * <p>
	 * Change log:</br>
	 * <ul>
	 * <li>Instead of initializing to '0', initialize to today's date</li>
	 * </ul>
	 * </p>
	 * 
	 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
	 * @version 2.0
	 */
	public M7Date() {
		this(Calendar.getInstance());
	}

	/**
	 * Constructor that uses the value in the java.util.Date object to set the
	 * year, month, and day.
	 * 
	 * @param dt
	 *            Date object
	 * @deprecated
	 */
	public M7Date(java.util.Date dt) {

		m_year = dt.getYear() + 1900;
		m_month = (dt.getMonth() + 1);
		m_day = dt.getDate();
	}

	/**
	 * Constructor that will use the value in the java.util.Calendar object to
	 * set the year, month, and day.
	 * 
	 * @param dt
	 *            Calendar object
	 */
	public M7Date(java.util.Calendar dt) {
		m_year = dt.get(java.util.Calendar.YEAR);
		m_month = dt.get(java.util.Calendar.MONTH) + 1;
		m_day = dt.get(java.util.Calendar.DAY_OF_MONTH);
	}

	/**
	 * Constructor that will set the year month and day from a string that must
	 * be in the format YYYY[MM[DD]]. If MM and/or DD are not provided then they
	 * default to 0.
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
	 * @param sDate
	 *            String representation of the date
	 */
	public M7Date(String sDate) throws M7Exception {
		fromString(sDate);
	}

	/**
	 * This function will set the year month and day from a string that must be
	 * in the format YYYY[MM[DD]]. If MM and/or DD are not provided then they
	 * default to 0.
	 * 
	 * @param sValue
	 *            String representation of the date
	 * @throws M7Exception
	 */
	public void fromString(String sValue) throws M7Exception {

		if(sValue==null)
		{
			throw new M7Exception("Attempting to parse a null string");
		}
		try {
			String part = null;
			sValue = sValue.trim();
			if (sValue.matches(regExSQL)) {
				StringTokenizer st = new StringTokenizer(sValue);
				if (st.hasMoreTokens()) {
					sValue = st.nextToken();
				}
				st = new StringTokenizer(sValue, "-");
				int i = 0;
				while (st.hasMoreTokens()) {
					Integer token = new Integer(st.nextToken());
					switch (i++) {
					case 0:
						this.setYear(token.intValue());
						break;
					case 1:
						this.setMonth(token.intValue());
						break;
					case 2:
						this.setDay(token.intValue());
						break;
					}
					
				}

			} else {
				// if we have 8 bytes then we have the day
				if (sValue.length() >= 8) {
					part = sValue.substring(6, 8);
					m_day = Integer.valueOf(part).intValue();
					sValue = sValue.substring(0, 6);
				}

				// if we have 6 bytes then we have the month
				if (sValue.length() >= 6) {
					part = sValue.substring(4, 6);
					m_month = Integer.valueOf(part).intValue();
					sValue = sValue.substring(0, 4);
				}

				// if we have 4 bytes then we have the year
				if (sValue.length() >= 4) {
					part = sValue.substring(0, 4);
					m_year = Integer.valueOf(part).intValue();
				}
			}
		} catch (NumberFormatException e) {

			//e.printStackTrace();
			throw new M7Exception("Unable to parse date '" + sValue + "' as an HL7 Date");
		}
	}

	/**
	 * Constructor that will set the year, month, and day from the Date portion
	 * of the M7DateTime parameter.
	 * 
	 * @param dttm
	 *            M7DateTime object specifiying both date and time.
	 */
	public M7Date(M7DateTime dttm) {
		fromDateTime(dttm);
	}

	/**
	 * This function will set the year, month, and day from the Date portion of
	 * the M7DateTime parameter.
	 * 
	 * @param dttm
	 *            M7DateTime object
	 */
	public void fromDateTime(M7DateTime dttm) {
		m_year = dttm.m_date.m_year;
		m_month = dttm.m_date.m_month;
		m_day = dttm.m_date.m_day;
	}

	/**
	 * Constructor that will set the year, month, and day from the values in the
	 * M7Date parameter.
	 * 
	 * @param dt
	 *            M7Date object
	 */
	public M7Date(M7Date dt) {
		fromDate(dt);
	}

	/**
	 * This function will set the year, month, and day from the values in the
	 * M7Date parameter.
	 * 
	 * @param dt
	 *            M7Date object
	 */
	public void fromDate(M7Date dt) {
		m_year = dt.m_year;
		m_month = dt.m_month;
		m_day = dt.m_day;
	}

	/**
	 * This function will set the year, month, and day from the parameters
	 * provided. No checking is done for validity.
	 * 
	 * @param year
	 *            Integer value for the year
	 * @param month
	 *            Integer value for the month
	 * @param day
	 *            Integer value for the day
	 */
	public void setYMD(int year, int month, int day) {
		m_year = year;
		m_month = month;
		m_day = day;
	}

	/**
	 * This function will set the year from the parameter provided. No checking
	 * is done for validity.
	 * 
	 * @param year
	 *            Integer value for the year
	 */
	public void setYear(int year) {
		m_year = year;
	}

	/**
	 * This function will set the month from the parameter provided. No checking
	 * is done for validity.
	 * 
	 * @param month
	 *            Integer value for the month
	 */
	public void setMonth(int month) {
		m_month = month;
	}

	/**
	 * This function will set the day from the parameter provided. No checking
	 * is done for validity.
	 * 
	 * @param day
	 *            Integer value for the day
	 */
	public void setDay(int day) {
		m_day = day;
	}

	/**
	 * This function will return the value of the year attribute.
	 * 
	 * @return Integer value for the year
	 */
	public int getYear() {
		return m_year;
	}

	/**
	 * This function will return the value of the month attribute.
	 * 
	 * @return Integer value for the month
	 */
	public int getMonth() {
		return m_month;
	}

	/**
	 * This function will return the value of the day attribute.
	 * 
	 * @return Integer value for the day
	 */
	public int getDay() {
		return m_day;
	}

	/**
	 * This function will return the date value in YYYY[MM[DD]] format
	 * 
	 * @return String value for the date
	 */
	public String getStringValue() {
		String str = String.valueOf(m_year);

		DecimalFormat decFormat = new DecimalFormat(Messages
				.getString("M7Date.DECIMAL_FORMAL_SHORT")); //$NON-NLS-1$
		if (m_month > 0) {
			str += decFormat.format(m_month);
			if (m_day > 0)
				str += decFormat.format(m_day);
		}

		return str;
	}

	/**
	 * This function will return the date value in YYYY[MM[DD]] format
	 * 
	 * @return String value for the date
	 */
	public String toString() {
		return getStringValue();
	}

	/**
	 * This function will return the date value as a java.util.Date object
	 * 
	 * @return Date value for the date
	 */
	public Date getDateValue() {
		return getCalendarValue().getTime();
	}

	/**
	 * This function will return the date value as a Calendar object
	 * 
	 * @return Calendar value for the date
	 */
	public Calendar getCalendarValue() {
		Calendar cal = Calendar.getInstance();
		cal.set(m_year, m_month - 1, m_day);
		return cal;
	}

	/**
	 * Returns a string like "2000-11-25" from an M7Date
	 * 
	 * @return String representing the default SQL date
	 */
	public String getSQLFormat() {

		String year = new Integer(getYear()).toString();

		String mon = new Integer(getMonth()).toString();
		// Preserve leading zero for values < 10
		if (mon.length() == 1)
			mon = "0" + mon;

		String day = new Integer(getDay()).toString();
		if (day.length() == 1)
			day = "0" + day;
		// + " 00:00:00"
		return year + delim + mon + delim + day;
	}

	// Member variables
	/**
	 * SQL format delimiter
	 */
	char delim = '-';
	
	protected int m_year = 0;

	protected int m_month = 0;

	protected int m_day = 0;

	public static final String regExSQL = "^[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]( 00:00:00[.]0)?$";

}
