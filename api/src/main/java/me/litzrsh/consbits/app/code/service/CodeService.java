package me.litzrsh.consbits.app.code.service;

import me.litzrsh.consbits.app.code.Code;
import me.litzrsh.consbits.app.code.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 일반 사용자용 코드 서비스를 제공하는 클래스.
 * 코드에 대한 조회 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;

    /**
     * 주어진 경로에 따라 사용 가능한 모든 코드를 조회합니다.
     * 이 메소드는 결과를 캐싱하여 성능을 최적화합니다.
     *
     * @param path 조회할 코드의 경로.
     * @return 사용 가능한 코드 리스트.
     */
    @Cacheable("CODE")
    public List<Code> findByPath(String path, Boolean all) {
        if (all) {
            return codeRepository.findAllByPath(path)
                    .stream()
                    .sorted()
                    .toList();
        } else {
            return codeRepository.findAllByPath(path)
                    .stream()
                    .filter(Code::getUse)
                    .sorted()
                    .toList();
        }
    }
}
