/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TInheritanceAll implements org.jooq.test.postgres.generatedclasses.tables.interfaces.ITInheritanceAll {

	private static final long serialVersionUID = 697220951;

	private final java.lang.String text_1;
	private final java.lang.String text_1_1;
	private final java.lang.String text_1_2;
	private final java.lang.String text_1All;

	public TInheritanceAll(
		java.lang.String text_1,
		java.lang.String text_1_1,
		java.lang.String text_1_2,
		java.lang.String text_1All
	) {
		this.text_1 = text_1;
		this.text_1_1 = text_1_1;
		this.text_1_2 = text_1_2;
		this.text_1All = text_1All;
	}

	@Override
	public java.lang.String getText_1() {
		return this.text_1;
	}

	@Override
	public java.lang.String getText_1_1() {
		return this.text_1_1;
	}

	@Override
	public java.lang.String getText_1_2() {
		return this.text_1_2;
	}

	@Override
	public java.lang.String getText_1All() {
		return this.text_1All;
	}
}
