package com.example.forum.domain.forum.scheduler;

import com.example.forum.common.error.exception.BusinessException;
import com.example.forum.domain.forum.entity.Forum;
import com.example.forum.domain.forum.repository.ForumRepository;
import com.example.forum.domain.forum.service.ForumService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.example.forum.common.error.ErrorCode.FORUM_COUNT_SCHEDULING_FAILURE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountScheduler {
    private final ForumService forumService;
    private final ForumRepository repository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JobLauncher jobLauncher;
    private final Job updateViewCountJob;

    @Scheduled(fixedRate = 10000)  // 10초 간격
    public void runViewCountJob() {
        // 배치 작업 실행 로직
        try {
            log.info("조회수 업데이트 Scheduler 동작");
            jobLauncher.run(updateViewCountJob, new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());

            log.info("조회수 증가 로직 시작!");
            saveViewCounts();
        } catch (Exception e) {
            log.error("조회수 스케줄러 작업 중 오류 발생", e);
            throw new BusinessException(FORUM_COUNT_SCHEDULING_FAILURE);
        }
    }

    @Transactional
    private void saveViewCounts() {
        List<String> forumIds = getForumIdsFromRedis();
        for (String forumId : forumIds) {
            Forum forum = forumService.getForumByForumId(forumId);
            Long viewCount = Long.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get("forum:viewCount:" + forumId)));
            // redis에서 해당 id 삭제
            redisTemplate.delete("forum:viewCount:" + forumId);
            forum.updateViews(viewCount);
            repository.save(forum);
        }
    }

    private List<String> getForumIdsFromRedis() {
        // Redis에서 'forum:viewCount:*' 형식의 모든 키 가져오기
        Set<String> keys = redisTemplate.keys("forum:viewCount:*");

        // 키에서 게시물 ID만 추출하여 반환
        return Objects.requireNonNull(keys).stream()
                .map(key -> key.replace("forum:viewCount:", ""))  // "post:viewCount:" 부분 제거
                .toList();
    }
}
