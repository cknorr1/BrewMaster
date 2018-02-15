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
 * Java-Klasse für brewStepID.
 *
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser
 * Klasse enthalten ist.
 * <p>
 *
 * <pre>
 * &lt;simpleType name="brewStepID">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MASHING"/>
 *     &lt;enumeration value="PROTEINE_REST"/>
 *     &lt;enumeration value="MALTOSE_REST"/>
 *     &lt;enumeration value="IODINE_TEST"/>
 *     &lt;enumeration value="PURIFICATION"/>
 *     &lt;enumeration value="ON_TRUANT"/>
 *     &lt;enumeration value="WORT_MEASURING"/>
 *     &lt;enumeration value="HOPING"/>
 *     &lt;enumeration value="WORT_BOIL"/>
 *     &lt;enumeration value="SPINDLING"/>
 *     &lt;enumeration value="HOT_TRUB_REMOVAL"/>
 *     &lt;enumeration value="COOLING_DOWN"/>
 *     &lt;enumeration value="YEAST_ADD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "brewStepID")
@XmlEnum
public enum BrewStepID {

	MASHING("Einmaischen"), PROTEINE_REST("Eiweiß Rast"), MALTOSE_REST("Maltose Rast"), IODINE_TEST(
			"Jod Test"), PURIFICATION("Läutern"), ON_TRUANT("Überschwänzen"), WORT_MEASURING("Würze messen"), HOPING(
					"Hopfung"), WORT_BOIL("Würze kochen"), SPINDLING("Spindeln"), HOT_TRUB_REMOVAL(
							"Heißtrubenentfernung"), COOLING_DOWN("Abkühlen"), YEAST_ADD("Hefe hinzufügen");

	public static BrewStepID fromValue(final String v) {
		return valueOf(v);
	}

	private final String label;

	private BrewStepID(final String label) {
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
