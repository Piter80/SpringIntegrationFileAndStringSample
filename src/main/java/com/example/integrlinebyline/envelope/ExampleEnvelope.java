package com.example.integrlinebyline.envelope;


import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
public class ExampleEnvelope implements Serializable {

    @XmlElement(name = "Header", namespace = "http://schemas.xmlsoap.org/soap/envelope")
    public ExampleHeaderEnvelope header;

    @XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope")
    public ExampleBodyEnvelope body;
}
