/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TInheritanceAllRecord extends org.jooq.impl.TableRecordImpl<org.jooq.test.postgres.generatedclasses.tables.records.TInheritanceAllRecord> implements org.jooq.Record4<java.lang.String, java.lang.String, java.lang.String, java.lang.String>, org.jooq.test.postgres.generatedclasses.tables.interfaces.ITInheritanceAll {

	private static final long serialVersionUID = -790084562;

	/**
	 * Setter for <code>public.t_inheritance_all.text_1</code>.
	 */
	public TInheritanceAllRecord setText_1(java.lang.String value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.t_inheritance_all.text_1</code>.
	 */
	@Override
	public java.lang.String getText_1() {
		return (java.lang.String) getValue(0);
	}

	/**
	 * Setter for <code>public.t_inheritance_all.text_1_1</code>.
	 */
	public TInheritanceAllRecord setText_1_1(java.lang.String value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.t_inheritance_all.text_1_1</code>.
	 */
	@Override
	public java.lang.String getText_1_1() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>public.t_inheritance_all.text_1_2</code>.
	 */
	public TInheritanceAllRecord setText_1_2(java.lang.String value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.t_inheritance_all.text_1_2</code>.
	 */
	@Override
	public java.lang.String getText_1_2() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>public.t_inheritance_all.text_1_all</code>.
	 */
	public TInheritanceAllRecord setText_1All(java.lang.String value) {
		setValue(3, value);
		return this;
	}

	/**
	 * Getter for <code>public.t_inheritance_all.text_1_all</code>.
	 */
	@Override
	public java.lang.String getText_1All() {
		return (java.lang.String) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.String, java.lang.String, java.lang.String, java.lang.String> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.String, java.lang.String, java.lang.String, java.lang.String> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field1() {
		return org.jooq.test.postgres.generatedclasses.tables.TInheritanceAll.T_INHERITANCE_ALL.TEXT_1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return org.jooq.test.postgres.generatedclasses.tables.TInheritanceAll.T_INHERITANCE_ALL.TEXT_1_1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return org.jooq.test.postgres.generatedclasses.tables.TInheritanceAll.T_INHERITANCE_ALL.TEXT_1_2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return org.jooq.test.postgres.generatedclasses.tables.TInheritanceAll.T_INHERITANCE_ALL.TEXT_1_ALL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value1() {
		return getText_1();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getText_1_1();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getText_1_2();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getText_1All();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TInheritanceAllRecord value1(java.lang.String value) {
		setText_1(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TInheritanceAllRecord value2(java.lang.String value) {
		setText_1_1(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TInheritanceAllRecord value3(java.lang.String value) {
		setText_1_2(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TInheritanceAllRecord value4(java.lang.String value) {
		setText_1All(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TInheritanceAllRecord values(java.lang.String value1, java.lang.String value2, java.lang.String value3, java.lang.String value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TInheritanceAllRecord
	 */
	public TInheritanceAllRecord() {
		super(org.jooq.test.postgres.generatedclasses.tables.TInheritanceAll.T_INHERITANCE_ALL);
	}

	/**
	 * Create a detached, initialised TInheritanceAllRecord
	 */
	public TInheritanceAllRecord(java.lang.String text_1, java.lang.String text_1_1, java.lang.String text_1_2, java.lang.String text_1All) {
		super(org.jooq.test.postgres.generatedclasses.tables.TInheritanceAll.T_INHERITANCE_ALL);

		setValue(0, text_1);
		setValue(1, text_1_1);
		setValue(2, text_1_2);
		setValue(3, text_1All);
	}
}
