package com.example.forum.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


/**
 * 공통 속성 클래스
 * 생성 시간, 수정 시간을 저장.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 생성, 수정 이벤트 사이클 리스너
public abstract class BaseEntity {

    @CreatedDate // 엔티티가 생성될 때 자동으로 시간이 저장됨.
    @Column(nullable = false)
    protected LocalDateTime createAt; // 생성일

    @LastModifiedDate // 엔티티의 값을 변경할 때 자동으로 시간이 저장됨.
    protected LocalDateTime updateAt; // 수정일
}
