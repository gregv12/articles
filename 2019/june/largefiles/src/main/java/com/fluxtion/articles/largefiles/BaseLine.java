/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.largefiles;

import com.fluxtion.ext.text.api.event.CharEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class BaseLine {

    public static void main(String[] argv) {
        int lineCount = 0;
        try (BufferedReader b = Files.newBufferedReader(new File("C:\\Users\\gregp\\Downloads\\indiv18\\itcont.txt").toPath())) {
//            CharBuffer chbuff = CharBuffer.allocate(8192);
//            while (b.read(chbuff) > 0) {
//                chbuff.flip();
//                while (chbuff.hasRemaining()) {
//                    lineCount++;
//                }
//                chbuff.clear();
//            }
            String readLine = "";
            while ((readLine = b.readLine()) != null) {
                lineCount++;
            }
        } catch (IOException ignored) {
        }

        System.out.println(lineCount);
    }
}
