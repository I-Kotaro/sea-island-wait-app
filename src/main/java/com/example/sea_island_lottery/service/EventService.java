package com.example.sea_island_lottery.service;

import com.example.sea_island_lottery.entity.Event;
import com.example.sea_island_lottery.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;

    //自動でDI注入(new)するアノテーション
    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    //イベントを全件取得するメソッド
    //DB操作がトランザクション内(処理成功時だけDBに反映される)で実行されるアノテーション
    @Transactional(readOnly = true)
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    //イベントを1件取得するメソッド
    @Transactional(readOnly = true)
    public Optional<Event> findEventById(Long id) {
        return eventRepository.findById(id);
    }

    //イベント新規作成メソッド
    public Event createEvent(Event event) {
        // ここでイベント作成に関するビジネスロジックを追加可能
        return eventRepository.save(event);
    }

    //イベント更新メソッド
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        // 更新するフィールドを設定
        event.setName(eventDetails.getName());
        event.setDescription(eventDetails.getDescription());
        event.setImageUrl(eventDetails.getImageUrl());
        event.setCapacity(eventDetails.getCapacity());
        event.setEntryDeadline(eventDetails.getEntryDeadline());

        return eventRepository.save(event);
    }

    //イベント削除メソッド
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
