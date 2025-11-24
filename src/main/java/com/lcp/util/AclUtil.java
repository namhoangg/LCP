package com.lcp.util;

import com.lcp.acl.helper.AclHelper;
import com.lcp.common.impl.CacheService;
import com.lcp.security.UserDetailsCustom;
import com.lcp.staff.dto.SubjectEvaluationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AclUtil {
    private final CacheService cacheService;
    private final AclHelper aclHelper;

    // Todo: handle cache when acl is update, cud
    public List<SubjectEvaluationDto> getAcls() {
        String cacheKey = "subject_evaluation:" + UserDetailsCustom.getCurrentUserId();
        Set<Object> subjectEvaluationsCache = cacheService.retrieveAll(cacheKey);
        List<SubjectEvaluationDto> subjectEvaluationDtos;

        if (subjectEvaluationsCache != null && !subjectEvaluationsCache.isEmpty()) {
            // Cache hit - convert Set<Object> to List<SubjectEvaluationDto>
            subjectEvaluationDtos = subjectEvaluationsCache.stream()
                    .map(obj -> (SubjectEvaluationDto) obj)
                    .collect(Collectors.toList());
        } else {
            // Cache miss - fetch from original source
            subjectEvaluationDtos = aclHelper.getSubjectEvaluations(UserDetailsCustom.getCurrentUserId());

            // Store in cache if we have results
            if (subjectEvaluationDtos != null && !subjectEvaluationDtos.isEmpty()) {
                cacheService.clearAndAdd(cacheKey, subjectEvaluationDtos.toArray());
            }
        }

        return subjectEvaluationDtos;
    }
}
