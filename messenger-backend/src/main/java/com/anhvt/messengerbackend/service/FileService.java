/**
 * Copyright 2024
 * Name: FileService
 */
package com.anhvt.messengerbackend.service;

import com.anhvt.messengerbackend.entity.FileEntity;
import com.anhvt.messengerbackend.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Comment class
 *
 * @author trunganhvu
 * @date 9/23/2024
 */
@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public FileEntity save(FileEntity f) {
        return fileRepository.save(f);
    }

    public FileEntity findByFkMessageId(int id) {
        return fileRepository.findByMessageId(id);
    }

    public List<String> getFilesUrlByGroupId(int groupId) {
        return fileRepository.findFilesUrlsByGroupId(groupId);
    }

    public String findFileUrlByMessageId(int id) {
        return fileRepository.findFileUrlByMessageId(id);
    }
}