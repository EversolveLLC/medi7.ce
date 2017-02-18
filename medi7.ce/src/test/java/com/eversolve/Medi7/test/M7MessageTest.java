/**
 * Project:Medi7
 * com.eversolve.Medi7.M7MessageTest created on Jan 26, 2006
 * Change log:
 * 
 * 	Initial version
 * 
 * 
 */

package com.eversolve.Medi7.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import com.eversolve.Medi7.M7DefinitionFile;
import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.M7Message;
import com.eversolve.Medi7.util.M7StringHolder;

/**
 * Class: M7MessageTest
 * 
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * 
 */
public class M7MessageTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(M7MessageTest.class);
	}

	M7DefinitionFile oruDefFile, ackDefFile, qrfDefFile, ao4DefFile;

	M7Message oruR01, ackR01, adta04;

	StringBuffer sampleORU_1 = new StringBuffer();

	StringBuffer sampleORU_2 = new StringBuffer();

	StringBuffer sampleADT_a04 = new StringBuffer();

	StringBuffer ABGPOC = new StringBuffer();

	public static String oruDefFileName = "test//data//vital_signs_oru_r01.dat";

	public static String ackDefFileName = "test//data//vital_signs_ack_r01.dat";

	public static String qrfDefFileName = "test//data//spacelabs.dat";

	public static String msgORU_1 = "test//messages//vital_signs_1.txt";

	public static String msgORU_2 = "test//messages//vital_signs_2.txt";

	public static String QRF = "test//messages//spacelabs_msg.txt";

	public static String abgORU = "test//messages//abgpoc.txt";

	public static String a34 = "test//data//a34_1.txt";

	public static String a34Dat = "test//data//a34.dat";

	public static String a04 = "test//messages//ADTAO4_Sample.txt";

	public static String a04Dat = "test//data//inbound_adt.dat";

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		oruDefFile = new M7DefinitionFile(oruDefFileName);
		ackDefFile = new M7DefinitionFile(ackDefFileName);
		qrfDefFile = new M7DefinitionFile(qrfDefFileName);
		ao4DefFile = new M7DefinitionFile(a04Dat);

		try {
			sampleORU_1 = new StringBuffer(M7Message.readMessage(msgORU_1));
			sampleORU_2 = new StringBuffer(M7Message.readMessage(msgORU_2));
			ABGPOC = new StringBuffer(M7Message.readMessage(abgORU));
			sampleADT_a04 = new StringBuffer(M7Message.readMessage(a04));
		} catch (M7Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail(e1.getLocalizedMessage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail(e1.getLocalizedMessage());
		}

		try {
			this.oruR01 = new M7Message(sampleORU_1.toString(), oruDefFile);
			adta04 = new M7Message(sampleADT_a04.toString().trim(), ao4DefFile);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unparsable " + e.toString() + "; \n" + sampleORU_1.toString());
		}

	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Constructor for M7MessageTest.
	 * 
	 * @param arg0
	 */
	public M7MessageTest(String arg0) {
		super(arg0);
	}

	public final void testA34() {
		M7Message loadedFromFile = new M7Message();
		try {

			loadedFromFile.loadPersistedMessage(a34, new M7DefinitionFile(
					a34Dat));
		} catch (M7Exception e) {

			e.printStackTrace();
			fail("unable to parse: " + a34 + " due to " + e.toString());
		} catch (IOException e) {

			e.printStackTrace();
			fail(e.getMessage());
		}
		try {
			FileOutputStream file = new FileOutputStream(
					"test//messages//debug//ADTAO4_contents.txt");
			String stream = loadedFromFile.debug();
			file.write(stream.getBytes());
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileOutputStream file = new FileOutputStream(
					"test//messages//debug//ADTAO4.txt");
			String stream = loadedFromFile.getMLLPFrame();
			file.write(stream.getBytes());
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Class under test for void M7Message()
	 */
	public final void testM7Message() {
		// TODO Implement M7Message().
		M7Message msgABG = new M7Message();
		try {
			msgABG.loadPersistedMessage(abgORU, oruDefFile);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}

		// System.out.println(msgABG.debug());

	}
	
	/*
	 * Class under test for void M7Message()
	 */
	public final void testBloodhoundMessage() {
	
		M7Message msgORM = new M7Message();
		try {
			msgORM.loadPersistedMessage("test//messages//Bloodhound//OML_O21.3.txt", new M7DefinitionFile("test//data//Bloodhound//OML_O21_v4.dat"));
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
			System.out.println(msgORM.debug());
			
			M7Message msgOUL = new M7Message();
			try {
				msgOUL.loadPersistedMessage("test//messages//Bloodhound//OUL_R22.1.txt", new M7DefinitionFile("test//data//Bloodhound//OUL_R22_v4.dat"));
			} catch (M7Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail(e.getLocalizedMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail(e.getLocalizedMessage());
			}
				System.out.println(msgOUL.debug());
				try {
					msgOUL.loadPersistedMessage("test//messages//Bloodhound//OUL_R22_2.txt", new M7DefinitionFile("test//data//Bloodhound//OUL_R22_v4.dat"));
				} catch (M7Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fail(e.getLocalizedMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fail(e.getLocalizedMessage());
				}
					System.out.println(msgOUL.debug());
			
			

	}

	/*
	 * Class under test for void M7Message(M7DefinitionFile, String)
	 */
	public final void testM7MessageM7DefinitionFileString() {
		// TODO Implement M7Message().
	}

	public final void testCreateNewMsg() {
		// TODO Implement CreateNewMsg().
	}

	/*
	 * Class under test for void M7Message(String, M7DefinitionFile)
	 */
	public final void testM7MessageStringM7DefinitionFile() {
		// TODO Implement M7Message().
	}

	public final void testParseMsg() {

		// System.out.println(oruR01.debug());
		String path = "MSH.SendingApplication.namespaceID";
		try {
			assertEquals(oruR01.getFieldValue(path), "EVERSOLVE");
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Invalid path '" + path + "'; " + e.getLocalizedMessage());
		}
	}

	public final void testValidate1() {
		// Parse a message
		System.out.println("testValidate1");
		M7Message oru1 = null;
		try {
			oru1 = new M7Message(sampleORU_1.toString(), oruDefFile);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unparsable " + e.toString() + "; \n" + sampleORU_1.toString());
		}

		M7StringHolder errorHolder = new M7StringHolder();
		boolean validMsg = false;
		try {
			validMsg = oru1.validate("ORU:R01:ORU_R01_Profile", oruDefFile,
					errorHolder);
		} catch (M7Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail("Validation :" + e1.toString());
		}
		if (!validMsg) {
			System.out.println(errorHolder.toString());
			fail("Validation of oru-1 failed");
		}
		// Validated
	}

	public final void testValidate2() {
		// Parse a message
		System.out.println("testValidate2");
		M7Message abg = null;
		try {
			abg = new M7Message(ABGPOC.toString(), oruDefFile);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unparsable " + e.toString() + "; \n" + ABGPOC.toString());
		}

		M7StringHolder errorHolder = new M7StringHolder();
		boolean validMsg = false;
		try {
			validMsg = abg.validate("ORU:R01:ORU_R01_Profile", oruDefFile,
					errorHolder);
		} catch (M7Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail("Validation :" + e1.toString());
		}

		if (!validMsg) {
			// System.out.println(errorHolder.toString());
		} else {

			fail("Validation of abg failed");
		}

		// Validated
	}

	public final void testValidate3() {
		// Parse a message
		System.out.println("testValidate3");
		M7Message registration = null;
		try {
			registration = new M7Message(sampleADT_a04.toString(), ao4DefFile);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unparsable " + e.toString() + "; \n" + ABGPOC.toString());
		}

		M7StringHolder errorHolder = new M7StringHolder();
		boolean validMsg = false;
		try {
			validMsg = registration.validate("ADT:A04:", ao4DefFile,
					errorHolder);
		} catch (M7Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail("Validation :" + e1.toString());
		}

		if (!validMsg) {
			// System.out.println(errorHolder.toString());
		} else {

			fail("Validation of registration failed");
		}

		// Validated
	}

	public final void testOldDef() {
		String msgStr = "MSH|^~\\&|GTWY|HOSP|RVMCC|RVMC|20040812141403||ADT^A01|1567|P|2.3\n" +
				"EVN|A01|20040812142030||02||20040812141524\n"+
				"PID|||UB56^^^||Smith^John^^^||19681025|M|||25554 Ave SE^^Seattle^WA^98007|||||||00010958098^^^|\n" +
				"PD1|||||||Y|N\nNK1|001|Jones^Amanda|F|522 MAIN ST^^CUMBERLAND^MD^28765^US|(301)555-2134\n"+
				"PV1||E|ICU^15^12^^^^^^||||||||||||||||200302271322|||||||||||||||||||||||||200410081430\n"+
				"AL1|1|DA|SK|SV|ConvL|200503041523|\nDG1|1|I9||SYNCOPAL EPISODE||A|||||||||\n"+
				"DRG|diagGroup|20050203|1|drg1|outT|20|4554.7|M|400.20|Y|\n"+
				"GT1|1|000511154|Jones^Paul^A^^|^^^^|665 PROTTSMAN WAY^^CAVE JUNCTION^WA^97523^^^JOSEPHINE|(541)592-3453||19100322|F||A|540445384||||RETIRED|^^^^00000|||5|||||||||00053|W|19750301|||||||Y|||JHW||||||||||||||C\n"+
				"IN1|1|88888|3000|MEDICARE NORTHWEST|PO BOX 8110^^ISSAQUAH^WA^97207|||||||19770901|||MCR|Jones^Paul^A^^|A|19100322|665 PROTTSMAN WAY^^CAVE JUNCTION^WA^97523^^^^|||1||||||||||||||448019458D||||||5|F|^^^^00000|N||||000511154";
		M7DefinitionFile mdf = null;
		try {
			mdf = new M7DefinitionFile("test//data//HL72.4MsgDef.dat");
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		M7Message msg = null;
		try {
			msg = new M7Message(msgStr, mdf);
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		try {
			System.out.println(msg.stream());
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		
	}

	public final void testStream() {
		// TODO Implement Stream().
	}

	public final void testStream_CRLF() {
		// TODO Implement Stream_CRLF().
	}

	public final void testCheckEval() {
		// TODO Implement checkEval().
	}

	public final void testPersistMessage() {
		// TODO Implement PersistMessage().
	}

	public final void testLoadPersistedMessage() {
		System.out.println("testLoadPersistedMessage");
		M7Message loadedFromFile = new M7Message();
		try {
			loadedFromFile.loadPersistedMessage(msgORU_1, oruDefFile);
		} catch (M7Exception e) {

			e.printStackTrace();
			fail("unable to parse: " + msgORU_1 + " due to " + e.toString());
		} catch (IOException e) {

			e.printStackTrace();
			fail(e.getMessage());
		}
		assertEquals(oruR01.toString(), loadedFromFile.toString());
	}

	public final void testLoadPersistedMessage2() {
		System.out.println("testLoadPersistedMessage");
		M7Message loadedFromFile = new M7Message();
		try {
			loadedFromFile.loadPersistedMessage(a04, ao4DefFile);
		} catch (M7Exception e) {

			e.printStackTrace();
			fail("unable to parse: " + a04 + " due to " + e.toString());
		} catch (IOException e) {

			e.printStackTrace();
			fail(e.getMessage());
		}

		System.out.println(loadedFromFile.debug());
		System.out.println(loadedFromFile.toString());
		try {
			System.out.println(loadedFromFile.stream());
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sampleADT_a04);
		assertEquals(sampleADT_a04.toString().trim(), loadedFromFile.toString()
				.trim());

	}

	public final void testLoadPersistedMessageSpaceLabs() {
		System.out.println("testLoadPersistedMessageSpaceLabs");
		M7Message loadedFromFile = new M7Message();
		long startTime = System.currentTimeMillis();
		long endTime = 0L;
		try {
			loadedFromFile.loadPersistedMessage(QRF, qrfDefFile);
			endTime = System.currentTimeMillis();
		} catch (M7Exception e) {
			e.printStackTrace();
			fail("unable to parse: " + QRF + " due to " + e.toString());
		} catch (IOException e) {

			e.printStackTrace();
			fail(e.getMessage());
		}
		System.out.println("Time required to load and parse ="
				+ (endTime - startTime) + " ms");
		startTime = System.currentTimeMillis();
		String debugString = loadedFromFile.debug();
		endTime = System.currentTimeMillis();
		System.out.println("Time required to read every field ="
				+ (endTime - startTime) + " ms");
		try {
			FileOutputStream file = new FileOutputStream(
					"test//data//M7MessageTest.txt");

			file.write(debugString.getBytes());
			file.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			long st = System.currentTimeMillis();
			loadedFromFile.stream();
			long et = System.currentTimeMillis();
			System.out.println("Time required to stream =" + (et - st) + " ms");

		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public final void testIsASTM() {
		assertTrue(!oruR01.isASTM());
	}

	public final void testIsHL7() {
		assertTrue(oruR01.isHL7());
	}

}
