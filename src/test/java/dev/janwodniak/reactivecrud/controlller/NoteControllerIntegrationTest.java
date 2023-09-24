package dev.janwodniak.reactivecrud.controlller;

import dev.janwodniak.reactivecrud.test.TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testingisdocumenting.webtau.data.table.TableData;
import org.testingisdocumenting.webtau.http.request.HttpQueryParams;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;

import java.util.Map;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.Matchers.containAll;
import static org.testingisdocumenting.webtau.Matchers.equal;
import static org.testingisdocumenting.webtau.WebTauCore.________________________________;
import static org.testingisdocumenting.webtau.WebTauCore._____________________________________;
import static org.testingisdocumenting.webtau.WebTauCore._______________________________________;
import static org.testingisdocumenting.webtau.WebTauCore._______________________________________________________;
import static org.testingisdocumenting.webtau.WebTauCore.table;
import static org.testingisdocumenting.webtau.http.Http.http;

public class NoteControllerIntegrationTest extends BaseIntegrationTest {

    private final static String NOTES_URL = "api/v1/notes";

    private static TestCase<HttpQueryParams, TableData> createTestCase(String testName, HttpQueryParams input, TableData output) {
        return TestCase.<HttpQueryParams, TableData>builder()
                .testName(testName)
                .input(input)
                .expectedOutput(output)
                .build();
    }

    private static TestCase<HttpRequestBody, TableData> createTestCase(String testName, HttpRequestBody input, TableData output) {
        return TestCase.<HttpRequestBody, TableData>builder()
                .testName(testName)
                .input(input)
                .expectedOutput(output)
                .build();
    }

    private static TestCase<HttpRequestBody, HttpRequestBody> createTestCase(String testName, HttpRequestBody input, HttpRequestBody output) {
        return TestCase.<HttpRequestBody, HttpRequestBody>builder()
                .testName(testName)
                .input(input)
                .expectedOutput(output)
                .build();
    }


    @Nested
    class ShouldSearchNotes {

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

    @Nested
    class ShouldNotSearchNotes {

        private static final String PAGE_NOT_NEGATIVE = "PAGE_NOT_NEGATIVE";
        private static final String PAGE_SIZE_NOT_LESS_THAN_ONE = "PAGE_SIZE_NOT_LESS_THAN_ONE";
        private static final String INVALID_SORT_DIRECTION = "INVALID_SORT_DIRECTION";
        private static final String INVALID_SORT_BY_VALUE_FIELD = "INVALID_SORT_BY_VALUE_FIELD";

        private static Stream<Arguments> provideInvalidSearchParameters() {
            return Stream.of(
                    createTestCase(
                            "Negative page number",
                            http.query("pageNumber", "-1"),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "pageNumber", PAGE_NOT_NEGATIVE
                            )),
                    createTestCase(
                            "Zero page size",
                            http.query("pageSize", "0"),
                            table(
                                    "field", "message",
                                    _______________________________________,
                                    "pageSize", PAGE_SIZE_NOT_LESS_THAN_ONE
                            )),
                    createTestCase(
                            "Invalid sort direction",
                            http.query("sortDirection", "INVALID"),
                            table(
                                    "field", "message",
                                    _______________________________________,
                                    "sortDirection", INVALID_SORT_DIRECTION
                            )),
                    createTestCase(
                            "Invalid sort by field",
                            http.query("sortBy", "unknownField"),
                            table(
                                    "field", "message",
                                    _____________________________________,
                                    "sortBy", INVALID_SORT_BY_VALUE_FIELD
                            )),
                    createTestCase(
                            "Negative page number with zero page size",
                            http.query("pageNumber", "-1", "pageSize", "0"),
                            table(
                                    "field", "message",
                                    _______________________________________,
                                    "pageNumber", PAGE_NOT_NEGATIVE,
                                    "pageSize", PAGE_SIZE_NOT_LESS_THAN_ONE
                            )),
                    createTestCase(
                            "Invalid sort direction with invalid sort by field",
                            http.query("sortDirection", "INVALID", "sortBy", "unknownField"),
                            table(
                                    "field", "message",
                                    _______________________________________,
                                    "sortDirection", INVALID_SORT_DIRECTION,
                                    "sortBy", INVALID_SORT_BY_VALUE_FIELD
                            )),
                    createTestCase(
                            "All params invalid",
                            http.query("pageNumber", "-1", "pageSize", "0", "sortDirection", "INVALID", "sortBy", "unknownField"),
                            table(
                                    "field", "message",
                                    _______________________________________,
                                    "pageNumber", PAGE_NOT_NEGATIVE,
                                    "pageSize", PAGE_SIZE_NOT_LESS_THAN_ONE,
                                    "sortDirection", INVALID_SORT_DIRECTION,
                                    "sortBy", INVALID_SORT_BY_VALUE_FIELD
                            ))
            ).map(Arguments::of);
        }

