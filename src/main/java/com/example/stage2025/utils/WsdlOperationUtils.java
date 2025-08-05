// src/main/java/com/example/stage2025/util/WsdlOperationUtils.java
package com.example.stage2025.utils;

import javax.wsdl.*;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import java.util.*;

public class WsdlOperationUtils {
    public static List<String> getAllOperationNames(String wsdlUrl) throws Exception {
        WSDLFactory factory = WSDLFactory.newInstance();
        WSDLReader reader = factory.newWSDLReader();
        Definition def = reader.readWSDL(wsdlUrl);

        List<String> operationNames = new ArrayList<>();
        Map<?, ?> portTypes = def.getAllPortTypes();
        for (Object portTypeObj : portTypes.values()) {
            PortType portType = (PortType) portTypeObj;
            for (Object opObj : portType.getOperations()) {
                Operation op = (Operation) opObj;
                operationNames.add(op.getName());
            }
        }
        return operationNames;
    }
}
