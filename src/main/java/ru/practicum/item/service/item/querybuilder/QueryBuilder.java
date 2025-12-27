package ru.practicum.item.service.item.querybuilder;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import ru.practicum.item.dto.reqeust.list.ContentType;
import ru.practicum.item.dto.reqeust.list.State;
import ru.practicum.item.model.Item;
import ru.practicum.item.model.QItem;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@org.springframework.context.annotation.Scope("prototype")
public class QueryBuilder {
    private final JPAQueryFactory queryFactory;
    private final QItem item = QItem.item;
    private BooleanExpression predicate = item.id.isNotNull();
    @Setter
    @Accessors(chain = true)
    private Long userId;
    @Setter
    @Accessors(chain = true)
    private State state;
    @Setter
    @Accessors(chain = true)
    private ContentType contentType;
    @Setter
    @Accessors(chain = true)
    private List<String> tags = new ArrayList<>();
    @Setter
    @Accessors(chain = true)
    private Integer limit;
    @Setter
    @Accessors(chain = true)
    private ru.practicum.item.dto.reqeust.list.Sort sort;

    public List<Item> fetch() {
        applyUserIdFilter();
        applyStateFilter();
        applyContentTypeFilter();
        applyTagsFilter();

        JPAQuery<Item> query = queryFactory.selectFrom(item).where(predicate);

        applySort(query);

        if (limit != null) {
            query.limit(limit);
        }

        List<Item> result = query.fetch();
        predicate = item.id.isNotNull(); // Reset predicate for next use if it's a singleton (but it's @Component)
        return result;
    }

    private void applySort(JPAQuery<Item> query) {
        if (sort == null) {
            return;
        }

        switch (sort) {
            case NEWEST -> query.orderBy(item.dateResolved.desc());
            case OLDEST -> query.orderBy(item.dateResolved.asc());
            case TITLE -> query.orderBy(item.title.asc());
        }
    }

    private void applyUserIdFilter() {
        predicate = predicate.and(item.userId.eq(userId));
    }

    private void applyStateFilter() {
        predicate = switch (state) {
            case UNREAD -> predicate.and(item.unread.isTrue());
            case READ -> predicate.and(item.unread.isFalse());
            default -> predicate;
        };
    }

    private void applyContentTypeFilter() {
        predicate = switch (contentType) {
            case IMAGE -> predicate.and(item.hasImage.isTrue());
            case VIDEO -> predicate.and(item.hasVideo.isTrue());
            case ARTICLE -> predicate.and(item.hasImage.isFalse()).and(item.hasVideo.isFalse());
            default -> predicate;
        };
    }

    private void applyTagsFilter() {
        if (!tags.isEmpty()) {
            for (String tag : tags) {
                predicate = predicate.and(com.querydsl.core.types.dsl.Expressions.booleanTemplate(
                        "{0} && array[{1}]", item.tags, tag));
            }
        }
    }
}