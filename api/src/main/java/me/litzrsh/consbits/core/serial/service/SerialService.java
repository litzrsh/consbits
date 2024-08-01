package me.litzrsh.consbits.core.serial.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.litzrsh.consbits.core.annotation.Retry;
import me.litzrsh.consbits.core.serial.Serial;
import me.litzrsh.consbits.core.serial.SerialGenerator;
import me.litzrsh.consbits.core.serial.SerialMutex;
import me.litzrsh.consbits.core.serial.repository.SerialMutexRepository;
import me.litzrsh.consbits.core.serial.repository.SerialRepository;
import me.litzrsh.consbits.core.util.SerialUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

@Service
@RequiredArgsConstructor
@Slf4j
public class SerialService implements InitializingBean {

    private final SerialRepository serialRepository;
    private final SerialMutexRepository serialMutexRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Retry
    public Queue<String> getQueue(SerialGenerator gen, Long size) {
        SerialMutex mutex = serialMutexRepository.findById(gen.getId()).orElse(new SerialMutex(gen.getId()));
        serialMutexRepository.saveAndFlush(mutex);
        final Date date = new Date();
        Serial serial = serialRepository.findById(gen.getId()).orElse(new Serial(gen.getId(), date));
        Long start = 1L;
        if (gen.compare(date, serial.getUpdateAt())) {
            start = serial.getValue();
        }
        serial.setValue(start + size);
        log.info("Serial : {} -> {}", gen.compare(date, serial.getUpdateAt()), serial);
        serial.setUpdateAt(date);
        serialRepository.saveAndFlush(serial);
        serialMutexRepository.delete(mutex);
        Queue<String> queue = new ArrayDeque<>();
        for (long value = start; value < serial.getValue(); value++) {
            queue.add(gen.format(date, value));
        }
        return queue;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SerialUtils.setSerialService(this);
    }
}
