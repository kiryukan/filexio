/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.utility;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 *
 * @author renj
 */
@Service
public class UtilityService {
    public UtilityService(){}
    
    public static String generateRootFolderName(int length, Random random, String symbols){
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase(Locale.ROOT);
        String digits = "0123456789";
        String alphanum = upper + lower + digits;
        char[] buf;
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        random = Objects.requireNonNull(random);
        char[] charSymbols = symbols.toCharArray();
        buf = new char[length];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = charSymbols[random.nextInt(charSymbols.length)];
        return new String(buf);
    }
    
    public static String testStaticFct(){
        return "Ma fonction static";
    }
    
    
}
