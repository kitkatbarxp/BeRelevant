/*
 * Names: Kit Tse, Khalid Almandeel
 * Date: 10/4/2014 - 10/5/2015 @ HackMIT2014
 * 
 * Took some code from a XML Parser guide on Vogella. 
 */

package com.tsealmandeel.berelevant;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * This serves as a Parser for the Google News RSS Feed and provides methods that can 
 * parse different data using different XML tags. 
 *
 */

public class GoogleNewsRSSFeedParser {
	// We don't use namespaces
    private static final String ns = null;
   
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
    
    private  List<Item> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Item> items = new ArrayList<Item>();

        parser.next();
        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }  
        return items;
    }

	public static class Item {
        public final String title;
        public final String link;
        public final String pubdate;
        public final String description;
        
        private Item(String title, String pubdate, String description, String link) {
        	this.title = title;
        	this.pubdate = pubdate;
        	this.description = description;
        	this.link = link;
        }
    }
	
	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String pubdate = null;
        String description = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("pubdate")) {
           	 pubdate = readDate(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }
        return new Item(title, pubdate, description, link);
    }
    
    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }
    
    // Process date tags in the feed.
    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "pubdate");
    	String pubdate = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "pubdate");
    	return pubdate;
    }
      
    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
    	String link = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "link");
        
        return link;
    }

    // Processes summary tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return summary;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    
    // Skip tags that we don't care about.
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
    }
}
