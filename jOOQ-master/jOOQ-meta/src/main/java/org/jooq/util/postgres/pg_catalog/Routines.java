/**
 * This class is generated by jOOQ
 */
package org.jooq.util.postgres.pg_catalog;

/**
 * This class is generated by jOOQ.
 *
 * Convenience access to all stored procedures and functions in pg_catalog
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Routines {

	/**
	 * Call <code>pg_catalog.count</code>
	 */
	public static java.lang.Long count1(org.jooq.Configuration configuration, java.lang.Object __1) {
		org.jooq.util.postgres.pg_catalog.routines.Count1 f = new org.jooq.util.postgres.pg_catalog.routines.Count1();
		f.set__1(__1);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>pg_catalog.count</code> as a field
	 */
	public static org.jooq.Field<java.lang.Long> count1(java.lang.Object __1) {
		org.jooq.util.postgres.pg_catalog.routines.Count1 f = new org.jooq.util.postgres.pg_catalog.routines.Count1();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Get <code>pg_catalog.count</code> as a field
	 */
	public static org.jooq.Field<java.lang.Long> count1(org.jooq.Field<java.lang.Object> __1) {
		org.jooq.util.postgres.pg_catalog.routines.Count1 f = new org.jooq.util.postgres.pg_catalog.routines.Count1();
		f.set__1(__1);

		return f.asField();
	}

	/**
	 * Call <code>pg_catalog.count</code>
	 */
	public static java.lang.Long count2(org.jooq.Configuration configuration) {
		org.jooq.util.postgres.pg_catalog.routines.Count2 f = new org.jooq.util.postgres.pg_catalog.routines.Count2();

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>pg_catalog.count</code> as a field
	 */
	public static org.jooq.Field<java.lang.Long> count2() {
		org.jooq.util.postgres.pg_catalog.routines.Count2 f = new org.jooq.util.postgres.pg_catalog.routines.Count2();

		return f.asField();
	}

	/**
	 * Call <code>pg_catalog.format_type</code>
	 */
	public static java.lang.String formatType(org.jooq.Configuration configuration, java.lang.Long __1, java.lang.Integer __2) {
		org.jooq.util.postgres.pg_catalog.routines.FormatType f = new org.jooq.util.postgres.pg_catalog.routines.FormatType();
		f.set__1(__1);
		f.set__2(__2);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Get <code>pg_catalog.format_type</code> as a field
	 */
	public static org.jooq.Field<java.lang.String> formatType(java.lang.Long __1, java.lang.Integer __2) {
		org.jooq.util.postgres.pg_catalog.routines.FormatType f = new org.jooq.util.postgres.pg_catalog.routines.FormatType();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Get <code>pg_catalog.format_type</code> as a field
	 */
	public static org.jooq.Field<java.lang.String> formatType(org.jooq.Field<java.lang.Long> __1, org.jooq.Field<java.lang.Integer> __2) {
		org.jooq.util.postgres.pg_catalog.routines.FormatType f = new org.jooq.util.postgres.pg_catalog.routines.FormatType();
		f.set__1(__1);
		f.set__2(__2);

		return f.asField();
	}

	/**
	 * Call <code>pg_catalog.pg_cursor</code>
	 */
	public static org.jooq.util.postgres.pg_catalog.routines.PgCursor pgCursor(org.jooq.Configuration configuration) {
		org.jooq.util.postgres.pg_catalog.routines.PgCursor p = new org.jooq.util.postgres.pg_catalog.routines.PgCursor();

		p.execute(configuration);
		return p;
	}
}
