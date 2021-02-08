/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.aot_csv;

import com.fluxtion.articles.aot_csv.generated.CityCsvDecoder0;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author gregp
 */
public class CsvTest {

    String csvSample = "country,latitude,longitude,name\n"
            + "England,60,0,Marlow\n";

    String csvSample2 = "Country,City,AccentCity,Region,Population,Latitude,Longitude\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            + "ad,aixas,Aix<E0>s,06,,42.4833333,1.4666667\n"
            ;
    
    @Test
    public void sampleCsvOut() throws IOException {
        City city = new City();
        city.setCountry("England");
        city.setCity("Marlow");
//        city.setLatitude(60);
//        city.setLongitude(0);

        StringBuilder sb = new StringBuilder(CityCsvDecoder0.csvHeader() + "\n");
        CityCsvDecoder0.asCsv(city, sb);

        System.out.println(sb);
    }

    @Test
    public void sampleParse() {
        CityCsvDecoder0.stream(System.out::println, csvSample2);
    }
    
    
}
