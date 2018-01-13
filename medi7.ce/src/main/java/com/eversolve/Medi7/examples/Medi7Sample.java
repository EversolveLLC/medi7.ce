/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
**/

package com.eversolve.Medi7.examples;

import java.util.Vector;

import com.eversolve.Medi7.M7Composite;
import com.eversolve.Medi7.M7DefinitionFile;
import com.eversolve.Medi7.M7Exception;
import com.eversolve.Medi7.M7Field;
import com.eversolve.Medi7.M7Message;
import com.eversolve.Medi7.M7MessageDefinition;
import com.eversolve.Medi7.M7Repeat;
import com.eversolve.Medi7.datatypes.M7Date;


/**
 * Medi7Sample demonstrates the use of the Medi7 API for constructing, parsing, and validating messages
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 1.2
 * 
 */
public class Medi7Sample
{
    public static void main( String argv[] )
    {
    	try
	    {
            //instantiate the message definition file
            M7DefinitionFile hl7MessgeDefinitionFile = new M7DefinitionFile( "test\\data\\M7MsgDf.dat" );
            M7DefinitionFile astmDef = new M7DefinitionFile("test\\data\\Bloodhound Message Spec.dat");
            M7Message astmMsg = new M7Message();
    		astmMsg.loadPersistedMessage("test\\messages\\astm.txt",astmDef);
    		if(astmMsg.isASTM())
    		{
    		System.out.println("Parsing ASTM message:");
    		}
    		
    		System.out.println(astmMsg.debug());
    		astmMsg.setFieldValue("H.Delimiters", "\\^~");
    		System.out.print(astmMsg.stream());

    		
    		M7Message msg = new M7Message(astmDef, "ASTM:BATCH:");
    		M7Message clone = new M7Message(astmDef, "ASTM:BATCH:");
            msg.setFieldValue("H.Delimiters", M7Message.ASTM_ENC_CHARS);
            msg.setFieldValue("H.Sender.Name", "Bloodhound");
            msg.setFieldValue("H.ReceiverID", "LIS");
            msg.setFieldValue("H.ProcessingID", "P");
            msg.setFieldValue("H.StandardVersion.Version", "1");
            msg.setFieldValue("H.MessageDateTime", new M7Date(java.util.Calendar.getInstance()));
            msg.setFieldValue("Q.SequenceNumber", "LIS1-Aa");
            msg.setFieldValue("Q.SpecimenID.SpecimenSource", "1221");
            msg.setFieldValue("Q.UniversalTestId[0].UniversalTestId", "ALL");
            msg.setFieldValue("Q.StatusCode", "O");
            System.out.println("MSG:"+msg.stream());
            //clone message
		     msg.copy(clone);
		     System.out.println("Clone:"+clone.stream_CRLF());
		     System.out.println("Original:"+msg.stream_CRLF());
            //create a message, first time loads the definition
		     msg = new M7Message( hl7MessgeDefinitionFile, "ADT:A05" );


		    //load the message with data
		    //fill the MSH
		    msg.setFieldValue( "MSH.EncodingCharacters", M7Message.MSH_ENC_CHARS );
		    msg.setFieldValue( "MSH.SendingApplication.namespaceID", "REGADT" );
		    msg.setFieldValue( "MSH.SendingFacility.namespaceID", "MCM" );
		    msg.setFieldValue( "MSH.ReceivingApplication", "IFENG" );
		    msg.setFieldValue( "MSH.MessageDateTime.DateTime", "199601061000" );
		    msg.setFieldValue( "MSH.MessageType.MessageTypeCode", "ADT" );
		    msg.setFieldValue( "MSH.MessageType.TriggerEvent", "A05" );
		    msg.setFieldValue( "MSH.ControlID", "000001" );
		    msg.setFieldValue( "MSH.ProcessingID", "P" );
		    msg.setFieldValue( "MSH.VersionID", "2.3" );

		    //fill in the EVN, do this by getting the EVN segment first
		    //so all names are now relative to the EVN segment object
		    M7Composite evnSeg = msg.newChild( "TheEVNSeg", null );
		    evnSeg.setFieldValue( "EventTypeCode", "A05" );
		    evnSeg.setFieldValue( "EventTime.DateTime", "199601061000" );
		    evnSeg.setFieldValue( "PlannedEventTime.DateTime", "199601101400" );
		    evnSeg.setFieldValue( "ReasonCode", "01" );

		    //fill in the PID
		    msg.setFieldValue( "PID.IntPatID[0].PatientID", 191919L );
		    msg.setFieldValue( "PID.IntPatID[0].AssigningFacility", "GENHOSP" );
		    msg.setFieldValue( "PID.AltPatId", "253763" );
		    msg.setFieldValue( "PID.PatientName.FamilyName", "MASSIE" );
		    msg.setFieldValue( "PID.PatientName.GivenName", "JAMES" );
		    msg.setFieldValue( "PID.PatientName.MiddleName", "A" );
		    msg.setFieldValue( "PID.DateOfBirth.DateTime", "19560129" );
		    msg.setFieldValue( "PID.Sex", "M" );
		    msg.setFieldValue( "PID.PatientAddress[0].Street", "171 ZOBERLEIN" );
		    msg.setFieldValue( "PID.PatientAddress[0].City", "ISHPEMING" );
		    msg.setFieldValue( "PID.PatientAddress[0].StateOrProvince", "MI" );
		    msg.setFieldValue( "PID.PatientAddress[0].ZipOrPostalCode", "49849" );
		    msg.setFieldNull( "PID.PatientAddress[0].Country" );
		    msg.setFieldValue( "PID.HomePhone[0]", "(900)485-5344" );
		    msg.setFieldValue( "PID.BusPhone[0]", "(900)485-5344" );
		    msg.setFieldValue( "PID.MaritalStatus", "S" );
		    msg.setFieldValue( "PID.Religion", "C" );
		    msg.setFieldValue( "PID.PatAccountNumber.ID", "10199925" );
		    msg.setFieldValue( "PID.PatSSN", "371-66-9256" );

		    //fill in the first NK1, shows how to do this using a
		    //repeat instead of a fully qualified name
		    M7Repeat nk1Rpt = msg.createRepeat( "NK1", null );
		    M7Composite nk1 = nk1Rpt.add();
		    nk1.setFieldValue( "SetId", 1L );
		    nk1.setFieldValue( "Name.FamilyName", "MASSIE" );
		    nk1.setFieldValue( "Name.GivenName", "ELLEN" );
		    nk1.setFieldValue( "Relationship.ID", "SPOUSE" );
		    nk1.setFieldValue( "Address.Street", "171 ZOBERLEIN" );
		    nk1.setFieldValue( "Address.City", "ISHPEMING" );
		    nk1.setFieldValue( "Address.StateOrProvince", "MI" );
		    nk1.setFieldValue( "Address.ZipOrPostalCode", "49849" );
		    nk1.setFieldNull( "Address.Country" );

		    //fill in the next NK1 by referencing the appropriate element in the repeat
		    nk1Rpt.setRepeatedCompositesValue( 0, "PhoneNumber[0]", "(900)485-5344" );
		    nk1Rpt.setRepeatedCompositesValue( 0, "BusPhoneNumber[0]", "(900)545-1234" );
		    nk1Rpt.setRepeatedCompositesValue( 0, "BusPhoneNumber[1]", "(900)545-1200" );
		    nk1Rpt.setRepeatedCompositesValue( 0, "ContactRole.ID", "EC" );
		    nk1Rpt.setRepeatedCompositesValue( 0, "ContactRole.Text", "EMERGENCY CONTACT" );

		    //fill in the third NK1, notice that repeat instances can be specified in the
		    //string by using an [n] notation embeded in the string
		    msg.setFieldValue( "NK1[2].SetId", 3L );

		    //fill in the fourth NK1
		    msg.setFieldValue( "NK1[3].SetId", 4L );
		    msg.setFieldValue( "NK1[3].Address.Street", "123 INDUSTRY WAY" );
		    msg.setFieldValue( "NK1[3].Address.City", "ISHPEMING" );
		    msg.setFieldValue( "NK1[3].Address.StateOrProvince", "MI" );
		    msg.setFieldValue( "NK1[3].Address.ZipOrPostalCode", "49849" );
		    msg.setFieldNull( "NK1[3].Address.Country" );
		    msg.setFieldValue( "NK1[3].BusPhoneNumber[0]", "(900)545-1200" );
		    msg.setFieldValue( "NK1[3].ContactRole.ID", "EM" );
		    msg.setFieldValue( "NK1[3].ContactRole.Text", "EMPLOYER" );
		    msg.setFieldValue( "NK1[3].StartDate", "19940605" );
		    msg.setFieldValue( "NK1[3].NOKJobTitle", "PROGRAMMER" );
		    msg.setFieldValue( "NK1[3].OrgName", "ACME SOFTWARE COMPANY" );

		    //fill in the PV1
		    msg.setFieldValue( "PV1.PatientClass", "O" );
		    msg.setFieldValue( "PV1.AttendingDoctor.PhysicianID", "0148" );
		    msg.setFieldValue( "PV1.AttendingDoctor.FamilyName", "ADDISON" );
		    msg.setFieldValue( "PV1.AttendingDoctor.GivenName", "JAMES" );
		    msg.setFieldValue( "PV1.ReferringDoctor.PhysicianID", "0148" );
		    msg.setFieldValue( "PV1.ReferringDoctor.FamilyName", "ADDISON" );
		    msg.setFieldValue( "PV1.ReferringDoctor.GivenName", "JAMES" );
		    msg.setFieldValue( "PV1.ConsultingDoctor[0].PhysicianID", "0148" );
		    msg.setFieldValue( "PV1.ConsultingDoctor[0].FamilyName", "ADDISON" );
		    msg.setFieldValue( "PV1.ConsultingDoctor[0].GivenName", "JAMES" );
		    msg.setFieldValue( "PV1.HospitalService", "AMB" );
		    msg.setFieldValue( "PV1.AdmittingDoctor.PhysicianID", "0148" );
		    msg.setFieldValue( "PV1.AdmittingDoctor.FamilyName", "ADDISON" );
		    msg.setFieldValue( "PV1.AdmittingDoctor.GivenName", "JAMES" );
		    msg.setFieldValue( "PV1.PatientType", "S" );
		    msg.setFieldValue( "PV1.VisitNumber", 1400L );
		    msg.setFieldValue( "PV1.PatientType", "S" );
		    msg.setFieldValue( "PV1.FinancialClass[0].FinancialClass", "A" );
		    msg.setFieldValue( "PV1.DischargeToLocation.Code", "GENHOSP" );

		    //fill in the PV2
		    msg.setFieldValue( "PV2.ExpectedAdmitDate", "199601101400" );
		    msg.setFieldValue( "PV2.ExpectedDischargeDate","199601101400" );

		    //fill in the first OBX
		    msg.setFieldValue( "OBX[0].ValueType", "ST" );
		    msg.setFieldValue( "OBX[0].ObservationID.ID", "1010.1" );
		    msg.setFieldValue( "OBX[0].ObservationID.Text", "BODY WEIGHT" );
		    msg.setFieldValue( "OBX[0].ObservationValue", "62" );
		    msg.setFieldValue( "OBX[0].Units.ID", "kg" );

		    //fill in the second OBX
		    msg.setFieldValue( "OBX[1].ValueType", "ST" );
		    msg.setFieldValue( "OBX[1].ObservationID.ID", "1010.1" );
		    msg.setFieldValue( "OBX[1].ObservationID.Text", "HEIGHT" );
		    msg.setFieldValue( "OBX[1].ObservationValue", "190" );
		    msg.setFieldValue( "OBX[1].Units.ID", "cm" );

		    //fill in DG1
		    msg.setFieldValue( "DG1[0].SetID", 1L );
		    msg.setFieldValue( "DG1[0].DiagCodingMethod", "19" );
		    msg.setFieldValue( "DG1[0].DiagDescription", "BIOPSY" );
		    msg.setFieldValue( "DG1[0].DiagDRGType", "00" );

		    //fill in GT1
		    msg.setFieldValue( "GT1[0].SetID", 1L );
		    msg.setFieldValue( "GT1[0].Name.FamilyName", "MASSIE" );
		    msg.setFieldValue( "GT1[0].Name.GivenName", "JAMES" );
		    msg.setFieldNull( "GT1[0].Name.MiddleName" );
		    msg.setFieldNull( "GT1[0].Name.Suffix" );
		    msg.setFieldNull( "GT1[0].Name.Prefix" );
		    msg.setFieldNull( "GT1[0].Name.Degree" );
		    msg.setFieldValue( "GT1[0].Address.Street", "171 ZOBERLEIN" );
		    msg.setFieldValue( "GT1[0].Address.City", "ISHPEMING" );
		    msg.setFieldValue( "GT1[0].Address.StateOrProvince", "MI" );
		    msg.setFieldValue( "GT1[0].Address.ZipOrPostalCode", "49849" );
		    msg.setFieldNull( "GT1[0].Address.Country" );
		    msg.setFieldValue( "GT1[0].HomePhone[0]", "(900)485-5344" );
		    msg.setFieldValue( "GT1[0].BusPhone[0]", "(900)485-5344" );
		    msg.setFieldValue( "GT1[0].Relationship", "SELF" );
		    msg.setFieldValue( "GT1[0].SSN", "371-66-925" );
		    msg.setFieldValue( "GT1[0].EmployerName", "MOOSES AUTO CLINIC" );
		    msg.setFieldValue( "GT1[0].EmployerName", "MOOSES AUTO CLINIC" );
		    msg.setFieldValue( "GT1[0].EmployerAddress.Street", "171 ZOBERLEIN" );
		    msg.setFieldValue( "GT1[0].EmployerAddress.City", "ISHPEMING" );
		    msg.setFieldValue( "GT1[0].EmployerAddress.StateOrProvince", "MI" );
		    msg.setFieldValue( "GT1[0].EmployerAddress.ZipOrPostalCode", "49849" );
		    msg.setFieldNull( "GT1[0].EmployerAddress.Country" );
		    msg.setFieldValue( "GT1[0].EmployerPhone[0]", "(900)485-5344" );

		    //fill in the first IN1, notice that the IN1 is part of a
		    //repeating segment group.
		    msg.setFieldValue( "INSGrp[0].IN1.SetID", 1L );
		    msg.setFieldValue( "INSGrp[0].IN1.InsPlanID", "0" );
		    msg.setFieldValue( "INSGrp[0].IN1.InsCompanyID", "BC1" );
		    msg.setFieldValue( "INSGrp[0].IN1.InsCompanyName", "BLUE CROSS" );
		    msg.setFieldValue( "INSGrp[0].IN1.InsCompanyAddress.Street", "171 ZOBERLEIN" );
		    msg.setFieldValue( "INSGrp[0].IN1.InsCompanyAddress.City", "ISHPEMING" );
		    msg.setFieldValue( "INSGrp[0].IN1.InsCompanyAddress.StateOrProvince", "MI" );
		    msg.setFieldValue( "INSGrp[0].IN1.InsCompanyAddress.ZipOrPostalCode", "49849" );
		    msg.setFieldNull( "INSGrp[0].IN1.InsCompanyAddress.Country" );
		    msg.setFieldValue( "INSGrp[0].IN1.InsCompanyPhone[0]", "(900)485-5344" );
		    msg.setFieldValue( "INSGrp[0].IN1.GroupNumber", "90" );
		    msg.setFieldValue( "INSGrp[0].IN1.AuthorizationInfo.AuthorizationNumber", "50 OK" );

		    //fill in the second IN1 which is in another instance of Insurance Group
		    msg.setFieldValue( "INSGrp[1].IN1.SetID", 2L );
		    msg.setFieldNull( "INSGrp[1].IN1.InsPlanID" );
		    msg.setFieldNull( "INSGrp[1].IN1.InsCompanyID" );

		   
		    //@TODO fix validation
		   //msg.validate( "ADT:A05:0", hl7MessgeDefinitionFile, h_errors );
		   //System.out.println( "VALIDATION ERRORS: " + h_errors.toString() );

		    // Stream the msg into an HL7 ASCII-encoded stream with only
                    //   CR delimiting the segments
		    String msgStream = msg.stream();

                    // Stream the msg into an HL7 ASCII-encoded stream with CR/LF
                    //   characters delimiting the segments
		    msgStream = msg.stream_CRLF();
			
            printMessage( msgStream );
          

		    //Here is a message stream to parse
		    String strMsg = new String();
		    strMsg += "MSH|^~\\&|REGADT|MCM|IFENG||199601061000||ADT^A05|000001|P|2.3||||||||||^|\r";
		    strMsg += "EVN|A05|199601061000|199601101400|01\r";
		    strMsg += "PID|||191919^^^GENHOSP|253763|MASSIE^JAMES^A||19560129|M|||";
		    strMsg += "171 ZOBERLEIN^^ISHPEMING^MI^49849^\"\"||(900)485-5344|(900)485-5344||S|C|";
		    strMsg += "10199925|371-66-9256\r";
		    strMsg += "NK1|1|MASSIE^ELLEN|SPOUSE|171 ZOBERLEIN^^ISHPEMING^MI^49849^\"\"|(900)485-5344|";
		    strMsg += "(900)545-1234~(900)545-1200|EC^EMERGENCY CONTACT\r";
		    strMsg += "NK1|2|MASSIE^MARYLOU|MOTHER|300 ZOBERLEIN^^ISHPEMING^MI^49849^\"\"|(900)485-5344|";
		    strMsg += "(900)545-1234~(900)545-1200|EC^EMERGENCY CONTACT\r";
		    strMsg += "NK1|3\r";
		    strMsg += "NK1|4|||123 INDUSTRY WAY^^ISHPEMING^MI^49849^\"\"||(900)545-1200|EM^EMPLOYER";
		    strMsg += "|19940605||PROGRAMMER|||ACME SOFTWARE COMPANY\r";
		    strMsg += "PV1||O|||||0148^ADDISON^JAMES|0148^ADDISON^JAMES|0148^ADDISON^JAMES|AMB|||||||";
		    strMsg += "0148^ADDISON^JAMES|S|1400|A|||||||||||||||||GENHOSP\r";
		    strMsg += "PV2||||||||199601101400|199601101400\r";
		    strMsg += "OBX||ST|1010.1^BODY WEIGHT||62|kg\r";
		    strMsg += "OBX||ST|1010.1^HEIGHT||190|cm\r";
		    strMsg += "DG1|1|19||BIOPSY||00\r";
		    strMsg += "GT1|1||MASSIE^JAMES^\"\"^\"\"^\"\"^\"\"||171 ZOBERLEIN^^ISHPEMING^MI^49849^\"\"|";
		    strMsg += "(900)485-5344|(900)485-5344||||SELF|371-66-925||||MOOSES AUTO CLINIC|";
		    strMsg += "171 ZOBERLEIN^^ISHPEMING^MI^49849^\"\"|(900)485-5344\r";
		    strMsg += "IN1|1|0|\"\"|BLUE CROSS|171 ZOBERLEIN^^ISHPEMING^MI^49849^\"\"||(900)485-5344|90||||||50 OK\r";
		    strMsg += "IN1|2|\r";
			strMsg += "JNK|er|rth|||tt\r";
		    M7Message msgFromString = new M7Message( strMsg, hl7MessgeDefinitionFile );

		    //query some values from the message
		    System.out.println( "SOME DATA FROM THE " + msgFromString.getName() + " MESSAGE" );
		    System.out.println( msgFromString.getFieldValue( "MSH.SendingApplication.namespaceID") );
		    System.out.println( msgFromString.getFieldValue( "PID.IntPatID[0].PatientID") );
		    System.out.println( msgFromString.getFieldValue( "NK1[0].Name.FamilyName") );
		    System.out.println( msgFromString.getFieldValue( "NK1[1].Name.FamilyName") );
		    System.out.println( msgFromString.getFieldValue( "NK1[2].SetId") );
		    System.out.println( msgFromString.getFieldValue( "NK1[3].Address.City") );
		    System.out.println( msgFromString.getFieldValue( "PV1.AttendingDoctor.FamilyName") );
		    System.out.println( msgFromString.getFieldValue( "PV2.ExpectedDischargeDate") );
		    System.out.println( msgFromString.getFieldValue( "OBX[0].ValueType") );
		    System.out.println( msgFromString.getFieldValue( "OBX[1].ValueType") );
		    System.out.println( msgFromString.getFieldValue( "DG1[0].SetID") );
		    System.out.println( msgFromString.getFieldValue( "GT1[0].Name.FamilyName") );
		    System.out.println( msgFromString.getFieldValue( "INSGrp[0].IN1.SetID") );
		    System.out.println( msgFromString.getFieldValue( "INSGrp[1].IN1.SetID") );

			try
			{
				Vector<String> v = new Vector<String>();
				getM7Segments( msgFromString, v );
				System.out.println( "\n\nALL FIELDS WITH A VALUE" );
				System.out.println( "\n============================" );
				{
					for( int i = 0; i < v.size(); i++ )
						System.out.println( v.elementAt(i) );
				}
			}
			catch( M7Exception e )
			{
				System.out.println( "\n\nERROR TRAVERSING MESSAGE FOR FIELDS THAT ARE PRESENT\n" );
				System.out.println( e.toString() );
			}

			try
			{
				M7MessageDefinition md = hl7MessgeDefinitionFile.getDefinition( "ADT:A05" );
				Vector<String> v = new Vector<String>();
				getM7MessageDefinitionFields( md, v, null );
				System.out.println( "\n\nALL FIELDS IN A MESSAGE DEFINITION" );
				System.out.println( "\n=====================================" );
				{
					for( int i = 0; i < v.size(); i++ )
						System.out.println( v.elementAt(i) );
				}
			}
			catch( M7Exception e )
			{
				System.out.println( "\n\nERROR TRAVERSING MESSAGE FOR FIELDS THAT ARE DEFINED IN A MESSAGE DEFINITION\n" );
				System.out.println( e.getMessage() );
			}
	    }
	    catch( M7Exception ex )
	    {
		    System.out.println( ex.toString() );
		    ex.printStackTrace();
	    }
	    catch( java.io.IOException ioex )
	    {
		    System.out.println( "IOException!" );
		    System.out.println( "Reason: " + ioex );
	    }
	}

