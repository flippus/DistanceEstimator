package it.unibz.inf.ait.distanceestimatorapp.export;

import it.unibz.inf.ait.distanceestimatorapp.model.MyLocation;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

public class SendXml {

    public String createXml(List<MyLocation> locationList) {
        XmlSerializer serializer = Xml.newSerializer();

        StringWriter stringWriter = new StringWriter();

        try {
            serializer.setOutput(stringWriter);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "training");
            if (locationList.size() > 0) {
                serializer.startTag("", "datetime");
                serializer.text(String.valueOf(locationList.get(0)
                        .getTimeAsLong()));
                serializer.endTag("", "datetime");
            }
            for (MyLocation location : locationList) {
                serializer.startTag("", "location");
                serializer.startTag("", "longitude");
                serializer.text(String.valueOf(location.getLongitude()));
                serializer.endTag("", "longitude");
                serializer.startTag("", "latitude");
                serializer.text(String.valueOf(location.getLatitude()));
                serializer.endTag("", "latitude");
                serializer.endTag("", "location");
            }
            serializer.endTag("", "training");
            serializer.endDocument();
        } catch (IllegalArgumentException e) {
        } catch (IllegalStateException e) {
        } catch (IOException e) {
        }
        return stringWriter.toString();
    }

    public int exportXml(List<MyLocation> locationList) {
        String link = "http://10.0.2.2:3000/export";
        //String link = "http://cryptic-sands-9137.herokuapp.com/export";

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 4000;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        int timeoutSocket = 6000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpPost httpPost = new HttpPost(link);
        int responseStatus = 0;
        try {
            String xmlFile = createXml(locationList);
            Log.d("xml_file", xmlFile);
            StringEntity entity = new StringEntity(xmlFile, "UTF-8");
            httpPost.setEntity(entity);
            httpPost.addHeader("Content-Type", "application/xml");
            HttpResponse response = httpClient.execute(httpPost);
            responseStatus = response.getStatusLine().getStatusCode();
            Log.d("response reason phrase", ""
                    + response.getStatusLine().getReasonPhrase());
            Log.d("response content", ""
                    + response.getStatusLine().getStatusCode());
        } catch (ClientProtocolException e) {
            Log.d("exception at exporting", "" + e.getMessage());
        } catch (IOException e) {
            Log.d("exception at exporting", "" + e.getMessage());
        }
        return responseStatus;
    }
}
