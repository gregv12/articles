/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.aot_csv;

import lombok.Data;

/**
 *
 * @author gregp
 */
@Data
public class City {
    
    private CharSequence country;
    private CharSequence city;
    private CharSequence accentCity;
    private CharSequence region;
    private CharSequence population;
    private CharSequence longitude;
    private CharSequence latitude;
    
}
