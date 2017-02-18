/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7.examples;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import com.eversolve.Medi7.M7;
import com.eversolve.Medi7.M7Composite;
import com.eversolve.Medi7.M7DefinitionFile;
import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.M7MessageDefinition;
import com.eversolve.Medi7.M7Profile;
import com.eversolve.Medi7.M7Rule;
import com.eversolve.Medi7.resources.Messages;

/**
 * Class Medi7Reporter scans the message definition file and extracts the defintion
 * tree for a message and for a profile
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.2
 */

public class Medi7Reporter {
	/**
	 * <code>mdf</code> message definition
	 */
	public static M7DefinitionFile mdf = null;

	/**
	 * Main
	 * 
	 * @param argv '
	 *            <b>-f</b> <i>[filename]</i> <b>-m</b> <i>[messagetype]</i>
	 *            <b>-p</b> <i>[profile]</i> <b>-o</b> <i>[output]</i>'
	 * @throws M7Exception
	 *             if the file is corrupted
	 * @throws IOException
	 *             if the file is missing
	 */
	public static void main(String argv[]) throws M7Exception, IOException {
		String fileName = new String();
		String messageType = new String();
		String profile = new String();
		String outputFileName = new String();

		for (int i = 0; i < argv.length; i++) {
			String sCmd = argv[i];

			if (sCmd.equals("-f"))
				fileName = argv[i + 1];
			else if (sCmd.equals("-m"))
				messageType = argv[i + 1];
			else if (sCmd.equals("-p"))
				profile = argv[i + 1];
			else if (sCmd.equals("-o"))
				outputFileName = argv[i + 1];
			i++;
		}
		fileName = (fileName.length() != 0) ? fileName : Messages
				.getString("M7DefinitionFile.DEFAULT_FILE");
		StringBuffer outputStr = new StringBuffer();
		outputStr.append("File: " + fileName).append('\n');
		outputStr.append("M7Message: " + messageType).append('\n');
		outputStr.append("Profile: " + profile).append('\n');
		outputStr
				.append(
						"This report includes all the absolute paths for fields defined in the message \n" +
						"definition and it indicates whether the field corresponding to a path is\n"+
						"required based if a profile is specified. \n" +
						"Maximum cardinalities/multiplicities are also specified if they appear in the profile rules.")
				.append('\n');

		try {
			mdf = new M7DefinitionFile(fileName);

			// if message type specified then only report on that one message
			// with the
			// profile provided, else report all messages in file
			if (messageType.length() == 0) {
				Vector<String> mtList = mdf.GetMsgTypeList();
				for (Enumeration<String> e = mtList.elements(); e.hasMoreElements();) {
					String mtListItem = (String) e.nextElement();
					M7MessageDefinition msgDef = mdf.getDefinition(mtListItem);
					String report = reportOnPart(msgDef, 0, null);

					outputStr
							.append("******************************************\n");
					outputStr.append(" *\t" + "Report for '" + mtListItem
							+ "'\t\t*\n");
					outputStr
							.append("******************************************\n");
					outputStr.append(report);
					if (outputFileName.length() > 0) {
						persistOutput(outputFileName, outputStr.toString());
					}
					//System.out.println(outputStr.toString());

				}
			} else {
				M7MessageDefinition pMd = mdf.getDefinition(messageType);
				M7Profile pProfile = null;
				if (profile.length() > 0)
					pProfile = pMd.getProfile(profile, mdf);
				Vector<String> v = new Vector<String>();
				outputStr
						.append("******************************************\n");
				outputStr.append(" *\t" + "Report for '" + messageType
						+ "'\t\t*\n");
				outputStr
						.append("******************************************\n");

				M7MessageDefinition msgDef = mdf.getDefinition(messageType);
				getM7DefinitionFields(msgDef, v, null, pProfile, null);
				// String report = ReportOnPart(pMd, 0, pProfile);

				for (int i = 0; i < v.size(); i++) {
					outputStr.append(v.elementAt(i)).append('\n');
				}
				if (outputFileName.length() > 0) {
					persistOutput(outputFileName, outputStr.toString());
				}
				System.out.println(outputStr.toString());
			}
		} catch (M7Exception e) {
			e.printStackTrace();
			System.out.println("Medi7 Exception: " + e.toString());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("IO Exception: " + e.toString());
		}
	}

