package com.webprogramming.middletermproject.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ApiExplorer {
    public static String fetchDataAndParse(String stationName) throws IOException {
        // Open API 요청을 보낼 URL 생성
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty");
        urlBuilder.append("?" + URLEncoder.encode("stationName", "UTF-8") + "=" + URLEncoder.encode(stationName, "UTF-8")); // 측정소명
        urlBuilder.append("&" + URLEncoder.encode("dataTerm", "UTF-8") + "=" + URLEncoder.encode("3month", "UTF-8")); // 요청 데이터기간(month, week, day)
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); // 페이지 번호
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); // 한 페이지 결과 수
        urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); // 응답 데이터 형식(xml 또는 json)
        urlBuilder.append("&" + URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode("1.5", "UTF-8")); // 요청 데이터기간(month, week, day)
        urlBuilder.append("&" + URLEncoder.encode("serviceKey", "UTF-8") + "=OpEXOLcXTcmAqlBCX1VIsKNPuLTGODfsP5ej0%2Ft6gJY5zG4c6tbGru2wum6dv7cDuSRSi94cuF3sSsq%2Fx3oDFQ%3D%3D"); // 인증키

        // Open API 요청 보내기
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        // 응답 확인
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        // 응답 내용 읽기
        StringBuilder xmlResponse = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            xmlResponse.append(line);
        }
        rd.close();
        conn.disconnect();

        // 응답 내용을 파싱하여 필요한 정보 추출
        String parsedData = parseXmlResponse(xmlResponse.toString());
        System.out.println("---------------------------------");
        System.out.println(parsedData);

        return parsedData;
    }

    public static String parseXmlResponse(String xmlResponse) {
        try {
            // XML 파싱
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlResponse)));

            // 파싱 결과를 저장할 StringBuilder 생성
            StringBuilder result = new StringBuilder();

            // item 태그 내의 정보 추출
            NodeList itemList = document.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Node itemNode = itemList.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;
                    String dataTime = itemElement.getElementsByTagName("dataTime").item(0).getTextContent();
                    String stationName = itemElement.getElementsByTagName("stationName").item(0).getTextContent();
                    String so2Value = itemElement.getElementsByTagName("so2Value").item(0).getTextContent();
                    String coValue = itemElement.getElementsByTagName("coValue").item(0).getTextContent();
                    String o3Value = itemElement.getElementsByTagName("o3Value").item(0).getTextContent();
                    String no2Value = itemElement.getElementsByTagName("no2Value").item(0).getTextContent();
                    String pm10Value = itemElement.getElementsByTagName("pm10Value").item(0).getTextContent();
                    String pm25Value = itemElement.getElementsByTagName("pm25Value").item(0).getTextContent();

                    // "-" 값을 가진 데이터는 유효한 값이 아니므로 무시합니다.
                    // 유효한 값들로만 리스트를 만들어 평균을 계산합니다.
                    List<Double> validSo2Values = new ArrayList<>();
                    if (!"-".equals(so2Value)) {
                        validSo2Values.add(Double.parseDouble(so2Value));
                    }

                    List<Double> validCoValues = new ArrayList<>();
                    if (!"-".equals(coValue)) {
                        validCoValues.add(Double.parseDouble(coValue));
                    }

                    List<Double> validO3Values = new ArrayList<>();
                    if (!"-".equals(o3Value)) {
                        validO3Values.add(Double.parseDouble(o3Value));
                    }

                    List<Double> validNo2Values = new ArrayList<>();
                    if (!"-".equals(no2Value)) {
                        validNo2Values.add(Double.parseDouble(no2Value));
                    }

                    List<Double> validPm10Values = new ArrayList<>();
                    if (!"-".equals(pm10Value)) {
                        validPm10Values.add(Double.parseDouble(pm10Value));
                    }

                    List<Double> validPm25Values = new ArrayList<>();
                    if (!"-".equals(pm25Value)) {
                        validPm25Values.add(Double.parseDouble(pm25Value));
                    }

                    // 유효한 값이 없으면 평균값을 0으로 설정합니다.
                    double avgSo2Value = validSo2Values.isEmpty() ? 0.0 : validSo2Values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    double avgCoValue = validCoValues.isEmpty() ? 0.0 : validCoValues.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    double avgO3Value = validO3Values.isEmpty() ? 0.0 : validO3Values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    double avgNo2Value = validNo2Values.isEmpty() ? 0.0 : validNo2Values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    double avgPm10Value = validPm10Values.isEmpty() ? 0.0 : validPm10Values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    double avgPm25Value = validPm25Values.isEmpty() ? 0.0 : validPm25Values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

                    // 파싱 결과를 문자열로 저장
                    result.append("dataTime=").append(dataTime).append(",")
                            .append("stationName=").append(stationName).append(",")
                            .append("so2Value=").append(so2Value.equals("-") ? avgSo2Value : so2Value).append(",") // 결측치인 경우 평균값으로 대체
                            .append("coValue=").append(coValue.equals("-") ? avgCoValue : coValue).append(",") // 결측치인 경우 평균값으로 대체
                            .append("o3Value=").append(o3Value.equals("-") ? avgO3Value : o3Value).append(",") // 결측치인 경우 평균값으로 대체
                            .append("no2Value=").append(no2Value.equals("-") ? avgNo2Value : no2Value).append(",") // 결측치인 경우 평균값으로 대체
                            .append("pm10Value=").append(pm10Value.equals("-") ? avgPm10Value : pm10Value).append(",") // 결측치인 경우 평균값으로 대체
                            .append("pm25Value=").append(pm25Value.equals("-") ? avgPm25Value : pm25Value).append("\n"); // 결측치인 경우 평균값으로 대체
                }
            }

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "파싱에 실패했습니다.";
        }
    }
}
