/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.enums;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public enum U_2781 implements org.jooq.EnumType {

	org_("org"),

	jooq("jooq");

	private final java.lang.String literal;

	private U_2781(java.lang.String literal) {
		this.literal = literal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Schema getSchema() {
		return org.jooq.test.postgres.generatedclasses.Public.PUBLIC;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String getName() {
		return "u_2781";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String getLiteral() {
		return literal;
	}
}
