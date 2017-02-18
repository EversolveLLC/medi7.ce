/**
 *	(c) Copyright 2006 Eversolve, LLC
**/
/**
 * Project:Medi7
 * com.eversolve.Medi7.resources.Messages created on Jan 16, 2006
 * Change log:
 * 
 * 	Initial version
 * @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 * 
 */
package com.eversolve.Medi7.resources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <p>Resource bundle accessor class</p>
  * <br/><code>Copyright (c) 2000-2007 Eversolve, LLC. All Rights
 * Reserved.</code>
 *  @author <a href="http://www.eversolve.com">Eversolve, LLC</a>
 *  @version 1.0
 * 
 */
public class Messages {
	private static final String BUNDLE_NAME = "com.eversolve.Medi7.resources.messages";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		//Method getString
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
