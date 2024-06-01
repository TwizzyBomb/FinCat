package hesh.zone.fincat.model;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Breakdown {
  double total;
  
  /* first key for each cat will be "total" */
  private HashMap<String, HashMap<String, Double>> breakdownList;
  
  public Breakdown(double t, HashMap<String, HashMap<String, Double>> bd) {
    this.total = t;
    this.breakdownList = bd;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    DecimalFormat df = new DecimalFormat("0.00");
    double parentTotal = 0.0;
    sb.append("Breakdown:\n");
    // outer parent map > child maps
    for (Map.Entry<String, HashMap<String, Double>> outerEntry : breakdownList.entrySet()) {
      // inner child map > cat,totals
      for (Map.Entry<String, Double> innerEntry : outerEntry.getValue().entrySet()) {
        // first entry should always be total
        if ("total".equals(innerEntry.getKey())) {
          parentTotal = innerEntry.getValue();
          continue;
        }
        sb.append( innerEntry.getValue()
                + " towards " + innerEntry.getKey() + "\t\t"
                + df.format((innerEntry.getValue() / total) * 100)
                + "% of total spent this month" + "\n");
      }
      sb.append("Total percent for " + outerEntry.getKey() + ": "
              + df.format((parentTotal / total) * 100) + "%\n");
    }
    return sb.toString();
  }
  
  public HashMap<String, HashMap<String, Double>> getBreakdownList() {
    return breakdownList;
  }
  
  public void setBreakdownList(HashMap<String, HashMap<String, Double>> breakdownList) {
    this.breakdownList = breakdownList;
  }
  
  public double getTotal() {
    return total;
  }
  
  public void setTotal(double total) {
    this.total = total;
  }
}
