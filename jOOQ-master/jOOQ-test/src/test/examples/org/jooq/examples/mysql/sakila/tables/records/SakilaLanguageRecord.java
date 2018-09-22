/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.mysql.sakila.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SakilaLanguageRecord extends org.jooq.impl.UpdatableRecordImpl<org.jooq.examples.mysql.sakila.tables.records.SakilaLanguageRecord> implements org.jooq.Record3<java.lang.Byte, java.lang.String, java.sql.Timestamp> {

	private static final long serialVersionUID = -887371367;

	/**
	 * Setter for <code>sakila.language.language_id</code>. 
	 */
	public void setLanguageId(java.lang.Byte value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>sakila.language.language_id</code>. 
	 */
	public java.lang.Byte getLanguageId() {
		return (java.lang.Byte) getValue(0);
	}

	/**
	 * Setter for <code>sakila.language.name</code>. 
	 */
	public void setName(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>sakila.language.name</code>. 
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>sakila.language.last_update</code>. 
	 */
	public void setLastUpdate(java.sql.Timestamp value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>sakila.language.last_update</code>. 
	 */
	public java.sql.Timestamp getLastUpdate() {
		return (java.sql.Timestamp) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Byte> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<java.lang.Byte, java.lang.String, java.sql.Timestamp> fieldsRow() {
		return (org.jooq.Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<java.lang.Byte, java.lang.String, java.sql.Timestamp> valuesRow() {
		return (org.jooq.Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Byte> field1() {
		return org.jooq.examples.mysql.sakila.tables.SakilaLanguage.LANGUAGE.LANGUAGE_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return org.jooq.examples.mysql.sakila.tables.SakilaLanguage.LANGUAGE.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field3() {
		return org.jooq.examples.mysql.sakila.tables.SakilaLanguage.LANGUAGE.LAST_UPDATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Byte value1() {
		return getLanguageId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value3() {
		return getLastUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SakilaLanguageRecord value1(java.lang.Byte value) {
		setLanguageId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SakilaLanguageRecord value2(java.lang.String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SakilaLanguageRecord value3(java.sql.Timestamp value) {
		setLastUpdate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SakilaLanguageRecord values(java.lang.Byte value1, java.lang.String value2, java.sql.Timestamp value3) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached SakilaLanguageRecord
	 */
	public SakilaLanguageRecord() {
		super(org.jooq.examples.mysql.sakila.tables.SakilaLanguage.LANGUAGE);
	}

	/**
	 * Create a detached, initialised SakilaLanguageRecord
	 */
	public SakilaLanguageRecord(java.lang.Byte languageId, java.lang.String name, java.sql.Timestamp lastUpdate) {
		super(org.jooq.examples.mysql.sakila.tables.SakilaLanguage.LANGUAGE);

		setValue(0, languageId);
		setValue(1, name);
		setValue(2, lastUpdate);
	}
}
