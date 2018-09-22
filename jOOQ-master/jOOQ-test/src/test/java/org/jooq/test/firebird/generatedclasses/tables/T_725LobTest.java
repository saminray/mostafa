/**
 * This class is generated by jOOQ
 */
package org.jooq.test.firebird.generatedclasses.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class T_725LobTest extends org.jooq.impl.TableImpl<org.jooq.test.firebird.generatedclasses.tables.records.T_725LobTestRecord> {

	private static final long serialVersionUID = 2130208323;

	/**
	 * The singleton instance of <code>T_725_LOB_TEST</code>
	 */
	public static final org.jooq.test.firebird.generatedclasses.tables.T_725LobTest T_725_LOB_TEST = new org.jooq.test.firebird.generatedclasses.tables.T_725LobTest();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.firebird.generatedclasses.tables.records.T_725LobTestRecord> getRecordType() {
		return org.jooq.test.firebird.generatedclasses.tables.records.T_725LobTestRecord.class;
	}

	/**
	 * The column <code>T_725_LOB_TEST.ID</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.firebird.generatedclasses.tables.records.T_725LobTestRecord, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>T_725_LOB_TEST.LOB</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.firebird.generatedclasses.tables.records.T_725LobTestRecord, byte[]> LOB = createField("LOB", org.jooq.impl.SQLDataType.BLOB.length(8), this, "");

	/**
	 * Create a <code>T_725_LOB_TEST</code> table reference
	 */
	public T_725LobTest() {
		this("T_725_LOB_TEST", null);
	}

	/**
	 * Create an aliased <code>T_725_LOB_TEST</code> table reference
	 */
	public T_725LobTest(java.lang.String alias) {
		this(alias, org.jooq.test.firebird.generatedclasses.tables.T_725LobTest.T_725_LOB_TEST);
	}

	private T_725LobTest(java.lang.String alias, org.jooq.Table<org.jooq.test.firebird.generatedclasses.tables.records.T_725LobTestRecord> aliased) {
		this(alias, aliased, null);
	}

	private T_725LobTest(java.lang.String alias, org.jooq.Table<org.jooq.test.firebird.generatedclasses.tables.records.T_725LobTestRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.firebird.generatedclasses.DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.jooq.test.firebird.generatedclasses.tables.records.T_725LobTestRecord> getPrimaryKey() {
		return org.jooq.test.firebird.generatedclasses.Keys.PK_T_725_LOB_TEST;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.jooq.test.firebird.generatedclasses.tables.records.T_725LobTestRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.test.firebird.generatedclasses.tables.records.T_725LobTestRecord>>asList(org.jooq.test.firebird.generatedclasses.Keys.PK_T_725_LOB_TEST);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.firebird.generatedclasses.tables.T_725LobTest as(java.lang.String alias) {
		return new org.jooq.test.firebird.generatedclasses.tables.T_725LobTest(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.test.firebird.generatedclasses.tables.T_725LobTest rename(java.lang.String name) {
		return new org.jooq.test.firebird.generatedclasses.tables.T_725LobTest(name, null);
	}
}
