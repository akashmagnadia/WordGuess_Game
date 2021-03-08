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
        StringBuilder urlString = new StringBuilder("https://api.datamuse.com/words?" + code + "=");

        for (int i = 0; i < hints.length; i++) {
            hints[i] = hints[i].replaceAll(" ", "+");
            if (i == hints.length - 1) {
                urlString.append(hints[i]);
            } else {
                urlString.append(hints[i]).append(",");
            }

        }

        URL url = new URL(urlString.toString());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responsecode = conn.getResponseCode();

        StringBuilder inline = new StringBuilder();
        if(responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        }
        else
        {
            Scanner sc = new Scanner(url.openStream());
            while(sc.hasNext())
            {
                inline.append(sc.nextLine());
            }
            sc.close();
        }

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(inline.toString());
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
