/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TTriggers implements org.jooq.test.postgres.generatedclasses.tables.interfaces.ITTriggers {

	private static final long serialVersionUID = -616422352;

	private final java.lang.Integer idGenerated;
	private final java.lang.Integer id;
	private final java.lang.Integer counter;

	public TTriggers(
		java.lang.Integer idGenerated,
		java.lang.Integer id,
		java.lang.Integer counter
	) {
		this.idGenerated = idGenerated;
		this.id = id;
		this.counter = counter;
	}

	@Override
	public java.lang.Integer getIdGenerated() {
		return this.idGenerated;
	}

	@Override
	public java.lang.Integer getId() {
		return this.id;
	}

	@Override
	public java.lang.Integer getCounter() {
		return this.counter;
	}
}
