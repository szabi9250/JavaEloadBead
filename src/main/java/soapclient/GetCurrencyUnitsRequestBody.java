
package soapclient;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>GetCurrencyUnitsRequestBody complex typeのJavaクラス。
 * 
 * <p>次のスキーマ・フラグメントは、このクラス内に含まれる予期されるコンテンツを指定します。
 * 
 * <pre>{@code
 * <complexType name="GetCurrencyUnitsRequestBody">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="currencyNames" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCurrencyUnitsRequestBody", propOrder = {
    "currencyNames"
})
public class GetCurrencyUnitsRequestBody {

    @XmlElementRef(name = "currencyNames", namespace = "http://www.mnb.hu/webservices/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> currencyNames;

    /**
     * currencyNamesプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCurrencyNames() {
        return currencyNames;
    }

    /**
     * currencyNamesプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCurrencyNames(JAXBElement<String> value) {
        this.currencyNames = value;
    }

}
