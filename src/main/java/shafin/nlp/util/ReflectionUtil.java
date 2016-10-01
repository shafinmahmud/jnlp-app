package shafin.nlp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import shafin.nlp.corpus.model.Document;

public class ReflectionUtil {

	public static <T> T updateBeanFields(Class<T> beanClass, T oldBean, T newBean) {
		Field[] fields = beanClass.getDeclaredFields();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {
				try {
					field.setAccessible(true);
					field.set(oldBean, field.get(newBean));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return oldBean;
	}

	@SuppressWarnings("unchecked")
	public static <V> V getValue(Object object, String fieldName) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				return (V) field.get(object);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return null;
	}

	public static boolean setValue(Object object, String fieldName, String fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);

				Object value = convertStringToValue(fieldValue, field.getType().getSimpleName());
				field.set(object, value);
				return true;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return false;
	}
	
	public static boolean setListValue(Object object, String fieldName, List<String> fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);

				field.set(object, fieldValue);
				return true;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return false;
	}


	public static Object convertStringToValue(String value, String type) {
		switch (type) {
		case "Integer":
			return Integer.valueOf(value.trim());

		case "Long":
			return Long.valueOf(value.trim());

		case "Float":
			return Float.valueOf(value.trim());

		case "Double":
			return Double.valueOf(value.trim());

		case "String":
			return value;
		}

		return value;
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {

		// System.out.println(convertStringToValue("211", "Integer"));
		Class<?> clazz = Document.class;
		Document instance = (Document) clazz.newInstance();
		
		List<String> list = new ArrayList<>();
		list.add("Bangladesh");
		list.add("Sylhet");
		
		setListValue(instance, "categories", list);
		setValue(instance, "writter", "Shafin Mahmud");

		System.out.println(getValue(instance, "categories") + " : " + getValue(instance, "writter"));
	}
}
