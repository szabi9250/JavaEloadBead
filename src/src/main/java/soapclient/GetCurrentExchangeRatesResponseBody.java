
package soapclient;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>GetCurrentExchangeRatesResponseBody complex typeのJavaクラス。
 * 
 * <p>次のスキーマ・フラグメントは、このクラス内に含まれる予期されるコンテンツを指定します。
 * 
 * <pre>{@code
 * <complexType name="GetCurrentExchangeRatesResponseBody">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="GetCurrentExchangeRatesResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCurrentExchangeRatesResponseBody", propOrder = {
    "getCurrentExchangeRatesResult"
})
public class GetCurrentExchangeRatesResponseBody {

    @XmlElementRef(name = "GetCurrentExchangeRatesResult", namespace = "http://www.mnb.hu/webservices/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> getCurrentExchangeRatesResult;

    /**
     * getCurrentExchangeRatesResultプロパティの値を取得します。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGetCurrentExchangeRatesResult() {
        return getCurrentExchangeRatesResult;
    }

    /**
     * getCurrentExchangeRatesResultプロパティの値を設定します。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGetCurrentExchangeRatesResult(JAXBElement<String> value) {
        this.getCurrentExchangeRatesResult = value;
    }

}
