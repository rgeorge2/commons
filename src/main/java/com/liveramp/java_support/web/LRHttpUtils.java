package com.liveramp.java_support.web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

import org.apache.commons.io.IOUtils;

import com.liveramp.java_support.functional.Either;

public class LRHttpUtils {

  public static String GETRequest(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
    urlConnection.setRequestMethod("GET");
    return IOUtils.toString(urlConnection.getInputStream());
  }

  public static String getRedirectUrlFromUrl(String startUrl) throws IOException, IllegalUrlException {
    URL url = new URL(startUrl);
    URLConnection untypedConnection = url.openConnection();
    if (untypedConnection instanceof HttpURLConnection) {
      HttpURLConnection urlConnection = (HttpURLConnection)untypedConnection;
      urlConnection.setInstanceFollowRedirects(false);
      urlConnection.connect();
      int responseCode = urlConnection.getResponseCode();
      if (responseCode == 302) {
        return urlConnection.getHeaderField("Location");
      } else {
        throw new IllegalUrlException("The provided URL does not link to a redirect page! Expected response code 302 but the code was " + responseCode + " for url " + startUrl);
      }
    } else {
      throw new IllegalUrlException("Can only get a redirect from a connection with underlying http protocol. URL was " + startUrl + " with protocol " + url.getProtocol());
    }
  }

  public static Optional<String> tryGetRedirectUrlFromUrl(String startUrl) {
    try {
      return Optional.ofNullable(getRedirectUrlFromUrl(startUrl));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public static Either<String, Exception> eitherGetRedirectUrlFromUrl(String startUrl) {
    try {
      return Either.left(getRedirectUrlFromUrl(startUrl));
    } catch (Exception e) {
      return Either.right(e);
    }
  }

  public static class IllegalUrlException extends Exception {
    IllegalUrlException(String message) {
      super(message);
    }
  }
}
