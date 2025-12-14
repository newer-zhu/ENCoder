package com.holden.vocabulary.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("vocabulary")
public class Vocabulary {

    private Long id;

    private String word;

    private String meaning;

    private LocalDateTime createdAt;
}
