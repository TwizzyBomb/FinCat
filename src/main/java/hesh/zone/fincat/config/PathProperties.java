package hesh.zone.fincat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="hesh.paths")
public class PathProperties {
    private String test;
    private String chargeList;
    private String incomeList;
    private String testFileSmall;
    private String testFile;
    private String pipeFile;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getChargeList() {
        return chargeList;
    }

    public void setChargeList(String chargeList) {
        this.chargeList = chargeList;
    }

    public String getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(String incomeList) {
        this.incomeList = incomeList;
    }

    public String getTestFileSmall() {
        return testFileSmall;
    }

    public void setTestFileSmall(String testFileSmall) {
        this.testFileSmall = testFileSmall;
    }

    public String getTestFile() {
        return testFile;
    }

    public void setTestFile(String testFile) {
        this.testFile = testFile;
    }

    public String getPipeFile() {
        return pipeFile;
    }

    public void setPipeFile(String pipeFile) {
        this.pipeFile = pipeFile;
    }
}
