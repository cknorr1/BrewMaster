//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.06.07 um 07:50:29 PM CEST 
//


package de.apps.brewmaster.model.recipe.persistence.object;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für brewStepParameter complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="brewStepParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="duration" type="{}duration"/>
 *         &lt;element name="temperature" type="{}temperature"/>
 *         &lt;element name="stirringSpeed" type="{}stirringSpeed"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "brewStepParameter", propOrder = {
    "duration",
    "temperature",
    "stirringSpeed"
})
public class BrewStepParameter {

    protected Long duration;
    protected BigInteger temperature;
    @XmlSchemaType(name = "string")
    protected StirringSpeed stirringSpeed;

    /**
     * Ruft den Wert der duration-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Legt den Wert der duration-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDuration(Long value) {
        this.duration = value;
    }

    /**
     * Ruft den Wert der temperature-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTemperature() {
        return temperature;
    }

    /**
     * Legt den Wert der temperature-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTemperature(BigInteger value) {
        this.temperature = value;
    }

    /**
     * Ruft den Wert der stirringSpeed-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link StirringSpeed }
     *     
     */
    public StirringSpeed getStirringSpeed() {
        return stirringSpeed;
    }

    /**
     * Legt den Wert der stirringSpeed-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link StirringSpeed }
     *     
     */
    public void setStirringSpeed(StirringSpeed value) {
        this.stirringSpeed = value;
    }

}
