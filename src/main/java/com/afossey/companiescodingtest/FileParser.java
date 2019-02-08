package com.afossey.companiescodingtest;

import io.reactivex.Flowable;
import java.io.File;

public interface FileParser<T> {
  Flowable<T> parse(File file);
}
