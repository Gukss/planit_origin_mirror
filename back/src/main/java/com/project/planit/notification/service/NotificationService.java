package com.project.planit.notification.service;

import com.project.planit.notification.dto.CreateNotificationRequest;
import com.project.planit.notification.dto.FindNotificationResponse;
import com.project.planit.notification.dto.UpdateNotificationRequest;

import java.util.ArrayList;
import java.util.List;

public interface NotificationService {
    List<FindNotificationResponse> findNotification(String memberAppId, Long memberId);
    void createNotification(List<CreateNotificationRequest> request, Long sendMemberId);
    void updateNotification(UpdateNotificationRequest request, Long memberId);
}
