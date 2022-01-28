package com.example.integrlinebyline.envelope;


import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
public class ExampleBodyEnvelope implements Serializable {

    @XmlElement(name = "id", namespace = "http://schemas.xmlsoap.org/soap/envelope")
    public String id;

    @XmlElement(name = "docNumber", namespace = "http://schemas.xmlsoap.org/soap/envelope")
    public String docNumber;

    @XmlElement(name = "dest", namespace = "http://schemas.xmlsoap.org/soap/envelope")
    public String fileDest;
}
