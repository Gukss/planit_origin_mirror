package com.project.planit.memberRoom.entity;

import com.project.planit.member.dto.UpdateMemberRequest;
import com.project.planit.member.entity.Member;
import com.project.planit.memberRoom.dto.CreateMemberRoomRequest;
<<<<<<< HEAD
import com.project.planit.memberRoom.dto.FindMemberRoomResponse;
=======
>>>>>>> 238216d15b44b9c3433b2f1723ab4d2689c983b1
import com.project.planit.memberRoom.dto.UpdateMemberRoomRequest;
import com.project.planit.room.entity.Room;
import com.project.planit.util.BaseEntity;
import com.project.planit.util.BaseRequest;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * packageName    : com.project.planit.memberRoom.entity
 * fileName       : MemberRoom
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
@Table(name = "member_room")
public class MemberRoom extends BaseEntity {

    @Id
    @Column(name="member_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Boolean participation;

    @Embedded
    @NotNull
    private BaseRequest baseRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Member member;

    public static MemberRoom create(CreateMemberRoomRequest request,Room room,Member member) {
        MemberRoom memberRoom = MemberRoom.builder()
                .participation(true)
                .room(room)
                .member(member)
                .baseRequest(BaseRequest.builder()
                        .constructor(request.getInvitedName())
                        .updater(request.getInvitedName())
                        .build())
                .build();

        return memberRoom;
    }

    public void update(String memberAppId, UpdateMemberRoomRequest request){
<<<<<<< HEAD
        this.participation=request.getParticipation();
=======
        this.participation=request.getInAndOut();
>>>>>>> 238216d15b44b9c3433b2f1723ab4d2689c983b1
        this.baseRequest=BaseRequest.builder()
            .constructor(this.baseRequest.getConstructor())
            .updater(memberAppId)
            .build();
    }
<<<<<<< HEAD

    //todo: 일단 colorCode 없는 버전으로 만들었다.
    public FindMemberRoomResponse createFindMemberRoomResponse(){
        return FindMemberRoomResponse.builder()
                .roomId(this.room.getId())
                .memberId(this.member.getId())
                .participation(this.participation)
//                .colorCode(this.)
                .build();
    }
=======
>>>>>>> 238216d15b44b9c3433b2f1723ab4d2689c983b1
}
