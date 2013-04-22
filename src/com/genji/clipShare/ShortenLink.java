package com.genji.clipShare;

import android.os.AsyncTask;
import android.util.Log;
import com.rosaloves.bitlyj.Url;

import static com.rosaloves.bitlyj.Bitly.as;
import static com.rosaloves.bitlyj.Bitly.shorten;

class ShortenLink extends AsyncTask<String, Void, String> {
  ASyncListener aSyncListener;

  public void setASyncListener(ASyncListener listener) {
    this.aSyncListener = listener;
  }

  protected String doInBackground(String... strings) {
    // Run link shortening request in background
    try {
      String longValue = strings[0];
      Url url = as("bitly_username", "bitly_api_key").call(shorten(longValue));
      String shortValue = url.getShortUrl();
      return shortValue;
    } catch(Exception e) {
      Log.w("Error", e);
      return null;
    }
  }

  protected void onProgressUpdate() {
  }

  protected void onPostExecute(String result) {
    try {
      aSyncListener.sendResult(result);
    } catch(Exception e) {
      Log.w("Error", e);
    }
  }
}
