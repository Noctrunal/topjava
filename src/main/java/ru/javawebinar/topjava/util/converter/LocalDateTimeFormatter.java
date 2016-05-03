package ru.javawebinar.topjava.util.converter;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.TimeUtil;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
    @Override
    public LocalDateTime parse(String dateTime, Locale locale) throws ParseException {
        return TimeUtil.parseLocalDateTime(dateTime);
    }

    @Override
    public String print(LocalDateTime dateTime, Locale locale) {
        return TimeUtil.toString(dateTime);
    }
}
