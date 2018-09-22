/**
 * This class is generated by jOOQ
 */
package org.jooq.test.derby.generatedclasses.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TDates extends org.jooq.impl.TableImpl<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord> {

	private static final long serialVersionUID = 1441957619;

	/**
	 * The singleton instance of <code>TEST.T_DATES</code>
	 */
	public static final org.jooq.test.derby.generatedclasses.tables.TDates T_DATES = new org.jooq.test.derby.generatedclasses.tables.TDates();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord> getRecordType() {
		return org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord.class;
	}

	/**
	 * The column <code>TEST.T_DATES.ID</code>.
	 */
	public static final org.jooq.TableField<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord, java.lang.Integer> ID = createField("ID", org.jooq.impl.SQLDataType.INTEGER.nullable(false), T_DATES, "");

	/**
	 * The column <code>TEST.T_DATES.D</code>.
	 */
	public static final org.jooq.TableField<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord, java.sql.Date> D = createField("D", org.jooq.impl.SQLDataType.DATE, T_DATES, "");

	/**
	 * The column <code>TEST.T_DATES.T</code>.
	 */
	public static final org.jooq.TableField<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord, java.sql.Time> T = createField("T", org.jooq.impl.SQLDataType.TIME, T_DATES, "");

	/**
	 * The column <code>TEST.T_DATES.TS</code>.
	 */
	public static final org.jooq.TableField<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord, java.sql.Timestamp> TS = createField("TS", org.jooq.impl.SQLDataType.TIMESTAMP, T_DATES, "");

	/**
	 * The column <code>TEST.T_DATES.D_INT</code>.
	 */
	public static final org.jooq.TableField<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord, java.lang.Integer> D_INT = createField("D_INT", org.jooq.impl.SQLDataType.INTEGER, T_DATES, "");

	/**
	 * The column <code>TEST.T_DATES.TS_BIGINT</code>.
	 */
	public static final org.jooq.TableField<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord, java.lang.Long> TS_BIGINT = createField("TS_BIGINT", org.jooq.impl.SQLDataType.BIGINT, T_DATES, "");

	/**
	 * No further instances allowed
	 */
	private TDates() {
		this("T_DATES", null);
	}

	private TDates(java.lang.String alias, org.jooq.Table<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord> aliased) {
		this(alias, aliased, null);
	}

	private TDates(java.lang.String alias, org.jooq.Table<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.jooq.test.derby.generatedclasses.Test.TEST, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord> getPrimaryKey() {
		return org.jooq.test.derby.generatedclasses.Keys.PK_T_DATES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.test.derby.generatedclasses.tables.records.TDatesRecord>>asList(org.jooq.test.derby.generatedclasses.Keys.PK_T_DATES);
	}
}
