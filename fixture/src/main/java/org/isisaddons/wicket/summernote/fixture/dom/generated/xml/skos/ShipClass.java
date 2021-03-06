//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.14 at 04:14:26 PM BST 
//


package org.isisaddons.wicket.summernote.fixture.dom.generated.xml.skos;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for ShipClass complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShipClass">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="min_tonnage_ex" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="max_tonnage_ex" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="min_tonnage_in" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="max_tonnage_in" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="min_length_ex" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="max_length_ex" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="min_length_in" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="max_length_in" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="min_draught_ex" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="max_draught_ex" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="min_draught_in" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="max_draught_in" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="min_passenger_ex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="max_passenger_ex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="min_passenger_in" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="max_passenger_in" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="min_keel_laid_ex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="max_keel_laid_ex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="min_keel_laid_in" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="max_keel_laid_in" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="length_unit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tonnage_unit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="draught_unit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipClass", propOrder = {
    "type",
    "minTonnageEx",
    "maxTonnageEx",
    "minTonnageIn",
    "maxTonnageIn",
    "minLengthEx",
    "maxLengthEx",
    "minLengthIn",
    "maxLengthIn",
    "minDraughtEx",
    "maxDraughtEx",
    "minDraughtIn",
    "maxDraughtIn",
    "minPassengerEx",
    "maxPassengerEx",
    "minPassengerIn",
    "maxPassengerIn",
    "minKeelLaidEx",
    "maxKeelLaidEx",
    "minKeelLaidIn",
    "maxKeelLaidIn",
    "lengthUnit",
    "tonnageUnit",
    "draughtUnit"
})
@XmlRootElement(name="ship_class_occurrence")
public class ShipClass {

    @XmlElement(required = true)
    protected String type;
    @XmlElement(name = "min_tonnage_ex")
    protected double minTonnageEx;
    @XmlElement(name = "max_tonnage_ex")
    protected double maxTonnageEx;
    @XmlElement(name = "min_tonnage_in")
    protected double minTonnageIn;
    @XmlElement(name = "max_tonnage_in")
    protected double maxTonnageIn;
    @XmlElement(name = "min_length_ex")
    protected double minLengthEx;
    @XmlElement(name = "max_length_ex")
    protected double maxLengthEx;
    @XmlElement(name = "min_length_in")
    protected double minLengthIn;
    @XmlElement(name = "max_length_in")
    protected double maxLengthIn;
    @XmlElement(name = "min_draught_ex")
    protected double minDraughtEx;
    @XmlElement(name = "max_draught_ex")
    protected double maxDraughtEx;
    @XmlElement(name = "min_draught_in")
    protected double minDraughtIn;
    @XmlElement(name = "max_draught_in")
    protected double maxDraughtIn;
    @XmlElement(name = "min_passenger_ex")
    protected int minPassengerEx;
    @XmlElement(name = "max_passenger_ex")
    protected int maxPassengerEx;
    @XmlElement(name = "min_passenger_in")
    protected int minPassengerIn;
    @XmlElement(name = "max_passenger_in")
    protected int maxPassengerIn;
    @XmlElement(name = "min_keel_laid_ex")
    protected int minKeelLaidEx;
    @XmlElement(name = "max_keel_laid_ex")
    protected int maxKeelLaidEx;
    @XmlElement(name = "min_keel_laid_in")
    protected int minKeelLaidIn;
    @XmlElement(name = "max_keel_laid_in")
    protected int maxKeelLaidIn;
    @XmlElement(name = "length_unit", required = true)
    protected String lengthUnit;
    @XmlElement(name = "tonnage_unit", required = true)
    protected String tonnageUnit;
    @XmlElement(name = "draught_unit", required = true)
    protected String draughtUnit;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the minTonnageEx property.
     * 
     */
    public double getMinTonnageEx() {
        return minTonnageEx;
    }

    /**
     * Sets the value of the minTonnageEx property.
     * 
     */
    public void setMinTonnageEx(double value) {
        this.minTonnageEx = value;
    }

    /**
     * Gets the value of the maxTonnageEx property.
     * 
     */
    public double getMaxTonnageEx() {
        return maxTonnageEx;
    }

    /**
     * Sets the value of the maxTonnageEx property.
     * 
     */
    public void setMaxTonnageEx(double value) {
        this.maxTonnageEx = value;
    }

    /**
     * Gets the value of the minTonnageIn property.
     * 
     */
    public double getMinTonnageIn() {
        return minTonnageIn;
    }

    /**
     * Sets the value of the minTonnageIn property.
     * 
     */
    public void setMinTonnageIn(double value) {
        this.minTonnageIn = value;
    }

    /**
     * Gets the value of the maxTonnageIn property.
     * 
     */
    public double getMaxTonnageIn() {
        return maxTonnageIn;
    }

    /**
     * Sets the value of the maxTonnageIn property.
     * 
     */
    public void setMaxTonnageIn(double value) {
        this.maxTonnageIn = value;
    }

    /**
     * Gets the value of the minLengthEx property.
     * 
     */
    public double getMinLengthEx() {
        return minLengthEx;
    }

    /**
     * Sets the value of the minLengthEx property.
     * 
     */
    public void setMinLengthEx(double value) {
        this.minLengthEx = value;
    }

    /**
     * Gets the value of the maxLengthEx property.
     * 
     */
    public double getMaxLengthEx() {
        return maxLengthEx;
    }

