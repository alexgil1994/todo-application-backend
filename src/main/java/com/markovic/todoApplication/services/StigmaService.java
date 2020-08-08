package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Stigma;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface StigmaService {

    Set<Stigma> getStigmaListByUsername(String username);

    void deleteStigma(Long id, String username);

}
