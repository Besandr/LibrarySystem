package com.library.web.controller;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dependency")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
class ServiceDependencyConfig {

    @XmlElement(name = "class")
    String serviceClass;

    @XmlElement(name = "name")
    String serviceVarName;
}
