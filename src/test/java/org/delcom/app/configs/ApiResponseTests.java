package org.delcom.app.configs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApiResponseTests {
    @Test
    @DisplayName("Menggunakan konstruktor ApiResponse dengan benar")
    void testMenggunakanKonstruktorApiResponse() throws Exception {
        ApiResponse<String> response = new ApiResponse<>("success", "Operasi berhasil", "Data hasil");

        assert (response.getStatus().equals("success"));
        assert (response.getMessage().equals("Operasi berhasil"));
        assert (response.getData().equals("Data hasil"));
    }

    @Test
    void testApiResponseSettersAndGetters() {
        // Test constructor 1
        ApiResponse<String> response1 = new ApiResponse<>("success", "OK", "Data");
        assertEquals("success", response1.getStatus());
        assertEquals("OK", response1.getMessage());
        assertEquals("Data", response1.getData());

        // Test constructor 2
        ApiResponse<Object> response2 = new ApiResponse<>("fail", "Error");
        assertNull(response2.getData());

        // Test setters
        response2.setStatus("error");
        response2.setMessage("Fatal Error");
        response2.setData(null);

        assertEquals("error", response2.getStatus());
        assertEquals("Fatal Error", response2.getMessage());
        assertNull(response2.getData());
    }

}
