//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.11 at 03:50:09 PM CET 
//


package org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SKOSConceptProperty.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SKOSConceptProperty"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="altLabel"/&gt;
 *     &lt;enumeration value="prefLabel"/&gt;
 *     &lt;enumeration value="hiddenLabel"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SKOSConceptProperty")
@XmlEnum
public enum SKOSConceptProperty {

    @XmlEnumValue("altLabel")
    ALT_LABEL("altLabel"),
    @XmlEnumValue("prefLabel")
    PREF_LABEL("prefLabel"),
    @XmlEnumValue("hiddenLabel")
    HIDDEN_LABEL("hiddenLabel");
    private final String value;

    SKOSConceptProperty(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SKOSConceptProperty fromValue(String v) {
        for (SKOSConceptProperty c: SKOSConceptProperty.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
