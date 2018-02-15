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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für brewStep complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="brewStep">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="brewStepID" type="{}brewStepID"/>
 *         &lt;element name="brewStepParameter" type="{}brewStepParameter"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "brewStep", propOrder = {
    "brewStepIDAndBrewStepParameter"
})
public class BrewStep {

    @XmlElements({
        @XmlElement(name = "brewStepID", type = BrewStepID.class),
        @XmlElement(name = "brewStepParameter", type = BrewStepParameter.class)
    })
    protected List<Object> brewStepIDAndBrewStepParameter;

    /**
     * Gets the value of the brewStepIDAndBrewStepParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the brewStepIDAndBrewStepParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBrewStepIDAndBrewStepParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BrewStepID }
     * {@link BrewStepParameter }
     * 
     * 
     */
    public List<Object> getBrewStepIDAndBrewStepParameter() {
        if (brewStepIDAndBrewStepParameter == null) {
            brewStepIDAndBrewStepParameter = new ArrayList<Object>();
        }
        return this.brewStepIDAndBrewStepParameter;
    }

}
