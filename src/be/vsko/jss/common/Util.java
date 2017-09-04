package be.vsko.jss.common;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import be.vsko.jss.exception.ReflectionException;

public class Util {
	
	/**
	 * A very simple string escape method
	 */
	public static String escape(String str) {
		String result = str.replace("'", "\\'");
				
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Object createObjectWithEmptyConstructor(Class clazz) {
		Object result = null;
		try {
			Class partypes[] = new Class[0];        
			Constructor constructor = clazz.getConstructor(partypes);
			Object arglist[] = new Object[0];            
		
			result = constructor.newInstance(arglist);
		}
		catch(Exception e) {
			throw new ReflectionException(e);
		}
		
		return result;
	}
	
	/**
	 * Creates an entity using its empty constructor.
	 */
	@SuppressWarnings("unchecked")
	public static AbstractEntity createAbstractEntity(String entityName) {
		AbstractEntity result = null;
		
		try {
			Class clazz = getEntityClass(entityName);
			result = (AbstractEntity)Util.createObjectWithEmptyConstructor(clazz);
		}
		catch(Exception e) {
			throw new ReflectionException(e);
		}				
		
		return result;
	}
	
	/**
	 * Gets the class of an entity using its name.
	 */
	@SuppressWarnings("unchecked")
	public static Class getEntityClass(String entityName) {
		Class clazz = null;
		try {
			//correct the entity name to have the first letter uppercase
			String entityNameAux = entityName.substring(0, 1).toUpperCase()+entityName.substring(1);
			clazz = Class.forName(JSSConfigurationProperties.getInstance().getModelPackage()+"."+entityNameAux);
		}
		catch(Exception e) {
			throw new ReflectionException(e);
		}
		
		return clazz;
	}
	
	/**
	 * Given an enum name, gets a list of all options with values and translated descriptions
	 */
	@SuppressWarnings("unchecked")
	public static List<Option> getEnumValues(String enumName) {
		Class clazz = getEntityClass(enumName);
		List<Option> result = new ArrayList<Option>();
		
		try {
			if(clazz.isEnum()) {
				
				for (Object object : clazz.getEnumConstants()) {					
					String desc = getTranslatedEnumDescription(clazz, (Enum)object);
					
					result.add(new Option(object.toString(), desc));
				}
			}
		}
		catch(Exception e) {
			throw new ReflectionException(e);
		}
		
		return result;
	}
	

	/**
	 * Translate the description of an enum value using an external resource
	 */
	@SuppressWarnings("unchecked")
	public static String getTranslatedEnumDescription(Class enumClass, Enum<?> value) {
		
		return JSSTranslationProperties.getInstance().getMessage(enumClass.getSimpleName().toLowerCase()+"."+value.toString().toLowerCase());
	}
}
