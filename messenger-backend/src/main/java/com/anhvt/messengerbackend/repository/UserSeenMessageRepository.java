package com.anhvt.messengerbackend.repository;

import com.anhvt.messengerbackend.entity.MessageUserEntity;
import com.anhvt.messengerbackend.entity.MessageUserKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/22/2024
 */
@Repository
public interface UserSeenMessageRepository extends JpaRepository<MessageUserEntity, MessageUserKey> {

    MessageUserEntity findAllByMessageIdAndUserId(int messageId, int userId);

}
