/**
 *	(c) Copyright 1998-2006 Eversolve, LLC
 **/
package com.eversolve.Medi7;

import com.eversolve.Medi7.resources.Messages;
import com.eversolve.Medi7.util.M7StringHolder;

/**
 * Contains a Medi7 validation rule referenced by a profile. Similar to the message defintion.
 * The rule identifies whehter the element is required, repeated, max multiplicity, 
 * and any regular expression contraint on its contents.
 * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * @version 2.3
 */
public class M7Rule {
	public final static char RULE_PART_DELIM = ',';

	public final static String RULE_REQUIRED = Messages
			.getString("M7Rule.REQUIRED_FLAG"); //$NON-NLS-1$

	public final static String RULE_OPTIONAL = Messages
			.getString("M7Rule.OPTIONAL_FLAG"); //$NON-NLS-1$

	public final static String RULE_EXCLUDE = Messages
			.getString("M7Rule.EXCLUDE_FLAG"); //$NON-NLS-1$

	public final static int eRequired = 0;

	public final static int eOptional = 1;

	public final static int eExclude = 2;

	String m_childProfileId;

	int m_usageType;

	long m_repeatMax;

	String m_regex = "^[\\x20-\\x7e]{1,199}$"; //all //$NON-NLS-1$

	String m_offset;

	/**
	 * Default constructor
	 */
	protected M7Rule() {
		m_usageType = eOptional;
		m_repeatMax = M7.NO_MAX_RPT;
	}

	/**
	 * Constructor
	 * 
	 * @param rule
	 *            Rule key in the message definition file
	 */
	protected M7Rule(String rule) {
		//System.out.println("Processing: "+rule);
		m_usageType = eOptional;
		m_repeatMax = M7.NO_MAX_RPT;

		//parse the rule
		if (rule.length() > 0) {
			String part;
			int pos, nextpos;
			

			//get the offset
			pos = rule.indexOf(RULE_PART_DELIM);
			m_offset = rule.substring(0, pos);

			//get the child profile name, could be empty
			nextpos = rule.indexOf(RULE_PART_DELIM, ++pos);
			if (nextpos == M7.NPOS)
				m_childProfileId = rule.substring(pos);
			else
				m_childProfileId = rule.substring(pos, nextpos);

			//the usage type is next but may not exist
			if (nextpos == M7.NPOS)
				return;

			pos = nextpos;
			nextpos = rule.indexOf(RULE_PART_DELIM, ++pos);
			if (nextpos == M7.NPOS)
				part = rule.substring(pos);
			else
				part = rule.substring(pos, nextpos);

			if (part.equalsIgnoreCase(RULE_REQUIRED)) //check for these two,
													  // rule defaults to
													  // optional
				m_usageType = eRequired;
			else if (part.equalsIgnoreCase(RULE_EXCLUDE))
				m_usageType = eExclude;

			//the max rpt is last, may not be there
			if (nextpos == M7.NPOS)
				return;

			//get the max repetition
			pos = nextpos;
			nextpos = rule.indexOf(RULE_PART_DELIM, ++pos);

			if (nextpos == M7.NPOS)
				part = rule.substring(pos);
			else
				part = rule.substring(pos, nextpos);

			if (part.length() > 0) {
				m_repeatMax = Long.valueOf(part).longValue();
			}
			//the regex may not be there
			if (nextpos == M7.NPOS)
				return;
			//get the regex expression
			part = rule.substring(nextpos + 1);
			if (part.length() > 0)
				m_regex = part;
		}
	}

	/**
	 * Applies the rule for validation
	 * 
	 * @param pMsgDef
	 * @param pComp
	 * @param h_errors
	 * @throws M7Exception
	 */
	protected void _ApplyRule(M7MessageDefinition pMsgDef, M7Composite pComp,
			M7StringHolder h_errors) throws M7Exception {
		//apply the appropriate rule
		switch (m_usageType) {
		case eRequired:
			this._ApplyRequiredRule(pMsgDef, pComp, h_errors);
			break;
		case eExclude:
			this._ApplyExcludeRule(pMsgDef, pComp, h_errors);
			break;
		default: //eOptional
			this._ApplyOptionalRule(pMsgDef, pComp, h_errors);
			break;
		}
	}

