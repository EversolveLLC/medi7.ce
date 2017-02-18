/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7;

import java.util.Enumeration;
import java.util.Vector;

import com.eversolve.Medi7.resources.Messages;
import com.eversolve.Medi7.util.M7StringHolder;

/**
 * Medi7 Profile class is used for validating a message against the message
 * profile specified in the message definition. <br/>It addesses such usage
 * rules as: require, optional, excluded, repeated, max cardinality.
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 2.3
 **/
public class M7Profile {
	protected Vector<M7Rule> m_ruleList = new Vector<M7Rule>();

	protected String m_profileId ;

	/**
	 * Profile definition delimiter
	 */

	protected M7Profile(String profileId, M7MessageDefinition MsgDef,
			M7DefinitionFile MsgDefFile) throws M7Exception {
		m_profileId = profileId;

		//get the profile line from the file
		String line = new String();
		switch (MsgDef.getType()) {
		case M7Composite.eM7Message:
			line = MsgDefFile.GetMessageProfileLine(profileId);
			break;
		case M7Composite.eM7SegmentGrp:
			line = MsgDefFile.GetSegGrpProfileLine(profileId);
			break;
		case M7Composite.eM7Segment:
			line = MsgDefFile.GetSegProfileLine(profileId);
			break;
		case M7Composite.eM7CompositeField:
			line = MsgDefFile.GetCompFldProfileLine(profileId);
			break;
		default:
			return;
		}

		//parse through the line and create rule objects
		_ParseLine(new M7StringHolder(line), MsgDef, MsgDefFile);
	}

	/**
	 * Applies the profile rules
	 */
	protected void _ApplyRules(M7MessageDefinition pMsgDef, M7Composite pComp,
			M7StringHolder h_errors) throws M7Exception {
		//walk the rule list passing in the parent component,
		//it will use its offset to get to the right child part
		for (Enumeration<M7Rule> e = m_ruleList.elements(); e.hasMoreElements();) {
			M7Rule r = (M7Rule) e.nextElement();

			//if the pComp is repeat then apply rule to every instance in
			// repeat
			if (pComp.isRepeat()) {
				M7Repeat pRepeat = (M7Repeat) pComp;
				//apply this rule to each instance of the repeat
				for (long i = 0; i < pRepeat.getSize(); i++) {
					M7Composite pRepComp = pRepeat.getItemAt(i);
					r._ApplyRule(pMsgDef, pRepComp, h_errors);
				}
			} else
				r._ApplyRule(pMsgDef, pComp, h_errors);
		}
	}

	/**
	 * Returns the rule for an element
	 * 
	 * @param name
	 *            Element name
	 * @return M7Rule object associated with this element
	 * @throws M7Exception
	 *             It throws an exception if the element name cannot be found
	 */
	public M7Rule getRuleForPart(String name) throws M7Exception {
		for (Enumeration<M7Rule> e = m_ruleList.elements(); e.hasMoreElements();) {
			M7Rule r = (M7Rule) e.nextElement();
			//System.out.println("Rule: " + r.toString());
			if (r.getOffset().equals(name))
				return r;
		}
		return null;
	}

	protected void _ParseLine(M7StringHolder line, M7MessageDefinition MsgDef,
			M7DefinitionFile MsgDefFile) throws M7Exception {
		//reset the line so upto and including the = sign are gone
		line.setString(line.toString().substring(
				line.toString().indexOf(M7DefinitionFile.KEY_DELIM) + 1));

		//loop through the entire line creating rules
		String part;
		while (true) {
			int pos = line.toString().indexOf(M7MessageDefinition.PART_DELIM);
			if (pos == M7.NPOS) {
				part = line.toString().substring(0);
				//part = part.substring( 0, part.length() - 1 );
			} else {
				part = line.toString().substring(0, pos);
			}

			//creating a new rule and adding to list
			if (part.trim().length() > 0) {
				//System.out.println("Rule:" + part);
				M7Rule Rule = new M7Rule(part);
				//System.out.println("Adding:" + Rule.toString());
				m_ruleList.addElement(Rule);
				//see if this new rule has a child rule and if so use the
				// offset
				//to get the child definition and ask that child to load its
				// profiles
				if (Rule.hasChildProfile()) {
					//this had better not return a null or the info in the
					// MsgDefFile is bad
					M7MessageDefinition ChildDef = MsgDef.getChild(Rule.getOffset());
					
					if(ChildDef != null)
					{
					ChildDef._LoadProfile(Rule.getChildProfile(), MsgDefFile);
					}
					else
					{
						throw new M7Exception(Messages.getString("M7Profile.MISSING_CHILD_DEF")+Rule.getOffset()); //$NON-NLS-1$
					}
				}
			}
			
				if (pos == M7.NPOS)
					break;
//				strip off the part from the line and continue
				line.setString(line.toString().substring(part.length() + 1));
			//}
			
		}
	}
}