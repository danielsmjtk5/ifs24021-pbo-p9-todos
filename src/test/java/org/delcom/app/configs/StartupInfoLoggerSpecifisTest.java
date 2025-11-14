package org.delcom.app.configs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit test murni untuk StartupInfoLogger untuk memastikan 100% branch coverage
 * tanpa perlu memuat seluruh konteks Spring.
 */
class StartupInfoLoggerSpecificTest {

    private StartupInfoLogger startupInfoLogger;
    private ConfigurableEnvironment mockEnvironment;
    private ApplicationReadyEvent mockEvent;

    @BeforeEach
    void setUp() {
        startupInfoLogger = new StartupInfoLogger();
        mockEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        ConfigurableApplicationContext mockContext = Mockito.mock(ConfigurableApplicationContext.class);
        mockEvent = Mockito.mock(ApplicationReadyEvent.class);
        
        when(mockEvent.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getEnvironment()).thenReturn(mockEnvironment);

        // ======================================================================
        // TAMBAHKAN BLOK INI UNTUK MELENGKAPI PERILAKU MOCK
        // ======================================================================
        // Atur perilaku default untuk semua properti yang dibutuhkan oleh metode onApplicationEvent
        when(mockEnvironment.getProperty("server.port", "8080")).thenReturn("8080");
        when(mockEnvironment.getProperty(eq("spring.devtools.livereload.enabled"), eq(Boolean.class), any(Boolean.class)))
            .thenReturn(false);
        when(mockEnvironment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        // ======================================================================
    }

    @Test
    void testOnApplicationEvent_withRootContextPath() {
        when(mockEnvironment.getProperty("server.servlet.context-path")).thenReturn("/");
        startupInfoLogger.onApplicationEvent(mockEvent);
    }
    
    @Test
    void testOnApplicationEvent_withNoContextPath() {
        when(mockEnvironment.getProperty("server.servlet.context-path")).thenReturn(null);
        startupInfoLogger.onApplicationEvent(mockEvent);
    }

    @Test
    void testOnApplicationEvent_withCustomContextPath() {
        when(mockEnvironment.getProperty("server.servlet.context-path")).thenReturn("/api");
        startupInfoLogger.onApplicationEvent(mockEvent);
    }
}