package com.lcp.util;


import com.lcp.common.Constant;
import lombok.Setter;
import org.springframework.data.util.Pair;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QueryUtil {

    @Setter
    private static EntityManager entityManager;

    private QueryUtil() {
    }

    public static <T> long count(
            final CriteriaBuilder cb,
            final CriteriaQuery<T> criteria,
            final Root<T> root) {
        CriteriaQuery<Long> query = createCountQuery(cb, criteria, root);
        return entityManager.createQuery(query).getSingleResult();
    }

    private static <T> CriteriaQuery<Long> createCountQuery(final CriteriaBuilder cb,
                                                            final CriteriaQuery<T> criteria,
                                                            final Root<T> root) {

        final CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        final Root<T> countRoot = countQuery.from(criteria.getResultType());

        doJoins(root.getJoins(), countRoot);
        doJoinsOnFetches(root.getFetches(), countRoot);

        countQuery.select(cb.countDistinct(countRoot));

        if (criteria.getRestriction() != null) {
            countQuery.where(criteria.getRestriction());
        }

        countRoot.alias(root.getAlias());

        return countQuery.distinct(criteria.isDistinct());
    }

    public static <T> long count(
            final CriteriaBuilder cb,
            final CriteriaQuery<Object[]> criteria,
            final Root<T> root,
            final Class<T> clazz) {
        CriteriaQuery<Long> query = createCountQuery(cb, criteria, root, clazz);
        return entityManager.createQuery(query).getSingleResult();
    }

    private static <T> CriteriaQuery<Long> createCountQuery(final CriteriaBuilder cb,
                                                            final CriteriaQuery<Object[]> criteria,
                                                            final Root<T> root,
                                                            final Class<T> clazz) {

        final CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        final Root<T> countRoot = countQuery.from(clazz);

        doJoins(root.getJoins(), countRoot);
        doJoinsOnFetches(root.getFetches(), countRoot);

        countQuery.select(cb.countDistinct(countRoot));
        countQuery.where(criteria.getRestriction());

        countRoot.alias(root.getAlias());

        return countQuery;
    }

    @SuppressWarnings("unchecked")
    private static void doJoinsOnFetches(Set<? extends Fetch<?, ?>> joins, Root<?> root) {
        doJoins((Set<? extends Join<?, ?>>) joins, root);
    }

    @SuppressWarnings("unchecked")
    private static void doJoinsOnFetches(Set<? extends Fetch<?, ?>> joins, Join<?, ?> root) {
        doJoins((Set<? extends Join<?, ?>>) joins, root);
    }

    private static void doJoins(Set<? extends Join<?, ?>> joins, Root<?> root) {
        for (Join<?, ?> join : joins) {
            Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
            joined.alias(join.getAlias());
            doJoins(join.getJoins(), joined);
            doJoinsOnFetches(join.getFetches(), joined);
        }
    }

    private static void doJoins(Set<? extends Join<?, ?>> joins, Join<?, ?> root) {
        for (Join<?, ?> join : joins) {
            Join<?, ?> joined = root.join(join.getAttribute().getName(), join.getJoinType());
            joined.alias(join.getAlias());
            doJoins(join.getJoins(), joined);
            doJoinsOnFetches(join.getFetches(), joined);
        }
    }

    public static Join<?, ?> getOrJoin(From<?, ?> from, String attribute, JoinType joinType) {
        return getOrJoin(from, List.of(Pair.of(attribute, joinType)));
    }

    /**
     * Find join from attributesPair list (parent -> child -> subChild -> subSubChild,...)
     *
     * @param from
     * @param attributesPair
     * @return
     */
    public static Join<?, ?> getOrJoin(From<?, ?> from, List<Pair<String, JoinType>> attributesPair) {
        var mutableAttributesPair = new ArrayList<>(attributesPair);

        var currentAttributePair = mutableAttributesPair.remove(0);
        var currentAttribute = currentAttributePair.getFirst();
        var matchedJoin = from.getJoins().stream().filter(join -> {
            var joinAttribute = join.getAttribute();
            return joinAttribute.getName().equals(currentAttribute);
        }).findFirst().orElse(null);

        if (matchedJoin == null) {
            matchedJoin = from.join(currentAttribute, currentAttributePair.getSecond());
        }

        if (mutableAttributesPair.size() == 0) {
            return matchedJoin;
        }

        return getOrJoin(matchedJoin, mutableAttributesPair);

    }

    public static Expression<String> concat(CriteriaBuilder cb,
                                            String separator,
                                            Expression<String>... expressions) {
        // Returns an empty string if no expression is provided
        if (expressions.length == 0) {
            return cb.literal("");
        }
        // Start with the first expression
        Expression<String> result = expressions[0];
        // Then concat subsequent expressions (starting from the second one)
        for (int i = 1; i < expressions.length; i++) {
            result = cb.concat(result, cb.concat(separator, expressions[i]));
        }
        return result;
    }

    public static Expression<String> formatBooleanExpr(CriteriaBuilder builder, Expression<Boolean> expr) {
        Expression<String> partialExpr = builder.function(Constant.REPLACE, String.class,
                builder.lower(expr.as(String.class)), builder.literal("true"), builder.literal("yes"));
        return builder.function(Constant.REPLACE, String.class,
                partialExpr, builder.literal("false"), builder.literal("no"));
    }

    public static <T> Expression<String> formatEnumExpr(CriteriaBuilder builder, Expression<T> expr) {
        return builder.function(Constant.REPLACE, String.class,
                builder.lower(expr.as(String.class)), builder.literal(Constant.DASH), builder.literal(Constant.SPACE));
    }

    public static Expression<String> formatDateExpr(CriteriaBuilder builder, Expression<LocalDate> expr) {
        return builder.function(Constant.TO_CHAR, String.class, expr, builder.literal(Constant.DATE_FORMAT));
    }

    public static Expression<String> formatDateTimeExpr(CriteriaBuilder builder, Expression<LocalDateTime> expr) {
        return builder.function(Constant.TO_CHAR, String.class, expr, builder.literal(Constant.DATE_TIME_FORMAT));
    }
}