    public static void printMessage( String sMessage )
    {
          StringBuffer sb = new StringBuffer( sMessage );
          for ( int i = 0; i < sb.length(); i++ )
          {
            if ( sb.charAt( i ) == '\r' )
                 sb.setCharAt( i, '\n' );
          }

          System.out.println( sb );
    }

	/**
	 * This function will walk the message and fill in the vector with the field names
	 * of all fields that are present and are not set to empty or null.
	 */
	public static void getM7Segments( M7Composite node, Vector<String> fields ) throws M7Exception
	{
	   if( node == null )
	      return;

	   if( node.isField() )
	   {
			M7Field field = (M7Field) node;
			if( field.getState() == M7Field.eFldPresent  )
				fields.addElement( new String( field.getAbsoluteName() + " = " + field.getValue() ) );
	   }

	   for( int i = 0; i < node.getChildCount(); i++ )
	   {
	      M7Composite branch = node.getChildByPosition( i );
	      getM7Segments( branch, fields );
	   }
	}

	/**
	 * This function will walk the message definition and fill in the vector with the field names
	 * of all fields that are defined for a message.
	 */
	public static void getM7MessageDefinitionFields( M7MessageDefinition msgDef, Vector<String> fields, String name ) throws M7Exception
	{
		if( name == null )
		{
			name = new String();
		}
		else
		{
			name += msgDef.getName();
			if( msgDef.isRepeat() )
				name += "[]";
		}

		if( msgDef.isField() )
		{
		 	fields.addElement( name );
		}
		else
		{
			if( name.length() > 0 )
				name += M7Composite.ABS_NAME_DELM;;

			for( int i = 0; i < msgDef.getChildCount(); i ++ )
			{
				M7MessageDefinition childDef = msgDef.getChild( i );
    			getM7MessageDefinitionFields( childDef, fields, name );
		    }
	    }
    }

}
