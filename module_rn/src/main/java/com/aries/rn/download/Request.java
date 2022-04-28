package com.aries.rn.download;

public class Request {

  private String mUrl;

  public Request(String mUrl) {
    this.mUrl = mUrl;
  }

  public String getUrl() {
    return mUrl;
  }

  public static class Builder {
    private String url;

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Request build() {
      return new Request(this.url);
    }
  }
}
