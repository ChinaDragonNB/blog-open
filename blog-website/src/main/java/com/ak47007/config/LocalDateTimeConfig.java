package com.ak47007.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author AK47007
 * @date 2019/11/22
 * LocalDate/LocalTime/LocalDateTime 时间格式化配置
 */
@Configuration
public class LocalDateTimeConfig implements Jackson2ObjectMapperBuilderCustomizer {

    /**
     * LocalDate格式
     */
    private final String localDatePattern = "yyyy-MM-dd";

    /**
     * LocalTime格式
     */
    private final String localTimePattern = "HH:mm:ss";

    /**
     * LocalDateTime格式
     */
    private final String localDateTimePattern = localDatePattern + " " + localTimePattern;


    /**
     * LocalDate 类格式化
     */
    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            /**
             * 输出时格式化
             */
            @Override
            public String print(LocalDate localDate, Locale locale) {
                return localDate.format(DateTimeFormatter.ofPattern(localDatePattern));
            }

            /**
             * 写入时格式化
             */
            @Override
            public LocalDate parse(String s, Locale locale) throws ParseException {
                return LocalDate.parse(s, DateTimeFormatter.ofPattern(localDatePattern));
            }
        };
    }

    /**
     * LocalTime 类格式化
     */
    @Bean
    public Formatter<LocalTime> localTimeFormatter() {
        return new Formatter<LocalTime>() {
            /**
             * 输出时格式化
             */
            @Override
            public String print(LocalTime localTime, Locale locale) {
                return localTime.format(DateTimeFormatter.ofPattern(localTimePattern));
            }

            /**
             * 写入时格式化
             */
            @Override
            public LocalTime parse(String s, Locale locale) throws ParseException {
                return LocalTime.parse(s, DateTimeFormatter.ofPattern(localTimePattern));
            }
        };
    }

    /**
     * LocalDateTime 类格式化
     */
    @Bean
    public Formatter<LocalDateTime> localDateTimeFormatter() {
        return new Formatter<LocalDateTime>() {
            /**
             * 输出时格式化
             */
            @Override
            public String print(LocalDateTime localDateTime, Locale locale) {
                return localDateTime.format(DateTimeFormatter.ofPattern(localDateTimePattern));
            }

            /**
             * 写入时格式化
             */
            @Override
            public LocalDateTime parse(String s, Locale locale) throws ParseException {
                return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(localDateTimePattern));
            }
        };
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        serialize(javaTimeModule);
        deserialize(javaTimeModule);
        jacksonObjectMapperBuilder.modules(javaTimeModule);
    }


    /**
     * 序列化
     */
    private void serialize(JavaTimeModule javaTimeModule) {
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(localDatePattern))).addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(localTimePattern))).addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(localDateTimePattern)));
    }

    /**
     * 反序列化
     */
    private void deserialize(JavaTimeModule javaTimeModule) {
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(localDatePattern))).addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(localTimePattern))).addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(localDateTimePattern)));
    }
}
