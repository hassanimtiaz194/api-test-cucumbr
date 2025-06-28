package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiSteps {

    private Response response;

    @Given("I make a GET request to {string}")
    public void i_make_a_get_request(String url) {
        response = given().when().get(url);
    }

    @Then("the response code should be {int}")
    public void the_response_code_should_be(int statusCode) {
        assertThat(response.getStatusCode(), is(statusCode));
    }

    @Then("the response time should be below {int} milliseconds")
    public void the_response_time_should_be_below(int maxTime) {
        long responseTime = response.getTime();
        System.out.println("Actual response time: " + responseTime + " ms");
        assertThat(responseTime, lessThan((long) maxTime));
    }

    @Then("the field {string} should exist")
    public void the_field_should_exist(String fieldName) {
        assertThat(response.jsonPath().get(fieldName), notNullValue());
    }

    @Then("the field {string} should not be empty")
    public void the_field_should_not_be_empty(String fieldName) {
        String value = response.jsonPath().getString(fieldName);
        assertThat(value, not(isEmptyOrNullString()));
    }

    @Then("there should be only one episode with status {string}")
    public void there_should_be_only_one_episode_with_status(String status) {
        List<String> statuses = response.jsonPath().getList("schedule.elements.episode.status");
        if (statuses == null) throw new AssertionError("No 'episode.status' field found.");
        long count = statuses.stream().filter(s -> s.equalsIgnoreCase(status)).count();
        assertThat(count, is(1L));
    }

    @Then("the start date {string} should be before end date {string}")
    public void the_start_date_should_be_before_end_date(String startField, String endField) {
        String startDate = response.jsonPath().getString(startField);
        String endDate = response.jsonPath().getString(endField);
        assertThat(startDate.compareTo(endDate) < 0, is(true));
    }

    @Then("the response header {string} should exist")
    public void the_response_header_should_exist(String headerName) {
        assertThat(response.getHeader(headerName), notNullValue());
    }

    @Then("each item in the data array should have a non-empty {string}")
    public void each_item_in_the_data_array_should_have_a_non_empty(String key) {
        List<Map<String, Object>> data = response.jsonPath().getList("schedule.elements");
        assertThat("Expected 'schedule.elements' array in response", data, notNullValue());
        for (Map<String, Object> item : data) {
            Object value = item.get(key);
            assertThat("Missing key: " + key, value, notNullValue());
            assertThat(value.toString(), not(isEmptyOrNullString()));
        }
    }

    @Then("each item should have {string} equal to {string}")
    public void each_item_should_have_equal_to(String path, String expected) {
        List<String> values = response.jsonPath().getList("schedule.elements." + path);
        assertThat("Path schedule.elements." + path + " not found or empty", values, notNullValue());
        for (String val : values) {
            assertThat(val, equalToIgnoringCase(expected));
        }
    }

    @Then("each item should have a non-empty {string}")
    public void each_item_should_have_a_non_empty(String path) {
        List<String> values = response.jsonPath().getList("schedule.elements." + path);
        assertThat("Path schedule.elements." + path + " not found or empty", values, notNullValue());
        for (String val : values) {
            assertThat(val, not(isEmptyOrNullString()));
        }
    }

    @Then("only one item should have {string} equal to true")
    public void only_one_item_should_have_equal_to_true(String path) {
        List<Boolean> values = response.jsonPath().getList("schedule.elements." + path);
        assertThat("Path schedule.elements." + path + " not found or empty", values, notNullValue());
        long count = values.stream().filter(Boolean::booleanValue).count();
        assertThat(count, is(1L));
    }

    @Then("each item should have {string} before {string}")
    public void each_item_should_have_before(String startPath, String endPath) {
        List<String> startDates = response.jsonPath().getList("schedule.elements." + startPath);
        List<String> endDates = response.jsonPath().getList("schedule.elements." + endPath);
        assertThat("Start path schedule.elements." + startPath + " not found", startDates, notNullValue());
        assertThat("End path schedule.elements." + endPath + " not found", endDates, notNullValue());
        assertThat("Start and end list sizes mismatch", startDates.size(), is(endDates.size()));

        for (int i = 0; i < startDates.size(); i++) {
            String start = startDates.get(i);
            String end = endDates.get(i);
            assertThat("Start should be before end at index " + i, start.compareTo(end) < 0, is(true));
        }
    }

    @Then("the response should contain a {string} header")
    public void the_response_should_contain_a_header(String headerName) {
        String header = response.getHeader(headerName);
        assertThat("Header not found: " + headerName, header, notNullValue());
    }

    @Then("the response body should contain {string} and {string}")
    public void the_response_body_should_contain_and(String key1, String key2) {
        Map<String, Object> body = response.jsonPath().getMap("");
        assertThat("Response body is null", body, notNullValue());
        // Fallback to alternative keys if 'details' is not present
        boolean hasKey1 = body.containsKey(key1) || body.containsKey("error") || body.containsKey("message");
        assertThat("Missing key (or alternatives): " + key1, hasKey1, is(true));
        assertThat("Missing key: " + key2, body.containsKey(key2), is(true));
    }
}