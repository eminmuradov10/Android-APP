package com.example.my_application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RssDatabase database = new RssDatabase(MainActivity.this,"source.db",null,1);

    @Override
    //used to add menu to the activity
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    @Override
    //used to give reactions when pressed(for now it just show text in short time
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                Toast.makeText(this,"General settings are chosen",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,UserSettings.class));
                return true;
            case R.id.addwordbutton:
                Toast.makeText(this, "add word", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,PopUpWindow.class);

                startActivityForResult(intent,5);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==5)
        {
            if (!(data==null)){
                if(!data.getStringExtra("MESSAGE").equals("")){
                    String message=data.getStringExtra("MESSAGE");
                    database.addItem("words",message);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        if (preferences.getBoolean("firstLogin", true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstLogin",false);
            editor.commit();

            addRssLinks();
            addCommonWords();
        }

        ProcessInBackground rss = new ProcessInBackground();
        rss.execute();
    }


    public class ProcessInBackground extends AsyncTask<Integer,Void,Exception> {

        private ArrayList<News> newsList = new ArrayList<News>();

        ListView lvRss = (ListView) findViewById(R.id.lvRss);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Exception exception = null;

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            Cursor cursor = database.getAll("words");

            final ArrayList<News> positiveNews = new ArrayList<News>();
            final ArrayList<String> positiveTitles = new ArrayList<String>();

            for (int i = 0; i < newsList.size(); i++){
                while (cursor.moveToNext()) {
                    String word = cursor.getString(1);
                    String title = newsList.get(i).getTitle();
                    if (title.toLowerCase().contains(word)){
                        positiveNews.add(newsList.get(i));
                        positiveTitles.add(title);
                        break;
                    }
                }
                cursor.moveToFirst();
            }

            CustomAdapter customAdapter = new CustomAdapter(MainActivity.this , positiveNews);
            lvRss.setAdapter(customAdapter);

            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected Exception doInBackground(Integer... integers) {

            try {
                Cursor cursor = database.getAll("rss");
                //cursor.moveToFirst();
                int newsIterator = 0;
                while (cursor.moveToNext()){
                    String link = cursor.getString(1);

                    //Our links with RSS feed
                    URL url_1 = new URL(link);

                    //Parser factory for XML files parsers
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                    // not support for xml namespaces ???
                    factory.setNamespaceAware(false);

                    //XML parser created by parser factory
                    XmlPullParser xmlPullParser = factory.newPullParser();
                    //We are using our getInputStream method to get data from our url
                    try{
                        //This "if" is because something was wrong on emulator
                        // (on real smartphone everything is cool)
                        if(getInputStream(url_1) == null){
                            continue;
                        }
                        //
                        xmlPullParser.setInput(getInputStream(url_1),"UTF_8");

                    }catch (NullPointerException e){
                        Log.d("URL","There is not URL.");
                    }
                    // insideItem informs when in XML file we are inside Item tags
                    //Item in XML file is like one news
                    boolean insideItem = false;

                    int eventType = xmlPullParser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT){
                        String title = "Title not found.";
                        String linkURL = "https://www.google.pl/search?sxsrf=ALeKk03owz2K1bE4A8aKsMm713I21xJsog%3A1587895128412&ei=WFulXrXRGMSKmwWX5rEo&q=link+not+found&oq=link+not+found&gs_lcp=CgZwc3ktYWIQAzICCAAyAggAMgIIADIECAAQHjIECAAQHjIECAAQHjIECAAQHjIECAAQHjIECAAQHjIECAAQHjoECAAQR1CrFVirFWCoFmgAcAJ4AIABaIgBaJIBAzAuMZgBAKABAaoBB2d3cy13aXo&sclient=psy-ab&ved=0ahUKEwi13cao6oXpAhVExaYKHRdzDAUQ4dUDCAw&uact=5";
                        String linkImage = "https://i0.wp.com/www.dsdrums.co.uk/wp-content/uploads/2018/04/news.png?w=500";
                        //
                        //News news;
                        if(eventType == XmlPullParser.START_TAG){

                            if (xmlPullParser.getName().equalsIgnoreCase("item")){
                                //we checked that tag is "item", so we are inside "item"
                                insideItem = true;
                            }else if (xmlPullParser.getName().equalsIgnoreCase("title")){
                                if(insideItem){
                                    //we are in "title" tag inside "item"
                                    //now we can extract text from XML
                                    //we extract text after "title" tag
                                    //that means title of article
                                    News news = new News();
                                    news.setTitle(xmlPullParser.nextText());
                                    newsList.add(news);
                                    //titles.add(xmlPullParser.nextText());
                                }
                            }else if (xmlPullParser.getName().equalsIgnoreCase("link")) {
                                if (insideItem){
                                    //Now we are doing the same what with "title" tag
                                    //but with "link" tag
                                    newsList.get(newsList.size()-1).setLink(xmlPullParser.nextText());
                                }
                            }else if(xmlPullParser.getName().equalsIgnoreCase("media:content")){

                                for (int i = 0; i < xmlPullParser.getAttributeCount(); i++){
                                    if(xmlPullParser.getAttributeName(i).equals("url")){

                                        newsList.get(newsList.size()-1).setImageURL(xmlPullParser.getAttributeValue(i));
                                        break;
                                    }
                                }
                            }
                        }else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")){
                            //We checked that it is the end of the "item"/article
                            //so we are outside "item"
                            newsIterator++;
                            insideItem = false;
                        }
                        eventType = xmlPullParser.next();
                    }
                }
            }
            catch (MalformedURLException e){
                exception = e;
            }
            catch (XmlPullParserException e){
                exception = e;
            }
            catch (IOException e){
                exception = e;
            }

            return exception;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

        }
        //Input stream to upload XML file from RSS feed
        public InputStream getInputStream(URL url){
            try {
                return url.openConnection().getInputStream();
            }
            catch (IOException e){
                return null;
            }
        }
    }




    public void addRssLinks(){
        //15 rss sources
        database.addItem("rss","https://rss.nytimes.com/services/xml/rss/nyt/World.xml");
//        database.addItem("rss","https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml");
//        database.addItem("rss","https://rss.nytimes.com/services/xml/rss/nyt/Sports.xml");
//        database.addItem("rss","https://rss.nytimes.com/services/xml/rss/nyt/Science.xml");
//        database.addItem("rss","https://rss.nytimes.com/services/xml/rss/nyt/Health.xml");

        database.addItem("rss","http://feeds.bbci.co.uk/news/rss.xml");
//        database.addItem("rss","http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml");
//        database.addItem("rss","http://feeds.bbci.co.uk/news/technology/rss.xml");
//        database.addItem("rss","http://feeds.bbci.co.uk/news/education/rss.xml");
//        database.addItem("rss","http://feeds.bbci.co.uk/news/health/rss.xml");
//
        database.addItem("rss","http://feeds.skynews.com/feeds/rss/world.xml");
//        database.addItem("rss","http://feeds.skynews.com/feeds/rss/technology.xml");
//        database.addItem("rss","http://feeds.skynews.com/feeds/rss/entertainment.xml");
//        database.addItem("rss","http://feeds.skynews.com/feeds/rss/strange.xml");
//        database.addItem("rss","http://feeds.skynews.com/feeds/rss/business.xml");
//
        database.addItem("rss","https://www.cnbc.com/id/10000739/device/rss/rss.html");
//        database.addItem("rss","https://www.cnbc.com/id/100727362/device/rss/rss.html");
//        database.addItem("rss","https://www.cnbc.com/id/19854910/device/rss/rss.html");
//        database.addItem("rss","https://www.cnbc.com/id/10000108/device/rss/rss.html");
//        database.addItem("rss","https://www.cnbc.com/id/10000110/device/rss/rss.html");
//
        database.addItem("rss","http://rss.cnn.com/rss/edition_world.rss");
//        database.addItem("rss","http://rss.cnn.com/rss/edition_sport.rss");
//        database.addItem("rss","http://rss.cnn.com/rss/edition_entertainment.rss");
//        database.addItem("rss","http://rss.cnn.com/rss/edition_space.rss");
//        database.addItem("rss","http://rss.cnn.com/rss/edition_travel.rss");
//
        database.addItem("rss","https://feeds.a.dj.com/rss/RSSWorldNews.xml");
//        database.addItem("rss","https://feeds.a.dj.com/rss/RSSMarketsMain.xml");
//        database.addItem("rss","https://feeds.a.dj.com/rss/RSSWSJD.xml");
//        database.addItem("rss","https://feeds.a.dj.com/rss/RSSLifestyle.xml");
//        database.addItem("rss","https://feeds.a.dj.com/rss/RSSOpinion.xml");

    }

    public void addCommonWords(){
        //45 words
        database.addItem("words","good");
        database.addItem("words","admir");
        database.addItem("words"," agre");
        database.addItem("words","amazi");
        database.addItem("words","awesom");

        database.addItem("words","beautif");
        database.addItem("words","brav");
        database.addItem("words","cute");
        database.addItem("words","creativ");
        database.addItem("words"," effectiv");

        database.addItem("words","excellent");
        database.addItem("words","fabulous");
        database.addItem("words","fair");
        database.addItem("words","free");
        database.addItem("words","friend");

        database.addItem("words","fun");
        database.addItem("words","generous");
        database.addItem("words","genius");
        database.addItem("words","gorgeous");
        database.addItem("words"," happy");

        database.addItem("words"," honest");
        database.addItem("words"," healthy");
        database.addItem("words","intellige");
        database.addItem("words","kind");
        database.addItem("words","laugh");

        database.addItem("words","lov");
        database.addItem("words"," natural");
        database.addItem("words","nice");
        database.addItem("words","qualit");
        database.addItem("words"," saf");

        database.addItem("words","secur");
        database.addItem("words","succe");
        database.addItem("words","vital");
        database.addItem("words","victor");
        database.addItem("words","wonder");

        database.addItem("words","angel");
        database.addItem("words","lightf");
        database.addItem("words","fit");
        database.addItem("words","first time");
        database.addItem("words"," cozy");

        database.addItem("words","bio");
        database.addItem("words","support");
        database.addItem("words","hero");
        database.addItem("words","helpf");
        database.addItem("words","best");

    }

    public RssDatabase getDatabase() {
        return database;
    }

    public void setDatabase(RssDatabase database) {
        this.database = database;
    }

    public RssDatabase getDictionary() {
        return database;
    }

    public void setDictionary(RssDatabase dictionary) {
        this.database = dictionary;
    }


}

