package com.project.planit.storage.controller;


import com.project.planit.member.entity.Member;
import com.project.planit.room.dto.UpdateRoomResponse;
import com.project.planit.room.entity.Room;
import com.project.planit.storage.dto.CreateStorageRequest;
import com.project.planit.storage.dto.CreateStorageResponse;
import com.project.planit.storage.dto.UpdateStorageRequest;
import com.project.planit.storage.dto.UpdateStorageResponse;
import com.project.planit.storage.entity.Category;
import com.project.planit.storage.entity.Storage;
import com.project.planit.storage.service.StorageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storages")
public class StorageController {
    private final StorageServiceImpl storageService;
    @GetMapping(path="/rooms/{roomId}")
    public ResponseEntity<?> findStorageList(@PathVariable Long roomId){
     return ResponseEntity.ok(storageService.findStorageList(roomId));
    }

    @PostMapping
    public ResponseEntity<CreateStorageResponse> createStorage(@RequestBody CreateStorageRequest request){
        // @TODO : 토큰 아이디로 변환
        Long reqestMemberId=1L;
        Storage newStorage = storageService.createStorage(request, reqestMemberId);
        String name = newStorage.getStorageName();
        Long storagesId = newStorage.getId();
        double lat = newStorage.getLat();
        double lng = newStorage.getLng();
        int dayOrder = newStorage.getDayOrder();
        Category categoryName = newStorage.getCategoryName();
        Long roomId = newStorage.getRoom().getId();
        Long memberId = newStorage.getMember().getId();
        Boolean confirmed = newStorage.getConfirmed();
        CreateStorageResponse createStorageResponse = CreateStorageResponse.create(memberId, storagesId, name, confirmed, lat, lng, dayOrder, categoryName, roomId);

        ResponseEntity res = ResponseEntity.ok().body(createStorageResponse);
        return res;
    }

    @PatchMapping
    public ResponseEntity<UpdateStorageResponse> updateStorage(@RequestBody UpdateStorageRequest request){
        // @TODO : 토큰 아이디로 변환
        Long memberId=1L;
        Storage updatedStorage = storageService.updateStorage(request, memberId);
        Member member = updatedStorage.getMember();
        Room room = updatedStorage.getRoom();
        UpdateStorageResponse updateStorageResponse = UpdateStorageResponse.create(member.getId(), updatedStorage.getId(), updatedStorage.getStorageName(), updatedStorage.getConfirmed(), updatedStorage.getLat(), updatedStorage.getLng(), updatedStorage.getDayOrder(), updatedStorage.getCategoryName(), room.getId());
        ResponseEntity res = ResponseEntity.ok().body(updateStorageResponse);
        return res;
    }
}
