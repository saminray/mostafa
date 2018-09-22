/**
 * This class is generated by jOOQ
 */
package org.jooq.util.postgres.pg_catalog;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PgCatalog extends org.jooq.impl.SchemaImpl {

	private static final long serialVersionUID = 1879929305;

	/**
	 * The singleton instance of <code>pg_catalog</code>
	 */
	public static final PgCatalog PG_CATALOG = new PgCatalog();

	/**
	 * No further instances allowed
	 */
	private PgCatalog() {
		super("pg_catalog");
	}

	@Override
	public final java.util.List<org.jooq.Table<?>> getTables() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getTables0());
		return result;
	}

	private final java.util.List<org.jooq.Table<?>> getTables0() {
		return java.util.Arrays.<org.jooq.Table<?>>asList(
			org.jooq.util.postgres.pg_catalog.tables.PgAttribute.PG_ATTRIBUTE,
			org.jooq.util.postgres.pg_catalog.tables.PgClass.PG_CLASS,
			org.jooq.util.postgres.pg_catalog.tables.PgEnum.PG_ENUM,
			org.jooq.util.postgres.pg_catalog.tables.PgInherits.PG_INHERITS,
			org.jooq.util.postgres.pg_catalog.tables.PgNamespace.PG_NAMESPACE,
			org.jooq.util.postgres.pg_catalog.tables.PgProc.PG_PROC,
			org.jooq.util.postgres.pg_catalog.tables.PgType.PG_TYPE);
	}
}