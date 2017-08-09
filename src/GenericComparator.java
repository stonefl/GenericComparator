
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A generic comparator class to sort collection of elements in
 * <code> Collections.sort()</code>:. <br>
 * - For a collection of primitive data use
 * <code>Collections.sort(aList, new GenericComparator(true/false));</code> <br>
 * - For a collection of objects use
 * <code>Collections.sort(aList, new GenericComparator(true/false, "fieldName"));</code>
 * <br>
 * - For a collection of objects use
 * <code>Collections.sort(aList, new GenericComparator(true/false, "fieldName", "anotherFieldName"));</code>
 * <br>
 * - For a collection of objects use
 * <code>Collections.sort(aList, new GenericComparator(true/false, "fieldName.subFieldName", "anotherFieldName"));</code>
 * <br>
 * - For a collection of objects use
 * <code>Collections.sort(aList, new GenericComparator(true/false, "fieldName.subFieldName", "anotherFieldName.subFeildName"));</code>
 * <p>
 * This class is inspired and adopted from the GenericComparator class developed
 * by <a href="http://myjeeva.com/generic-comparator-in-java.html">myjeeva
 * blog</a>.
 * 
 * @author <a href="mailto:leifeng@fedex.com">Lei Feng 5004756</a>
 * 
 */
public class GenericComparator implements Comparator<Object> {
	/**
	 * {@link java.lang.String} data type name.
	 */
	private static final String DATATYPE_STRING = "java.lang.String";
	/**
	 * {@link java.util.Date} data type name.
	 */
	private static final String DATATYPE_DATE = "java.util.Date";
	/**
	 * {@link java.lang.Integer} data type name.
	 */
	private static final String DATATYPE_INTEGER = "java.lang.Integer";
	/**
	 * {@link java.lang.Long} data type name.
	 */
	private static final String DATATYPE_LONG = "java.lang.Long";
	/**
	 * {@link java.lang.Float} data type name.
	 */
	private static final String DATATYPE_FLOAT = "java.lang.Float";
	/**
	 * {@link java.lang.Double} data type name.
	 */
	private static final String DATATYPE_DOUBLE = "java.lang.Double";

	/**
	 * Specified sort field names, which is a vararg separated by commas. Each of
	 * the vararg is a <code>fieldName</code> or
	 * <code>fieldName.subFieldName</code>.
	 */
	private List<List<String>> alFieldNameMatrix = new ArrayList<List<String>>();
	/**
	 * Specified order direction.
	 * <p>
	 * true - ascending; false - descending;
	 */
	private boolean bAscendingOrder;

	/**
	 * <p>
	 * Constructor with <code>sortFieldNames</code> parameter for derived type of
	 * <code>Class</code> with default ascending sorting.
	 * </p>
	 * 
	 * <p>
	 * Implementation: <br>
	 * <code>Collections.sort(personList, new GenericComparator("name"));</code><br
	 * />
	 * </p>
	 * 
	 * @param sortFieldNames
	 *            - an array of {@link java.lang.String} specifying which fields
	 *            will be used for sorting."
	 */
	public GenericComparator(String... sortFieldNames) {
		this.bAscendingOrder = true;
		// read sort filed names into
		for (String fieldName : sortFieldNames) {
			List<String> tempList = new ArrayList<String>();
			for (String name : fieldName.split("\\.")) {
				tempList.add(name);
			}
			this.alFieldNameMatrix.add(tempList);
		}
	}

	/**
	 * <p>
	 * Constructor with <code>sortAscending, sortFieldNames</code> parameters for
	 * derived type of <code>Class</code>.
	 * </p>
	 * 
	 * <p>
	 * Implementation: <br>
	 * <code>Collections.sort(personList, new GenericComparator(true, "id");</code>
	 * <br>
	 * <code>Collections.sort(personList, new GenericComparator(false, "name", "id");</code>
	 * <br>
	 * <code>Collections.sort(personList, new GenericComparator(false, "payment.startSalary", "name");</code>
	 * </p>
	 * 
	 * @param sortAscending
	 *            - a {@link boolean} - <code>true</code> ascending order or
	 *            <code>false</code> descending order.
	 * @param sortFieldNames
	 *            - a {@link java.lang.String} - which fields used for sorting.
	 * 
	 */
	public GenericComparator(final boolean sortAscending, String... sortFieldNames) {
		this.bAscendingOrder = sortAscending;
		// read sort filed names into
		for (String fieldName : sortFieldNames) {
			List<String> tempList = new ArrayList<String>();
			for (String name : fieldName.split("\\.")) {
				tempList.add(name);
			}
			this.alFieldNameMatrix.add(tempList);
		}
	}

