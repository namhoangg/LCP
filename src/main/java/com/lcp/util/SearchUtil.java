package com.lcp.util;

import com.lcp.common.dto.SearchCondition;
import com.lcp.common.enumeration.OperatorType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchUtil {
    private static final int MAX_SECOND = 59;
    private static final int MAX_NANOSECOND = 999999999;

    public static String format(String data) {
        return "%" + data.toLowerCase() + "%";
    }

    public static Predicate getSearchPredicate(CriteriaBuilder builder, Expression expression, SearchCondition condition) {

        switch (condition.getOperandType()) {
            case STRING:
                return getStringPredicate(builder, expression, condition);
            case DATETIME:
                return getDateTimePredicate(builder, expression, condition);
            case DATE:
                return getDatePredicate(builder, expression, condition);
            //case TIME:
            case BOOLEAN:
                return getBoolPredicate(builder, expression, condition);
            case DECIMAL:
                return getDecimalPredicate(builder, expression, condition);
            case INTEGER:
                return getIntegerPredicate(builder, expression, condition);
            case ENUM:
                return getEnumPredicate(builder, expression, condition);
        }
        return null;
    }

    private static Predicate getStringPredicate(CriteriaBuilder builder, Expression<?> expression, SearchCondition condition) {
        switch (condition.getOperatorType()) {
            case CONTAIN:
                return builder.like(builder.lower(expression.as(String.class)), format(condition.getData()));
            case START_WITH:
                String formatStartSearch = condition.getData().toLowerCase() + "%";
                return builder.like(builder.lower(expression.as(String.class)), formatStartSearch);
            case END_WITH:
                String formatEndSearch = "%" + condition.getData().toLowerCase();
                return builder.like(builder.lower(expression.as(String.class)), formatEndSearch);
            case EQUAL:
                return builder.equal(expression.as(String.class), condition.getData());
        }
        return null;
    }

    private static Predicate getEnumPredicate(CriteriaBuilder builder, Expression<?> expression, SearchCondition condition) {
/*
        String data = condition.getData().trim().replaceAll(Constant.BLANK_REGEX, Constant.DASH);

        switch (condition.getOperatorType()) {
            case CONTAIN:
                return builder.like(builder.lower(expression.as(String.class)), format(data));
            case START_WITH:
                String formatStartSearch = data.toLowerCase() + "%";
                return builder.like(builder.lower(expression.as(String.class)), formatStartSearch);
            case END_WITH:
                String formatEndSearch = "%" + data.toLowerCase();
                return builder.like(builder.lower(expression.as(String.class)), formatEndSearch);
        }
        return null;
*/
        return builder.equal(expression.as(String.class), condition.getData());
    }

    private static Predicate getDateTimePredicate(CriteriaBuilder builder, Expression expression, SearchCondition condition) {
        if (condition.getOperatorType() == OperatorType.BETWEEN) {
            return builder.and(
                    builder.greaterThanOrEqualTo(expression, parseDateTime(condition.getDatas().get(0))),
                    builder.lessThanOrEqualTo(expression, parseDateTime(condition.getDatas().get(1)))
            );
        }
        switch (condition.getOperatorType()) {
            case GREATER_EQUAL:
                return builder.greaterThanOrEqualTo(expression, parseDateTime(condition.getData()));
            case LESS_EQUAL:
                return builder.lessThanOrEqualTo(expression, parseDateTime(condition.getData()));
            case EQUAL:
                // fix issue FE only display timestamp without second and nanosecond
                // => support querying return values
                LocalDateTime dataTime = parseDateTime(condition.getData());
                return builder.and(
                        builder.greaterThanOrEqualTo(expression, dataTime.withSecond(0).withNano(0)),
                        builder.lessThanOrEqualTo(expression, dataTime.withSecond(MAX_SECOND).withNano(MAX_NANOSECOND))
                );
        }
        return null;
    }

    private static Predicate getDatePredicate(CriteriaBuilder builder, Expression expression, SearchCondition condition) {
        if (condition.getOperatorType() == OperatorType.BETWEEN) {
            return builder.and(
                    builder.greaterThanOrEqualTo(expression, parseDate(condition.getDatas().get(0))),
                    builder.lessThanOrEqualTo(expression, parseDate(condition.getDatas().get(1)))
            );
        }
        switch (condition.getOperatorType()) {
            case GREATER_EQUAL:
                return builder.greaterThanOrEqualTo(expression, parseDate(condition.getData()));
            case LESS_EQUAL:
                return builder.lessThanOrEqualTo(expression, parseDate(condition.getData()));
            case EQUAL:
                return builder.equal(expression, parseDate(condition.getData()));
        }
        return null;
    }

    private static Predicate getBoolPredicate(CriteriaBuilder builder, Expression expression, SearchCondition condition) {
        if (condition.getOperatorType() == OperatorType.EQUAL) {
            var data = parseBool(condition.getData());
            if (BooleanUtil.isTrue(data)) {
                return builder.isTrue(expression);
            }
            return builder.isFalse(expression);
        }
        return null;
    }

    private static Predicate getDecimalPredicate(CriteriaBuilder builder, Expression expression, SearchCondition condition) {
        if (condition.getOperatorType() == OperatorType.BETWEEN) {
            return builder.and(
                    builder.greaterThanOrEqualTo(expression, parseDecimal(condition.getDatas().get(0))),
                    builder.lessThanOrEqualTo(expression, parseDecimal(condition.getDatas().get(1)))
            );
        }

        switch (condition.getOperatorType()) {
            case GREATER_EQUAL:
                return builder.greaterThanOrEqualTo(expression, parseDecimal(condition.getData()));
            case LESS_EQUAL:
                return builder.lessThanOrEqualTo(expression, parseDecimal(condition.getData()));
            case EQUAL:
                return builder.equal(expression, parseDecimal(condition.getData()));
        }
        return null;
    }

    private static Predicate getIntegerPredicate(CriteriaBuilder builder, Expression expression, SearchCondition condition) {
        if (condition.getOperatorType() == OperatorType.BETWEEN) {
            return builder.and(
                    builder.greaterThanOrEqualTo(expression, parseInteger(condition.getDatas().get(0))),
                    builder.lessThanOrEqualTo(expression, parseInteger(condition.getDatas().get(1)))
            );
        }

        switch (condition.getOperatorType()) {
            case GREATER_EQUAL:
                return builder.greaterThanOrEqualTo(expression, parseInteger(condition.getData()));
            case LESS_EQUAL:
                return builder.lessThanOrEqualTo(expression, parseInteger(condition.getData()));
            case EQUAL:
                return builder.equal(expression, parseInteger(condition.getData()));
        }
        return null;
    }

    private static Predicate getRefId(CriteriaBuilder builder, Expression expression, SearchCondition condition) {
        if (condition.getOperatorType() == OperatorType.IN) {
            return builder.in(expression.as(String.class)).value(condition.getDatas());
        }
        return null;
    }

    private static Predicate getRefEnum(CriteriaBuilder builder, Expression expression, SearchCondition condition) {
        if (condition.getOperatorType() == OperatorType.IN) {
            return builder.in(expression.as(String.class)).value(condition.getDatas());
        }
        return null;
    }

    private static LocalDate parseDate(String date) {
        if (StringUtils.isEmpty(date))
            return null;
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static LocalDateTime parseDateTime(String dateTime) {
        if (StringUtils.isEmpty(dateTime))
            return null;
        dateTime = dateTime.replace(".000Z", "");
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private static Boolean parseBool(String data) {
        return Boolean.parseBoolean(data);
    }

    private static BigDecimal parseDecimal(String data) {
        return new BigDecimal(data);
    }

    private static Integer parseInteger(String data) {
        return Integer.parseInt(data);
    }
}
