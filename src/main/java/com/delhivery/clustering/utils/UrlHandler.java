package com.delhivery.clustering.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * @author Anurag Paul(anurag.paul@delhivery.com)
 *         Date: 8/2/17
 */
public class UrlHandler {

    private static Logger logger = LoggerFactory.getLogger(UrlHandler.class);

    static{
        //connectionTimeout, socketTimeout in milliseconds
        Unirest.setTimeouts(90000, 30000);
    }

    /**
     * Allows getting data from a HTTP API with optional apikey authorization
     *
     * @param link HTTP URL link
     * @param apikey optional authentication apikey
     * @return output of the API request
     * @throws NullPointerException just in case of no output
     */
    public static String processUrl(String link, String apikey) throws NullPointerException {
        String output = null;
        boolean UrlProcessed = false;
        int attempts = 0;

        while (!UrlProcessed) {

            try {
                HttpResponse<String> response;
                if (apikey != null) {
                    response = Unirest.get(link)
                            .header("content-type", "application/json")
                            .header("accept", "application/json")
                            .header("authorization", apikey)
                            .header("cache-control", "no-cache")
                            .asString();
                } else {
                    response = Unirest.get(link)
                            .header("content-type", "application/json")
                            .header("accept", "application/json")
                            .header("cache-control", "no-cache")
                            .asString();
                }

                if (response.getStatus() == HTTP_OK)
                    output = response.getBody();

                UrlProcessed = true;

            }catch(UnirestException exception){

                logger.error("API-Exception", exception);

            } catch (Exception exception) {

                logger.error("IOException: ", exception);

                if (!UrlHandler.testInet("google.com"))
                    throw new RuntimeException("FLP> No Internet Connection!");
            }
            attempts++;
            //Limit the number of attempts to 3
            if (attempts > 3)
                break;
        }

        if (output == null)
            throw new NullPointerException("Nothing to return");

        return output;
    }

    /**
     * to check if internet is connected
     *
     * @param site any website
     * @return True if internet is connected
     */
    public static boolean testInet(String site) {

        Socket sock = new Socket();
        InetSocketAddress addr = new InetSocketAddress(site, 80);

        try {
            sock.connect(addr, 3000);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                sock.close();
            } catch (IOException exception) {
                logger.error("IOException: ", exception);
            }
        }
    }

    /**
     * To check if a port is open
     *
     * @param host server
     * @param port port of the server
     * @return True if a server is listening on a specified port
     */
    public static boolean isServerListening(String host, int port) {
        Socket s = null;
        try {
            s = new Socket(host, port);
            return true;
        } catch (Exception exception) {
            return false;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (Exception exception) {
                    logger.error("Exception: ", exception);
                }
            }
        }
    }

    /**
     * To check if server is up and running
     * @param host server to be checked
     * @return true or false
     */
    public static boolean isServerListening(String host){
        HttpResponse<String> response;
        try {
            response = Unirest.get(host).asString();
            if(response.getStatus()!=502)
                return true;
        }catch(UnirestException exception){
            logger.error("API-Exception", exception);
        }
        return false;
    }
}