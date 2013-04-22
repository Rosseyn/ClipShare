package com.genji.clipShare;

public class QueryParamView {
  public String key, value;

  public QueryParamView(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return this.key;
  }

  public String getValue() {
    return this.value;
  }
}