	/**
	 * Report on part of the message definition
	 * 
	 * @param pMd
	 * @param depth
	 * @param pProfile
	 * @return
	 * @throws M7Exception
	 */
	public static String reportOnPart(M7MessageDefinition pMd, int depth,
			M7Profile pProfile) throws M7Exception {
		StringBuffer reportString = new StringBuffer();

		String indent = new String();
		for (int j = 0; j < depth; j++)
			indent += '\t';

		depth++;

		for (int i = 0; i < pMd.getChildCount(); i++) {
			M7MessageDefinition pChild = pMd.getChild(i);

			if (depth == 1)
				reportString.append('\n');

			reportString.append(indent);

			reportString.append(pChild.getName());

			M7Profile pChildProfile = null;

			// if we have a profile then put out rule about this part
			if (pProfile != null) {
				// get the rule for this part
				M7Rule pRule = pProfile.getRuleForPart(pChild.getName());
				if (pRule != null) {
					if (pChild.isRepeat()) {
						if (pRule.getRepeatMax() == M7.NO_MAX_RPT)
							reportString.append(M7Composite.ABS_NAME_REPEAT_START_DELIM+"n"+M7Composite.ABS_NAME_REPEAT_END_DELIM);
						else {
							reportString.append(M7Composite.ABS_NAME_REPEAT_START_DELIM);
							reportString.append(String.valueOf(pRule
									.getRepeatMax()));

							reportString.append(M7Composite.ABS_NAME_REPEAT_END_DELIM);
						}

					}

					// put out the info about the rule
					switch (pRule.getUsageType()) {
					case M7Rule.eRequired:
						reportString.append(" (required)");
						break;
					case M7Rule.eExclude:
						reportString.append(" (exclude)");
						break;
					}

					// if there is a child profile for this rule then pass that
					// down the chain
					String childProfileName = pRule.getChildProfile();

					if (childProfileName.length() > 0)
						pChildProfile = pChild
								.getProfile(childProfileName, mdf);

				} else {
					if (pChild.isRepeat()) {
						reportString
								.append(M7Composite.ABS_NAME_REPEAT_START_DELIM
										+ "n"
										+ M7Composite.ABS_NAME_REPEAT_END_DELIM);
					}
				}
			} else {
				if (pChild.isRepeat()) {
					reportString.append(M7Composite.ABS_NAME_REPEAT_START_DELIM
							+ "n" 
							+ M7Composite.ABS_NAME_REPEAT_END_DELIM);
				}
			}

			reportString.append('\n');

			reportString.append(reportOnPart(pChild, depth, pChildProfile));
		}

		return reportString.toString();
	}

	/**
	 * This function will walk the message definition and fill in the vector
	 * with the field names of all fields that are defined for a message.
	 * 
	 * @param msgDef
	 * @param fields
	 * @param name
	 * @param pProfile
	 * @param rule
	 * @throws M7Exception
	 */
	public static void getM7DefinitionFields(M7MessageDefinition msgDef,
			Vector<String> fields, String name, M7Profile pProfile, M7Rule rule)
			throws M7Exception {
		if (name == null) {
			name = new String();
		} else {
			name += msgDef.getName();
			if (rule != null) {
				if (msgDef.isRepeat()) {
					if (rule.getRepeatMax() == M7.NO_MAX_RPT)
						name += M7Composite.ABS_NAME_REPEAT_START_DELIM + "n"
								+ M7Composite.ABS_NAME_REPEAT_END_DELIM;
					else {
						name += M7Composite.ABS_NAME_REPEAT_START_DELIM;
						name += String.valueOf(rule.getRepeatMax());
						;
						name += M7Composite.ABS_NAME_REPEAT_END_DELIM;
					}

				}
				if (msgDef.isField()) {
					// put out the info about the rule
					switch (rule.getUsageType()) {
					case M7Rule.eRequired:
						name += " (required)";
						break;
					case M7Rule.eExclude:
						name += " (exclude)";
						break;
					}
				}

			} else {
				if (msgDef.isRepeat())
					name += M7Composite.ABS_NAME_REPEAT_START_DELIM + "n"
							+ M7Composite.ABS_NAME_REPEAT_END_DELIM;
			}

		}

		if (msgDef.isField()) {
			fields.addElement(name);
		} else {
			if (name.length() > 0) {
				name += M7Composite.ABS_NAME_DELM;
			}

			for (int i = 0; i < msgDef.getChildCount(); i++) {
				M7MessageDefinition pChild = msgDef.getChild(i);
				M7Profile pChildProfile = null;
				M7Rule pRule = null;
				if (pProfile != null) {
					// get the rule for this part
					pRule = pProfile.getRuleForPart(pChild.getName());

					if (pRule != null) {
						// if there is a child profile for this rule then pass
						// that
						// down the chain
						String childProfileName = pRule.getChildProfile();

						if (childProfileName.length() > 0) {
							pChildProfile = pChild.getProfile(childProfileName,
									mdf);
						}
					}
				}
				getM7DefinitionFields(pChild, fields, name, pChildProfile,
						pRule);
			}
		}
	}

	/**
	 * Saves the file contents to the destination file name
	 * 
	 * @param fileName
	 *            String
	 * @param contents
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void persistOutput(String fileName, String contents)
			throws FileNotFoundException, IOException {
		// attempt to open the file
		FileOutputStream file = new FileOutputStream(fileName);
		file.write(contents.getBytes());
		file.close();
	}

}
