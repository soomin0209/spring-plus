package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Page<TodoSearchResponse> searchTodosMultiConditions(TodoSearchRequest request, Pageable pageable) {

        // 실제 데이터 값
        List<TodoSearchResponse> result = queryFactory
                .select(Projections.constructor(TodoSearchResponse.class,
                        todo.title,
                        // 서브쿼리로 count 계산 (N+1 문제 방지)
                        JPAExpressions.select(manager.count()).from(manager).where(manager.todo.eq(todo)),
                        JPAExpressions.select(comment.count()).from(comment).where(comment.todo.eq(todo))))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .where(
                        titleContains(request.getTitle()),
                        createdAtBetween(request.getStartDate(), request.getEndDate()),
                        nicknameContains(request.getNickname())
                )
                .groupBy(todo.id)   // 1:N Join으로 인한 중복 제거
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 데이터 개수
        Long total = queryFactory
                .select(todo.countDistinct())   // 중복 제거 count
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .where(
                        titleContains(request.getTitle()),
                        createdAtBetween(request.getStartDate(), request.getEndDate()),
                        nicknameContains(request.getNickname())
                )
                .fetchOne();

        // 전체 개수가 null인 경우 방지
        if (total == null) {
            total = 0L;
        }

        // Page 객체로 변환
        return new PageImpl<>(result, pageable, total);
    }

    // 제목 검색
    private BooleanExpression titleContains(String title) {
        return title != null ? todo.title.contains(title) : null;
    }

    // 생성일 범위 검색
    private BooleanExpression createdAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return todo.createdAt.between(startDate, endDate);
        } else if (startDate != null) {
            return todo.createdAt.goe(startDate);
        } else if (endDate != null) {
            return todo.createdAt.loe(endDate);
        }
        return null;
    }

    // 담당자 닉네임 검색
    private BooleanExpression nicknameContains(String nickname) {
        return nickname != null ? manager.user.nickname.contains(nickname) : null;
    }
}
