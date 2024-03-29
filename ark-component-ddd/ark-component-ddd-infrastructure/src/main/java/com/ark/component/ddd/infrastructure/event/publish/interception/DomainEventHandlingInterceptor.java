//package com.ark.ddd.infrastructure.event.publish.interception;
//
//import com.ark.ddd.infrastructure.event.publish.DomainEventPublisher;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class DomainEventHandlingInterceptor implements HandlerInterceptor {
//
//    private final DomainEventPublisher domainEventPublisher;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        DomainEventIdThreadLocalHolder.clear();//确保开始处理请求时，holder中没有其它事件ID
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//        List<Long> eventIds = DomainEventIdThreadLocalHolder.allEventIds();
//        try {
//            domainEventPublisher.publish(eventIds);
//        } finally {
//            DomainEventIdThreadLocalHolder.remove();
//        }
//    }
//
//}
