/**
 * This class is generated by jOOQ
 */
package org.jooq.test.firebird.generatedclasses.tables.interfaces;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
@javax.persistence.Entity
@javax.persistence.Table(name = "V_LIBRARY")
public interface IVLibrary extends java.io.Serializable {

	/**
	 * Setter for <code>V_LIBRARY.AUTHOR</code>.
	 */
	public void setAuthor(java.lang.String value);

	/**
	 * Getter for <code>V_LIBRARY.AUTHOR</code>.
	 */
	@javax.persistence.Column(name = "AUTHOR", length = 101)
	@javax.validation.constraints.Size(max = 101)
	public java.lang.String getAuthor();

	/**
	 * Setter for <code>V_LIBRARY.TITLE</code>.
	 */
	public void setTitle(java.lang.String value);

	/**
	 * Getter for <code>V_LIBRARY.TITLE</code>.
	 */
	@javax.persistence.Column(name = "TITLE", length = 400)
	@javax.validation.constraints.Size(max = 400)
	public java.lang.String getTitle();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IVLibrary
	 */
	public void from(org.jooq.test.firebird.generatedclasses.tables.interfaces.IVLibrary from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IVLibrary
	 */
	public <E extends org.jooq.test.firebird.generatedclasses.tables.interfaces.IVLibrary> E into(E into);
}
