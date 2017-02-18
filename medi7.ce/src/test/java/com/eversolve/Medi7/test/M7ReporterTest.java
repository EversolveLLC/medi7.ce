package com.eversolve.Medi7.test;

import java.io.IOException;

import junit.framework.TestCase;

import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.examples.Medi7Reporter;

public class M7ReporterTest extends TestCase {

	String oruInsightDefFileName = "test//data//ORU_R01.dat";
	String oruInsightReport = "test//data//ORU_R01_FieldPath.txt";
	
	String oruDefFileName = "test//data//vital_signs_oru_r01.dat";

	String ormMGHDefFileName = "test//data//orm_o01.dat";

	String ormDefFileName = "test//data//PACS_ORM_O01.dat";

	String ackDefFileName = "test//data//vital_signs_ack_r01.dat";

	String defaultDefName = "test//data//M7MsgDf1.dat";
	
	String astmDefName = "test//data//Bloodhound Message Spec.dat";
	
	String astmReportName = "test//data//ASTM_E1394_report.txt";
	// String ackFileName =
	String oruReportFileName = "test//data//vital_signs_oru_report.txt";

	String ormMGHReportFileName = "test//data//orm_mgh_report.txt";

	String ormReportFileName = "test//data//orm_report.txt";

	String ackReportFileName = "test//data//vital_signs_ack_report.txt";

	String defaultReportFileName = "test//data//msgdef_report.txt";

	String msgType = "ORU:R01";

	String oruProfile = msgType + ":ORU_R01_Profile";

	String ackType = "ACK:R01";

	String ackProfile = ackType + ":ACK_Profile";

	String ormType = "ORM:O01";

	String ormProfile = ormType + ":omnisolve";

	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(M7ReporterTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Reporter.main(String[])'
	 */
	public void testMainORU() {
		String[] args1 = { "-f", "test//data//ACK.dat", "-m", "ACK:ALL", "-p",
				"ACK:ALL:", "-o", "test//data//ACK_paths.txt" };
		try {
			Medi7Reporter.main(args1);
		} catch (M7Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());

		}
	}
	
	/*
	 * Test method for 'com.eversolve.Medi7.M7Reporter.main(String[])'
	 */
	public void testBloodhoundPathGenerator() {
		String[] args1 = { "-f", "test//data//Bloodhound//OML_O21_v4.dat", "-m", "OML:O21", "-p",
				"OML:O21:CMI", "-o", "test//data//Bloodhound//OML_21_4_paths.txt" };
		try {
			Medi7Reporter.main(args1);
		} catch (M7Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());

		}
		String[] args2 = { "-f", "test//data//Bloodhound//OUL_R22_v4.dat", "-m", "OUL:R22", "-p",
				"OUL:R22:", "-o", "test//data//Bloodhound//OUL_R22_4_paths.txt" };
		try {
			Medi7Reporter.main(args2);
		} catch (M7Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		
		String[] args3 = { "-f", "test//data//Bloodhound//ORL_O22_v4.dat", "-m", "ORL:O22", "-p",
				"ORL:O22:", "-o", "test//data//Bloodhound//ORL_O22_4_paths.txt" };
		try {
			Medi7Reporter.main(args3);
		} catch (M7Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());

		}
		
		String[] args4 = { "-f", "test//data//Bloodhound//ACK_ALL_v4.dat", "-m", "ACK:ALL", "-p",
				"ACK:ALL:", "-o", "test//data//Bloodhound//ACK_ALL_4_paths.txt" };
		try {
			Medi7Reporter.main(args4);
		} catch (M7Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());

		}
	}
	
	
	

	
	public void testMainORUInsight() {
		String[] args1 = { "-f", oruInsightDefFileName, "-m", msgType, "-p",
				oruProfile, "-o", oruInsightReport };
		try {
			Medi7Reporter.main(args1);
		} catch (M7Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());

		}
	}
	
	
	public void testASTM() {
		String[] args1 = { "-f", astmDefName, "-m", "ASTM", "-o", astmReportName };
		try {
			Medi7Reporter.main(args1);
		} catch (M7Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());

		}
	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Reporter.main(String[])'
	 */
//	public void testMainORMLarge() {
//		String[] args1 = { "-f", ormMGHDefFileName, "-m", ormType, "-p",
//				ormProfile, "-o", ormMGHReportFileName };
//		try {
//			Medi7Reporter.main(args1);
//		} catch (M7Exception e) {
//			e.printStackTrace();
//			fail(e.getLocalizedMessage());
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail(e.getLocalizedMessage());
//
//		}
//	}

	/*
	 * Test method for 'com.eversolve.Medi7.M7Reporter.main(String[])'
	 */
//	public void testMainORM() {
//		String[] args1 = { "-f", ormDefFileName, "-m", ormType, "-p",
//				ormProfile, "-o", ormReportFileName };
//		try {
//			Medi7Reporter.main(args1);
//		} catch (M7Exception e) {
//			e.printStackTrace();
//			fail(e.getLocalizedMessage());
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail(e.getLocalizedMessage());
//
//		}
//	}
//
//	public void testMainACK() {
//		String[] args3 = { "-f", ackDefFileName, "-m", ackType, "-p",
//				ackProfile, "-o", ackReportFileName };
//		try {
//			Medi7Reporter.main(args3);
//		} catch (M7Exception e) {
//			e.printStackTrace();
//			fail(e.getLocalizedMessage());
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail(e.getLocalizedMessage());
//
//		}
//	}

	// public void testMain() {
	// String[] args2 = { "-f", defaultDefName,, "-o",
	// "test//data//a34report.txt" };
	// try {
	// Medi7Reporter.main(args2);
	// } catch (M7Exception e) {
	// e.printStackTrace();
	// fail(e.getLocalizedMessage());
	// } catch (IOException e) {
	// e.printStackTrace();
	// fail(e.getLocalizedMessage());
	//
	// }
	// }

//	public void testMainACK34() {
//		String[] args2 = { "-f", "test//data//a34.dat", "-m", "ADT:A34", "-o",
//				"test//data//a34report.txt" };
//		try {
//			Medi7Reporter.main(args2);
//		} catch (M7Exception e) {
//			e.printStackTrace();
//			fail(e.getLocalizedMessage());
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail(e.getLocalizedMessage());
//
//		}
//	}

	/*
	 * Test method for
	 * 'com.eversolve.Medi7.M7Reporter.reportOnPart(M7MessageDefinition, int,
	 * M7Profile)'
	 */
	public void testReportOnPart() {

	}

	/*
	 * Test method for
	 * 'com.eversolve.Medi7.M7Reporter.getM7DefinitionFields(M7MessageDefinition,
	 * Vector, String, M7Profile, M7Rule)'
	 */
	public void testGetM7DefinitionFields() {

	}

}
