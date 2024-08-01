package me.litzrsh.consbits.app.menu.service;

import me.litzrsh.consbits.app.menu.Menu;
import me.litzrsh.consbits.app.menu.event.MenuSingleDeleteEvent;
import me.litzrsh.consbits.app.menu.repository.MenuRepository;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.annotation.HasAuthority;
import me.litzrsh.consbits.core.exception.RestfulException;
import me.litzrsh.consbits.core.util.RestUtils;
import me.litzrsh.consbits.core.util.SerialUtils;
import me.litzrsh.consbits.core.util.TreeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static me.litzrsh.consbits.core.CommonConstants.ADMIN;
import static me.litzrsh.consbits.core.CommonConstants.SYS_ADMIN;

/**
 * 관리자용 메뉴 서비스 클래스.
 * 메뉴에 대한 조회, 저장, 삭제 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@HasAuthority({SYS_ADMIN, ADMIN})
@Slf4j
public class MenuAdminService {

    public static final RestfulException NOT_FOUND = new RestfulException(HttpStatus.NOT_FOUND, "ME0001");
    public static final RestfulException PARENT_NOT_FOUND = new RestfulException(HttpStatus.NOT_FOUND, "ME0002");

    private final MenuRepository menuRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 모든 메뉴를 조회합니다.
     *
     * @return 트리 구조로 변환된 모든 메뉴 리스트.
     */
    public List<Menu> findAll() {
        // 모든 메뉴를 데이터베이스에서 조회
        List<Menu> menus = menuRepository.findAll();
        // 트리 구조로 변환하여 반환
        return TreeUtils.convert(menus);
    }

    /**
     * ID로 메뉴를 조회합니다.
     *
     * @param id 조회할 메뉴의 ID.
     * @return 조회된 메뉴 객체.
     * @throws RestfulException 메뉴를 찾을 수 없는 경우 예외를 던집니다.
     */
    public Menu findById(String id) throws RestfulException {
        // ID로 메뉴를 조회하고, 존재하지 않을 경우 예외를 던짐
        return menuRepository.findById(id)
                .orElseThrow(() -> new RestfulException(NOT_FOUND));
    }

    /**
     * 메뉴를 저장합니다. 새로운 메뉴를 생성하거나 기존 메뉴를 업데이트합니다.
     *
     * @param id 저장할 메뉴의 ID. 새 메뉴인 경우 null.
     * @param menu 저장할 메뉴 정보가 담긴 객체.
     * @return 저장된 메뉴 객체.
     * @throws RestfulException 저장 과정에서 문제가 발생한 경우 예외를 던집니다.
     */
    @Transactional
    public Menu save(String id, Menu menu) throws RestfulException {
        final boolean isEdit = StringUtils.hasText(id); // 편집 여부 확인
        Menu parent = null;
        if (isEdit) {
            // 기존 메뉴를 편집하는 경우
            Menu exists = menuRepository.findById(id)
                    .orElseThrow(() -> new RestfulException(NOT_FOUND)); // 메뉴가 존재하지 않으면 예외를 던짐
            menu.setId(exists.getId()); // 기존 메뉴 ID 설정
            menu.setParentId(exists.getParentId()); // 부모 ID 설정
            menu.setLevel(exists.getLevel()); // 메뉴 레벨 설정
            menu.setPath(exists.getPath()); // 메뉴 경로 설정
        } else {
            // 새로운 메뉴를 생성하는 경우
            menu.setId(SerialUtils.get(Menu.SERIAL)); // 새로운 메뉴 ID 생성
            if (StringUtils.hasText(menu.getParentId())) {
                // 부모 ID가 있는 경우 부모 메뉴를 조회하고, 존재하지 않으면 예외를 던짐
                parent = menuRepository.findById(menu.getParentId())
                        .orElseThrow(() -> new RestfulException(PARENT_NOT_FOUND));
            } else {
                // 부모 ID가 없는 경우 루트 메뉴로 설정
                menu.setLevel(0); // 루트 레벨 설정
                menu.setPath(CommonConstants.PATH_DELIM); // 루트 경로 설정
            }
        }
        if (parent != null) {
            // 부모 메뉴가 있는 경우 부모 메뉴의 레벨과 경로를 기반으로 자식 메뉴 설정
            menu.setLevel(parent.getLevel() + 1); // 부모 메뉴의 레벨 + 1 설정
            menu.setPath(RestUtils.append(parent.getPath(), parent.getId())); // 부모 메뉴의 경로에 부모 ID 추가
        }
        menuRepository.save(menu); // 메뉴 저장
        return menu;
    }

    /**
     * 메뉴를 삭제합니다.
     *
     * @param id 삭제할 메뉴의 ID.
     * @throws RestfulException 메뉴를 찾을 수 없는 경우 예외를 던집니다.
     */
    @Transactional
    public void delete(String id) throws RestfulException {
        // ID로 메뉴를 조회하고, 존재하지 않을 경우 예외를 던짐
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RestfulException(NOT_FOUND));
        // 메뉴 삭제 이벤트 발행
        eventPublisher.publishEvent(new MenuSingleDeleteEvent(menu));
        // 메뉴와 하위 메뉴 삭제
        menuRepository.deleteWithChild(menu);
    }
}
