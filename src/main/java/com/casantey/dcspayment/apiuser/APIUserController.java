package com.casantey.dcspayment.apiuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apiUser")
public class APIUserController {

    @Autowired
    private APIUserService service;

    @GetMapping("/all")
    public ResponseEntity<List<APIUser>> findAll(){
        return new ResponseEntity<>(service.fetchAll(), HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<APIUser> findByUsername(@PathVariable("username") String username){
        return new ResponseEntity<>(service.findByUsername(username), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<APIUser> findByUsernameAndPassword(@RequestBody APIUser user){
        return new ResponseEntity<>(service.findByUsernameAndPassword(user.getUsername(), user.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/saveUser")
    public ResponseEntity<APIUser> saveUser(@RequestBody APIUser user){
        return new ResponseEntity<>(service.save(user), HttpStatus.CREATED);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody APIUser user){
        service.delete(user);
        return new ResponseEntity<>( HttpStatus.OK);
    }



}