        @DisplayName("Should not search notes")
        @ParameterizedTest(name = "{index} {0}")
        @MethodSource("provideInvalidSearchParameters")
        void shouldNotSearchNotesWithInvalidParams(TestCase<HttpQueryParams, TableData> testCase) {
            // given
            var queryParams = testCase.input();
            var tableData = testCase.expectedOutput();

            // when
            // then
            http.get(NOTES_URL, queryParams, (header, body) -> {
                header.statusCode.should(equal(400));
                body.should(containAll(tableData));
            });
        }

    }

    @Nested
    class ShouldGetNoteById {

        @DisplayName("Should get note by ID")
        @Test
        void shouldGetNoteById() {
            // given
            var id = 1L;
            var expectedResponse = http.json(
                    "id", id,
                    "title", "AA",
                    "content", "Content for note AA",
                    "date", "2022-09-23 08:00:00"
            );

            // when
            // then
            http.get(NOTES_URL + "/" + id, (header, body) -> {
                header.statusCode.should(equal(200));
                body.should(equal(expectedResponse));
            });
        }

    }

    @Nested
    class ShouldNotGetNoteById {

        @DisplayName("Should not get note by ID if does not exist")
        @Test
        void shouldNotGetNoteById() {
            // given
            var nonExistingId = 9999L;
            var expectedResponse = http.json(
                    "timestamp", "2023-09-21 21:45:00",
                    "httpStatusCode", 404,
                    "httpStatus", "NOT_FOUND",
                    "reason", "Not Found",
                    "message", "NOTE_WITH_ID_9999_NOT_FOUND"
            );

            // when
            // then
            http.get(NOTES_URL + "/" + nonExistingId, (header, body) -> {
                header.statusCode.should(equal(404));
                body.should(equal(expectedResponse));
            });
        }

    }

    @Nested
    class ShouldCreateNote {

        @DisplayName("Should create note")
        @Test
        void shouldCreateNote() {
            // given
            var input = http.json(
                    "title", "TestNote",
                    "content", "Test note content"
            );

            var expectedId = 2705L;
            var expectedResponse = http.json(
                    "id", expectedId,
                    "title", "TestNote",
                    "content", "Test note content",
                    "date", "2023-09-21 21:45:00"
            );

            // when
            // then
            http.post(NOTES_URL, input, (header, body) -> {
                header.statusCode.should(equal(201));
                body.should(equal(expectedResponse));
            });

            http.get(NOTES_URL + "/" + expectedId, (header, body) -> {
                header.statusCode.should(equal(200));
                body.should(equal(expectedResponse));
            });
        }

    }

    @Nested
    class ShouldNotCreateNote {

        private static final String TITLE_TOO_LONG = "TITLE_TOO_LONG";
        private static final String TITLE_REQUIRED = "TITLE_REQUIRED";
        private static final String TITLE_INVALID_FORMAT = "TITLE_INVALID_FORMAT";
        private static final String CONTENT_REQUIRED = "CONTENT_REQUIRED";
        private static final String CONTENT_INVALID_FORMAT = "CONTENT_INVALID_FORMAT";

