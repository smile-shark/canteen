package com.smileshark.controller;

import com.smileshark.common.Result;
import com.smileshark.entity.Taste;
import com.smileshark.service.TasteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 口味 前端控制器
 * </p>
 *
 * @author smile鲨鱼
 * @since 2025年08月03日
 */
@RestController
@RequestMapping("/taste")
@RequiredArgsConstructor
public class TasteController {
    private final TasteService tasteService;
    @GetMapping("/list")
    public Result<List<Taste>> tasteList() {
        return tasteService.tasteList();
    }
    @PostMapping("/add")
    public Result<?> addTaste(@RequestBody Taste taste) {
        return tasteService.add(taste);
    }
    @DeleteMapping("/delete")
    public Result<?> deleteTaste(@RequestParam String id) {
        return tasteService.delete(id);
    }
}