    /**
     * Sets the value of the maxLengthEx property.
     * 
     */
    public void setMaxLengthEx(double value) {
        this.maxLengthEx = value;
    }

    /**
     * Gets the value of the minLengthIn property.
     * 
     */
    public double getMinLengthIn() {
        return minLengthIn;
    }

    /**
     * Sets the value of the minLengthIn property.
     * 
     */
    public void setMinLengthIn(double value) {
        this.minLengthIn = value;
    }

    /**
     * Gets the value of the maxLengthIn property.
     * 
     */
    public double getMaxLengthIn() {
        return maxLengthIn;
    }

    /**
     * Sets the value of the maxLengthIn property.
     * 
     */
    public void setMaxLengthIn(double value) {
        this.maxLengthIn = value;
    }

    /**
     * Gets the value of the minDraughtEx property.
     * 
     */
    public double getMinDraughtEx() {
        return minDraughtEx;
    }

    /**
     * Sets the value of the minDraughtEx property.
     * 
     */
    public void setMinDraughtEx(double value) {
        this.minDraughtEx = value;
    }

    /**
     * Gets the value of the maxDraughtEx property.
     * 
     */
    public double getMaxDraughtEx() {
        return maxDraughtEx;
    }

    /**
     * Sets the value of the maxDraughtEx property.
     * 
     */
    public void setMaxDraughtEx(double value) {
        this.maxDraughtEx = value;
    }

    /**
     * Gets the value of the minDraughtIn property.
     * 
     */
    public double getMinDraughtIn() {
        return minDraughtIn;
    }

    /**
     * Sets the value of the minDraughtIn property.
     * 
     */
    public void setMinDraughtIn(double value) {
        this.minDraughtIn = value;
    }

    /**
     * Gets the value of the maxDraughtIn property.
     * 
     */
    public double getMaxDraughtIn() {
        return maxDraughtIn;
    }

    /**
     * Sets the value of the maxDraughtIn property.
     * 
     */
    public void setMaxDraughtIn(double value) {
        this.maxDraughtIn = value;
    }

    /**
     * Gets the value of the minPassengerEx property.
     * 
     */
    public int getMinPassengerEx() {
        return minPassengerEx;
    }

    /**
     * Sets the value of the minPassengerEx property.
     * 
     */
    public void setMinPassengerEx(int value) {
        this.minPassengerEx = value;
    }

    /**
     * Gets the value of the maxPassengerEx property.
     * 
     */
    public int getMaxPassengerEx() {
        return maxPassengerEx;
    }

    /**
     * Sets the value of the maxPassengerEx property.
     * 
     */
    public void setMaxPassengerEx(int value) {
        this.maxPassengerEx = value;
    }

    /**
     * Gets the value of the minPassengerIn property.
     * 
     */
    public int getMinPassengerIn() {
        return minPassengerIn;
    }

    /**
     * Sets the value of the minPassengerIn property.
     * 
     */
    public void setMinPassengerIn(int value) {
        this.minPassengerIn = value;
    }

    /**
     * Gets the value of the maxPassengerIn property.
     * 
     */
    public int getMaxPassengerIn() {
        return maxPassengerIn;
    }

    /**
     * Sets the value of the maxPassengerIn property.
     * 
     */
    public void setMaxPassengerIn(int value) {
        this.maxPassengerIn = value;
    }

    /**
     * Gets the value of the minKeelLaidEx property.
     * 
     */
    public int getMinKeelLaidEx() {
        return minKeelLaidEx;
    }

    /**
     * Sets the value of the minKeelLaidEx property.
     * 
     */
    public void setMinKeelLaidEx(int value) {
        this.minKeelLaidEx = value;
    }

    /**
     * Gets the value of the maxKeelLaidEx property.
     * 
     */
    public int getMaxKeelLaidEx() {
        return maxKeelLaidEx;
    }

    /**
     * Sets the value of the maxKeelLaidEx property.
     * 
     */
    public void setMaxKeelLaidEx(int value) {
        this.maxKeelLaidEx = value;
    }

    /**
     * Gets the value of the minKeelLaidIn property.
     * 
     */
    public int getMinKeelLaidIn() {
        return minKeelLaidIn;
    }

    /**
     * Sets the value of the minKeelLaidIn property.
     * 
     */
    public void setMinKeelLaidIn(int value) {
        this.minKeelLaidIn = value;
    }

    /**
     * Gets the value of the maxKeelLaidIn property.
     * 
     */
    public int getMaxKeelLaidIn() {
        return maxKeelLaidIn;
    }

    /**
     * Sets the value of the maxKeelLaidIn property.
     * 
     */
    public void setMaxKeelLaidIn(int value) {
        this.maxKeelLaidIn = value;
    }

    /**
     * Gets the value of the lengthUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLengthUnit() {
        return lengthUnit;
    }

    /**
     * Sets the value of the lengthUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLengthUnit(String value) {
        this.lengthUnit = value;
    }

    /**
     * Gets the value of the tonnageUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTonnageUnit() {
        return tonnageUnit;
    }

    /**
     * Sets the value of the tonnageUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTonnageUnit(String value) {
        this.tonnageUnit = value;
    }

    /**
     * Gets the value of the draughtUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDraughtUnit() {
        return draughtUnit;
    }

    /**
     * Sets the value of the draughtUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDraughtUnit(String value) {
        this.draughtUnit = value;
    }

}
