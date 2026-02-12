package com.kh.osori.badge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.osori.badge.model.vo.Badge;
import com.kh.osori.badge.service.BadgeService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    @Autowired
    private BadgeService badgeService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Badge>> getUserBadges(@PathVariable int userId) {
        List<Badge> badges = badgeService.mergeUserBadge(userId);
        return ResponseEntity.ok(badges);
    }
}