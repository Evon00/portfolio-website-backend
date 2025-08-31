package com.example.portfolio_website_backend.post.service;

import com.example.portfolio_website_backend.common.exception.BusinessException;
import com.example.portfolio_website_backend.common.exception.ExceptionCode;
import com.example.portfolio_website_backend.common.service.ClientInfoExtractor;
import com.example.portfolio_website_backend.post.domain.DailyPostViews;
import com.example.portfolio_website_backend.post.domain.Post;
import com.example.portfolio_website_backend.post.domain.PostViewLog;
import com.example.portfolio_website_backend.post.dto.response.DailyViewStats;
import com.example.portfolio_website_backend.post.repository.DailyPostViewsRepository;
import com.example.portfolio_website_backend.post.repository.PostRepository;
import com.example.portfolio_website_backend.post.repository.PostViewLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ViewService {

    private final PostRepository postRepository;
    private final PostViewLogRepository postViewLogRepository;
    private final DailyPostViewsRepository dailyPostViewsRepository;

    private final ClientInfoExtractor clientInfoExtractor;

    /**
     * 게시글 조회수 추가
     * 
     * @param postId 게시글 ID값
     * @param request 서블릿 객체
     * @throws BusinessException 해당되는 게시글이 없을 시 발생
     */
    @Transactional
    public void addView(Long postId, HttpServletRequest request) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(ExceptionCode.POST_NOT_FOUND));

        String agent = clientInfoExtractor.getUserAgent(request);
        String referer = clientInfoExtractor.getReferer(request);
        String ipAddress = clientInfoExtractor.getClientIpAddress(request);

        boolean alreadyViewed = postViewLogRepository.existsByPostAndIpAddressAndViewedAtAfter(
                post, ipAddress, Instant.now().minus(24, ChronoUnit.HOURS)
        );

        if (!alreadyViewed) {
            PostViewLog log = PostViewLog.builder()
                    .post(post)
                    .userAgent(agent)
                    .refer(referer)
                    .ipAddress(ipAddress)
                    .build();

            postViewLogRepository.save(log);

            post.incrementView();
        }

    }


    /**
     * 일일 조회수 집계
     *
     * 매일 00시 05분에 집계 시작
     * 전날 게시글의 조회수 집계후 데이터 저장
     *
     */
    @Scheduled(cron = "0 5 0 * * ?", zone = "Asia/Seoul")
    @Transactional
    public void aggregateDailyViews() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        ZoneId zone = ZoneId.of("Asia/Seoul");
        Instant startOfDay = yesterday.atStartOfDay(zone).toInstant();
        Instant endOfDay = yesterday.atTime(23, 59, 59).atZone(zone).toInstant();

        List<DailyViewStats> logs = postViewLogRepository.getPostViewLogsByViewedAtRange(startOfDay, endOfDay);

        List<DailyPostViews> dailyViews = logs.stream()
                .map(log -> DailyPostViews.builder()
                        .post(log.post())
                        .viewDate(startOfDay)
                        .viewCount(log.totalViews())
                        .uniqueVisitors(log.uniqueVisitors())
                        .createdAt(Instant.now())
                        .build())
                .toList();

        dailyPostViewsRepository.saveAll(dailyViews);
    }
}
