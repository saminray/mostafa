/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.h2.matchers.routines;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class F1256 extends org.jooq.impl.AbstractRoutine<java.lang.String> {

	private static final long serialVersionUID = -1435430983;

	/**
	 * The parameter <code>PUBLIC.F1256.RETURN_VALUE</code>. 
	 */
	public static final org.jooq.Parameter<java.lang.String> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.VARCHAR.length(2147483647));

	/**
	 * The parameter <code>PUBLIC.F1256.P1</code>. 
	 */
	public static final org.jooq.Parameter<java.lang.String> P1 = createParameter("P1", org.jooq.impl.SQLDataType.VARCHAR.length(2147483647));

	/**
	 * Create a new routine call instance
	 */
	public F1256() {
		super("F1256", org.jooq.examples.h2.matchers.NonPublic.NON_PUBLIC, org.jooq.impl.SQLDataType.VARCHAR.length(2147483647));

		setReturnParameter(RETURN_VALUE);
		addInParameter(P1);
	}

	/**
	 * Set the <code>P1</code> parameter IN value to the routine
	 */
	public void setP1(java.lang.String value) {
		setValue(org.jooq.examples.h2.matchers.routines.F1256.P1, value);
	}

	/**
	 * Set the <code>P1</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public void setP1(org.jooq.Field<java.lang.String> field) {
		setField(P1, field);
	}
}
