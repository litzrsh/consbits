package me.litzrsh.consbits.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class DataGovTests {

    @Test
    public void test() throws URISyntaxException {
        final String url = "https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo";
        final String key = "kX3rMVPaYOKdjKC1cN134Dp5o1TFS9HarTdDhbBUi9aa4uhikJDcMJxpZ5tvNB%2BmTJ%2FVfiLloVVQpu6G6Y%2FeOA%3D%3D";
        final String year = "2024";
        final String month = "05";

        URI uri = new URI(url + "?serviceKey=" + key + "&solYear=" + year + "&solMonth=" + month);
        log.info("URI : {}", uri);

        RestTemplate template = new RestTemplate();
        String result = template.getForObject(uri, String.class);
        log.info("Result : {}", result);

        // 응답 형식 판별 및 처리
        if (result.trim().startsWith("<")) {
            // XML 파싱
            processXmlResponse(result);
        } else if (result.trim().startsWith("{")) {
            // JSON 파싱
            processJsonResponse(result);
        } else {
            log.error("Unknown response format");
        }
    }

    private void processXmlResponse(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new java.io.ByteArrayInputStream(xml.getBytes()));

            NodeList cmmMsgHeaderList = document.getElementsByTagName("cmmMsgHeader");

            if (cmmMsgHeaderList.getLength() > 0) {
                // 오류 응답 처리
                NodeList errMsgList = document.getElementsByTagName("errMsg");
                NodeList returnAuthMsgList = document.getElementsByTagName("returnAuthMsg");
                NodeList returnReasonCodeList = document.getElementsByTagName("returnReasonCode");

                String errMsg = errMsgList.item(0).getTextContent();
                String returnAuthMsg = returnAuthMsgList.item(0).getTextContent();
                String returnReasonCode = returnReasonCodeList.item(0).getTextContent();

                log.error("Error: {} - {} - {}", errMsg, returnAuthMsg, returnReasonCode);
            } else {
                log.error("Unknown XML response format");
            }
        } catch (Exception e) {
            log.error("Error parsing XML response", e);
        }
    }

    private void processJsonResponse(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);
            JsonNode items = rootNode.path("response").path("body").path("items").path("item");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    String dateName = item.path("dateName").asText();
                    String locdate = item.path("locdate").asText();
                    log.info("Holiday Name: {}, Date: {}", dateName, locdate);
                }
            } else {
                log.warn("No items found in the JSON response");
            }
        } catch (Exception e) {
            log.error("Error parsing JSON response", e);
        }
    }
}
