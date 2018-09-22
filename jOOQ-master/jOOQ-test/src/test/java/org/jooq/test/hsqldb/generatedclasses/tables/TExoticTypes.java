/**
 * This class is generated by jOOQ
 */
package org.jooq.test.hsqldb.generatedclasses.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TExoticTypes extends org.jooq.impl.TableImpl<org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord> {

	private static final long serialVersionUID = 859047123;

	/**
	 * The singleton instance of <code>PUBLIC.T_EXOTIC_TYPES</code>
	 */
	public static final org.jooq.test.hsqldb.generatedclasses.tables.TExoticTypes T_EXOTIC_TYPES = new org.jooq.test.hsqldb.generatedclasses.tables.TExoticTypes();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord> getRecordType() {
		return org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord.class;
	}

	/**
	 * The column <code>PUBLIC.T_EXOTIC_TYPES.ID</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>PUBLIC.T_EXOTIC_TYPES.UU</code>.
	 */
	public final org.jooq.TableField<org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord, java.util.UUID> UU = createField("UU", org.jooq.impl.SQLDataType.UUID, this, "");

	/**
	 * Create a <code>PUBLIC.T_EXOTIC_TYPES</code> table reference
	 */
	public TExoticTypes() {
		this("T_EXOTIC_TYPES", null);
	}

	/**
	 * Create an aliased <code>PUBLIC.T_EXOTIC_TYPES</code> table reference
	 */
	public TExoticTypes(java.lang.String alias) {
		this(alias, org.jooq.test.hsqldb.generatedclasses.tables.TExoticTypes.T_EXOTIC_TYPES);
	}

	private TExoticTypes(java.lang.String alias, org.jooq.Table<org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord> aliased) {
		this(alias, aliased, null);
	}

	private TExoticTypes(java.lang.String alias, org.jooq.Table<org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.hsqldb.generatedclasses.Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord> getPrimaryKey() {
		return org.jooq.test.hsqldb.generatedclasses.Keys.PK_T_EXOTIC_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.test.hsqldb.generatedclasses.tables.records.TExoticTypesRecord>>asList(org.jooq.test.hsqldb.generatedclasses.Keys.PK_T_EXOTIC_TYPES);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.test.hsqldb.generatedclasses.tables.TExoticTypes as(java.lang.String alias) {
		return new org.jooq.test.hsqldb.generatedclasses.tables.TExoticTypes(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.test.hsqldb.generatedclasses.tables.TExoticTypes rename(java.lang.String name) {
		return new org.jooq.test.hsqldb.generatedclasses.tables.TExoticTypes(name, null);
	}
}
