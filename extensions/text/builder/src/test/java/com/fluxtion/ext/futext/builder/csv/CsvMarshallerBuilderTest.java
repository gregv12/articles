/* 
 * Copyright (C) 2018 V12 Technology Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program.  If not, see 
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.ext.futext.builder.csv;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.builder.node.SEPConfig;
import com.fluxtion.ext.streaming.api.Wrapper;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.count;
import com.fluxtion.ext.streaming.builder.log.LogBuilder;
import com.fluxtion.ext.text.api.csv.RowProcessor;
import com.fluxtion.ext.text.api.csv.RulesEvaluator;
import com.fluxtion.ext.text.api.event.RegisterEventHandler;
import com.fluxtion.ext.text.builder.csv.CharTokenConfig;
import static com.fluxtion.ext.text.builder.csv.CsvMarshallerBuilder.csvMarshaller;
import static com.fluxtion.ext.text.builder.csv.FixedLenMarshallerBuilder.fixedLenMarshaller;
import static com.fluxtion.ext.text.builder.csv.RulesEvaluatorBuilder.validator;
import com.fluxtion.ext.text.api.util.StringDriver;
import static com.fluxtion.generator.compiler.InprocessSepCompiler.sepTestInstance;
import com.fluxtion.generator.util.BaseSepTest;
import java.util.concurrent.atomic.LongAdder;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author gregp
 */
public class CsvMarshallerBuilderTest extends BaseSepTest {
//
//    @Override
//    protected String testPackageID() {
//        return "";
//    }

