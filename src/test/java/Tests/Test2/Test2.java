package Tests.Test2;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Test2 {
    private static String url = "https://www.wiley.com/en-us/search/autocomplete/comp_00001H9J?term=Java";
    private String jsonString;
    private static StringBuffer response;
    private int count = 4;
    private int size = 300;

    @BeforeClass
    public static void setup() throws IOException {
        makeRequest(url);
    }

    public static void makeRequest(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Charset","UTF-8");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

    public void search(String obj, String attribute, String word) throws ParseException {
        jsonString = response.toString();
        JSONParser parser = new JSONParser();

        int n = 0;
        Object o  = parser.parse(jsonString);
        JSONObject jsonObj = (JSONObject) o;
        JSONArray ja = (JSONArray) jsonObj.get(obj);
        for (int i =0; i < ja.size(); i++) {
            Object o1 = parser.parse(ja.get(i).toString());
            JSONObject term = (JSONObject) o1;
            if(term.get(attribute)!=null && term.get(attribute).toString().contains(word))
                n++;
        }
        Assert.assertEquals(count, n);
    }

    @Test
    public void check1() throws ParseException {
        search("suggestions", "term", "java");
    }

    @Test
    public void check2() throws ParseException {
        search("products", "name", "Java");
    }

    @Test
    public void check3() throws ParseException {
        search("pages", "title", "Wiley");
    }

    @Test
    public void check4() throws IOException, ParseException {
        jsonString = response.toString();
        JSONParser parser = new JSONParser();

        Object o  = parser.parse(jsonString);
        JSONObject jsonObj = (JSONObject) o;
        JSONArray ja = (JSONArray) jsonObj.get("products");

        o = parser.parse(ja.get(0).toString());
        jsonObj = (JSONObject) o;
        ja = (JSONArray) jsonObj.get("images");

        o = parser.parse(ja.get(0).toString());
        jsonObj = (JSONObject) o;
        o = jsonObj.get("url");

        URL url = new URL(o.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Charset","UTF-8");

        int image = ImageIO.read(url).getWidth();
        Assert.assertEquals(size, image);
    }

    @Test
    public void post() throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
        HttpConnectionParams.setSoTimeout(httpParameters, 10000);
        CloseableHttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpPost httppost = new HttpPost("https://httpbin.org/");

        List<NameValuePair> params = new ArrayList<>(2);
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        InputStream instream = entity.getContent();
    }

}
