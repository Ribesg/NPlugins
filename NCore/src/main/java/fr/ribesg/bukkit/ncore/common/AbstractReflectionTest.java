/***************************************************************************
 * Project file:    NPlugins - NCore - AbstractReflectionTest.java         *
 * Full Class name: fr.ribesg.bukkit.ncore.common.AbstractReflectionTest   *
 *                                                                         *
 *                Copyright (c) 2012-2014 Ribesg - www.ribesg.fr           *
 *   This file is under GPLv3 -> http://www.gnu.org/licenses/gpl-3.0.txt   *
 *    Please contact me at ribesg[at]yahoo.fr if you improve this file!    *
 ***************************************************************************/

package fr.ribesg.bukkit.ncore.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class will just contains some helper for tests.
 */
public abstract class AbstractReflectionTest {

	protected Object executeStaticMethod(final Class clazz, final String methodName, final Class[] argClasses, final Object... args) {
		try {
			final Method method = clazz.getDeclaredMethod(methodName, argClasses);
			method.setAccessible(true);
			return method.invoke(null, args);
		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException("Failed to access method '" + methodName + "'", e);
		}
	}

	protected Object executeMethod(final Object obj, final String methodName, final Class[] argClasses, final Object... args) {
		try {
			final Method method = obj.getClass().getDeclaredMethod(methodName, argClasses);
			method.setAccessible(true);
			return method.invoke(obj, args);
		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException("Failed to access method '" + methodName + "'", e);
		}
	}

	protected Object getStaticFieldValue(final Class clazz, final String fieldName) {
		try {
			final Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(null);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new RuntimeException("Failed to field method '" + fieldName + "'", e);
		}
	}

	protected Object getFieldValue(final Object obj, final String fieldName) {
		try {
			final Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new RuntimeException("Failed to field method '" + fieldName + "'", e);
		}
	}
}
