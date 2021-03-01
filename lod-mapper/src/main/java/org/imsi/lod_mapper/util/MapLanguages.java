package org.imsi.lod_mapper.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Method: getLangURI
 *
 * @author Giorgos Alexiou
 */

public class MapLanguages {

    /**
     * This Method takes ISO iso639_3 codes for languages as input and returns the respective URI from lexvo.org as output.
     * For example iso639_3 for Greek is "ell" and the respective URI from lexvo is <http://lexvo.org/id/iso639-3/ell>
     */

    public MapLanguages() {

    }

    //	@SuppressWarnings("resource")
    public String getLangURI(String iso639_3_Code) throws IOException {

        InputStream is = ClassLoader.getSystemResourceAsStream("lexvo-iso639-3.tsv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            String[] line = currentLine.split("\t");
//			System.out.println(line[0].trim()+"--------------"+line[1].trim());

            if (line[0].trim().equals(iso639_3_Code)) {
                return line[1].trim();
            }

        }
        reader.close();
        return "";
    }


}
	
