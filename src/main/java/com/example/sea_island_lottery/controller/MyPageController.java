package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.dto.EventDto;
import com.example.sea_island_lottery.entity.Entry;
import com.example.sea_island_lottery.entity.User;
import com.example.sea_island_lottery.repository.EntryRepository;
import com.example.sea_island_lottery.service.EventService;
import com.example.sea_island_lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private final UserService userService;
    private final EventService eventService;
    private final EntryRepository entryRepository;

    // テスト用の固定ユーザーID
    private final UUID TEST_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    public MyPageController(UserService userService, EventService eventService, EntryRepository entryRepository) {
        this.userService = userService;
        this.eventService = eventService;
        this.entryRepository = entryRepository;
    }

    @GetMapping
    public String index(Model model) {
        User user = userService.findUserById(TEST_USER_ID)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);

        // 応募中のイベントを取得し、DTOに変換
        List<Entry> entries = entryRepository.findByUserId(TEST_USER_ID);
        List<EventDto> waitingEvents = entries.stream()
                .filter(entry -> "WAITING".equals(entry.getStatus()))
                .map(entry -> eventService.convertToDto(entry.getEvent()))
                .collect(Collectors.toList());
        model.addAttribute("waitingEvents", waitingEvents);
        return "mypage/index";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute User userForm) {
        User user = new User();
        user.setName(userForm.getName());
        userService.updateUser(TEST_USER_ID, user);
        return "redirect:/mypage";
    }

    @PostMapping("/delete")
    public String delete() {
        userService.deleteUser(TEST_USER_ID);
        return "redirect:/";
    }
}
