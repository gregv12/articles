package com.fluxtion.articles.fxportfolio;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.api.annotations.Config;
import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.Inject;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.articles.fxportfolio.event.LimitConfig;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
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
import com.fluxtion.ext.text.api.util.marshaller.CsvRecordMarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.fluxtion.ext.text.api.ascii.Conversion.*;
import static com.fluxtion.ext.text.api.csv.Converters.*;
import com.fluxtion.ext.text.api.util.StringDriver;

/**
 * Fluxtion generated CSV decoder.
 *
 * <p>target class : LimitConfig
 *
 * @author Greg Higgins
 */
public class LimitConfigCsvDecoder implements RowProcessor<LimitConfig> {

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
  private LimitConfig target;
  //source field index: -1
  private final CharSequenceView setCcy = seq.view();
  private int fieldName_ccy = -1;
  //source field index: -1
  private final CharSequenceView setLimit = seq.view();
  private int fieldName_limit = -1;
  //source field index: -1
  private final CharSequenceView setTarget = seq.view();
  private int fieldName_target = -1;
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
    if (chars[0] != '#') {
      if (HEADER_ROWS < rowNumber & writeIndex > 0) {
        targetChanged = updateTarget();
      } else if (rowNumber == MAPPING_ROW) {
        mapHeader();
      }
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
    fieldName_ccy = headers.indexOf("ccy");
    fieldMap.put(fieldName_ccy, "setCcy");
    if (fieldName_ccy < 0) {
      logHeaderProblem("problem mapping field:'ccy' missing column header, index row:", true, null);
    }
    fieldName_limit = headers.indexOf("limit");
    fieldMap.put(fieldName_limit, "setLimit");
    if (fieldName_limit < 0) {
      logHeaderProblem(
          "problem mapping field:'limit' missing column header, index row:", true, null);
    }
    fieldName_target = headers.indexOf("target");
    fieldMap.put(fieldName_target, "setTarget");
    if (fieldName_target < 0) {
      logHeaderProblem(
          "problem mapping field:'target' missing column header, index row:", true, null);
    }
  }

  private boolean updateTarget() {
    try {
      updateFieldIndex();
      fieldIndex = fieldName_ccy;
      setCcy.subSequenceNoOffset(delimIndex[fieldName_ccy], delimIndex[fieldName_ccy + 1] - 1);
      target.setCcy(Ccy.valueOf(setCcy.toString()));

      fieldIndex = fieldName_limit;
      setLimit.subSequenceNoOffset(
          delimIndex[fieldName_limit], delimIndex[fieldName_limit + 1] - 1);
      target.setLimit(atoi(setLimit));

      fieldIndex = fieldName_target;
      setTarget.subSequenceNoOffset(
          delimIndex[fieldName_target], delimIndex[fieldName_target + 1] - 1);
      target.setTarget(atoi(setTarget));

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
        .append("LimitConfigCsvDecoder0 ")
        .append(prefix)
        .append(" fieldIndex:'")
        .append(fieldIndex)
        .append("' targetMethod:'LimitConfig#")
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
    errorLog.getSb().append("LimitConfigCsvDecoder0 ").append(prefix).append(rowNumber);
    if (fatal) {
      errorLog.logFatal("");
      throw new RuntimeException(errorLog.getSb().toString(), e);
    }
    errorLog.logError("");
  }

  @Override
  public LimitConfig event() {
    return target;
  }

  @Override
  public Class<LimitConfig> eventClass() {
    return LimitConfig.class;
  }

  @Initialise
  @Override
  public void init() {
    target = new LimitConfig();
    fieldMap.put(fieldName_ccy, "setCcy");
    fieldMap.put(fieldName_limit, "setLimit");
    fieldMap.put(fieldName_target, "setTarget");
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
    out += "ccy,";
    out += "limit,";
    out += "target";
    return out;
  }

  public static void asCsv(LimitConfig src, StringBuilder msgSink) throws IOException {
    msgSink.append(src.getCcy());
    msgSink.append(",");
    msgSink.append(src.getLimit());
    msgSink.append(",");
    msgSink.append(src.getTarget());
    msgSink.append("\n");
  }

  public static void asCsv(LimitConfig src, Appendable target) throws IOException {
    StringBuilder sb = new StringBuilder();
    asCsv(src, sb);
    target.append(sb);
  }

  @Override
  public String csvHeaders() {
    return csvHeader();
  }

  @Override
  public Appendable toCsv(LimitConfig src, Appendable target) throws IOException {
    msgSink.setLength(0);
    asCsv(src, msgSink);
    target.append(msgSink);
    msgSink.setLength(0);
    return target;
  }

  public static CsvRecordMarshaller marshaller() {
    return new CsvRecordMarshaller(new LimitConfigCsvDecoder());
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
