package ru.practicum.shareit.error.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ErrorResponseTest {
    private final JacksonTester<ErrorResponse> json;

    @Test
    void testErrorResponse() throws Exception {
        JsonContent<ErrorResponse> result = json.write(new ErrorResponse("test"));

        assertThat(result).extractingJsonPathStringValue("$.error").isEqualTo("test");
    }
}