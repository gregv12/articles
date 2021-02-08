package com.fluxtion.articles.aot_csv.generated;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.api.annotations.Config;
import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.Inject;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.articles.aot_csv.City;
import com.fluxtion.ext.streaming.api.log.LogControlEvent;
import com.fluxtion.ext.streaming.api.log.LogService;
import com.fluxtion.ext.streaming.api.util.CharArrayCharSequence;
import com.fluxtion.ext.streaming.api.util.CharArrayCharSequence.CharSequenceView;
import com.fluxtion.ext.text.api.csv.RowProcessor;
import com.fluxtion.ext.text.api.csv.ValidationLogger;
import com.fluxtion.ext.text.api.event.CharEvent;
import com.fluxtion.ext.text.api.event.EofEvent;
import com.fluxtion.ext.text.api.event.RegisterEventHandler;
import com.fluxtion.ext.text.api.util.CharStreamer;
import com.fluxtion.ext.text.api.util.StringDriver;
import com.fluxtion.ext.text.api.util.marshaller.CsvRecordMarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static com.fluxtion.ext.text.api.csv.Converters.*;

/**
 * Fluxtion generated CSV decoder.
 *
 * <p>target class : City
 *
 * @author Greg Higgins
 */
public class CityCsvDecoder0 implements RowProcessor<City> {

  @Inject
  @Config(key = "id", value = "validationLog")
  @PushReference
  public ValidationLogger errorLog;
  //buffer management
  private final char[] chars = new char[4096];
  private final int[] delimIndex = new int[1024];
  private StringBuilder msgSink = new StringBuilder(256);
  private final CharArrayCharSequence seq = new CharArrayCharSequence(chars);
  private int fieldIndex = 0;
  private int writeIndex = 0;
  //target
  private City target;
  //source field index: -1
  private final CharSequenceView setAccentCity = seq.view();
  private int fieldName_accentCity = -1;
  //source field index: -1
  private final CharSequenceView setCity = seq.view();
  private int fieldName_city = -1;
  //source field index: -1
  private final CharSequenceView setCountry = seq.view();
  private int fieldName_country = -1;
  //source field index: -1
  private final CharSequenceView setLatitude = seq.view();
  private int fieldName_latitude = -1;
  //source field index: -1
  private final CharSequenceView setLongitude = seq.view();
  private int fieldName_longitude = -1;
  //source field index: -1
  private final CharSequenceView setPopulation = seq.view();
  private int fieldName_population = -1;
  //source field index: -1
  private final CharSequenceView setRegion = seq.view();
  private int fieldName_region = -1;
  //processing state and meta-data
  private int rowNumber;
  private final HashMap fieldMap = new HashMap<>();
  private static final int HEADER_ROWS = 1;
  private static final int MAPPING_ROW = 1;
  private boolean passedValidation;

  @EventHandler
  @Override
  public boolean charEvent(CharEvent event) {
    final char character = event.getCharacter();
    passedValidation = true;
    if (character == '\r') {
      return false;
    }
    if (character == '\n') {
      return processRow();
    }
    if (character == ',') {
      updateFieldIndex();
    }
    chars[writeIndex++] = character;
    return false;
  }

  @EventHandler
  @Override
  public boolean eof(EofEvent eof) {
    return writeIndex == 0 ? false : processRow();
  }

  private boolean processRow() {
    boolean targetChanged = false;
    rowNumber++;
    if (HEADER_ROWS < rowNumber) {
      targetChanged = updateTarget();
    } else if (rowNumber == MAPPING_ROW) {
      mapHeader();
    }
    writeIndex = 0;
    fieldIndex = 0;
    return targetChanged;
  }