    @Test
    public void testCsvNoHeader() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCitiesCsvCfg.class);
        WorldCity city = ((Wrapper<WorldCity>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        String dataCsh = "mexico,aixas,Aixàs,06,,42,1.4666667\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "brazil,santiago,Aixirivall,06,,130,1.5\n";
        StringDriver.streamChars(dataCsh, sep, false);

        Assert.assertEquals(3, count.intValue());
        Assert.assertThat("brazil", is(city.getCountry()));
        Assert.assertThat("santiago", is(city.getCity().toString()));
        Assert.assertEquals(130, city.getLongitude(), 0.1);
        Assert.assertThat("1.5", is(city.getLatitudeCharSequence().toString()));
        //none escaped parsing
        dataCsh = "af,dekh\"yaki-lal'begi-dzhanubi,Dekh\"yaki-Lal'begi-Dzhanubi,39,,32.561313,65.864451\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(32.561313, city.getLongitude(), 0.00001);
        Assert.assertThat("65.864451", is(city.getLatitudeCharSequence().toString()));

    }

    @Test
    public void testCsvNoHeader_Trim() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCitiesTrimCsvCfg.class);
        WorldCity city = ((Wrapper<WorldCity>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        String dataCsh = "mexico,aixas,Aixàs,06,,42,1.4666667\n"
                + "mexico,    aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "   brazil   ,santiago,Aixirivall,06,,130,1.5\n";
        StringDriver.streamChars(dataCsh, sep, false);

        Assert.assertEquals(3, count.intValue());
        Assert.assertThat("brazil", is(city.getCountry()));
        Assert.assertThat("santiago", is(city.getCity().toString()));
        Assert.assertEquals(130, city.getLongitude(), 0.1);
        Assert.assertThat("1.5", is(city.getLatitudeCharSequence().toString()));

    }

    @Test
    public void testCsvNoHeader_Trim_skipEmtyLines() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCitiesTrimSkipEmptyCsvCfg.class);
        WorldCity city = ((Wrapper<WorldCity>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        String dataCsh = "mexico,aixas,Aixàs,06,,42,1.4666667\n"
                + "\n"
                + "mexico,    aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "   brazil   ,santiago,Aixirivall,06,,130,1.5\n";
        StringDriver.streamChars(dataCsh, sep, false);

        Assert.assertEquals(3, count.intValue());
        Assert.assertThat("brazil", is(city.getCountry()));
        Assert.assertThat("santiago", is(city.getCity().toString()));
        Assert.assertEquals(130, city.getLongitude(), 0.1);
        Assert.assertThat("1.5", is(city.getLatitudeCharSequence().toString()));

    }

    @Test
    public void testCsvWithHeaderManualMapping() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCitiesCsv_Header_1_Cfg.class);
        WorldCity city = ((Wrapper<WorldCity>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        String dataCsh = "country,city,accent city,region,population,longitude,latitude\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "brazil,santiago,Aixirivall,06,,130,1.5\n";
        StringDriver.streamChars(dataCsh, sep, false);

        Assert.assertEquals(2, count.intValue());
        Assert.assertThat("brazil", is(city.getCountry()));
        Assert.assertThat("santiago", is(city.getCity().toString()));
        Assert.assertEquals(130, city.getLongitude(), 0.1);
        Assert.assertThat("1.5", is(city.getLatitudeCharSequence().toString()));
    }

    @Test
    public void testCsvWithAutoBeanMapping() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCityBean_Header.class);
        WorldCityBean city = ((Wrapper<WorldCityBean>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        String dataCsh = "Country,City,AccentCity,Region,Population,Latitude,Longitude\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "brazil,santiago,Aixirivall,06,,130,1.5\n";
        StringDriver.streamChars(dataCsh, sep, false);

        Assert.assertEquals(2, count.intValue());
        Assert.assertThat("brazil", is(city.getCountry()));
        Assert.assertThat("santiago", is(city.getCity().toString()));
        Assert.assertEquals("1.5", city.getLongitude().toString());
        Assert.assertThat("130", is(city.getLatitude().toString()));
    }

    @Test(expected = RuntimeException.class)
    public void testCsvWithAutoBeanMappingBadHeader() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCityBean_Header.class);
        String dataCsh = "Country,City,Latitude,Longitude\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "brazil,santiago,Aixirivall,06,,130,1.5\n";
        StringDriver.streamChars(dataCsh, sep, false);
    }

    @Test
    public void testCsvWithAutoBeanMappingTransientHeader() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCityBeanTransient_Header.class);
        String dataCsh = "Country,City,population\n"
                + "mexico,aixirivali,5000\n"
                + "brazil,santiago,20000\n";
        StringDriver.streamChars(dataCsh, sep, false);
    }

    @Test
    public void testCsvWithAutoBeanMappingTransientHeaderInline() throws Exception {
        StaticEventProcessor sep  = sepTestInstance((c) -> csvMarshaller(WorldCityBeanTransient.class)
                .addEventPublisher().build()
                , pckg, className);
        WorldCityBeanTransient[] city = new WorldCityBeanTransient[1];
        LongAdder count = new LongAdder();
        sep.onEvent(new RegisterEventHandler((e) ->{
            city[0] = (WorldCityBeanTransient) e;
            count.increment();
        }));
        String dataCsh = "Country,City,population\n"
                + "mexico,aixirivali,5000\n"
                + "brazil,santiago,20000\n";
        StringDriver.streamChars(dataCsh, sep, false);
        assertThat(count.intValue(), is(2));
        assertThat(city[0].getCountry(), is("brazil"));
        assertThat(city[0].getAccentCity(), is("none provided"));
    }

    @Test
    public void testCsvWithAutoBeanMapping_UseEof() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCityBean_Header.class);
        WorldCityBean city = ((Wrapper<WorldCityBean>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        String dataCsh = "Country,City,AccentCity,Region,Population,Latitude,Longitude\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "brazil,santiago,Aixirivall,06,,130,1.5";
        StringDriver.streamChars(dataCsh, sep, false);

        Assert.assertEquals(2, count.intValue());
        Assert.assertThat("brazil", is(city.getCountry()));
        Assert.assertThat("santiago", is(city.getCity().toString()));
        Assert.assertEquals("1.5", city.getLongitude().toString());
        Assert.assertThat("130", is(city.getLatitude().toString()));
    }

    @Test
    public void testCsvWithMappingHeader() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCitiesCsv_MappingNameHeader.class);
        WorldCity city = ((Wrapper<WorldCity>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        String dataCsh = "country,city,accent city,region,population,longitude,latitude\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "brazil,santiago,Aixirivall,06,,130,1.5\n";
        StringDriver.streamChars(dataCsh, sep, false);

        Assert.assertEquals(2, count.intValue());
        Assert.assertThat("brazil", is(city.getCountry()));
        Assert.assertThat("santiago", is(city.getCity().toString()));
        Assert.assertEquals(130, city.getLongitude(), 0.1);
        Assert.assertThat("1.5", is(city.getLatitudeCharSequence().toString()));
    }

    @Test
    public void testCsvWindowsWithMappingHeader() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCitiesCsv_MappingNameHeaderWindows.class);
        WorldCity city = ((Wrapper<WorldCity>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        String dataCsh = "country,city,accent city,region,population,longitude,latitude\r\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\r\n"
                + "brazil,santiago,Aixirivall,06,,130,1.5\r\n";
        StringDriver.streamChars(dataCsh, sep, false);

        Assert.assertEquals(2, count.intValue());
        Assert.assertThat("brazil", is(city.getCountry()));
        Assert.assertThat("santiago", is(city.getCity().toString()));
        Assert.assertEquals(130, city.getLongitude(), 0.1);
        Assert.assertThat("1.5", is(city.getLatitudeCharSequence().toString()));
    }

    @Test
    public void testCsvWithHeaderSkipCommentSkipEmpty() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCitiesCsv_Header_SkipEmpty_SkipComments.class);
        WorldCity city = ((Wrapper<WorldCity>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        String dataCsh = "country,city,accent city,region,population,longitude,latitude\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "\n"
                + "#dfff,rgrtrt,rtr,tr,tr,tr,tr,tr,t,rt,rt,r,tr,tr,t\n"
                + "brazil,santiago#,Aixirivall,06,,130,1.5\n"
                ;
//                + "#mexico,aixirivali,Aixirivali,06,,25.19,1.5\n";
        StringDriver.streamChars(dataCsh, sep, false);

        Assert.assertEquals(2, count.intValue());
        Assert.assertThat(city.getCountry(), is("brazil"));
        Assert.assertThat(city.getCity().toString(), is("santiago#"));
        Assert.assertEquals(130, city.getLongitude(), 0.1);
        Assert.assertThat(city.getLatitudeCharSequence().toString(), is("1.5"));
    }

    @Test
    public void testCsvWithHeaderAndConverter() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCitiesCsv_Header_and_Converter_Cfg.class);
        WorldCity city = ((Wrapper<WorldCity>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        String dataCsh = "country,city,accent city,region,population,longitude,latitude\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "brazil,santiago,Aixirivall,06,,130,1.5\n";
        StringDriver.streamChars(dataCsh, sep, false);

        Assert.assertEquals(2, count.intValue());
        Assert.assertThat("brazil", is(city.getCountry()));
        Assert.assertThat("santiago", is(city.getCity().toString()));
        Assert.assertEquals(1.0, city.getLongitude(), 0.1);
        Assert.assertEquals(0.0, city.getLatitude(), 0.1);
        Assert.assertThat("1.5", is(city.getLatitudeCharSequence().toString()));
    }

    //errors ar eno longer thrown on row level problems - caught and sent to the 
    //logger and marked as failed validation
    public void testCsvWithHeaderAndError() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCitiesCsv_Header_1_Cfg.class);
        String dataCsh = "country,city,accent city,region,population,longitude,latitude\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "brazil,santiago,Aixirivall,06,,16*90,1.5\n";
        StringDriver.streamChars(dataCsh, sep, false);
    }

    @Test
    public void testCsvWithHeaderAndRowCB() {
        buildAndInitSep(WorldCitiesCsv_Header_OnEventCB.class);
        String dataCsh = "country,city,accent city,region,population,longitude,latitude\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "brazil,santiago,Aixirivall,06,,130,1.5\n";
        WorldCityOnEvent city = ((Wrapper<WorldCityOnEvent>) getField("city")).event();
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(2, city.parse);
        Assert.assertEquals(2, city.postProcess);
    }

    @Test
