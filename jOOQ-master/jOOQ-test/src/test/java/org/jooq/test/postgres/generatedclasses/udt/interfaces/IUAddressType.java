/**
 * This class is generated by jOOQ
 */
package org.jooq.test.postgres.generatedclasses.udt.interfaces;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface IUAddressType extends java.io.Serializable {

	/**
	 * Getter for <code>public.u_address_type.street</code>.
	 */
	public org.jooq.test.postgres.generatedclasses.udt.interfaces.IUStreetType getStreet();

	/**
	 * Getter for <code>public.u_address_type.zip</code>.
	 */
	public java.lang.String getZip();

	/**
	 * Getter for <code>public.u_address_type.city</code>.
	 */
	public java.lang.String getCity();

	/**
	 * Getter for <code>public.u_address_type.country</code>.
	 */
	public org.jooq.test.postgres.generatedclasses.enums.UCountry getCountry();

	/**
	 * Getter for <code>public.u_address_type.since</code>.
	 */
	public java.sql.Date getSince();

	/**
	 * Getter for <code>public.u_address_type.code</code>.
	 */
	public java.lang.Integer getCode();

	/**
	 * Getter for <code>public.u_address_type.f_1323</code>.
	 */
	public byte[] getF_1323();
}
