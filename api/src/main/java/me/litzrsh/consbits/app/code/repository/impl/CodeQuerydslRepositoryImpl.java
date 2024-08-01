package me.litzrsh.consbits.app.code.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPADeleteClause;
import me.litzrsh.consbits.app.code.Code;
import me.litzrsh.consbits.app.code.CodeSearchParams;
import me.litzrsh.consbits.app.code.QCode;
import me.litzrsh.consbits.app.code.repository.CodeQuerydslRepository;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.Page;
import me.litzrsh.consbits.core.QuerydslSearchHelper;
import me.litzrsh.consbits.core.util.RestUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * QueryDSL을 사용한 코드 데이터의 커스텀 쿼리를 구현한 클래스.
 * QuerydslRepositorySupport를 상속받아 복잡한 쿼리 메서드를 정의합니다.
 */
public class CodeQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CodeQuerydslRepository, QuerydslSearchHelper {

    /**
     * CodeQuerydslRepositoryImpl 생성자.
     * Code 엔티티를 위한 QuerydslRepositorySupport 초기화.
     */
    public CodeQuerydslRepositoryImpl() {
        super(Code.class);
    }

    /**
     * 페이지별로 코드를 조회합니다.
     *
     * @param params 코드 검색 조건이 담긴 객체.
     * @return 검색된 코드 리스트를 포함하는 페이지 객체.
     */
    @Override
    public Page<Code> findByPage(CodeSearchParams params) {
        QCode c = QCode.code;

        // 기본 경로 설정
        String basePath = params.getPath();
        if (!StringUtils.hasText(basePath)) basePath = CommonConstants.PATH_DELIM;

        // 부모 ID를 기준으로 조건을 설정
        BooleanExpression where = params.getParentId() == null ? c.parentId.isNull() : c.parentId.eq(params.getParentId());

        // 검색어가 있는 경우 조건 추가
        if (StringUtils.hasText(params.getQuery())) {
            QCode cc = new QCode("cc");
            String query = "%" + params.getQuery() + "%";
            String path = RestUtils.append(basePath, "%");
            where = and(where,
                    c.value.like(query)
                            .or(c.label.like(query))
                            .or(
                                    from(cc).where(
                                            cc.path.like(path).and(
                                                    cc.value.like(query)
                                                            .or(cc.label.like(query))
                                            )
                                    ).exists()
                            )
            );
        }

        // 쿼리 생성 및 조건 설정
        JPQLQuery<Code> query = from(c)
                .select(c)
                .where(where);

        // 쿼리 실행 및 결과 페이징
        final List<Code> contents = query
                .limit(params.getPerPage())
                .offset(params.getOffset())
                .orderBy(c.sort.asc())
                .fetch();
        final long totalItems = query.fetchCount();

        return new Page<>(contents, params, totalItems);
    }

    /**
     * 하위 코드를 포함하여 코드를 삭제합니다.
     *
     * @param code 삭제할 코드 객체.
     */
    @Override
    public void deleteWithChildren(Code code) {
        QCode c = QCode.code;
        String path = RestUtils.append(code.getPath(), code.getValue()) + "%";
        JPADeleteClause delete = new JPADeleteClause(Objects.requireNonNull(getEntityManager()), c);
        // 주어진 코드와 경로를 기반으로 하위 코드를 포함하여 삭제
        delete
                .where(c.id.eq(code.getId()).or(c.path.like(path)))
                .execute();
    }

    /**
     * 특정 값과 부모 ID를 가진 코드가 존재하는지 확인합니다.
     *
     * @param value 확인할 코드 값.
     * @param parentId 확인할 부모 ID.
     * @return 코드 존재 여부.
     */
    @Override
    public boolean exists(String value, String parentId) {
        QCode c = QCode.code;
        // 값과 부모 ID를 기준으로 조건 설정
        BooleanExpression where = c.value.eq(value);
        where = and(where, StringUtils.hasText(parentId) ? c.parentId.eq(parentId) : c.parentId.isNull());
        // 조건에 맞는 코드가 존재하는지 확인
        return from(c)
                .select(c)
                .where(where)
                .fetchCount() != 0;
    }
}
