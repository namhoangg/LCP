package com.lcp.rate.controller;

import com.lcp.rate.dto.GoodKindCreate;
import com.lcp.rate.dto.GoodKindResponse;
import com.lcp.rate.dto.GoodKindUpdate;
import com.lcp.rate.service.GoodKindService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/good-kind")
@RequiredArgsConstructor
public class GoodKindController {
    private final GoodKindService goodKindService;

    @GetMapping("/list")
    public List<GoodKindResponse> list() {
        return goodKindService.list();
    }

    @PostMapping("/create")
    public void create(@Valid @RequestBody GoodKindCreate goodKindCreate) {
        goodKindService.create(goodKindCreate);
    }

    @PostMapping("/update")
    public void update(@Valid @RequestBody GoodKindUpdate goodKindUpdate) {
        goodKindService.update(goodKindUpdate);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        goodKindService.delete(id);
    }
}
