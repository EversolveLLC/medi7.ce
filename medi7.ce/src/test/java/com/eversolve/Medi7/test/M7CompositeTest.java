package com.eversolve.Medi7.test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.eversolve.Medi7.M7Composite;
import com.eversolve.Medi7.M7DefinitionFile;
import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.M7Message;
import com.eversolve.Medi7.M7Repeat;

public class M7CompositeTest extends TestCase {

	M7Message oru = new M7Message();

	M7Message abg = new M7Message();

	/** Root tag for our document. */
	static String DOC_ROOT = "doc-root";

	public static void main(String[] args) {
		junit.textui.TestRunner.run(M7CompositeTest.class);
	}

	public M7CompositeTest(String arg0) {
		super(arg0);

	}

	protected void setUp() throws Exception {
		super.setUp();
		// oru.loadPersistedMessage(M7MessageTest.msgORU_1, new
		// M7DefinitionFile(
		// M7MessageTest.oruDefFileName));
		// abg.loadPersistedMessage(M7MessageTest.abgORU, new M7DefinitionFile(
		// M7MessageTest.oruDefFileName));

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.M7Composite()'
	 */
	public void testM7Composite() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.M7Composite(int)'
	 */
	public void testM7CompositeInt() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.M7Composite(String, int,
	 * M7Composite)'
	 */
	public void testM7CompositeStringIntM7Composite() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.newChild(String,
	 * M7BooleanHolder)'
	 */
	public void testNewChild() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.isRepeat()'
	 */
	public void testIsRepeat() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.isField()'
	 */
	public void testIsField() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.getParent()'
	 */
	public void testGetParent() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.getChildCount()'
	 */
	public void testGetChildCount() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.getChild(String)'
	 */
	public void testGetChild() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.getName()'
	 */
	public void testGetName() {

	}

	/*
	 * Test method for
	 * 'com.eversolve.Medi7.M7Composite.getChildByPosition(long)'
	 */
	public void testGetChildByPosition() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.getAbsoluteName()'
	 */
	public void testGetAbsoluteName() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.getType()'
	 */
	public void testGetType() {

	}

	/*
	 * Test method for
	 * 'com.eversolve.Medi7.M7Composite.deleteChildWithComposite(M7Composite)'
	 */
	public void testDeleteChildWithComposite() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.deleteChild(String)'
	 */
	public void testDeleteChild() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.setFieldValue(String,
	 * String)'
	 */
	public void testSetFieldValueStringString() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.createField(String,
	 * M7BooleanHolder)'
	 */
	public void testCreateField() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.getField(String)'
	 */
	public void testGetField() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.createRepeat(String,
	 * M7BooleanHolder)'
	 */
	public void testCreateRepeat() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.getRepeat(String)'
	 */
	public void testGetRepeat() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.setFieldValue(String,
	 * long)'
	 */
	public void testSetFieldValueStringLong() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.setFieldValue(String,
	 * double)'
	 */
	public void testSetFieldValueStringDouble() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.setFieldValue(String,
	 * M7DateTime)'
	 */
	public void testSetFieldValueStringM7DateTime() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.setFieldValue(String,
	 * M7Date)'
	 */
	public void testSetFieldValueStringM7Date() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.setFieldValue(String,
	 * M7Time)'
	 */
	public void testSetFieldValueStringM7Time() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.setFieldValue(String,
	 * M7PhoneNumber)'
	 */
	public void testSetFieldValueStringM7PhoneNumber() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.setFieldEmpty(String)'
	 */
	public void testSetFieldEmpty() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.setFieldNull(String)'
	 */
	public void testSetFieldNull() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.getFieldState(String)'
	 */
	public void testGetFieldState() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.getFieldValue(String)'
	 */
	public void testGetFieldValue() {

	}

	/*
	 * Test method for
	 * 'com.eversolve.Medi7.M7Composite.getFieldValueAsLong(String)'
	 */
	public void testGetFieldValueAsLong() {

	}

	/*
	 * Test method for
	 * 'com.eversolve.Medi7.M7Composite.getFieldValueAsDouble(String)'
	 */
	public void testGetFieldValueAsDouble() {

	}

	/*
	 * Test method for
	 * 'com.eversolve.Medi7.M7Composite.getFieldValueAsDateTime(String)'
	 */
	public void testGetFieldValueAsDateTime() {

	}

	/*
	 * Test method for
	 * 'com.eversolve.Medi7.M7Composite.getFieldValueAsDate(String)'
	 */
	public void testGetFieldValueAsDate() {

	}

	/*
	 * Test method for
	 * 'com.eversolve.Medi7.M7Composite.getFieldValueAsTime(String)'
	 */
	public void testGetFieldValueAsTime() {

	}

	/*
	 * Test method for
	 * 'com.eversolve.Medi7.M7Composite.getFieldValueAsPhoneNumber(String)'
	 */
	public void testGetFieldValueAsPhoneNumber() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.deleteAllChildren()'
	 */
	public void testDeleteAllChildren() {

	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.debug()'
	 */
	public void testDebug() {

	}

	public void testToString() {

		try {
			oru.loadPersistedMessage(M7MessageTest.msgORU_1,
					new M7DefinitionFile(M7MessageTest.oruDefFileName));
			abg.loadPersistedMessage(M7MessageTest.abgORU,
					new M7DefinitionFile(M7MessageTest.oruDefFileName));
		} catch (M7Exception e3) {

			e3.printStackTrace();
			fail(e3.getLocalizedMessage());
		} catch (IOException e3) {

			e3.printStackTrace();
			fail(e3.getLocalizedMessage());
		}

		try {
			System.out.println(oru.getChild("MSH").toString());
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}

		try {
			M7Repeat segment = oru.getRepeat("OBX");
			M7Composite duplicate = segment.add();
			segment.copy(duplicate);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Composite.toXML()'
	 */
	public void testToXML() {

		try {
			oru.loadPersistedMessage(M7MessageTest.msgORU_1,
					new M7DefinitionFile(M7MessageTest.oruDefFileName));
			abg.loadPersistedMessage(M7MessageTest.abgORU,
					new M7DefinitionFile(M7MessageTest.oruDefFileName));
		} catch (M7Exception e3) {

			e3.printStackTrace();
			fail(e3.getLocalizedMessage());
		} catch (IOException e3) {

			e3.printStackTrace();
			fail(e3.getLocalizedMessage());
		}
		// setup the parser
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);
		String outXML = null;
		try {
			outXML = oru.toXML();
		} catch (M7Exception e2) {

			e2.printStackTrace();
			fail("Unable to create the XML string for "
					+ M7MessageTest.msgORU_1);
		}
		ByteArrayInputStream is = new ByteArrayInputStream(outXML.getBytes());
		try {
			FileOutputStream file = new FileOutputStream(
					"test/data/testToXML.xml");
			file.write(outXML.getBytes());
			file.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unable to create a document builder");
		}
		if (db != null) {
			try {

				// System.out.println("Parsing:"+outXML);
				// Document doc =
				db.parse(is);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("Invalid xml string:" + e.getLocalizedMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("Unexpected: " + e.getLocalizedMessage());
			}
		}

	}

}
