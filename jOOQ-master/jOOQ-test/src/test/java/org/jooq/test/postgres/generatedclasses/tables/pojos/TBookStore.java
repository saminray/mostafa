/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TBookStore implements org.jooq.test.postgres.generatedclasses.tables.interfaces.ITBookStore {

	private static final long serialVersionUID = 1381788466;

	private final java.lang.String name;

	public TBookStore(
		java.lang.String name
	) {
		this.name = name;
	}

	@Override
	public java.lang.String getName() {
		return this.name;
	}
}
