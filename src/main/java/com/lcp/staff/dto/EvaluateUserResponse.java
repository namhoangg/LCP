package com.lcp.staff.dto;

import lombok.Data;

import java.util.List;

@Data
public class EvaluateUserResponse {
    List<SubjectEvaluationDto> evaluations;
}
