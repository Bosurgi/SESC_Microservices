package com.sesc.libraryservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

//TODO: Implement the Client for searching book through ISBN
public class BookClient {

    /**
     * It fetches a book from the Google Book API through its isbn
     *
     * @param isbn   the ISBN to look for
     * @param apiKey the API Key set up in the properties file
     * @return the JSON reponse from the API#
     * @throws IOException Error if it can't fetch the book.
     */
    public static String getBookByISBN(String isbn, String apiKey) throws IOException {
        String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
        String urlString = BASE_URL + "isbn:" + isbn + "&key=" + apiKey;
        URL url = new URL(urlString);
        // Opening connection with the URL specified
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Getting the entire response as one token
            InputStream inStream = connection.getInputStream();
            Scanner scanner = new Scanner(inStream);
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } else {
            throw new IOException("Failed to fetch book information: " + responseCode);
        }
    }
}