//    @Ignore
    //To be replaced with external validator test. Validation has moved from
    //the OnEvent callback method
    public void testCsvWithHeaderAndRowCBFailedValidation() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCitiesCsv_Header_OnEventCB_Validator.class);
        Number count = ((Wrapper<Number>)getField("count")).event();
        Number failedValidationcount = ((Wrapper<Number>)getField("failedValidationCount")).event();
        String dataCsh = "country,city,accent city,region,population,longitude,latitude\n"
                + "mexico,aixirivali,Aixirivali,06,,25.19,1.5\n"
                + "brazil,santiago,Aixirivall,06,,130,1.5\n"
                + "#next line fails validation - missing population\n"
                + "GB,trax,Aixirivall,06,,-130,1.5\n";
        WorldCityOnEvent city = ((Wrapper<WorldCityOnEvent>) getField("city")).event();
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(3, city.parse);
        Assert.assertEquals(3, city.postProcess);
        Assert.assertEquals(3, count.intValue());
        Assert.assertEquals(0, failedValidationcount.intValue());
    }

    @Test
    //To be replaced with external validator test. Validation has moved from
    //the OnEvent callback method
//    @Ignore
    public void testFixedLen() {
        final StaticEventProcessor sep = buildAndInitSep(WorldCityFixedLen.class);
        String dataCsh
                = "country   city      long    lat     \n"
                + "mexico    aixirival    25.25 1.5     \n"
                + "UK        London     51.5     -52     \n"
                + "\n"
                + "#comment line\n"
                + "brazil    sao paolo  123.5    35.78 \n";
        WorldCityOnEvent city = ((Wrapper<WorldCityOnEvent>) getField("city")).event();
        Number count = ((Wrapper<Number>)getField("count")).event();
        Number failedValidationcount = ((Wrapper<Number>)getField("failedValidationCount")).event();
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(3, city.parse);
        Assert.assertEquals(3, city.postProcess);
        Assert.assertEquals(3, count.intValue());
        Assert.assertEquals(0, failedValidationcount.intValue());
    }

    @Test
    public void testEscapedCsvCarAd() {
        final StaticEventProcessor sep = buildAndInitSep(CarAd_Header.class);
        CarAd carAd = ((Wrapper<CarAd>) getField("carAd")).event();
        String dataCsh = "Year,Make,Model,Description,Price\n"
                + "1997,Ford,E350,\"ac, abs, moon\",3000.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1997, carAd.getYear());
        Assert.assertEquals("Ford", carAd.getMake());
        Assert.assertEquals("E350", carAd.getModel());
        Assert.assertEquals("ac, abs, moon", carAd.getDescription());
        Assert.assertEquals(3000.00, carAd.getPrice(), 0.001);

        dataCsh = "1999,Chevy,\"Venture \"\"Extended Edition\"\"\",\"\",4900.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1999, carAd.getYear());
        Assert.assertEquals("Chevy", carAd.getMake());
        Assert.assertEquals("Venture \"Extended Edition\"", carAd.getModel());
        Assert.assertEquals("", carAd.getDescription());
        Assert.assertEquals(4900.00, carAd.getPrice(), 0.001);

        dataCsh = "1999,Chevy,\"Venture \"\"Extended Edition, Very Large\"\"\",,5000.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1999, carAd.getYear());
        Assert.assertEquals("Chevy", carAd.getMake());
        Assert.assertEquals("Venture \"Extended Edition, Very Large\"", carAd.getModel());
        Assert.assertEquals("", carAd.getDescription());
        Assert.assertEquals(5000.00, carAd.getPrice(), 0.001);

        dataCsh = "1996,Jeep,Grand Cherokee,\"MUST SELL!\n"
                + "air, moon roof, loaded\",4799.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1996, carAd.getYear());
        Assert.assertEquals("Jeep", carAd.getMake());
        Assert.assertEquals("Grand Cherokee", carAd.getModel());
        Assert.assertEquals("MUST SELL!\nair, moon roof, loaded", carAd.getDescription());
        Assert.assertEquals(4799.00, carAd.getPrice(), 0.001);

    }

    @Test
    public void testEscapedCsvCarAdPartials() {
        final StaticEventProcessor sep = buildAndInitSep(CarAd_Header_Partial.class);
        CarAd carAd = ((Wrapper<CarAd>) getField("carAd")).event();
        String dataCsh = "Year,Make,Model,Description,Price\n"
                + "1997,Ford,E350,\"ac, abs, moon\",3000.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1997, carAd.getYear());
        Assert.assertEquals("Ford", carAd.getMake());
        Assert.assertEquals("E350", carAd.getModel());
        Assert.assertEquals("ac, abs, moon", carAd.getDescription());
        Assert.assertEquals(3000.00, carAd.getPrice(), 0.001);

        dataCsh = "1999,Chevy,\"Venture \"\"Extended Edition\"\"\",\"\"\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1999, carAd.getYear());
        Assert.assertEquals("Chevy", carAd.getMake());
        Assert.assertEquals("Venture \"Extended Edition\"", carAd.getModel());
        Assert.assertEquals("", carAd.getDescription());
        //this is a partial uses the previous result
        Assert.assertEquals(3000.00, carAd.getPrice(), 0.001);

        dataCsh = "1999,Chevy,\"Venture \"\"Extended Edition, Very Large\"\"\",,5000.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1999, carAd.getYear());
        Assert.assertEquals("Chevy", carAd.getMake());
        Assert.assertEquals("Venture \"Extended Edition, Very Large\"", carAd.getModel());
        Assert.assertEquals("", carAd.getDescription());
        Assert.assertEquals(5000.00, carAd.getPrice(), 0.001);

        dataCsh = "1996,Jeep,Grand Cherokee,\"MUST SELL!\n"
                + "air, moon roof, loaded\",4799.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1996, carAd.getYear());
        Assert.assertEquals("Jeep", carAd.getMake());
        Assert.assertEquals("Grand Cherokee", carAd.getModel());
        Assert.assertEquals("MUST SELL!\nair, moon roof, loaded", carAd.getDescription());
        Assert.assertEquals(4799.00, carAd.getPrice(), 0.001);

    }

    @Test
    public void testEscapedCsvCarAdBean() {
        final StaticEventProcessor sep = buildAndInitSep(CarAdBean_Header.class);
        CarAd carAd = ((Wrapper<CarAd>) getField("carAd")).event();
        String dataCsh = "year,make,model,description,price\n"
                + "1997,Ford,E350,\"ac, abs, moon\",3000.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1997, carAd.getYear());
        Assert.assertEquals("Ford", carAd.getMake());
        Assert.assertEquals("E350", carAd.getModel());
        Assert.assertEquals("ac, abs, moon", carAd.getDescription());
        Assert.assertEquals(3000.00, carAd.getPrice(), 0.001);

        dataCsh = "1999,Chevy,\"Venture \"\"Extended Edition\"\"\",\"\",4900.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1999, carAd.getYear());
        Assert.assertEquals("Chevy", carAd.getMake());
        Assert.assertEquals("Venture \"Extended Edition\"", carAd.getModel());
        Assert.assertEquals("", carAd.getDescription());
        Assert.assertEquals(4900.00, carAd.getPrice(), 0.001);

        dataCsh = "1999,Chevy,\"Venture \"\"Extended Edition, Very Large\"\"\",,5000.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1999, carAd.getYear());
        Assert.assertEquals("Chevy", carAd.getMake());
        Assert.assertEquals("Venture \"Extended Edition, Very Large\"", carAd.getModel());
        Assert.assertEquals("", carAd.getDescription());
        Assert.assertEquals(5000.00, carAd.getPrice(), 0.001);

        dataCsh = "1996,Jeep,Grand Cherokee,\"MUST SELL!\n"
                + "air, moon roof, loaded\",4799.00\n";
        StringDriver.streamChars(dataCsh, sep, false);
        Assert.assertEquals(1996, carAd.getYear());
        Assert.assertEquals("Jeep", carAd.getMake());
        Assert.assertEquals("Grand Cherokee", carAd.getModel());
        Assert.assertEquals("MUST SELL!\nair, moon roof, loaded", carAd.getDescription());
        Assert.assertEquals(4799.00, carAd.getPrice(), 0.001);

    }

    public static class WorldCitiesCsvCfg extends SEPConfig {

        {
            Wrapper<WorldCity> city = csvMarshaller(WorldCity.class, 0)
                    .map(0, WorldCity::setCountry)
                    .map(1, WorldCity::setCity)
                    .map(5, WorldCity::setLongitude)
                    .trim(5)
                    .map(6, WorldCity::setLatitudeCharSequence)
                    .trim(6)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
            maxFiltersInline = 25;
        }

    }

    public static class WorldCitiesTrimCsvCfg extends SEPConfig {

        {
            Wrapper<WorldCity> city = csvMarshaller(WorldCity.class, 0)
                    .map(0, WorldCity::setCountry)
                    .trim(0)
                    .map(1, WorldCity::setCity)
                    .trim(1)
                    .map(5, WorldCity::setLongitude)
                    .map(6, WorldCity::setLatitudeCharSequence)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
            maxFiltersInline = 25;
        }

    }

    public static class WorldCitiesTrimSkipEmptyCsvCfg extends SEPConfig {

        {
            Wrapper<WorldCityOnEvent> city = csvMarshaller(WorldCityOnEvent.class, 0)
                    .map(0, WorldCity::setCountry)
                    .trim(0)
                    .map(1, WorldCity::setCity)
                    .trim(1)
                    .map(5, WorldCity::setLongitude)
                    .map(6, WorldCity::setLatitudeCharSequence)
                    .map(6, WorldCity::setLatitude)
                    .skipEmptyLines(true)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
            maxFiltersInline = 25;
        }

    }

    public static class WorldCitiesCsv_Header_1_Cfg extends SEPConfig {

        {
            Wrapper<WorldCity> city = csvMarshaller(WorldCity.class, 0)
                    .map(0, WorldCity::setCountry)
                    .map(1, WorldCity::setCity)
                    .map(5, WorldCity::setLongitude)
                    .map(6, WorldCity::setLatitudeCharSequence)
                    .headerLines(1)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
            maxFiltersInline = 25;
        }

    }

    public static class WorldCitiesCsv_MappingNameHeader extends SEPConfig {

        /**
         * country,city,accent city,region,population,longitude,latitude
         *
         * The name mapping overrides the row header value of 0 and expects the
         * first row to be a mapping row
         */
        {
            Wrapper<WorldCity> city = csvMarshaller(WorldCity.class, 0)
                    .map("country", WorldCity::setCountry)
                    .map(1, WorldCity::setCity)
                    .map("longitude", WorldCity::setLongitude)
                    .map(6, WorldCity::setLatitudeCharSequence)
                    //                    .headerLines(1)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
            maxFiltersInline = 25;
        }

    }

    public static class WorldCitiesCsv_MappingNameHeaderWindows extends SEPConfig {
//country,city,accent city,region,population,longitude,latitude

        {
            Wrapper<WorldCity> city = csvMarshaller(WorldCity.class, 0)
                    .tokenConfig(CharTokenConfig.WINDOWS)
                    .map("country", WorldCity::setCountry)
                    .map(1, WorldCity::setCity)
                    .map("longitude", WorldCity::setLongitude)
                    .map(6, WorldCity::setLatitudeCharSequence)
                    //                    .headerLines(1)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
            maxFiltersInline = 25;
        }

    }

    public static class WorldCitiesCsv_Header_SkipEmpty_SkipComments extends SEPConfig {

        {
            Wrapper<WorldCityOnEvent> city = csvMarshaller(WorldCityOnEvent.class, 0)
                    .map(0, WorldCity::setCountry)
                    .map(1, WorldCity::setCity)
                    .map(5, WorldCity::setLongitude)
                    .map(6, WorldCity::setLatitudeCharSequence)
                    .map(6, WorldCity::setLatitude)
                    .skipEmptyLines(true)
                    .skipCommentLines(true)
                    .headerLines(1)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
            maxFiltersInline = 25;
        }

    }

    public static class WorldCitiesCsv_Header_and_Converter_Cfg extends SEPConfig {

        {
            MyFunctions functions = new MyFunctions();
            Wrapper<WorldCity> city = csvMarshaller(WorldCity.class, 0)
                    .map(0, WorldCity::setCountry)
                    .map(1, WorldCity::setCity)
                    .map(5, WorldCity::setLongitude)
                    .converter(5, CsvMarshallerBuilderTest::always_1)
                    .map(6, WorldCity::setLatitude)
                    .map(6, WorldCity::setLatitudeCharSequence)
                    .converter(6, functions::always_Zero)
                    .map("population", WorldCity::setPopulation)
                    .converter("population", functions::always_100)
                    .headerLines(1)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
            maxFiltersInline = 25;
        }

    }

    public static class WorldCitiesCsv_Header_OnEventCB extends SEPConfig {

        {
            Wrapper<WorldCityOnEvent> city = csvMarshaller(WorldCityOnEvent.class, 0)
                    .map(0, WorldCity::setCountry)
                    .map(1, WorldCity::setCity)
                    .map(5, WorldCity::setLongitude)
                    .map(6, WorldCity::setLatitudeCharSequence)
                    .map(6, WorldCity::setLatitude)
                    .headerLines(1)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
            maxFiltersInline = 25;
        }

    }

    public static class WorldCitiesCsv_Header_OnEventCB_Validator extends SEPConfig {

        {
            RowProcessor<WorldCityOnEvent> city = csvMarshaller(WorldCityOnEvent.class, 0)
                    .map(0, WorldCity::setCountry)
                    .map(1, WorldCity::setCity)
                    .map(5, WorldCity::setLongitude)
                    .map(6, WorldCity::setLatitudeCharSequence)
                    .map(6, WorldCity::setLatitude)
                    .headerLines(1)
                    .skipCommentLines(true)
                    //                    .validationListener(failureListener)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");

            //validator count
            RulesEvaluator<WorldCityOnEvent> validator = validator(city).build();
            addPublicNode(count(validator.passedNotifier()), "countPassed");
            addPublicNode(count(validator.failedNotifier()), "failedValidationCount");
            maxFiltersInline = 25;
        }
    }

    public static class CarAd_Header extends SEPConfig {

        //Year,Make,Model,Description,Price
        {
            Wrapper<CarAd> city = csvMarshaller(CarAd.class, 0)
                    .map(0, CarAd::setYear)
                    .map(1, CarAd::setMake)
                    .map(2, CarAd::setModel)
                    .map(3, CarAd::setDescription)
                    .map(4, CarAd::setPrice)
                    .headerLines(1)
                    .processEscapeSequences(true)
                    .build();
            addPublicNode(city, "carAd");
        }

    }

    public static class CarAd_Header_Partial extends SEPConfig {

        //Year,Make,Model,Description,Price
        {
            Wrapper<CarAd> city = csvMarshaller(CarAd.class, 0)
                    .map(0, CarAd::setYear)
                    .map(1, CarAd::setMake)
                    .map(2, CarAd::setModel)
                    .map(3, CarAd::setDescription)
                    .map(4, CarAd::setPrice)
                    .headerLines(1)
                    .acceptPartials(true)
                    .processEscapeSequences(true)
                    .build();
            addPublicNode(city, "carAd");
        }

    }

    public static class CarAdBean_Header extends SEPConfig {

        //Year,Make,Model,Description,Price
        {
            Wrapper<CarAd> city = csvMarshaller(CarAd.class)
                    //                    .map(CarAd.class)
                    .processEscapeSequences(true)
                    .build();
            addPublicNode(city, "carAd");
        }

    }

    public static class WorldCityBean_Header extends SEPConfig {

        //Country,City,AccentCity,Region,Population,Latitude,Longitude
        {
            Wrapper<WorldCityBean> city = csvMarshaller(WorldCityBean.class).build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
        }

    }

    public static class WorldCityBeanTransient_Header extends SEPConfig {

        //3 non-transient properties
        //Country,City,Population
        {
            csvMarshaller(WorldCityBeanTransient.class).build();
        }

    }

    public static class WorldCityBeanEscaped_Header extends SEPConfig {

        //Country,City,AccentCity,Region,Population,Latitude,Longitude
        {
            Wrapper<WorldCityBean> city = csvMarshaller(WorldCityBean.class)
                    .processEscapeSequences(true)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
        }

    }

    public static class WorldCityFixedLen extends SEPConfig {

        {
            RowProcessor<WorldCityOnEvent> city = fixedLenMarshaller(WorldCityOnEvent.class)
                    .headerLines(1)
                    .mapFixed(0, 10, WorldCity::setCountry)
                    .mapFixed(10, 10, WorldCity::setCity)
                    .mapFixed(20, 8, WorldCity::setLongitude)
                    .mapFixed(28, 8, WorldCity::setLatitude)
                    .skipCommentLines(true)
                    .skipEmptyLines(true)
                    .build();
            addPublicNode(city, "city");
            addPublicNode(count(city), "count");
            //validator count
            RulesEvaluator<WorldCityOnEvent> validator = validator(city).build();
            addPublicNode(count(validator.passedNotifier()), "countPassed");
            addPublicNode(count(validator.failedNotifier()), "failedValidationCount");
//            addPublicNode(count(failedValidationListener(city)), "failedValidationCount");
        }
    }

    public static double always_1(CharSequence cs) {
        return 1.0;
    }

    public static class MyFunctions {

        public double always_Zero(CharSequence cs) {
            return 0.0;
        }

        public int always_100(CharSequence cs) {
            return 100;
        }

    }
}