	/**
	 * Returns the usage for this element. <br/>The element/composite may be
	 * required, optional(empty allowed), or excluded. If it is excluded,
	 * validation will fail if the element is present, validation will fail if a
	 * required element is null or empty.
	 * 
	 * @return Integer, required, optional, excluded
	 * @see M7Rule#eRequired
	 * @see M7Rule#eOptional
	 * @see M7Rule#eExclude
	 */
	public int getUsageType() {
		return m_usageType;
	}

	/**
	 * Returns the maximum cardinality.
	 * 
	 * @return Integer, the maximum number of repetitions supported
	 */
	public long getRepeatMax() {
		return m_repeatMax;
	}

	/**
	 * Applies the rule for required elements
	 * 
	 * @param pMsgDef
	 * @param pComp
	 * @param h_errors
	 * @throws M7Exception
	 */
	protected void _ApplyRequiredRule(M7MessageDefinition pMsgDef, M7Composite pComp,
			M7StringHolder h_errors) throws M7Exception {
		//the child part must exist,
		//if it does exist apply the child profile
		M7Composite pChildComp = pComp.getChild(m_offset);

		if (pChildComp == null) {
			//ERROR, required child must exist
			h_errors.append( Messages
					.getString("M7Rule.MISSING_REQUIRED_COMPONENT")); //$NON-NLS-1$
			String absName = pComp.getAbsoluteName();
			h_errors.append( absName );
			if (absName.length() > 0)
				h_errors.append( Messages.getString("M7Rule.PERIOD")); //$NON-NLS-1$
			if (pMsgDef.getChild(m_offset) != null)
				h_errors.append(pMsgDef.getChild(m_offset).getName());
			else
				h_errors.append( Messages.getString("M7Rule.NULL_STRING")); //$NON-NLS-1$
			h_errors.append( '\n');
			return;
		}
		
		//if the child is a field then it cannot be null if required
		if (pChildComp.isField()) {
			M7Field pChildField = (M7Field) pChildComp;
			//System.out.println("Validating required:" +pChildField.GetAbsoluteName()+"="+pChildField.GetValue()+"; "+this.toString());
			if (pChildField.isNull()) {
				//ERROR, required field cannot have value of null
				h_errors.append(Messages
						.getString("M7Rule.NULL_REQUIRED_COMPONENT")); //$NON-NLS-1$
				h_errors.append(pChildComp.getAbsoluteName());
				h_errors.append('\n');
				return;
			} else if (pChildField.isEmpty()) {
				//ERROR, required field cannot have value of null
				h_errors.append(Messages
						.getString("M7Rule.EMPTY_REQUIRED_COMPONENT")); //$NON-NLS-1$
				h_errors.append( pChildComp.getAbsoluteName());
				h_errors.append('\n');
				return;
			} else {
				//check the regex for the field
				String value = ((M7Field)pChildComp).getValue();
				if((value!=null)&&(!value.matches(this.m_regex)))
				{
					//	ERROR, required field does not match the regular expression
					h_errors.append( Messages.getString("M7Rule.REGEX_ERROR")); //$NON-NLS-1$
					h_errors.append( value+ " '"+this.m_regex+"' "); //$NON-NLS-1$ //$NON-NLS-2$
					h_errors.append( pChildComp.getAbsoluteName());
					h_errors.append( '\n');
				}

			}
		}

		//if the child is repeat then check repeat rules
		if (pChildComp.isRepeat())
			this._ApplyRuleToRepeat(pChildComp, h_errors);

		//apply child profiles
		if (this.hasChildProfile()) {
			M7MessageDefinition pChildDef = pMsgDef.getChild(m_offset);
			pChildDef._ApplyProfile(m_childProfileId, pChildComp, h_errors);
		}
	}

