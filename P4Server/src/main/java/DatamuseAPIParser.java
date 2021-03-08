import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DatamuseAPIParser {
    String[] words;

    public DatamuseAPIParser(String code, String[] hints) throws IOException, ParseException {
        String urlString = "https://api.datamuse.com/words?" + code + "=";

        for (int i = 0; i < hints.length; i++) {
            hints[i] = hints[i].replaceAll(" ", "+");
            if (i == hints.length - 1) {
                urlString+=(hints[i]);
            } else {
                urlString+=(hints[i] + ",");
            }

        }

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responsecode = conn.getResponseCode();

        String inline = "";
        if(responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        }
        else
        {
            Scanner sc = new Scanner(url.openStream());
            while(sc.hasNext())
            {
                inline+=sc.nextLine();
            }
//            System.out.println("\nJSON data in string format");
//            System.out.println(inline);
            sc.close();
        }

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(inline);
        JSONArray jsonArray = (JSONArray) obj;

        //initlize words array
        words = new String[jsonArray.size()];

        //Get data for Results array
        for(int i=0;i<jsonArray.size();i++)
        {
            //Store the JSON objects in an array
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);

            words[i] = (String) jsonObject.get("word");
        }
    }

    public String[] getWords() {
        return this.words;
    }

}
