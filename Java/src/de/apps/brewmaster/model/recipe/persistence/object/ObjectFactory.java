//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.06.07 um 07:50:29 PM CEST 
//


package de.apps.brewmaster.model.recipe.persistence.object;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.apps.brewmaster.model.persistence.recipe package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _BrewRecipe_QNAME = new QName("", "brewRecipe");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.apps.brewmaster.model.persistence.recipe
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BrewRecipe }
     * 
     */
    public BrewRecipe createBrewRecipe() {
        return new BrewRecipe();
    }

    /**
     * Create an instance of {@link BrewStepParameter }
     * 
     */
    public BrewStepParameter createBrewStepParameter() {
        return new BrewStepParameter();
    }

    /**
     * Create an instance of {@link BrewStep }
     * 
     */
    public BrewStep createBrewStep() {
        return new BrewStep();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BrewRecipe }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "brewRecipe")
    public JAXBElement<BrewRecipe> createBrewRecipe(BrewRecipe value) {
        return new JAXBElement<BrewRecipe>(_BrewRecipe_QNAME, BrewRecipe.class, null, value);
    }

}
