package com.holden.vocabulary.repository;


import com.holden.common.entity.SysDict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/14 18:34
 */
public interface SysDictRepository extends JpaRepository<SysDict, Long> {
    Optional<SysDict> findByDictTypeAndDictKey(String type, String key);
}