  private void mapHeader() {
    String header = new String(chars).trim();
    header = header.replaceAll("\\P{InBasic_Latin}", "");
    header = header.replace("\"", "");
    List<String> headers = new ArrayList();
    for (String colName : header.split("[,]")) {
      headers.add(getIdentifier(colName));
    }
    fieldName_accentCity = headers.indexOf("accentCity");
    fieldMap.put(fieldName_accentCity, "setAccentCity");
    if (fieldName_accentCity < 0) {
      logHeaderProblem(
          "problem mapping field:'accentCity' missing column header, index row:", true, null);
    }
    fieldName_city = headers.indexOf("city");
    fieldMap.put(fieldName_city, "setCity");
    if (fieldName_city < 0) {
      logHeaderProblem(
          "problem mapping field:'city' missing column header, index row:", true, null);
    }
    fieldName_country = headers.indexOf("country");
    fieldMap.put(fieldName_country, "setCountry");
    if (fieldName_country < 0) {
      logHeaderProblem(
          "problem mapping field:'country' missing column header, index row:", true, null);
    }
    fieldName_latitude = headers.indexOf("latitude");
    fieldMap.put(fieldName_latitude, "setLatitude");
    if (fieldName_latitude < 0) {
      logHeaderProblem(
          "problem mapping field:'latitude' missing column header, index row:", true, null);
    }
    fieldName_longitude = headers.indexOf("longitude");
    fieldMap.put(fieldName_longitude, "setLongitude");
    if (fieldName_longitude < 0) {
      logHeaderProblem(
          "problem mapping field:'longitude' missing column header, index row:", true, null);
    }
    fieldName_population = headers.indexOf("population");
    fieldMap.put(fieldName_population, "setPopulation");
    if (fieldName_population < 0) {
      logHeaderProblem(
          "problem mapping field:'population' missing column header, index row:", true, null);
    }
    fieldName_region = headers.indexOf("region");
    fieldMap.put(fieldName_region, "setRegion");
    if (fieldName_region < 0) {
      logHeaderProblem(
          "problem mapping field:'region' missing column header, index row:", true, null);
    }
  }

  private boolean updateTarget() {
    try {
      updateFieldIndex();
      fieldIndex = fieldName_accentCity;
      setAccentCity.subSequenceNoOffset(
          delimIndex[fieldName_accentCity], delimIndex[fieldName_accentCity + 1] - 1);
      target.setAccentCity(setAccentCity);

      fieldIndex = fieldName_city;
      setCity.subSequenceNoOffset(delimIndex[fieldName_city], delimIndex[fieldName_city + 1] - 1);
      target.setCity(setCity);

      fieldIndex = fieldName_country;
      setCountry.subSequenceNoOffset(
          delimIndex[fieldName_country], delimIndex[fieldName_country + 1] - 1);
      target.setCountry(setCountry);

      fieldIndex = fieldName_latitude;
      setLatitude.subSequenceNoOffset(
          delimIndex[fieldName_latitude], delimIndex[fieldName_latitude + 1] - 1);
      target.setLatitude(setLatitude);

      fieldIndex = fieldName_longitude;
      setLongitude.subSequenceNoOffset(
          delimIndex[fieldName_longitude], delimIndex[fieldName_longitude + 1] - 1);
      target.setLongitude(setLongitude);

      fieldIndex = fieldName_population;
      setPopulation.subSequenceNoOffset(
          delimIndex[fieldName_population], delimIndex[fieldName_population + 1] - 1);
      target.setPopulation(setPopulation);

      fieldIndex = fieldName_region;
      setRegion.subSequenceNoOffset(
          delimIndex[fieldName_region], delimIndex[fieldName_region + 1] - 1);
      target.setRegion(setRegion);

    } catch (Exception e) {
      logException(
          "problem pushing '"
              + seq.subSequence(delimIndex[fieldIndex], delimIndex[fieldIndex + 1] - 1).toString()
              + "'"
              + " from row:'"
              + rowNumber
              + "'",
          false,
          e);
      passedValidation = false;
      return false;
    } finally {
      fieldIndex = 0;
    }
    return true;
  }

  private void updateFieldIndex() {
    fieldIndex++;
    delimIndex[fieldIndex] = writeIndex + 1;
  }

