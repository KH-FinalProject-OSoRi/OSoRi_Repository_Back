package com.kh.osori.notification.model.service;
import com.kh.osori.user.controller.UserController;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.kh.osori.notification.model.Dao.NotificationDao;
import com.kh.osori.notification.model.vo.Notification;

@Service
public class NotificationService {

    private final UserController userController;
    @Autowired
    private SimpMessagingTemplate messageTemp;
	
	@Autowired
	private NotificationDao dao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;

    NotificationService(UserController userController) {
        this.userController = userController;
    }
	
	public void sendMessageToMembers(String receiver, String sender, String message, String type, int inviteNum) {
		Notification noti = Notification.builder()
										.nType(type)
										.sender(sender)
										.receiver(receiver)
										.isRead("N")
										.message(message)
										.inviteNum(inviteNum)
										.build();
	
		dao.insertNotification(sqlSession,noti);
		noti.setNotiId(noti.getNotiId());
			
		// 실시간 WebSocket 개별 전송
		messageTemp.convertAndSend("/single/notifications/"+receiver.trim(), noti);
	}

}
