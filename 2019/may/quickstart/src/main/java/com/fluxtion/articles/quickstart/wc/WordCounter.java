/* 
 * Copyright (C) 2016 Greg Higgins (greg.higgins@v12technology.com)
 *
 * This file is part of Fluxtion.
 *
 * Fluxtion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.fluxtion.articles.quickstart.wc;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.FilterType;
import com.fluxtion.api.annotations.TearDown;
import com.fluxtion.builder.annotation.SepInstance;
import com.fluxtion.ext.text.api.event.CharEvent;

/**
 * Word count logic replicating behaviour of unix wc for bytes:
 * <pre>
 * wc -clw [file]
 * </pre>
 *
 * Processes a {@link CharEvent} and keeps a running total of chars, words and
 * lines. Each of the annotated EventHandler methods is an entry point for event
 * processing. Some of the methods supply optional filters.<p>
 *
 * As the generated sep contains only this node the {@link SepInstance}
 * annotation is used to mark this class to included in a static event
 * processor.
 *
 * @author Greg Higgins
 */
@SepInstance(
        name = "Wc",
        packageName = "com.fluxtion.articles.quickstart.wc.generated",
        outputDir = "src/main/java",
        cleanOutputDir = true,
        supportDirtyFiltering = false
)
public class WordCounter {

    public transient int wordCount;
    public transient int charCount;
    public transient int lineCount;
    private int increment = 1;

    @EventHandler
    public void onAnyChar(CharEvent event) {
        charCount++;
    }

    @EventHandler(filterId = '\t')
    public void onTabDelimiter(CharEvent event) {
        increment = 1;
    }

    @EventHandler(filterId = ' ')
    public void onSpaceDelimiter(CharEvent event) {
        increment = 1;
    }

    @EventHandler(filterId = '\n')
    public void onEol(CharEvent event) {
        lineCount++;
        increment = 1;
    }

    @EventHandler(filterId = '\r')
    public void onCarriageReturn(CharEvent event) {
        //do nothing handle \r\n
    }

    @EventHandler(FilterType.unmatched)
    public void onUnmatchedChar(CharEvent event) {
        wordCount += increment;
        increment = 0;
    }

    @TearDown
    public void printReport() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        int pad = charCount < 1e6 ? 6 : charCount < 1e9 ? 11 : 14;
        return String.format("%," + pad + "d chars%n%," + pad + "d words%n%," + pad + "d lines %n", charCount, wordCount, lineCount);
    }

}
