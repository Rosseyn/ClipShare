package com.genji.clipShare;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyActivity extends Activity implements ASyncListener {
  protected final static int FILTER_PARAMETERS = 1;
  final String urlRegex = "^(https?:\\/\\/)?([\\da-zA-Z]+\\.)?([\\w-]+)\\.([\\w-]+)(\\.[\\w-]{2,6})?([\\/|\\?&=%!@#\\w\\.-]+)*\\/?";
  EditText editText;
  Button shortenText;
  public String undoValue = "";
  public final static String EXTRA_MESSAGE = "com.genji.clipshare.MESSAGE";
  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    editText = (EditText) findViewById(R.id.edit_data);
    shortenText = (Button) findViewById(R.id.button_shorten);
    Intent intent = getIntent();

    try {
      // Populate EditText with shared string
      String sharedText;
      if(intent.hasExtra(Intent.EXTRA_TEXT)) {
        sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
      } else {
        sharedText = intent.getDataString();
      }
      if (sharedText != null) {
        // Update UI to reflect text being shared
          editText.setText(sharedText);
  //    } else { // Uncomment these 3 lines to set default URL for testing purposes
  //      String defaultText = "http://www.thislink.com/page.html?key=value&foo=bar" +
  //      editText.setText(defaultText);
      }
    } catch(Exception e) {
      Log.w("Error", e);
    }
  }

  public void copyToClipboard(View view) {
    try {
      // Ensure EditText contents are in a valid URL format
      String pasteValue = editText.getText().toString();
      if(pasteValue.matches(urlRegex)) {
        if(pasteValue.length() != 0) {
          ClipboardManager clipboard = (ClipboardManager)
              getSystemService(Context.CLIPBOARD_SERVICE);
          ClipData clip = ClipData.newPlainText("Paste Value", pasteValue);
          clipboard.setPrimaryClip(clip);
          finish();
        }
      } else {
        Context context = getApplicationContext();
        String string = "Does not match URL pattern.";
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.show();
      }
    } catch(Exception e) {
      Log.w("Error", e);
    }
  }

  public void filter(View view) {
    try {
      // If a valid URL is detected with parameters, send query string to be parsed for filter selection
      String message = editText.getText().toString();
      if(message.matches(urlRegex) && (!message.matches(".*\\?.*") || !message.matches(".*=.*"))) {
        Context context = getApplicationContext();
        String string = "Does not appear to have parameters.";
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.show();
      } else if(message.matches(urlRegex)) {
        Intent intent = new Intent(this, FilterParameters.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivityForResult(intent, FILTER_PARAMETERS);
      } else {
        Context context = getApplicationContext();
        String string = "Does not match URL pattern.";
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.show();
      }
    } catch(Exception e) {
      Log.w("Error", e);

    }
  }

  public void shortenLink(View view) {
    try{
      // Send link to be shortened, record initial value and change button to 'Undo'
      String status = view.getTag().toString();
      if(status.equals("1")) {
        undoValue = editText.getText().toString();
        if(undoValue.matches(urlRegex)) {
          ShortenLink shortenLink = new ShortenLink();
          shortenLink.setASyncListener(this);
          shortenLink.execute(undoValue);
          view.setTag(0);
        } else {
          Context context = getApplicationContext();
          String string = "Does not match URL pattern.";
          Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
          toast.show();
        }
      } else {
        editText.setText(undoValue);
        undoValue = "";
        shortenText.setText("Shorten");
        view.setTag(1);
      }
    } catch(Exception e) {
      Log.w("Error", e);
    }
  }

  public void sendResult(String result) {
    // Receive shortened URL from ShortenLink class
    editText.setText(result);
    shortenText.setText("Undo");
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    String sharedText = data.getStringExtra("URL");
    if (sharedText != null) {
      editText.setText(sharedText);
    }
  }
}