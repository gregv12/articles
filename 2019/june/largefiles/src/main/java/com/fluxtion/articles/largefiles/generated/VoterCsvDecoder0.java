package com.fluxtion.articles.largefiles.generated;

import com.fluxtion.api.annotations.Config;
import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.Inject;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.articles.largefiles.Voter;
import com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.SubSeq;
import com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.SubSeqBefore;
import com.fluxtion.ext.streaming.api.util.CharArrayCharSequence;
import com.fluxtion.ext.streaming.api.util.CharArrayCharSequence.CharSequenceView;
import com.fluxtion.ext.text.api.csv.RowProcessor;
import com.fluxtion.ext.text.api.csv.ValidationLogger;
import com.fluxtion.ext.text.api.event.CharEvent;
import com.fluxtion.ext.text.api.event.EofEvent;
import java.util.HashMap;

/**
 * Fluxtion generated CSV decoder.
 *
 * <p>target class : Voter
 *
 * @author Greg Higgins
 */
public class VoterCsvDecoder0 implements RowProcessor<Voter> {

  @Inject
  @Config(key = "id", value = "validationLog")
  @PushReference
  public ValidationLogger errorLog;
  //buffer management
  private final char[] chars = new char[4096];
  private final int[] delimIndex = new int[1024];
  private final CharArrayCharSequence seq = new CharArrayCharSequence(chars);
  private int fieldIndex = 0;
  private int writeIndex = 0;
  //target
  private Voter target;
  //source field index: 4
  private final CharSequenceView setDateString = seq.view();
  private final int fieldIndex_4 = 4;
  //source field index: 7
  private final CharSequenceView setFirstName = seq.view();
  private final int fieldIndex_7 = 7;
  //source field index: 7
  private final CharSequenceView setFullName = seq.view();
  //processing state and meta-data
  @NoEventReference public SubSeq subSeq_0;
  @NoEventReference public SubSeqBefore subSeqBefore_1;
  private int rowNumber;
  private final HashMap fieldMap = new HashMap<>();
  private boolean passedValidation;

  @EventHandler
  public boolean charEvent(CharEvent event) {
    final char character = event.getCharacter();
    passedValidation = true;
    if (character == '\r') {
      return false;
    }
    if (character == '\n') {
      return processRow();
    }
    if (character == '|') {
      updateFieldIndex();
    }
    chars[writeIndex++] = character;
    return false;
  }

  @EventHandler
  public boolean eof(EofEvent eof) {
    return writeIndex == 0 ? false : processRow();
  }

  private boolean processRow() {
    boolean targetChanged = false;
    rowNumber++;
    //updateTarget();
    targetChanged = updateTarget();
    writeIndex = 0;
    fieldIndex = 0;
    return targetChanged;
  }

  private boolean updateTarget() {
    try {
      updateFieldIndex();
      fieldIndex = fieldIndex_4;
      setDateString.subSequenceNoOffset(delimIndex[fieldIndex_4], delimIndex[fieldIndex_4 + 1] - 1);
      target.setDateString(subSeq_0.subSequence(setDateString));

      fieldIndex = fieldIndex_7;
      setFirstName.subSequenceNoOffset(delimIndex[fieldIndex_7], delimIndex[fieldIndex_7 + 1] - 1);
      target.setFirstName(subSeqBefore_1.subSequence(setFirstName));

      fieldIndex = fieldIndex_7;
      setFullName.subSequenceNoOffset(delimIndex[fieldIndex_7], delimIndex[fieldIndex_7 + 1] - 1);
      target.setFullName(setFullName);

    } catch (Exception e) {
      logException("problem pushing data from row:", false, e);
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
        .append(prefix)
        .append(rowNumber)
        .append(" fieldIndex:")
        .append(fieldIndex)
        .append(" targetMethod:")
        .append(fieldMap.get(fieldIndex));
    if (fatal) {
      errorLog.logFatal("");
      throw new RuntimeException(errorLog.getSb().toString(), e);
    }
    errorLog.logError("");
  }

  private void logHeaderProblem(String prefix, boolean fatal, Exception e) {
    errorLog.getSb().append(prefix).append(rowNumber);
    if (fatal) {
      errorLog.logFatal("");
      throw new RuntimeException(errorLog.getSb().toString(), e);
    }
    errorLog.logError("");
  }

  @Override
  public Voter event() {
    return target;
  }

  @Override
  public Class<Voter> eventClass() {
    return Voter.class;
  }

  @Initialise
  public void init() {
    target = new Voter();
    fieldMap.put(fieldIndex_4, "setDateString");
    fieldMap.put(fieldIndex_7, "setFirstName");
    fieldMap.put(fieldIndex_7, "setFullName");
  }

  @Override
  public boolean passedValidation() {
    return passedValidation;
  }

  @Override
  public int getRowNumber() {
    return rowNumber;
  }
}
