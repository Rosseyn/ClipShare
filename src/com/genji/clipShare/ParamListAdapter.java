package com.genji.clipShare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ParamListAdapter extends ArrayAdapter<QueryParamView> {
  public ParamListAdapter(Context context, int textViewResourceId) {
    super(context, textViewResourceId);
  }

  private List<QueryParamView> params;

  public ParamListAdapter(Context context, int resource, List<QueryParamView> params) {
    super(context, resource, params);
    this.params = params;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    View v = convertView;

    if(v == null) {
      LayoutInflater vi;
      vi = LayoutInflater.from(getContext());
      v = vi.inflate(R.layout.param_view, null);
    }

    QueryParamView p = params.get(position);

    if(p != null) {
      TextView key = (TextView) v.findViewById(R.id.paramItemKey);
      TextView value = (TextView) v.findViewById(R.id.paramItemValue);

      if(key != null) {
        key.setText(p.getKey());
      }

      if(value != null) {
        value.setText(p.getValue());
      } else {
        value.setText("*No value set*");
      }
    }

    return v;
  }
}
