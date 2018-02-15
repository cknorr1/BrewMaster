//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2017.06.07 um 07:50:29 PM CEST
//

package de.apps.brewmaster.model.recipe.persistence.object;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java-Klasse für brewRecipe complex type.
 *
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser
 * Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="brewRecipe">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="brewStep" type="{}brewStep"/>
 *       &lt;/sequence>
 *       &lt;attribute name="recipeID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "brewRecipe", propOrder = { "brewStep" })
@XmlRootElement(name = "brewRecipe")
public class BrewRecipe {

	protected List<BrewStep> brewSteps;
	@XmlAttribute(name = "recipeID")
	protected String recipeID;

	/**
	 * Gets the value of the brewStep property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the brewStep property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getBrewStep().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link BrewStep
	 * }
	 *
	 *
	 */
	public List<BrewStep> getBrewSteps() {
		if (brewSteps == null) {
			brewSteps = new ArrayList<BrewStep>();
		}
		return this.brewSteps;
	}

	/**
	 * Ruft den Wert der recipeID-Eigenschaft ab.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getRecipeID() {
		return recipeID;
	}

	/**
	 * Legt den Wert der recipeID-Eigenschaft fest.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setRecipeID(final String value) {
		this.recipeID = value;
	}

}
