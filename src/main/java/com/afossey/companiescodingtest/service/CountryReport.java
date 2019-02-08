package com.afossey.companiescodingtest.service;

import java.util.ArrayList;
import java.util.List;

public class CountryReport {

  private List<Float> fundings = new ArrayList<>();
  private float sum = 0f;
  private int companyCount = 0;


  public void addFunding(Float funding) {
    this.fundings.add(funding);
    this.sum += funding;
    this.companyCount++;
  }

  public int getCompanyCount() {
    return this.companyCount;
  }

  public float getAverageFunding() {
    if (this.companyCount == 0) {
      return 0f;
    }
    return this.sum / this.companyCount;
  }
}