	/**
	 * Applies rule for repetition validation
	 * 
	 * @param pComp
	 * @param h_errors
	 */
	protected void _ApplyRuleToRepeat(M7Composite pComp, M7StringHolder h_errors) {
		M7Repeat pRepeat = (M7Repeat) pComp;

		//if this is exclude then size of repeat must be 0
		if (m_usageType == eExclude) {
			//TO DO: what if all fields are empty so that when streamed nothing
			// would exist
			if (pRepeat.getSize() > 0) {
				//ERROR, exclusion rule, no repeats allowed
				h_errors.append( Messages
						.getString("M7Rule.EXCLUDED_BUT_PRESENT_REPEATED")); //$NON-NLS-1$
				h_errors.append( pRepeat.getAbsoluteName());
				h_errors.append( '\n');
				return;
			}
		}

		//if this is required and the size of repeat is 0 then error
		if (m_usageType == eRequired) {
			if (pRepeat.getSize() == 0) {
				//ERROR, required must have at least 1 instance in the repeat
				h_errors.append(Messages
						.getString("M7Rule.MISSING_REQUIRED_REPEATED")); //$NON-NLS-1$
				h_errors.append(pRepeat.getAbsoluteName());
				h_errors.append( '\n');
				return;
			}
		}

		//check the caridinality to see if more than the max have been created
		if (m_repeatMax != M7.NO_MAX_RPT) {
			if (pRepeat.getSize() > m_repeatMax) {
				//ERROR, exceeded max
				h_errors.append( Messages
						.getString("M7Rule.EXCEEDED_MAX_CARDINALITY_1") + m_repeatMax);
				h_errors.append(Messages
						.getString("M7Rule.EXCEEDED_MAX_CARDINALITY_2")); //$NON-NLS-1$
				h_errors.append(pRepeat.getSize());
				h_errors.append( Messages.getString("M7Rule.COLON")); //$NON-NLS-1$
				h_errors.append( pRepeat.getAbsoluteName());
				h_errors.append( '\n');
				return;
			}
		}
	}

	/**
	 * Applies rule for exclusion validation
	 * 
	 * @param pMsgDef
	 * @param pComp
	 * @param h_errors
	 * @throws M7Exception
	 */
	protected void _ApplyExcludeRule(M7MessageDefinition pMsgDef, M7Composite pComp,
			M7StringHolder h_errors) throws M7Exception {
		M7Composite pChildComp = pComp.getChild(m_offset);
		if (pChildComp != null) {
			//TO DO: what if streaming it would have no values, all marked as
			// empty

			if (pChildComp.isRepeat()) {
				this._ApplyRuleToRepeat(pChildComp, h_errors);
			} else {
				//ERROR, must not exist
				h_errors.append(Messages
						.getString("M7Rule.EXCLUDED_BUT_PRESENT")); //$NON-NLS-1$
				h_errors.append( pChildComp.getAbsoluteName());
				h_errors.append( '\n');
			}
		}

		//do not bother validating child profiles here because this part should
		// not exist
	}

	/**
	 * Applies rule for elements that may be optional
	 * 
	 * @param pMsgDef
	 * @param pComp
	 * @param h_errors
	 * @throws M7Exception
	 */
	protected void _ApplyOptionalRule(M7MessageDefinition pMsgDef, M7Composite pComp,
			M7StringHolder h_errors) throws M7Exception {
		//there is no rule for optional but if the child part exists
		//then we have to validate that the optional part is correct.
		M7Composite pChildComp = pComp.getChild(m_offset);
		if (pChildComp != null) {
			//if the child is repeat then check repeat rules
			if (pChildComp.isRepeat())
				this._ApplyRuleToRepeat(pChildComp, h_errors);

			//apply child profiles
			if (this.hasChildProfile()) {
				M7MessageDefinition pChildDef = pMsgDef.getChild(m_offset);
				pChildDef._ApplyProfile(m_childProfileId, pChildComp, h_errors);
			}
		}
	}

	/**
	 * Indicates whether a child profile exists
	 */
	protected boolean hasChildProfile() {
		if (m_childProfileId.length() > 0)
			return true;

		return false;
	}

	/**
	 * Returns the child profile
	 */
	public String getChildProfile() {
		return m_childProfileId;
	}

	/**
	 * Returns the profile location
	 */
	public String getOffset() {
		return m_offset;
	}

	public String toString() {
		StringBuffer strBuf = new StringBuffer(Messages.getString("M7Rule.M7RULE")); //$NON-NLS-1$
		strBuf.append(this.m_offset).append(Messages.getString("M7Rule.TOSTRING_DELIMITER")); //$NON-NLS-1$
		strBuf.append(this.m_usageType).append(Messages.getString("M7Rule.TOSTRING_DELIMITER")); //$NON-NLS-1$
		strBuf.append(this.m_childProfileId).append(Messages.getString("M7Rule.TOSTRING_DELIMITER")); //$NON-NLS-1$
		strBuf.append(this.m_repeatMax).append(Messages.getString("M7Rule.TOSTRING_DELIMITER")); //$NON-NLS-1$
		strBuf.append(this.m_regex);
		return strBuf.toString();
	}

}