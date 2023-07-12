package com.manitosdev.gcatcast.api.models;

/**
 * Created by gilbertohdz on 01/06/20.
 */
public class ApiResult<T> {

  private final T result;
  private String failureMessage;
  private Throwable error;

  public ApiResult(T result, String failureMessage, Throwable error) {
    this.result = result;
    this.failureMessage = failureMessage;
    this.error = error;
  }

  public T getResult() {
    return result;
  }

  public String getFailureMessage() {
    return failureMessage;
  }

  public Throwable getError() {
    return error;
  }
}
