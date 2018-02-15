//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2017.06.07 um 07:50:29 PM CEST
//

package de.apps.brewmaster.model.recipe.persistence.object;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java-Klasse für stirringSpeed.
 *
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser
 * Klasse enthalten ist.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="stirringSpeed">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NO"/>
 *     &lt;enumeration value="SLOW"/>
 *     &lt;enumeration value="FAST"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "stirringSpeed")
@XmlEnum
public enum StirringSpeed {

	NO("Kein Rühren"), SLOW("Langsames Rühren"), FAST("Schnelles Rühren");

	public static StirringSpeed fromValue(final String v) {
		return valueOf(v);
	}

	private final String label;

	private StirringSpeed(final String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public String value() {
		return name();
	}

}
