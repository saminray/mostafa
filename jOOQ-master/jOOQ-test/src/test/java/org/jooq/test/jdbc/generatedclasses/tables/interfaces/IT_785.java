/**
 * This class is generated by jOOQ
 */
package org.jooq.test.jdbc.generatedclasses.tables.interfaces;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface IT_785 extends java.io.Serializable {

	/**
	 * Setter for <code>PUBLIC.T_785.ID</code>. 
	 */
	public void setId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_785.ID</code>. 
	 */
	public java.lang.Integer getId();

	/**
	 * Setter for <code>PUBLIC.T_785.NAME</code>. 
	 */
	public void setName(java.lang.String value);

	/**
	 * Getter for <code>PUBLIC.T_785.NAME</code>. 
	 */
	public java.lang.String getName();

	/**
	 * Setter for <code>PUBLIC.T_785.VALUE</code>. 
	 */
	public void setValue(java.lang.String value);

	/**
	 * Getter for <code>PUBLIC.T_785.VALUE</code>. 
	 */
	public java.lang.String getValue();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IT_785
	 */
	public void from(org.jooq.test.jdbc.generatedclasses.tables.interfaces.IT_785 from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IT_785
	 */
	public <E extends org.jooq.test.jdbc.generatedclasses.tables.interfaces.IT_785> E into(E into);
}
