package com.lcp.rate.controller;

import com.lcp.common.Response;
import com.lcp.rate.dto.ContainerTypeCreate;
import com.lcp.rate.dto.ContainerTypeResponse;
import com.lcp.rate.dto.ContainerTypeUpdate;
import com.lcp.rate.service.ContainerTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/container-type")
@RequiredArgsConstructor
public class ContainerTypeController {
    private final ContainerTypeService containerTypeService;

    @GetMapping("/list")
    public List<ContainerTypeResponse> list() {
        return containerTypeService.list();
    }

    @GetMapping("/list-ids")
    public List<ContainerTypeResponse> listByIds(@RequestParam List<Long> ids) {
        return containerTypeService.listByIds(ids);
    }

    @PostMapping("/create")
    public Response<Void> create(@Valid @RequestBody ContainerTypeCreate containerTypeCreate) {
        containerTypeService.create(containerTypeCreate);
        return Response.success();
    }

    @PostMapping("/update")
    public Response<Void> update(@Valid @RequestBody ContainerTypeUpdate containerTypeUpdate) {
        containerTypeService.update(containerTypeUpdate);
        return Response.success();
    }

    @PostMapping("/delete/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        containerTypeService.delete(id);
        return Response.success();
    }
}
