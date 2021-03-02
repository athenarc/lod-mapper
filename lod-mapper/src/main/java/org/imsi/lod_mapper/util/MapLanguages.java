package org.imsi.lod_mapper.util;

import java.io.*;

/**
 * Method: getLangURI
 *
 * @author Giorgos Alexiou
 */

public class MapLanguages implements Serializable {
    private static final long serialVersionUID = 2896628578961769658L;

    /*
     * This Method takes ISO iso639_3 codes for languages as input and returns the respective URI from lexvo.org as output.
     * For example iso639_3 for Greek is "ell" and the respective URI from lexvo is <http://lexvo.org/id/iso639-3/ell>
     */

    private InputStream is = this.getClass().getClassLoader().getResourceAsStream("lexvo-iso639-3.tsv");
    private BufferedReader reader = new BufferedReader(new InputStreamReader(is));

    public MapLanguages() {

    }

    //	@SuppressWarnings("resource")
    public String getLangURI(String iso639_3_Code) throws IOException {

        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            String[] line = currentLine.split("\t");
//			System.out.println(line[0].trim()+"--------------"+line[1].trim());

            if (line[0].trim().equals(iso639_3_Code)) {
                return line[1].trim();
            }

        }
        reader.close();
        return iso639_3_Code;
    }


}
	