  private void logException(String prefix, boolean fatal, Exception e) {
    errorLog
        .getSb()
        .append("CityCsvDecoder0 ")
        .append(prefix)
        .append(" fieldIndex:'")
        .append(fieldIndex)
        .append("' targetMethod:'City#")
        .append(fieldMap.get(fieldIndex))
        .append("' error:'")
        .append(e.toString())
        .append("'");
    if (fatal) {
      errorLog.logFatal("");
      throw new RuntimeException(errorLog.getSb().toString(), e);
    }
    errorLog.logError("");
  }

  private void logHeaderProblem(String prefix, boolean fatal, Exception e) {
    errorLog.getSb().append("CityCsvDecoder0 ").append(prefix).append(rowNumber);
    if (fatal) {
      errorLog.logFatal("");
      throw new RuntimeException(errorLog.getSb().toString(), e);
    }
    errorLog.logError("");
  }

  @Override
  public City event() {
    return target;
  }

  @Override
  public Class<City> eventClass() {
    return City.class;
  }

  @Initialise
  @Override
  public void init() {
    target = new City();
    fieldMap.put(fieldName_accentCity, "setAccentCity");
    fieldMap.put(fieldName_city, "setCity");
    fieldMap.put(fieldName_country, "setCountry");
    fieldMap.put(fieldName_latitude, "setLatitude");
    fieldMap.put(fieldName_longitude, "setLongitude");
    fieldMap.put(fieldName_population, "setPopulation");
    fieldMap.put(fieldName_region, "setRegion");
  }

  @Override
  public boolean passedValidation() {
    return passedValidation;
  }

  @Override
  public int getRowNumber() {
    return rowNumber;
  }

  @Override
  public void setErrorLog(ValidationLogger errorLog) {
    this.errorLog = errorLog;
  }

  public static String csvHeader() {
    String out = "";
    out += "accentCity,";
    out += "city,";
    out += "country,";
    out += "latitude,";
    out += "longitude,";
    out += "population,";
    out += "region";
    return out;
  }

  public static void asCsv(City src, StringBuilder msgSink) throws IOException {
    msgSink.append(src.getAccentCity());
    msgSink.append(",");
    msgSink.append(src.getCity());
    msgSink.append(",");
    msgSink.append(src.getCountry());
    msgSink.append(",");
    msgSink.append(src.getLatitude());
    msgSink.append(",");
    msgSink.append(src.getLongitude());
    msgSink.append(",");
    msgSink.append(src.getPopulation());
    msgSink.append(",");
    msgSink.append(src.getRegion());
    //msgSink.append("\n");
  }

  public static void asCsv(City src, Appendable target) throws IOException {
    StringBuilder sb = new StringBuilder();
    asCsv(src, sb);
    target.append(sb);
  }

  @Override
  public String csvHeaders() {
    return csvHeader();
  }

  @Override
  public Appendable toCsv(City src, Appendable target) throws IOException {
    msgSink.setLength(0);
    asCsv(src, msgSink);
    target.append(msgSink);
    msgSink.setLength(0);
    return target;
  }

  public static CsvRecordMarshaller marshaller() {
    return new CsvRecordMarshaller(new CityCsvDecoder0());
  }

  public static void stream(StaticEventProcessor target, String input) {
    CsvRecordMarshaller marshaller = marshaller();
    marshaller.handleEvent(new RegisterEventHandler(target));
    StringDriver.streamChars(input, marshaller);
    target.onEvent(EofEvent.EOF);
  }

  public static void stream(StaticEventProcessor target, File input) throws IOException {
    CsvRecordMarshaller marshaller = marshaller();
    marshaller.handleEvent(new RegisterEventHandler(target));
    CharStreamer.stream(input, marshaller).sync().stream();
    target.onEvent(EofEvent.EOF);
  }

  public static void stream(StaticEventProcessor target, File input, LogService validationLogger)
      throws IOException {
    CsvRecordMarshaller marshaller = marshaller();
    marshaller.init();
    marshaller.handleEvent(new RegisterEventHandler(target));
    marshaller.handleEvent(LogControlEvent.setLogService(validationLogger));
    CharStreamer.stream(input, marshaller).noInit().sync().stream();
    target.onEvent(EofEvent.EOF);
  }
}