        private static Stream<Arguments> provideInvalidNoteData() {
            return Stream.of(
                    createTestCase(
                            "Special character content",
                            http.json(
                                    "title", "ValidTitle",
                                    "content", "A.*"
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "content", CONTENT_INVALID_FORMAT
                            )
                    ),
                    createTestCase(
                            "Null title and null content",
                            http.json(
                                    "title", null,
                                    "content", null
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "title", TITLE_REQUIRED,
                                    "content", CONTENT_REQUIRED
                            )
                    ),
                    createTestCase(
                            "Null title",
                            http.json(
                                    "title", null,
                                    "content", "Valid Content"),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "title", TITLE_REQUIRED
                            )
                    ),
                    createTestCase(
                            "Null content",
                            http.json(
                                    "title", "ValidTitle",
                                    "content", null
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "content", CONTENT_REQUIRED
                            )
                    ),
                    createTestCase(
                            "Too long title and character content",
                            http.json(
                                    "title", "A".repeat(256),
                                    "content", "A.*"
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "title", TITLE_TOO_LONG,
                                    "content", CONTENT_INVALID_FORMAT
                            )
                    ),
                    createTestCase(
                            "Empty title",
                            http.json(
                                    "title", "",
                                    "content", "Valid content"
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "title", TITLE_INVALID_FORMAT
                            )
                    ),
                    createTestCase(
                            "Null content and special character title",
                            http.json(
                                    "title", "A.",
                                    "content", null
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "title", TITLE_INVALID_FORMAT,
                                    "content", CONTENT_REQUIRED
                            )
                    )
            ).map(Arguments::of);
        }

