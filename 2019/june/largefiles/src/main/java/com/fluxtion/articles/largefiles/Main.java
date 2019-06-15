package com.fluxtion.articles.largefiles;

import com.fluxtion.ext.streaming.api.stream.ListCollector;
import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import com.fluxtion.ext.streaming.api.Wrapper;
import static com.fluxtion.ext.streaming.api.group.AggregateFunctions.Count;
import com.fluxtion.ext.streaming.api.group.GroupBy;
import static com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.subSeqBefore;
import static com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.subSequence;
import static com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.subString;
import static com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.subStringBefore;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.equal;
import static com.fluxtion.ext.streaming.api.util.GroupByPrint.printFrequencyMap;
import static com.fluxtion.ext.streaming.builder.event.EventSelect.select;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.count;
import com.fluxtion.ext.text.api.csv.RowProcessor;
import com.fluxtion.ext.text.api.event.EofEvent;
import com.fluxtion.ext.text.api.util.CharStreamer;
import com.fluxtion.ext.text.builder.csv.CharTokenConfig;
import static com.fluxtion.ext.text.builder.csv.CsvMarshallerBuilder.csvMarshaller;
import java.io.File;
import java.util.List;
import static com.fluxtion.ext.streaming.api.util.GroupByPrint.printTopN;

/**
 *
 * @author V12 Technology Ltd.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CharStreamer.stream(new File("C:\\Users\\gregp\\Downloads\\indiv18\\itcont.txt"),
                (Class<com.fluxtion.api.lifecycle.EventHandler>) Class.forName("com.fluxtion.articles.largefiles.generated.VoterProcessor"))
                .sync().stream();
    }

    @SepBuilder(name = "VoterProcessor",
            packageName = "com.fluxtion.articles.largefiles.generated",
            outputDir = "src/main/java",
            cleanOutputDir = true
    )
    public void buildLambda(SEPConfig cfg) {
        RowProcessor<Voter> voter = csvMarshaller(Voter.class, 0)
                .map(4, Voter::setDateString, subString(0, 6))
                .map(7, Voter::setFirstName, subStringBefore(','))
                .map(7, Voter::setFullName)
                .tokenConfig(new CharTokenConfig('\n', '|', '\r')).build();
        //calcs and printing
        Wrapper<EofEvent> eofTrigger = select(EofEvent.class);
        eofTrigger.map(e -> "").console("Results\n--------------");
        //names
        Wrapper<Number> nameCount = voter.map(count());
        nameCount.notifierOverride(eofTrigger).console("count:", Number::intValue);
//        voter.notifierOverride(nameCount.filter(equal(3))).console("3rd name:", Voter::getFullName);
//        voter.notifierOverride(nameCount.filter(equal(46565))).console("46565th name:", Voter::getFullName);
        //stats
//        printTopN("\ngroupByName:\n-------------", 3, voter.group(Voter::getFirstName, Voter::hashCode, Count), eofTrigger);
//        printFrequencyMap("\ngroupByDate:\n-------------", voter.group(Voter::getDateString, Voter::hashCode, Count), eofTrigger);

    }



}
