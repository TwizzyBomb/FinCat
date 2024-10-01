package hesh.zone.fincat.config;

import org.springframework.beans.factory.annotation.Value;

public class Constants {

    @Value("hesh.paths.charge")
    public static String CHARGE_LIST_PATH; // "artifacts/out/charge_list.json";

    @Value("hesh.paths.income")
    public static String INCOME_LIST_PATH; // "artifacts/out/income_list.json";

    @Value("hesh.paths.test_small")
    public static String TEST_SMALL_PATH; // "artifacts/sample_files/CheckingTestSmall.csv";

    @Value("hesh.paths.test_empty_file")
    public static String TEST_EMPTY_FILE_PATH; // "artifacts/sample_files/testEmpty.csv";

    @Value("hesh.paths.test_file")
    public static String TEST_FILE_PATH; // "artifacts/sample_files/CheckingTest.csv";

    @Value("hesh.paths.pipe_file")
    public static String PIPE_FILE_PATH; // "artifacts/sample_files/PipeDelimitedTransactions.csv";

}
