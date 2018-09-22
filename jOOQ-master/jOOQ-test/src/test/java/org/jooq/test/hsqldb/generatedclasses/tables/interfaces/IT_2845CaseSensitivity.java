/**
 * This class is generated by jOOQ
 */
package org.jooq.test.hsqldb.generatedclasses.tables.interfaces;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface IT_2845CaseSensitivity extends java.io.Serializable {

	/**
	 * Setter for <code>PUBLIC.T_2845_CASE_sensitivity.ID</code>.
	 */
	public void setId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_2845_CASE_sensitivity.ID</code>.
	 */
	public java.lang.Integer getId();

	/**
	 * Setter for <code>PUBLIC.T_2845_CASE_sensitivity.INSENSITIVE</code>.
	 */
	public void setInsensitive(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_2845_CASE_sensitivity.INSENSITIVE</code>.
	 */
	public java.lang.Integer getInsensitive();

	/**
	 * Setter for <code>PUBLIC.T_2845_CASE_sensitivity.UPPER</code>.
	 */
	public void setUpper(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_2845_CASE_sensitivity.UPPER</code>.
	 */
	public java.lang.Integer getUpper();

	/**
	 * Setter for <code>PUBLIC.T_2845_CASE_sensitivity.lower</code>.
	 */
	public void setLower(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_2845_CASE_sensitivity.lower</code>.
	 */
	public java.lang.Integer getLower();

	/**
	 * Setter for <code>PUBLIC.T_2845_CASE_sensitivity.Mixed</code>.
	 */
	public void setMixed(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_2845_CASE_sensitivity.Mixed</code>.
	 */
	public java.lang.Integer getMixed();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IT_2845CaseSensitivity
	 */
	public void from(org.jooq.test.hsqldb.generatedclasses.tables.interfaces.IT_2845CaseSensitivity from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IT_2845CaseSensitivity
	 */
	public <E extends org.jooq.test.hsqldb.generatedclasses.tables.interfaces.IT_2845CaseSensitivity> E into(E into);
}
