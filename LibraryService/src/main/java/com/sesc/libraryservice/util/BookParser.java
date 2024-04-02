package com.sesc.libraryservice.util;

import com.sesc.libraryservice.model.Book;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Book Parser class to parse the JSON response from the Google Books API
 */
public class BookParser {

    /**
     * It parses the JSON response from the Google Books API
     *
     * @param jsonResponse the JSON response
     * @return the Book entity
     */
    public static Book parseBookResponse(String jsonResponse) {
        try {
            JSONObject responseJson = new JSONObject(jsonResponse);
            JSONArray items = responseJson.getJSONArray("items");

            if (!items.isEmpty()) {
                // Fetching the book information
                JSONObject bookInfo = items.getJSONObject(0).getJSONObject("volumeInfo");
                String title = bookInfo.getString("title");
                String author = bookInfo.getJSONArray("authors").join(", ");
                String publishedDate = bookInfo.optString("publishedDate", "");
                // Parsing the year from the fetched date
                int year = extractYear(publishedDate);
                String isbn = fetchISNB13(bookInfo.getJSONArray("industryIdentifiers"));

                String thumbnailUrl = "";
                if (bookInfo.has("imageLinks")) {
                    thumbnailUrl = bookInfo.getJSONObject("imageLinks").getString("thumbnail");
                }

                return new Book(isbn, title, author, year);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Helper method to extracts the year from the published date
     *
     * @param publishedDate the published date
     * @return the year
     */
    public static int extractYear(String publishedDate) {
        int year = 0;
        String[] dateDetails = publishedDate.split("-");
        if (dateDetails.length > 0) {
            year = Integer.parseInt(dateDetails[0]);
        }
        return year;
    }

    /**
     * Helper method to fetch the ISBN 13 from the identifiers
     *
     * @param identifiers the identifiers
     * @return the ISBN 13
     */
    public static String fetchISNB13(JSONArray identifiers) {
        if (identifiers != null) {
            for (int i = 0; i < identifiers.length(); i++) {
                JSONObject identifier = identifiers.getJSONObject(i);
                if (identifier.getString("type").equals("ISBN_13")) {
                    return identifier.optString("identifier");
                }
            }
        }
        return null;
    }
}
