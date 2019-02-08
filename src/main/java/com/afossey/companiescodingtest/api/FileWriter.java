package com.afossey.companiescodingtest.api;

import java.io.IOException;

public interface FileWriter<T> {

  void write(T content) throws IOException;
}
