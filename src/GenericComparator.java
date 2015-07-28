
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Date;

/**
 * A generic comparator class to sort collection of elements in <code> Collections.sort()</code>:.
 * <br> - For a collection of primitive data use <code>Collections.sort(aa, new GenericComparator(true/false));</code>
 * <br> - For a collection of objects use <code>Collections.sort(aList, new GenericComparator(true/false, "fieldName"));</code>
 * <p>
 *  This class is inspired and adopted from the GenericComparator class developed by
 *    <a href="http://myjeeva.com/generic-comparator-in-java.html">myjeeva blog</a>.
 *    
 * @author <a href="mailto:leifeng@fedex.com">Lei Feng 5004756</a>
 * 
 */
public class GenericComparator implements Comparator {
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
	 * Specified sort field names, which is a vararg separated by commas.
	 */
	private String[] sfieldNames;
	/**
	 * Specified order direction.
	 * <p>true - ascending;
	 * false - descending;
	 */
	private boolean bAscendingOrder;

	/**
	 * <p>Constructor with <code>sortFieldNames</code> parameter for derived type of <code>Class</code> with default ascending sorting.</p>
	 * 
	 * <p>Implementation: 
	 * <br><code>Collections.sort(personList, new GenericComparator("name"));</code><br /></p>
	 *  
	 * @param sortFieldName - a {@link java.lang.String} specify which field is used for sorting."
	 */
	public GenericComparator(String... sortFieldNames) {
		this.bAscendingOrder = true;
		this.sfieldNames = sortFieldNames;
	}
	/**
	 * <p>Constructor with <code>sortAscending, sortFieldNames</code> parameters for derived type of <code>Class</code>.</p>
	 * 
	 * <p>Implementation:
	 * <br><code>Collections.sort(personList, new GenericComparator(false, "payment", "starting salary");</code><br /></p> 
	 * @param sortAscending - a {@link boolean} - <code>true</code> ascending order or <code>false</code> descending order.
	 * @param sortFieldNames - a {@link java.lang.String} - which fields used for sorting.
	 * 
	 */
	public GenericComparator(final boolean sortAscending, String... sortFieldNames) {
		this.bAscendingOrder = sortAscending;
		this.sfieldNames = sortFieldNames;
	}

	/**
	 * Override method for comparison of two elements(Objects).
	 */
	@Override
	public int compare(final Object obj1, final Object obj2) {
		int response = 1;
		try {
			//initialize obj1 and obj2 to v1 and v2
			Object v1 = obj1;
			Object v2 = obj2;
			//loop on fieldnames length
			for(int i = 0; i < sfieldNames.length; i++){
				v1 = getTargetFieldValue(sfieldNames[i], v1);
				v2 = getTargetFieldValue(sfieldNames[i], v2);
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
	
	/**
	 * Get the target field value.
	 * @param obj 
	 * @return rValue 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private Object getTargetFieldValue(final String sfieldName, final Object obj) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		//get the class of obj
		Class<?> c = obj.getClass();
		//get target field
		Field targetField = c.getDeclaredField(sfieldName);
		//specify accessible
		targetField.setAccessible(true);
		//define returned value
		Object rValue = targetField.get(obj);
		return rValue;
		
	}
	/**
	 * Compare two actual values, according to different data types.
	 * @param v1 
	 * @param v2 
	 * @return 1,-1, 0
	 */
	private int compareValue(final Object v1, final Object v2) {
		int acutal = 1;
		// Make sure v1 and v2 belong to the same data type.
		if (v1.getClass() == v2.getClass()) {
			//get fieldType
			String fieldType = v1.getClass().getName();
			//get actual value based on data types
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
	 * @return 1/-1 
	 */
	private int determineDirect() {
		return bAscendingOrder ? 1 : -1;
	}
}
