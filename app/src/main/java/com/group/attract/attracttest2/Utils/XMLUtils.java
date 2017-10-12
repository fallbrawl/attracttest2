package com.group.attract.attracttest2.Utils;

import android.util.Log;

import com.group.attract.attracttest2.RssItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nexus on 11.10.2017.
 */
public class XMLUtils {
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;

    private ArrayList<RssItem> rssItems;

    private String title = "title";
    private String description = "description";

    private List<String> titles;
    private  List<String> descriptions;

    public volatile boolean parsingComplete = true;

    public XMLUtils(String url) {
        this.urlString = url;
    }

    public ArrayList<RssItem> getRssItems() {
        Log.e("staty", "size temp " + rssItems.size());
        return rssItems;}

    public ArrayList<RssItem> parseXMLAndStoreIt(XmlPullParser myParser) {
        RssItem rssItem;
        int event;
        String text=null;
        rssItems = new ArrayList<>();
        titles = new ArrayList<>();
        descriptions = new ArrayList<>();

        try {
            String name = null;
            while ((event = myParser.getEventType()) != XmlPullParser.END_DOCUMENT) {


                switch (event){
                    case XmlPullParser.START_TAG:
                        name = myParser.getName();
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if(name.equals("title")){
                            title = text;
                            titles.add(title);

                        }

                        else if(name.equals("description")){
                            description = text;
                            descriptions.add(description);

                        }

                        else{

                        }

                        break;
                }

                myParser.next();
            }

            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        //Convert String arrays into rss
        for (int i = 1; i < titles.size(); i++){
            rssItem = new RssItem(titles.get(i), descriptions.get(i));
            rssItems.add(rssItem);
            Log.e("staty", "size temp real" + rssItems.size());
        }
        return rssItems;

    }


    public void fetchXML() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {

                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    stream.close();

                } catch (Exception e) {
                }
            }
//        });
//        thread.start();
    //}
}
