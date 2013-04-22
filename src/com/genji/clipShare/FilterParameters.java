package com.genji.clipShare;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FilterParameters extends ListActivity {
  private ListView mainListView;
  private ParamListAdapter listAdapter;
  private String url;
  private List<QueryParamView> params;
  private ArrayList<Integer> excludedIdList;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.filter_parameters);
    excludedIdList = new ArrayList<Integer>();
    mainListView = getListView();

    Intent intent = getIntent();
    String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
    params = new ArrayList<QueryParamView>();
    try {
      if(message.matches(".*\\?.*")) {
        String[] urlPieces = message.split("\\?");
        url = urlPieces[0];
        int last = urlPieces.length - 1;
        if(urlPieces[last].matches(".*&.*")) {
          String[] pairs = urlPieces[last].split("&");
          String[] pair;
          int pairsLength = pairs.length;
          for(int i = 0;i < pairsLength;i++) {
            pair = pairs[i].split("=");
            params.add(new QueryParamView(pair[0], pair[1]));
          }
        } else {
          String[] pair = urlPieces[1].split("=");
          params.add(new QueryParamView(pair[0], pair[1]));
        }
        listAdapter = new ParamListAdapter(this, R.layout.param_view, params);
        mainListView.setAdapter(listAdapter);
//        mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
      } else {

      }
    } catch(Exception e) {
      Log.w("Filter Params Error", e);
    }
  }

  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    try{
      int excIdIndex = excludedIdList.indexOf(position);
      if(excIdIndex == -1) {
        excludedIdList.add(position);
      } else {
        excludedIdList.remove(excIdIndex);
      }
    } catch(Exception e) {
      Log.w("Click Error", e);
    }
  }

  public void selectNone(View view) {
    try {
      for (int i=0;i<mainListView.getChildCount();i++) {
        if(mainListView.isItemChecked(i)) {
          Log.w("Item Checked?", "Yes");
          View rowView = this.getListView().getChildAt(i);
          int rowId = rowView.getId();
          Log.w("Item Checked?", rowView.toString() + " : " + rowId);
          mainListView.performItemClick(rowView, i, rowId);
//          mainListView.setItemChecked(i, false);
        }
      }
    } catch(Exception e) {
      Log.w("Select All Error", e);
    }
  }

  public void selectAll(View view) {
    try {
      int listCount = mainListView.getChildCount();
      for (int i=0;i<listCount;i++) {
        if(!mainListView.isItemChecked(i)) {
          Log.w("Item Checked?", "No");
          View rowView = this.getListView().getChildAt(i);
          int rowId = rowView.getId();
          Log.w("Item Checked?", rowView.toString() + " : " + rowId);
          mainListView.performItemClick(rowView, i, rowId);
//          this.getListView().getChildAt(i).performClick();
//          this.getListView().performItemClick(this.getListView(), i, this.getListAdapter().getItemId(i));
//          mainListView.setItemChecked(i, true);
        }
      }
    } catch(Exception e) {
      Log.w("Select All Error", e);
    }
  }

  public void filterConfirm(View view) {
    try {
      int paramSize = params.size();
      String rebuiltUrl = url;
      boolean first = true;
      for(int i=0;i<paramSize;i++) {
        if(excludedIdList.indexOf(i) == -1) {
          if(first) {
            rebuiltUrl += "?";
          } else {
            rebuiltUrl += "&";
          }
          rebuiltUrl += params.get(i).key + "=" + params.get(i).value;
          first = false;
        }
      }
      Intent intent = new Intent();
      intent.putExtra("URL", rebuiltUrl);
      setResult(RESULT_OK, intent);
      this.finish();
    } catch(Exception e) {
      Log.w("Send Error", e);
    }
  }
}
