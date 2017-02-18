/*
 * Project:Medi7
 * com.eversolve.Medi7.datatypes.test.M7DateTest created on Feb 1, 2006
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * Change log: Initial Version Feb 1, 2006
 * 
 */
package com.eversolve.Medi7.datatypes.test;

import java.util.Calendar;

import junit.framework.TestCase;

import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.datatypes.M7Date;

/**
 * Class: M7DateTest
 * 
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0 Change log: Initial Version Feb 1, 2006
 */
public class M7DateTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(M7DateTest.class);
	}

	M7Date testDate;

	String testString = "20000303";

	String wrongDateString = "20001345";

	String yearOnly = "2000";

	String yearMonth = "200003";

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		testDate = new M7Date();

	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Class under test for void M7Date()
	 */
	public final void testM7Date() {
		// TODO Implement M7Date().
	}

	/*
	 * Class under test for void M7Date(java.util.Calendar)
	 */
	public final void testM7DateCalendar() {
		// TODO Implement M7Date().
		
		Calendar rightNow = Calendar.getInstance();
		M7Date date = new M7Date(rightNow);
		System.out.println("testM7DateCalendar = "+date.toString());
		
		
	}

	/*
	 * Class under test for void M7Date(String)
	 */
	public final void testM7DateString() {
		// TODO Implement M7Date().
	}

	public final void testFromString() {
	    System.out.println("testFromString");
		M7Date dateWrong;
		M7Date dateYear;
		M7Date dateYearMonth;
		M7Date dateYearMonthDay;
		try {
			dateWrong = new M7Date(wrongDateString);
			dateYear = new M7Date(yearOnly);
			dateYearMonth = new M7Date(yearMonth);
			dateYearMonthDay = new M7Date(testString);
			assertEquals(dateYear.getYear(), dateYearMonth.getYear());
			assertEquals(dateYearMonth.getYear(), dateYearMonthDay.getYear());
			assertEquals(dateYearMonth.getMonth(), dateYearMonthDay.getMonth());
			assertTrue(dateYear.getMonth() == 0);
			assertTrue(dateYear.getDay() == 0);
			assertTrue(dateYearMonthDay.getDay() == 3);
			assertTrue(dateWrong.getDay() == 45);
			assertTrue(dateWrong.getMonth() == 13);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}

	}

	public final void testMySQLDate() {
		String mySQLDateStr = "1978-11-25 00:00:00.0";
		M7Date mySQLDT = null;
		try {
			mySQLDT = new M7Date(mySQLDateStr);
			assertTrue(mySQLDT.getYear() == 1978);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Attempting to instantiate " + mySQLDateStr + "; " + e);
		}
	}

	/*
	 * Class under test for void M7Date(M7DateTime)
	 */
	public final void testM7DateM7DateTime() {
		// TODO Implement M7Date().
	}

	public final void testFromDateTime() {
		// TODO Implement fromDateTime().
	}

	/*
	 * Class under test for void M7Date(M7Date)
	 */
	public final void testM7DateM7Date() {
		// System.out.println("testM7DateM7Date");
		M7Date date1 = new M7Date();
		M7Date date2 = new M7Date(Calendar.getInstance());
		assertEquals(date1.getYear(), date2.getYear());
		assertEquals(date1.getMonth(), date2.getMonth());
		assertEquals(date1.getDay(), date2.getDay());
	}

	public final void testFromDate() {
		// TODO Implement fromDate().
	}

	public final void testSetYMD() {
		// TODO Implement setYMD().
		testDate.setYMD(2000, 10, 16);
		assertTrue(testDate.getYear() == 2000);
		assertTrue(testDate.getMonth() == 10);
		assertTrue(testDate.getDay() == 16);
	}

	public final void testGetYear() {
		testDate.setYear(100);
		assertTrue(testDate.getYear() == 100);
	}

	public final void testGetMonth() {
		testDate.setMonth(100);
		assertTrue(testDate.getMonth() == 100);
	}

	public final void testGetDay() {
		testDate.setDay(100);
		assertTrue(testDate.getDay() == 100);
	}

	public final void testgetStringValue() {
		M7Date d;
		try {
			d = new M7Date(testString);

			assertEquals(d.getStringValue(), testString);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/*
	 * Class under test for String toString()
	 */
	public final void testToString() {
		// System.out.println("testToString");
		M7Date d;
		try {
			d = new M7Date(testString);
			assertEquals(d.toString(), testString);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}

	}

	public final void testGetDateValue() {
		// System.out.println("testGetDateValue");
		try {
			M7Date d = new M7Date(testString);
			
			Calendar c = d.getCalendarValue();
			assertTrue(c.get(Calendar.DAY_OF_MONTH) == d.getDay());
			assertTrue((c.get(Calendar.MONTH) + 1) == d.getMonth());
			assertTrue(c.get(Calendar.YEAR) == d.getYear());
			//Date dt = d.getDateValue();
			M7Date d2 = new M7Date(Calendar.getInstance());
			assertTrue(d2.getDay() == d.getDay());
			assertTrue(d2.getMonth() == d.getMonth());
			assertTrue(d2.getYear() == d.getYear());
		} catch (M7Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	public final void testGetSQLFormat() {

		System.out.println(testDate.getSQLFormat());
		assertTrue(testDate.getSQLFormat().matches(M7Date.regExSQL));
	}

}
