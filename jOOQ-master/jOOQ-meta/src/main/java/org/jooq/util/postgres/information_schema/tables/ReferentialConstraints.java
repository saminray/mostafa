/**
 * This class is generated by jOOQ
 */
package org.jooq.util.postgres.information_schema.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ReferentialConstraints extends org.jooq.impl.TableImpl<org.jooq.Record> {

	private static final long serialVersionUID = -1061275407;

	/**
	 * The singleton instance of <code>information_schema.referential_constraints</code>
	 */
	public static final org.jooq.util.postgres.information_schema.tables.ReferentialConstraints REFERENTIAL_CONSTRAINTS = new org.jooq.util.postgres.information_schema.tables.ReferentialConstraints();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.Record> getRecordType() {
		return org.jooq.Record.class;
	}

	/**
	 * The column <code>information_schema.referential_constraints.constraint_catalog</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> CONSTRAINT_CATALOG = createField("constraint_catalog", org.jooq.impl.SQLDataType.VARCHAR, this, "");

	/**
	 * The column <code>information_schema.referential_constraints.constraint_schema</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> CONSTRAINT_SCHEMA = createField("constraint_schema", org.jooq.impl.SQLDataType.VARCHAR, this, "");

	/**
	 * The column <code>information_schema.referential_constraints.constraint_name</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> CONSTRAINT_NAME = createField("constraint_name", org.jooq.impl.SQLDataType.VARCHAR, this, "");

	/**
	 * The column <code>information_schema.referential_constraints.unique_constraint_catalog</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> UNIQUE_CONSTRAINT_CATALOG = createField("unique_constraint_catalog", org.jooq.impl.SQLDataType.VARCHAR, this, "");

	/**
	 * The column <code>information_schema.referential_constraints.unique_constraint_schema</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> UNIQUE_CONSTRAINT_SCHEMA = createField("unique_constraint_schema", org.jooq.impl.SQLDataType.VARCHAR, this, "");

	/**
	 * The column <code>information_schema.referential_constraints.unique_constraint_name</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> UNIQUE_CONSTRAINT_NAME = createField("unique_constraint_name", org.jooq.impl.SQLDataType.VARCHAR, this, "");

	/**
	 * The column <code>information_schema.referential_constraints.match_option</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> MATCH_OPTION = createField("match_option", org.jooq.impl.SQLDataType.VARCHAR, this, "");

	/**
	 * The column <code>information_schema.referential_constraints.update_rule</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> UPDATE_RULE = createField("update_rule", org.jooq.impl.SQLDataType.VARCHAR, this, "");

	/**
	 * The column <code>information_schema.referential_constraints.delete_rule</code>.
	 */
	public final org.jooq.TableField<org.jooq.Record, java.lang.String> DELETE_RULE = createField("delete_rule", org.jooq.impl.SQLDataType.VARCHAR, this, "");

	/**
	 * Create a <code>information_schema.referential_constraints</code> table reference
	 */
	public ReferentialConstraints() {
		this("referential_constraints", null);
	}

	/**
	 * Create an aliased <code>information_schema.referential_constraints</code> table reference
	 */
	public ReferentialConstraints(java.lang.String alias) {
		this(alias, org.jooq.util.postgres.information_schema.tables.ReferentialConstraints.REFERENTIAL_CONSTRAINTS);
	}

	private ReferentialConstraints(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased) {
		this(alias, aliased, null);
	}

	private ReferentialConstraints(java.lang.String alias, org.jooq.Table<org.jooq.Record> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.util.postgres.information_schema.InformationSchema.INFORMATION_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.util.postgres.information_schema.tables.ReferentialConstraints as(java.lang.String alias) {
		return new org.jooq.util.postgres.information_schema.tables.ReferentialConstraints(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.jooq.util.postgres.information_schema.tables.ReferentialConstraints rename(java.lang.String name) {
		return new org.jooq.util.postgres.information_schema.tables.ReferentialConstraints(name, null);
	}
}