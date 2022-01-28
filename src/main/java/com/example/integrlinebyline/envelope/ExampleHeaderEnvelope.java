package com.example.integrlinebyline.envelope;

import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "Header", namespace = "http://schemas.xmlsoap.org/soap/envelope")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
public class ExampleHeaderEnvelope implements Serializable {

}
