package com.project.planit.storage.entity;

import com.project.planit.member.entity.Member;
import com.project.planit.room.entity.Room;
import com.project.planit.storage.dto.CreateStorageRequest;
import com.project.planit.storage.dto.UpdateStorageRequest;
import com.project.planit.util.BaseEntity;
import com.project.planit.util.BaseRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.project.planit.storages.entity
 * fileName       : Storage
 * author         : dongk
 * date           : 2023-01-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-01-23        dongk       최초 생성
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="storage")
public class Storage extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="storage_id")
    private Long id;

    @Column(name="storage_name")
    private String storageName;

<<<<<<< HEAD
    private Boolean confirmed;
=======
    private boolean confirmed;
>>>>>>> 238216d15b44b9c3433b2f1723ab4d2689c983b1

    @Column(name="day_order")
    private int dayOrder;

    private double lat;

    private double lng;

    @Column(name="category_name")
    @Enumerated(EnumType.STRING) //이넘 타입은 문자열로
    private Category categoryName;

    @Embedded
    @NotNull
    private BaseRequest baseRequest;

    @ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="member_id" ,referencedColumnName = "member_id")
    private Member member;

    @ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="room_id",referencedColumnName = "room_id")
    private Room room;

    public static Storage create(CreateStorageRequest request,Member member,Room room){
        Storage storage=Storage.builder()
            .storageName(request.getStorageName())
            .confirmed(request.getConfirmed())
            .dayOrder(request.getDayOrder())
            .lat(request.getLat())
            .lng(request.getLng())
            .member(member)
            .room(room)
            .categoryName(request.getCategoryName())
            .baseRequest(BaseRequest.builder()
                .updater(member.getName())
                .constructor(member.getName())
                .build())
            .build();
        return storage;
    }

    public void update(UpdateStorageRequest request,Member member){
        this.confirmed=request.getConfirmed();
        this.dayOrder=request.getDayOrder();
        this.baseRequest=BaseRequest.builder()
            .constructor(this.baseRequest.getConstructor())
            .updater(member.getAppId())
            .build();
    }
}
