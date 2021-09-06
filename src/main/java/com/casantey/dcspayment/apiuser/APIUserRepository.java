package com.casantey.dcspayment.apiuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface APIUserRepository extends JpaRepository<APIUser, Long> {

    APIUser findByUsername(String username);
    APIUser findByUsernameAndPassword(String username, String password);

}
