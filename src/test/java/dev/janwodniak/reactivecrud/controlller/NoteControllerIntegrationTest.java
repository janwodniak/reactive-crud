package dev.janwodniak.reactivecrud.controlller;

import dev.janwodniak.reactivecrud.test.TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.http.request.HttpQueryParams;

import java.util.Map;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.Matchers.equal;
import static org.testingisdocumenting.webtau.WebTauCore._______________________________________________________;
import static org.testingisdocumenting.webtau.WebTauCore.table;
import static org.testingisdocumenting.webtau.http.Http.http;

public class NoteControllerIntegrationTest extends BaseIntegrationTest {

    private final static String NOTES_URL = "api/v1/notes";

    @Nested
    class ShouldSearchNotes {

        private static TestCase<HttpQueryParams, TableData> createTestCase(String testName, HttpQueryParams input, TableData output) {
            return TestCase.<HttpQueryParams, TableData>builder()
                    .testName(testName)
                    .input(input)
                    .expectedOutput(output)
                    .build();
        }

        private static Stream<Arguments> provideSearchParameters() {
            return Stream.of(
                    createTestCase(
                            "Default params",
                            http.query(Map.of()),
                            table(
                                    "id", "title", "content", "date",
                                    _______________________________________________________,
                                    1, "AA", "Content for note AA", "2022-09-23 08:00:00",
                                    2, "AB", "Content for note AB", "2022-09-23 09:00:00",
                                    3, "AC", "Content for note AC", "2022-09-23 10:00:00",
                                    4, "AD", "Content for note AD", "2022-09-23 11:00:00",
                                    5, "AE", "Content for note AE", "2022-09-23 12:00:00"
                            )),
                    createTestCase(
                            "Search by title",
                            http.query("title", "AA"),
                            table(
                                    "id", "title", "content", "date",
                                    _______________________________________________________,
                                    1, "AA", "Content for note AA", "2022-09-23 08:00:00",
                                    27, "Aa", "Content for note Aa", "2022-09-24 10:00:00",
                                    1353, "aA", "Content for note aA", "2022-11-18 16:00:00",
                                    1379, "aa", "Content for note aa", "2022-11-19 18:00:00"
                            )),
                    createTestCase(
                            "Search by title, second page, 2 items, sort by ID ascending",
                            http.query("title", "AA", "pageNumber", "1", "pageSize", "2", "sortDirection", "ASC", "sortBy", "id"),
                            table(
                                    "id", "title", "content", "date",
                                    _______________________________________________________,
                                    1353, "aA", "Content for note aA", "2022-11-18 16:00:00",
                                    1379, "aa", "Content for note aa", "2022-11-19 18:00:00"
                            )),
                    createTestCase(
                            "Search by title, first page, 10 items, sort by content descending",
                            http.query("title", "AA", "pageNumber", "0", "pageSize", "10", "sortDirection", "DESC", "sortBy", "content"),
                            table(
                                    "id", "title", "content", "date",
                                    _______________________________________________________,
                                    1, "AA", "Content for note AA", "2022-09-23 08:00:00",
                                    27, "Aa", "Content for note Aa", "2022-09-24 10:00:00",
                                    1353, "aA", "Content for note aA", "2022-11-18 16:00:00",
                                    1379, "aa", "Content for note aa", "2022-11-19 18:00:00"
                            )),
                    createTestCase(
                            "Search by title, first page, 5 items, sort by date descending",
                            http.query("title", "AA", "pageNumber", "0", "pageSize", "5", "sortDirection", "DESC", "sortBy", "date"),
                            table(
                                    "id", "title", "content", "date",
                                    _______________________________________________________,
                                    1379, "aa", "Content for note aa", "2022-11-19 18:00:00",
                                    1353, "aA", "Content for note aA", "2022-11-18 16:00:00",
                                    27, "Aa", "Content for note Aa", "2022-09-24 10:00:00",
                                    1, "AA", "Content for note AA", "2022-09-23 08:00:00"
                            )),
                    createTestCase(
                            "Second page, 3 items, sort by title ascending",
                            http.query("title", "AA", "pageNumber", "1", "pageSize", "3", "sortDirection", "ASC", "sortBy", "title"),
                            table(
                                    "id", "title", "content", "date",
                                    _______________________________________________________,
                                    1, "AA", "Content for note AA", "2022-09-23 08:00:00"
                            )),
                    createTestCase(
                            "Second page, 2 items, sort by ID ascending",
                            http.query("pageNumber", "1", "pageSize", "2", "sortDirection", "ASC", "sortBy", "id"),
                            table(
                                    "id", "title", "content", "date",
                                    _______________________________________________________,
                                    3, "AC", "Content for note AC", "2022-09-23 10:00:00",
                                    4, "AD", "Content for note AD", "2022-09-23 11:00:00"
                            )),
                    createTestCase(
                            "First page, 10 items, sort by content descending",
                            http.query("pageNumber", "0", "pageSize", "10", "sortDirection", "DESC", "sortBy", "content"),
                            table(
                                    "id", "title", "content", "date",
                                    _______________________________________________________,
                                    1326, "ZZ", "Content for note ZZ", "2022-11-17 13:00:00",
                                    1352, "Zz", "Content for note Zz", "2022-11-18 15:00:00",
                                    2678, "zZ", "Content for note zZ", "2023-01-12 21:00:00",
                                    2704, "zz", "Content for note zz", "2023-01-13 23:00:00",
                                    1325, "ZY", "Content for note ZY", "2022-11-17 12:00:00",
                                    1351, "Zy", "Content for note Zy", "2022-11-18 14:00:00",
                                    2677, "zY", "Content for note zY", "2023-01-12 20:00:00",
                                    2703, "zy", "Content for note zy", "2023-01-13 22:00:00",
                                    1324, "ZX", "Content for note ZX", "2022-11-17 11:00:00",
                                    1350, "Zx", "Content for note Zx", "2022-11-18 13:00:00"
                            )),
                    createTestCase(
                            "First page, 5 items, sort by date descending",
                            http.query("pageNumber", "0", "pageSize", "5", "sortDirection", "DESC", "sortBy", "date"),
                            table(
                                    "id", "title", "content", "date",
                                    _______________________________________________________,
                                    2704, "zz", "Content for note zz", "2023-01-13 23:00:00",
                                    2703, "zy", "Content for note zy", "2023-01-13 22:00:00",
                                    2702, "zx", "Content for note zx", "2023-01-13 21:00:00",
                                    2701, "zw", "Content for note zw", "2023-01-13 20:00:00",
                                    2700, "zv", "Content for note zv", "2023-01-13 19:00:00"
                            )),
                    createTestCase(
                            "Second page, 3 items, sort by title ascending",
                            http.query("pageNumber", "1", "pageSize", "3", "sortDirection", "ASC", "sortBy", "title"),
                            table(
                                    "id", "title", "content", "date",
                                    _______________________________________________________,
                                    1, "AA", "Content for note AA", "2022-09-23 08:00:00",
                                    1380, "ab", "Content for note ab", "2022-11-19 19:00:00",
                                    1354, "aB", "Content for note aB", "2022-11-18 17:00:00"
                            ))
            ).map(Arguments::of);
        }

        @DisplayName("Should search notes")
        @ParameterizedTest(name = "{index} {0}")
        @MethodSource("provideSearchParameters")
        void shouldSearchNotes(TestCase<HttpQueryParams, TableData> testCase) {
            // given
            var queryParams = testCase.input();
            var tableData = testCase.expectedOutput();

            // when
            // then
            http.get(NOTES_URL, queryParams, (header, body) -> {
                header.statusCode.should(equal(200));
                body.get("content").should(equal(tableData));
            });
        }

    }

}
