/*
 * Project:Medi7
 * com.eversolve.Medi7.datatypes.test.M7DateTimeTest created on Feb 1, 2006
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * Change log: Initial Version Feb 1, 2006
 * 
 */
package com.eversolve.Medi7.datatypes.test;

import junit.framework.TestCase;

import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.datatypes.M7DateTime;

/**
 * Class: M7DateTimeTest
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * Change log: Initial Version Feb 1, 2006
 */
public class M7DateTimeTest extends TestCase {
	
	M7DateTime testDateTime;
	public static void main(String[] args) {
		junit.textui.TestRunner.run(M7DateTimeTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		testDateTime = new M7DateTime();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Class under test for void M7DateTime()
	 */
	public final void testM7DateTime() {
		//TODO Implement M7DateTime().
	}

	/*
	 * Class under test for void M7DateTime(Calendar)
	 */
	public final void testM7DateTimeCalendar() {
		//TODO Implement M7DateTime().
	}

	/*
	 * Class under test for void M7DateTime(String)
	 */
	public final void testM7DateTimeString() {
		//TODO Implement M7DateTime().
	}

	/*
	 * Class under test for void M7DateTime(M7Date, M7Time)
	 */
	public final void testM7DateTimeM7DateM7Time() {
		//TODO Implement M7DateTime().
	}

	/*
	 * Class under test for void M7DateTime(M7DateTime)
	 */
	public final void testM7DateTimeM7DateTime() {
		//TODO Implement M7DateTime().
	}

	public final void testFromString() {

		String sqlDT = testDateTime.getSQLFormat();
		M7DateTime m7DT = null;
		try {
			m7DT = new M7DateTime(sqlDT);
			assertEquals(sqlDT, m7DT.getSQLFormat());
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		String testStr = "2006-12-09 22:00:00.0";
		try {
			m7DT = new M7DateTime(testStr);
			assertEquals(testStr, m7DT.getSQLFormat());
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		testStr = "2006-02-09 12:00:00";
		try {
			m7DT = new M7DateTime(testStr);
			assertEquals(testStr+".0", m7DT.getSQLFormat());
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		
	}

	public final void testFromDateTime() {
		//TODO Implement fromDateTime().
	}

	public final void testFromDateAndTime() {
		//TODO Implement fromDateAndTime().
	}

	public final void testGetDate() {
		//TODO Implement getDate().
	}

	public final void testGetTime() {
		//TODO Implement getTime().
	}

	public final void testgetStringValue() {
		//TODO Implement getStringValue().
	}

	/*
	 * Class under test for String toString()
	 */
	public final void testToString() {
		//TODO Implement toString().
	}
	
	public final void testGetSQLFormat()
	{
		
		System.out.println(testDateTime.getSQLFormat());
		assertTrue(testDateTime.getSQLFormat().matches(M7DateTime.regExSQL));
		
	}


}
