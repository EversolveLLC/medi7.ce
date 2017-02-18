/**
 * (c) Copyright 1998-2006 Eversolve, L
 */
package com.eversolve.Medi7.examples;

import java.io.IOException;

import com.eversolve.Medi7.M7DefinitionFile;
import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.M7Message;
import com.eversolve.Medi7.util.M7StringHolder;

/**
 * Medi7Validation demonstrates the use of the Medi7 API for parsing messages
 * This sample shows how we can use message defintion files generated from the <a href="http://www.hl7.org/Special/committees/conformance/docs.cfm">Messaging Workbench</a> output files (XML)
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 */
public class Medi7Validation {

	static M7Message oruR01, abgR01;

	/**
	 * Name of the file containing the MWB-generated message profile for ORU^R01
	 * messages
	 */
	static String oruDefFileName = "test//data//vital_signs_oru_r01.dat";

	static String ackDefFileName = "test//data//vital_signs_ack_r01.dat";

	static String msgORU_1 = "test//messages//vital_signs_1.txt";

	static String msgORU_2 = "test//messages//vital_signs_2.txt";

	/**
	 * Name of the file containing an invalid ORU message
	 */
	static String abgORU = "test//messages//abgpoc.txt";

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {

		oruR01 = new M7Message();
		M7DefinitionFile oruMessageDef = null;

		try {
			oruMessageDef = new M7DefinitionFile(oruDefFileName);
			oruR01.loadPersistedMessage(msgORU_1, oruMessageDef);
			// the profile name is "ORU:R01:ORU_R01_Profile" as specified in the message profile
			M7StringHolder errors1 = new M7StringHolder();
			System.out.println("---Validating a correct message ---");
			/*
			 * ORU:R01=...
			 * ORU:R01:ORU_R01_Profile=...
			 */
			oruR01.validate("ORU:R01:ORU_R01_Profile", oruMessageDef, errors1);
			// check the errors
			if (errors1.toString().length() > 0)

			{
				System.out.println("Errors:\n" + errors1.toString());

			} else {
				System.out.println("M7Message valid: \n"+oruR01);

			}
			// Parsing a non conformant message...
			System.out.println("---Validating a non-conformant message ---");
			
			abgR01 = new M7Message();
			abgR01.loadPersistedMessage(abgORU, oruMessageDef);
			M7StringHolder errors2 = new M7StringHolder();
			// validate the message ...
			abgR01.validate("ORU:R01:ORU_R01_Profile", oruMessageDef, errors2);
			// check the errors
			if (errors2.toString().length() > 0)

			{
				System.out.println("Non-conformant message:\n" +abgR01+"Errors:\n"
						+ errors2.toString());

			} else {

			}
		} catch (M7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
