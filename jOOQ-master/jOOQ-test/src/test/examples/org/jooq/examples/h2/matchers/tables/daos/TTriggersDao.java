/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.h2.matchers.tables.daos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TTriggersDao extends org.jooq.impl.DAOImpl<org.jooq.examples.h2.matchers.tables.records.TTriggersRecord, org.jooq.examples.h2.matchers.tables.pojos.TTriggers, java.lang.Integer> {

	/**
	 * Create a new TTriggersDao without any configuration
	 */
	public TTriggersDao() {
		super(org.jooq.examples.h2.matchers.tables.TTriggers.T_TRIGGERS, org.jooq.examples.h2.matchers.tables.pojos.TTriggers.class);
	}

	/**
	 * Create a new TTriggersDao with an attached configuration
	 */
	public TTriggersDao(org.jooq.Configuration configuration) {
		super(org.jooq.examples.h2.matchers.tables.TTriggers.T_TRIGGERS, org.jooq.examples.h2.matchers.tables.pojos.TTriggers.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected java.lang.Integer getId(org.jooq.examples.h2.matchers.tables.pojos.TTriggers object) {
		return object.getIdGenerated();
	}

	/**
	 * Fetch records that have <code>ID_GENERATED IN (values)</code>
	 */
	public java.util.List<org.jooq.examples.h2.matchers.tables.pojos.TTriggers> fetchByIdGenerated(java.lang.Integer... values) {
		return fetch(org.jooq.examples.h2.matchers.tables.TTriggers.ID_GENERATED, values);
	}

	/**
	 * Fetch a unique record that has <code>ID_GENERATED = value</code>
	 */
	public org.jooq.examples.h2.matchers.tables.pojos.TTriggers fetchOneByIdGenerated(java.lang.Integer value) {
		return fetchOne(org.jooq.examples.h2.matchers.tables.TTriggers.ID_GENERATED, value);
	}

	/**
	 * Fetch records that have <code>ID IN (values)</code>
	 */
	public java.util.List<org.jooq.examples.h2.matchers.tables.pojos.TTriggers> fetchById(java.lang.Integer... values) {
		return fetch(org.jooq.examples.h2.matchers.tables.TTriggers.ID, values);
	}

	/**
	 * Fetch records that have <code>COUNTER IN (values)</code>
	 */
	public java.util.List<org.jooq.examples.h2.matchers.tables.pojos.TTriggers> fetchByCounter(java.lang.Integer... values) {
		return fetch(org.jooq.examples.h2.matchers.tables.TTriggers.COUNTER, values);
	}
}
