package com.afossey.companiescodingtest;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CountryReport {

  private List<Float> fundings = new ArrayList<>();

  public void addFunding(Float funding) {
    this.fundings.add(funding);
  }

  public int getCompanyCount() {
    return this.fundings.size();
  }

  public float getAverageFunding() {
    float sum = this.fundings.stream().reduce(0f, (a, b) -> a + b);
    int count = getCompanyCount();
    if (count > 0) {
      return sum / count;
    } else {
      return 0f;
    }
  }
}
