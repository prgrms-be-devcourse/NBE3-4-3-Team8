package com.ll.nbe342team8.standard.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.text.StringEscapeUtils;


public class Ut {

    public static class str {
        public static boolean isBlank(String str) {
            return str == null || str.trim().isEmpty();
        }
    }

    public static class json {
        private static final ObjectMapper om = new ObjectMapper();

        @SneakyThrows
        public static String toString(Object obj) {
            return om.writeValueAsString(obj);
        }
    }

    public static class XSSSanitizer {
        public static String sanitize(String input) {
            return StringEscapeUtils.escapeHtml4(input);
        }
    }
}
