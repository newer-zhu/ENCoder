package com.holden.vocabulary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.holden.vocabulary.entity.Vocabulary;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VocabularyMapper extends BaseMapper<Vocabulary> {
}