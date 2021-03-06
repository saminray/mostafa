/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.interfaces;

/**
 * This class is generated by jOOQ.
 *
 * An m:n relation between books and book stores
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface ITBookToBookStore extends java.io.Serializable {

	/**
	 * Setter for <code>PUBLIC.T_BOOK_TO_BOOK_STORE.BOOK_STORE_NAME</code>. The book store name
	 */
	public ITBookToBookStore setBookStoreName(java.lang.String value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK_TO_BOOK_STORE.BOOK_STORE_NAME</code>. The book store name
	 */
	public java.lang.String getBookStoreName();

	/**
	 * Setter for <code>PUBLIC.T_BOOK_TO_BOOK_STORE.BOOK_ID</code>. The book ID
	 */
	public ITBookToBookStore setBookId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK_TO_BOOK_STORE.BOOK_ID</code>. The book ID
	 */
	public java.lang.Integer getBookId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK_TO_BOOK_STORE.STOCK</code>. The number of books on stock
	 */
	public ITBookToBookStore setStock(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK_TO_BOOK_STORE.STOCK</code>. The number of books on stock
	 */
	public java.lang.Integer getStock();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ITBookToBookStore
	 */
	public void from(org.jooq.test.h2.generatedclasses.tables.interfaces.ITBookToBookStore from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ITBookToBookStore
	 */
	public <E extends org.jooq.test.h2.generatedclasses.tables.interfaces.ITBookToBookStore> E into(E into);
}