	/**
	 * Override method for comparison of two elements(Objects).
	 */
	@Override
	public int compare(final Object obj1, final Object obj2) {
		int response = 1;
		try {
			// initialize obj1 and obj2 to v1 and v2
			Object v1 = obj1;
			Object v2 = obj2;
			// loop on field names length
			for (int i = 0; i < alFieldNameMatrix.size(); i++) {
				for (int j = 0; j < alFieldNameMatrix.get(i).size(); j++) {
					v1 = getTargetFieldValue(alFieldNameMatrix.get(i).get(j), v1);
					v2 = getTargetFieldValue(alFieldNameMatrix.get(i).get(j), v2);
				}
				// get the response
				response = compareValue(v1, v2);
				if (response != 0 || i == alFieldNameMatrix.size() - 1) {
					return response;
				} else {
					// initialize obj1 and obj2 to v1 and v2
					v1 = obj1;
					v2 = obj2;
				}
			}
			response = compareValue(v1, v2);
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return response;
	}

	public static List<Field> getAllFields(Class<?> type) {
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(type.getDeclaredFields()));

		if (type.getSuperclass() != null) {
			fields.addAll(getAllFields(type.getSuperclass()));
		}

		return fields;
	}

	/**
	 * Get the target field value.
	 * 
	 * @param obj
	 * @return rValue
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private Object getTargetFieldValue(final String sfieldName, final Object obj)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// get the class of obj
		Class<?> c = obj.getClass();
		// c.getSuperclass()
		Field targetField = null;// c.getDeclaredField(sfieldName);
		List<Field> fields = getAllFields(c);
		for (Field field : fields) {
			if (sfieldName.equals(field.getName())) {
				// get target field
				targetField = field;
				break;
			}
		}
		if (targetField == null) {
			throw new NoSuchFieldException();
		}

		// specify accessible
		targetField.setAccessible(true);
		// define returned value
		Object rValue = targetField.get(obj);
		return rValue;

	}

	/**
	 * Compare two actual values, according to different data types.
	 * 
	 * @param v1
	 * @param v2
	 * @return 1,-1, 0
	 */
	private int compareValue(final Object v1, final Object v2) {
		int acutal = 1;
		// Make sure v1 and v2 belong to the same data type.
		if (v1.getClass() == v2.getClass()) {
			// get fieldType
			String fieldType = v1.getClass().getName();
			// get actual value based on data types
			if (fieldType.equals(DATATYPE_INTEGER)) {
				acutal = (((Integer) v1).compareTo((Integer) v2) * determineDirect());
			} else if (fieldType.equals(DATATYPE_LONG)) {
				acutal = (((Long) v1).compareTo((Long) v2) * determineDirect());
			} else if (fieldType.equals(DATATYPE_STRING)) {
				acutal = (((String) v1).compareTo((String) v2) * determineDirect());
			} else if (fieldType.equals(DATATYPE_DATE)) {
				acutal = (((Date) v1).compareTo((Date) v2) * determineDirect());
			} else if (fieldType.equals(DATATYPE_FLOAT)) {
				acutal = (((Float) v1).compareTo((Float) v2) * determineDirect());
			} else if (fieldType.equals(DATATYPE_DOUBLE)) {
				acutal = (((Double) v1).compareTo((Double) v2) * determineDirect());
			}
		} else {
			System.err.println("Objects v1 and v2 are different classes");
		}
		return acutal;
	}

	/**
	 * Determine sorting direction.
	 * 
	 * @return 1/-1
	 */
	private int determineDirect() {
		return bAscendingOrder ? 1 : -1;
	}
}
