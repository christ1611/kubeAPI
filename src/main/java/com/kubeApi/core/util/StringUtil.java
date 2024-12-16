package com.kubeApi.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Formatter;

@Slf4j
@Component
public class StringUtil {

    public String makeFullPath(String path) {
        return String.format(path);
    }

    public String makeFullPath(String path, String param1) {
        return String.format(path, param1);
    }

    public String makeFullPath(String path, String param1, String param2) {
        return String.format(path, param1, param2);
    }

    public String makeCustomPath(String msgFmt, Object... args) {
        return new Formatter().format(msgFmt, args).toString();
    }

}
