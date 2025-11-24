package com.lcp.template.service;

import com.lcp.template.dto.TemplateRenderRequest;

public interface TemplateService {
    byte[] render(TemplateRenderRequest request);
}
