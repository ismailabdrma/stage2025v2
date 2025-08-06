// src/main/java/com/example/stage2025/service/SoapProductFetcher.java
package com.example.stage2025.service;

import com.example.stage2025.dto.SupplierProductDto;
import com.example.stage2025.entity.SoapSupplier;
import com.example.stage2025.entity.Supplier;
import jakarta.xml.soap.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SoapProductFetcher implements ProductFetcher {

    @Override
    public List<SupplierProductDto> fetchProducts(Supplier supplier) throws Exception {
        if (!(supplier instanceof SoapSupplier soap)) {
            throw new IllegalArgumentException("Supplier is not SOAP based");
        }
        String opName = soap.getOperations().stream()
                .filter(op -> op.getOperationName().equalsIgnoreCase("getAllProducts")
                        || op.getOperationName().equalsIgnoreCase("FETCH_PRODUCTS"))
                .map(op -> op.getOperationName())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No suitable SOAP operation for fetching products"));

        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage request = factory.createMessage();
        SOAPPart part = request.getSOAPPart();
        SOAPEnvelope envelope = part.getEnvelope();
        SOAPBody body = envelope.getBody();

        SOAPBodyElement bodyElement = body.addBodyElement(
                envelope.createName(opName, "ws", soap.getNamespace())
        );

        request.saveChanges();

        SOAPConnection conn = SOAPConnectionFactory.newInstance().createConnection();
        SOAPMessage response = conn.call(request, soap.getWsdlUrl());
        conn.close();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        response.writeTo(out);
        String xml = out.toString();

        List<SupplierProductDto> result = new ArrayList<>();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes()));
        NodeList products = doc.getElementsByTagName("products");
        for (int i = 0; i < products.getLength(); i++) {
            Element p = (Element) products.item(i);
            String id = getTag(p, "id");
            String name = getTag(p, "name");
            String description = getTag(p, "description");
            double price = Double.parseDouble(getTag(p, "displayedPrice"));
            int stock = Integer.parseInt(getTag(p, "availableQuantity"));
            String imageUrl = getTag(p, "pictureUrl");
            result.add(new SupplierProductDto(
                    id, name, description, price, stock, List.of(imageUrl)
            ));
        }
        return result;
    }

    private String getTag(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        return list.getLength() > 0 ? list.item(0).getTextContent() : "";
    }
}