        @DisplayName("Should not create note with invalid data")
        @ParameterizedTest(name = "{index} {0}")
        @MethodSource("provideInvalidNoteData")
        void shouldNotCreateNoteWithInvalidData(TestCase<HttpRequestBody, TableData> testCase) {
            // given
            var requestBody = testCase.input();
            var tableData = testCase.expectedOutput();

            // when
            // then
            http.post(NOTES_URL, requestBody, (header, body) -> {
                header.statusCode.should(equal(400));
                body.should(containAll(tableData));
            });
        }

    }

    @Nested
    class ShouldDeleteNote {

        private static final String NOTE_WITH_ID_NOT_FOUND = "NOTE_WITH_ID_%d_NOT_FOUND";

        void ensureNoteExists(Long id) {
            var expectedResponse = http.json(
                    "id", id,
                    "title", "AA",
                    "content", "Content for note AA",
                    "date", "2022-09-23 08:00:00"
            );

            http.get(NOTES_URL + "/" + id, (header, body) -> {
                header.statusCode.should(equal(200));
                body.should(equal(expectedResponse));
            });
        }

        @DisplayName("Should delete note")
        @Test
        void shouldDeleteNote() {
            // given
            var noteId = 1L;

            // when
            ensureNoteExists(noteId);

            http.delete(NOTES_URL + "/" + noteId, (header, body) -> {
                header.statusCode.should(equal(204));
            });

            // then
            assertNoteNotFound(noteId);
        }

        private void assertNoteNotFound(Long id) {
            var expectedNotFoundResponse = http.json(
                    "timestamp", "2023-09-21 21:45:00",
                    "httpStatusCode", 404,
                    "httpStatus", "NOT_FOUND",
                    "reason", "Not Found",
                    "message", String.format(NOTE_WITH_ID_NOT_FOUND, id)
            );

            http.get(NOTES_URL + "/" + id, (header, body) -> {
                header.statusCode.should(equal(404));
                body.should(equal(expectedNotFoundResponse));
            });
        }

    }

    @Nested
    class ShouldNotDeleteNote {

        private static final String NOTE_WITH_ID_NOT_FOUND = "NOTE_WITH_ID_%d_NOT_FOUND";

        @DisplayName("Should not delete note if does not exist")
        @Test
        void shouldNotDeleteNoteIfDoesNotExist() {
            // given
            var nonExistingId = 9999L;

            // when
            assertNoteNotFound(nonExistingId);

            // then
            http.delete(NOTES_URL + "/" + nonExistingId, (header, body) -> {
                header.statusCode.should(equal(404));
                body.should(equal(getExpectedNotFoundResponse(nonExistingId)));
            });
        }

        private void assertNoteNotFound(Long id) {
            http.get(NOTES_URL + "/" + id, (header, body) -> {
                header.statusCode.should(equal(404));
                body.should(equal(getExpectedNotFoundResponse(id)));
            });
        }

        private HttpRequestBody getExpectedNotFoundResponse(Long id) {
            return http.json(
                    "timestamp", "2023-09-21 21:45:00",
                    "httpStatusCode", 404,
                    "httpStatus", "NOT_FOUND",
                    "reason", "Not Found",
                    "message", String.format(NOTE_WITH_ID_NOT_FOUND, id)
            );
        }

    }

    @Nested
    class ShouldEditNote {

        private static final String EDITION_DATE_FORMAT = "2023-09-21 21:45:00";

        static Stream<Arguments> provideEditNoteData() {
            return Stream.of(
                    createTestCase(
                            "Edit title",
                            http.json(
                                    "title", "NewTitle",
                                    "content", "Content for note AA"
                            ),
                            http.json(
                                    "id", 1,
                                    "title", "NewTitle",
                                    "content", "Content for note AA",
                                    "date", EDITION_DATE_FORMAT
                            )
                    ),
                    createTestCase(
                            "Edit content",
                            http.json(
                                    "title", "AA",
                                    "content", "New content"
                            ),
                            http.json(
                                    "id", 1,
                                    "title", "AA",
                                    "content", "New content",
                                    "date", EDITION_DATE_FORMAT
                            )
                    ),
                    createTestCase(
                            "Edit title and content",
                            http.json(
                                    "title", "NewTitle",
                                    "content", "New content"
                            ),
                            http.json(
                                    "id", 1,
                                    "title", "NewTitle",
                                    "content", "New content",
                                    "date", EDITION_DATE_FORMAT
                            )
                    )
            ).map(Arguments::of);
        }

        @MethodSource("provideEditNoteData")
        @ParameterizedTest(name = "{index} {0}")
        void shouldEditNote(TestCase<HttpRequestBody, HttpRequestBody> testCase) {
            // given
            var id = 1L;
            verifyInitialNoteState(id);

            // when
            modifyNoteAndAssert(id, testCase.input(), testCase.expectedOutput());

            // then
            verifyModifiedNoteState(id, testCase.expectedOutput());
        }

        private void verifyInitialNoteState(Long id) {
            var initialNote = http.json(
                    "id", id,
                    "title", "AA",
                    "content", "Content for note AA",
                    "date", "2022-09-23 08:00:00"
            );
            http.get(NOTES_URL + "/" + id, (header, body) -> {
                header.statusCode.should(equal(200));
                body.should(equal(initialNote));
            });
        }

        private void modifyNoteAndAssert(Long id, HttpRequestBody requestBody, HttpRequestBody expectedResponse) {
            http.put(NOTES_URL + "/" + id, requestBody, (header, body) -> {
                header.statusCode.should(equal(200));
                body.should(equal(expectedResponse));
            });
        }

        private void verifyModifiedNoteState(Long id, HttpRequestBody expectedResponse) {
            http.get(NOTES_URL + "/" + id, (header, body) -> {
                header.statusCode.should(equal(200));
                body.should(equal(expectedResponse));
            });
        }

    }

    @Nested
    class ShouldNotEditNote {

        private static final String TITLE_TOO_LONG = "TITLE_TOO_LONG";
        private static final String TITLE_REQUIRED = "TITLE_REQUIRED";
        private static final String TITLE_INVALID_FORMAT = "TITLE_INVALID_FORMAT";
        private static final String CONTENT_REQUIRED = "CONTENT_REQUIRED";
        private static final String CONTENT_INVALID_FORMAT = "CONTENT_INVALID_FORMAT";

        static Stream<Arguments> provideInvalidEditNoteData() {
            return Stream.of(
                    createTestCase(
                            "Special character content",
                            http.json(
                                    "title", "AA",
                                    "content", "A.*"
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "content", CONTENT_INVALID_FORMAT
                            )
                    ),
                    createTestCase(
                            "Invalid title and content",
                            http.json(
                                    "title", "A".repeat(256),
                                    "content", "A.*"
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "title", TITLE_TOO_LONG,
                                    "content", CONTENT_INVALID_FORMAT
                            )
                    ),
                    createTestCase(
                            "Null title",
                            http.json(
                                    "title", null,
                                    "content", "Valid Content"
                            ),
                            table("field", "message",
                                    ________________________________,
                                    "title", TITLE_REQUIRED)
                    ),
                    createTestCase(
                            "Empty title",
                            http.json(
                                    "title", "",
                                    "content", "Valid Content"
                            ),
                            table("field", "message",
                                    ________________________________,
                                    "title", TITLE_INVALID_FORMAT
                            )
                    ),
                    createTestCase(
                            "Null content and special character title",
                            http.json(
                                    "title", "A.",
                                    "content", null
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "title", TITLE_INVALID_FORMAT,
                                    "content", CONTENT_REQUIRED
                            )
                    ),
                    createTestCase(
                            "Long title and special character content",
                            http.json(
                                    "title", "A".repeat(256),
                                    "content", "A.*"
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "title", TITLE_TOO_LONG,
                                    "content", CONTENT_INVALID_FORMAT
                            )
                    ),
                    createTestCase(
                            "Null title and null content",
                            http.json(
                                    "title", null
                                    , "content", null
                            ),
                            table(
                                    "field", "message",
                                    ________________________________,
                                    "title", TITLE_REQUIRED,
                                    "content", CONTENT_REQUIRED
                            )
                    )
            ).map(Arguments::of);
        }

        @MethodSource("provideInvalidEditNoteData")
        @ParameterizedTest(name = "{index} {0}")
        void shouldNotEditNoteWithInvalidData(TestCase<HttpRequestBody, TableData> testCase) {
            // given
            var id = 1L;

            // when
            assertNoteState(id, initialNoteState(id));
            attemptNoteEdit(id, testCase.input(), testCase.expectedOutput());

            // then
            assertNoteState(id, initialNoteState(id));
        }

        private HttpRequestBody initialNoteState(Long id) {
            return http.json(
                    "id", id,
                    "title", "AA",
                    "content", "Content for note AA",
                    "date", "2022-09-23 08:00:00"
            );
        }

        private void assertNoteState(Long id, HttpRequestBody expectedState) {
            http.get(NOTES_URL + "/" + id, (header, body) -> {
                header.statusCode.should(equal(200));
                body.should(equal(expectedState));
            });
        }

        private void attemptNoteEdit(Long id, HttpRequestBody editRequest, TableData expectedErrors) {
            http.put(NOTES_URL + "/" + id, editRequest, (header, body) -> {
                header.statusCode.should(equal(400));
                body.should(containAll(expectedErrors));
            });
        }

        @Test
        void shouldNotEditNoteIfDoesNotExist() {
            // given
            var nonExistingId = 9999L;
            var requestBody = http.json(
                    "title", "NewTitle",
                    "content", "New content"
            );

            // when
            assertNoteNotFound(nonExistingId);

            // then
            attemptEditAndAssertNotFound(nonExistingId, requestBody);
        }

        private void assertNoteNotFound(Long id) {
            http.get(NOTES_URL + "/" + id, (header, body) -> {
                header.statusCode.should(equal(404));
                body.should(equal(noteNotFoundResponse(id)));
            });
        }

        private void attemptEditAndAssertNotFound(Long id, HttpRequestBody editRequest) {
            http.put(NOTES_URL + "/" + id, editRequest, (header, body) -> {
                header.statusCode.should(equal(404));
                body.should(equal(noteNotFoundResponse(id)));
            });
        }

        private HttpRequestBody noteNotFoundResponse(Long id) {
            return http.json(
                    "timestamp", "2023-09-21 21:45:00",
                    "httpStatusCode", 404,
                    "httpStatus", "NOT_FOUND",
                    "reason", "Not Found",
                    "message", String.format("NOTE_WITH_ID_%d_NOT_FOUND", id)
            );
        }

    }

}
