package com.afossey.companiescodingtest;

import java.io.IOException;

public interface FileWriter<T> {
  void write(T content) throws IOException;
}
