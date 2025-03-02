package com.webprogramming.middletermproject.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class InfluxDB {

    private static char[] token = "DcKEp5NVq6hrMfrvjbEBkwW3TdqKtukNrnLoSwijEUxhN90454sSnHGubrOlm8ZJdiJDmb3tPSj5ZylOdIZ1cw==".toCharArray();
    private static String org = "Ubicomp";
    private static String bucket = "Ubicomp-Bucket";

    private static double firstPm10Value = -1;
    private static double firstPm25Value = -1;
    public static void saveDataToInfluxDB(String parsedData) {

        String[] dataLines = parsedData.split("\n");

        InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://localhost:8086", token, org, bucket);

        WriteApi writeApi = influxDBClient.getWriteApi();

        for (String dataLine : dataLines) {
            String[] fields = dataLine.split(",");
            String dataTimeString = fields[0].split("=")[1];

            LocalDateTime dateTime = LocalDateTime.parse(dataTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            Instant instant = dateTime.toInstant(ZoneOffset.UTC);
            long timeMillis = instant.toEpochMilli();

            String stationName = fields[1].split("=")[1];
            double so2Value = Double.parseDouble(fields[2].split("=")[1]);
            double coValue = Double.parseDouble(fields[3].split("=")[1]);
            double o3Value = Double.parseDouble(fields[4].split("=")[1]);
            double no2Value = Double.parseDouble(fields[5].split("=")[1]);
            double pm10Value = Double.parseDouble(fields[6].split("=")[1]);
            double pm25Value = Double.parseDouble(fields[7].split("=")[1]);

            if (firstPm10Value == -1) {
                firstPm10Value = pm10Value;
                System.out.println(firstPm10Value);
            }
            if (firstPm25Value == -1) {
                firstPm25Value = pm25Value;
                System.out.println(firstPm25Value);
            }

            Point point = Point.measurement("air_quality")
                    .addTag("station_name", stationName)
                    .addField("so2_value", so2Value)
                    .addField("co_value", coValue)
                    .addField("o3_value", o3Value)
                    .addField("no2_value", no2Value)
                    .addField("pm10_value", pm10Value)
                    .addField("pm25_value", pm25Value)
                    .time(timeMillis, WritePrecision.MS);

            writeApi.writePoint(point);
            System.out.println("Data point written successfully:" + point.toLineProtocol());
        }
        influxDBClient.close();
    }

    public static double getFirstPm10Value() {
        return firstPm10Value;
    }

    public static double getFirstPm25Value() {
        return firstPm25Value;
    }

    public static void resetFirstPmValues() {
        firstPm10Value = -1;
        firstPm25Value = -1;
    }
}
