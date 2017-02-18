package com.eversolve.Medi7.examples;

import java.io.FileOutputStream;
import java.io.IOException;

import com.eversolve.Medi7.M7DefinitionFile;
import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.M7Message;
import com.eversolve.Medi7.resources.Messages;

/**
 * Medi7MessageUtilities demonstrates the use of the Medi7 API for debugging
 * messages and validating them against a message profile <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * 
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.0
 * 
 */
public class Medi7MessageUtilities {

	public static M7DefinitionFile mdf = null;
	

	/**
	 * Main
	 * 
	 * @param argv '
	 *            <b>-f</b> <i>[msgdef_filename]</i> <b>-i</b>
	 *            <i>[input_message]</i> <b>-o</b> <i>[output]</i>'
	 * @throws M7Exception
	 *             if the file is corrupted
	 * @throws IOException
	 *             if the file is missing
	 */
	public static void main(String argv[]) throws M7Exception, IOException {
		String messageDefFileName = new String();
		String messageFile = new String();
		String outputFileName = new String();

		for (int i = 0; i < argv.length; i++) {
			String sCmd = argv[i];

			if (sCmd.equals("-f"))
				messageDefFileName = argv[i + 1];
			else if (sCmd.equals("-i"))
				messageFile = argv[i + 1];
			else if (sCmd.equals("-o"))
				outputFileName = argv[i + 1];
			i++;
		}
		messageDefFileName = (messageDefFileName.length() != 0) ? messageDefFileName
				: Messages.getString("M7DefinitionFile.DEFAULT_FILE");
		StringBuffer outputStr = new StringBuffer();
		
		outputStr.append("Message Definition File: " + messageDefFileName)
				.append('\n');
		outputStr.append("M7Message: " + messageFile).append('\n');
		
		System.out.println(outputStr);

		mdf = new M7DefinitionFile(messageDefFileName);
		M7Message msg = new M7Message();
		msg.loadPersistedMessage(messageFile,mdf);
		
		FileOutputStream file = new FileOutputStream(outputFileName);		
		file.write(msg.debug().getBytes());
		file.close();
		
		FileOutputStream fileMLLP = new FileOutputStream(messageFile+"MLLP.txt");		
		fileMLLP.write(msg.getMLLPFrame().getBytes());
		fileMLLP.write(M7Message.MSG_TERMINATE_CHAR);
		fileMLLP.write(M7Message.MSG_END_CHAR);
		fileMLLP.close();
		
	}

}
