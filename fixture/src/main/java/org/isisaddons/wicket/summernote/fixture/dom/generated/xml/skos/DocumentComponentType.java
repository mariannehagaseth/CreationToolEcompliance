//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.14 at 04:14:26 PM BST 
//


package org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DocumentComponentType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DocumentComponentType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Document"/>
 *     &lt;enumeration value="Fragment"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DocumentComponentType")
@XmlEnum
public enum DocumentComponentType {

    @XmlEnumValue("Document")
    DOCUMENT("Document"),
    @XmlEnumValue("Fragment")
    FRAGMENT("Fragment");
    private final String value;

    DocumentComponentType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DocumentComponentType fromValue(String v) {
        for (DocumentComponentType c: DocumentComponentType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
