package com.casantey.dcspayment.apiuser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service @Transactional @Slf4j
public class APIUserService implements UserDetailsService {

    @Autowired
    private APIUserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        APIUser apiUser = repo.findByUsername(username);
        if(apiUser == null){
            log.error("User {}, not found",username);
            throw new UsernameNotFoundException("User, not found");
        }else{
            log.info("User {}, found",username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(apiUser.getRole()));
        return new User(apiUser.getUsername(),apiUser.getPassword(),authorities);
    }

    public List<APIUser> fetchAll(){
        return repo.findAll();
    }

    public APIUser findById(Long id){
        return repo.findById(id).get();
    }

    public APIUser findByUsername(String username){
        return  repo.findByUsername(username);
    }

    public APIUser findByUsernameAndPassword(String username, String password){
        return  repo.findByUsernameAndPassword(username,password);
    }

    public APIUser save(APIUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return  repo.save(user);
    }

    public void delete(APIUser user){
         repo.delete(user);
    }

    public void deleteById(Long id){
        repo.deleteById(id);
    }


}
