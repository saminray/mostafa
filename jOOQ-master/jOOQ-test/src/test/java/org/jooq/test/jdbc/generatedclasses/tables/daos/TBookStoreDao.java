/**
 * This class is generated by jOOQ
 */
package org.jooq.test.jdbc.generatedclasses.tables.daos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TBookStoreDao extends org.jooq.impl.DAOImpl<org.jooq.test.jdbc.generatedclasses.tables.records.TBookStoreRecord, org.jooq.test.jdbc.generatedclasses.tables.pojos.TBookStore, java.lang.String> {

	/**
	 * Create a new TBookStoreDao without any configuration
	 */
	public TBookStoreDao() {
		super(org.jooq.test.jdbc.generatedclasses.tables.TBookStore.T_BOOK_STORE, org.jooq.test.jdbc.generatedclasses.tables.pojos.TBookStore.class);
	}

	/**
	 * Create a new TBookStoreDao with an attached configuration
	 */
	public TBookStoreDao(org.jooq.Configuration configuration) {
		super(org.jooq.test.jdbc.generatedclasses.tables.TBookStore.T_BOOK_STORE, org.jooq.test.jdbc.generatedclasses.tables.pojos.TBookStore.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected java.lang.String getId(org.jooq.test.jdbc.generatedclasses.tables.pojos.TBookStore object) {
		return object.getName();
	}

	/**
	 * Fetch records that have <code>NAME IN (values)</code>
	 */
	public java.util.List<org.jooq.test.jdbc.generatedclasses.tables.pojos.TBookStore> fetchByName(java.lang.String... values) {
		return fetch(org.jooq.test.jdbc.generatedclasses.tables.TBookStore.NAME, values);
	}

	/**
	 * Fetch a unique record that has <code>NAME = value</code>
	 */
	public org.jooq.test.jdbc.generatedclasses.tables.pojos.TBookStore fetchOneByName(java.lang.String value) {
		return fetchOne(org.jooq.test.jdbc.generatedclasses.tables.TBookStore.NAME, value);
	}
}
