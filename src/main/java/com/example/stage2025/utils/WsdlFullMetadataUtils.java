package com.example.stage2025.utils;

import com.example.stage2025.dto.SoapFieldDto;
import com.example.stage2025.dto.SoapOperationMeta;
import org.w3c.dom.*;
import javax.wsdl.*;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class WsdlFullMetadataUtils {

    public static List<SoapOperationMeta> getAllOperationMetadata(String wsdlUrl) throws Exception {
        // Step 1: Parse WSDL
        WSDLFactory factory = WSDLFactory.newInstance();
        WSDLReader reader = factory.newWSDLReader();
        Definition def = reader.readWSDL(wsdlUrl);

        // Step 2: Parse embedded XSD for element/fields
        Map<String, List<SoapFieldDto>> xsdElements = parseAllXSDElements(wsdlUrl);

        Map<String, SoapOperationMeta> result = new LinkedHashMap<>();

        Map<?, ?> portTypes = def.getAllPortTypes();
        for (Object portTypeObj : portTypes.values()) {
            PortType portType = (PortType) portTypeObj;
            for (Object opObj : portType.getOperations()) {
                Operation op = (Operation) opObj;
                SoapOperationMeta meta = new SoapOperationMeta();
                meta.setOperationName(op.getName());

                // Get input/output element names and fields
                if (op.getInput() != null) {
                    Message inputMsg = op.getInput().getMessage();
                    for (Object partObj : inputMsg.getParts().values()) {
                        Part part = (Part) partObj;
                        QName element = part.getElementName();
                        if (element != null) {
                            String inputEl = element.getLocalPart();
                            meta.setInputElement(inputEl);
                            meta.setInputFields(xsdElements.getOrDefault(inputEl, List.of()));
                        }
                    }
                }
                if (op.getOutput() != null) {
                    Message outputMsg = op.getOutput().getMessage();
                    for (Object partObj : outputMsg.getParts().values()) {
                        Part part = (Part) partObj;
                        QName element = part.getElementName();
                        if (element != null) {
                            String outputEl = element.getLocalPart();
                            meta.setOutputElement(outputEl);
                            meta.setOutputFields(xsdElements.getOrDefault(outputEl, List.of()));
                        }
                    }
                }

                // SOAP Action
                meta.setSoapAction("");
                for (Object bindingObj : def.getAllBindings().values()) {
                    Binding binding = (Binding) bindingObj;
                    for (Object bopObj : binding.getBindingOperations()) {
                        BindingOperation bop = (BindingOperation) bopObj;
                        if (bop.getName().equals(op.getName())) {
                            List<?> exts = bop.getExtensibilityElements();
                            for (Object ext : exts) {
                                if (ext instanceof javax.wsdl.extensions.soap.SOAPOperation) {
                                    javax.wsdl.extensions.soap.SOAPOperation soapOp = (javax.wsdl.extensions.soap.SOAPOperation) ext;
                                    meta.setSoapAction(soapOp.getSoapActionURI());
                                }
                            }
                        }
                    }
                }

                result.put(meta.getOperationName(), meta);
            }
        }
        return new ArrayList<>(result.values());
    }

    // Parse XSD <xs:element> definitions to get parameter/field info
    private static Map<String, List<SoapFieldDto>> parseAllXSDElements(String wsdlUrl) throws Exception {
        Map<String, List<SoapFieldDto>> elements = new HashMap<>();
        Map<String, List<SoapFieldDto>> complexTypes = new HashMap<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        Document doc;
        try (InputStream is = new URL(wsdlUrl).openStream()) {
            doc = dbf.newDocumentBuilder().parse(is);
        }

        // Step 1: Find all complexTypes by name
        NodeList schemaList = doc.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
        for (int s = 0; s < schemaList.getLength(); s++) {
            Element schema = (Element) schemaList.item(s);

            NodeList complexTypeList = schema.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "complexType");
            for (int c = 0; c < complexTypeList.getLength(); c++) {
                Element complexType = (Element) complexTypeList.item(c);
                String typeName = complexType.getAttribute("name");

                // Look for <xs:sequence> in this complexType
                NodeList sequenceList = complexType.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "sequence");
                for (int q = 0; q < sequenceList.getLength(); q++) {
                    Element sequence = (Element) sequenceList.item(q);
                    NodeList fieldEls = sequence.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "element");
                    List<SoapFieldDto> fields = new ArrayList<>();
                    for (int f = 0; f < fieldEls.getLength(); f++) {
                        Element field = (Element) fieldEls.item(f);
                        String fname = field.getAttribute("name");
                        String ftype = field.getAttribute("type");
                        fields.add(new SoapFieldDto(fname, ftype));
                    }
                    if (!fields.isEmpty() && typeName != null && !typeName.isEmpty()) {
                        complexTypes.put(typeName, fields);
                    }
                }
            }
        }

        // Step 2: Find all elements and their (inline or referenced) fields
        for (int s = 0; s < schemaList.getLength(); s++) {
            Element schema = (Element) schemaList.item(s);
            NodeList elList = schema.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "element");
            for (int i = 0; i < elList.getLength(); i++) {
                Element el = (Element) elList.item(i);
                String elName = el.getAttribute("name");

                // Inline <complexType>
                NodeList children = el.getChildNodes();
                boolean found = false;
                for (int j = 0; j < children.getLength(); j++) {
                    Node n = children.item(j);
                    if (n instanceof Element && "complexType".equals(n.getLocalName())) {
                        Element ct = (Element) n;
                        NodeList seqList = ct.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "sequence");
                        for (int q = 0; q < seqList.getLength(); q++) {
                            Element seq = (Element) seqList.item(q);
                            NodeList fieldEls = seq.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "element");
                            List<SoapFieldDto> fields = new ArrayList<>();
                            for (int f = 0; f < fieldEls.getLength(); f++) {
                                Element field = (Element) fieldEls.item(f);
                                String fname = field.getAttribute("name");
                                String ftype = field.getAttribute("type");
                                fields.add(new SoapFieldDto(fname, ftype));
                            }
                            if (!fields.isEmpty()) {
                                elements.put(elName, fields);
                                found = true;
                            }
                        }
                    }
                }
                // If not found inline, try referenced type
                if (!found && el.hasAttribute("type")) {
                    String typeRef = el.getAttribute("type");
                    if (typeRef.contains(":")) {
                        typeRef = typeRef.split(":")[1];
                    }
                    if (complexTypes.containsKey(typeRef)) {
                        elements.put(elName, complexTypes.get(typeRef));
                    }
                }
            }
        }
        return elements;
    }


}
