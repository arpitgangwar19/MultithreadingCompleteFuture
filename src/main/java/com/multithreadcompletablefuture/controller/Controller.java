package com.multithreadcompletablefuture.controller;


import com.multithreadcompletablefuture.model.User;
import com.multithreadcompletablefuture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class Controller {


    @Autowired
    UserService userService;

    @PostMapping(value = "/adduser", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseEntity addUsers(@RequestParam(value = "file") MultipartFile[] file) throws Exception {
        for(MultipartFile files : file)
        {
            userService.saveUsers(files);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "users",produces = "application/json")
    public CompletableFuture<ResponseEntity> getUsers()
    {
        return userService.findAllUsers().thenApply(ResponseEntity::ok);

    }

    @GetMapping(value = "usersMuilti",produces = "application/json")
    public ResponseEntity getUsersThread()
    {
        CompletableFuture<List<User>> user1 = userService.findAllUsers();
        CompletableFuture<List<User>> user2 = userService.findAllUsers();
        CompletableFuture<List<User>> user3 = userService.findAllUsers();
        CompletableFuture.allOf(user2,user1,user3).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
