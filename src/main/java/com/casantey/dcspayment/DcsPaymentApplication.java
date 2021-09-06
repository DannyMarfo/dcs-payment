package com.casantey.dcspayment;

import com.casantey.dcspayment.apiuser.APIUser;
import com.casantey.dcspayment.apiuser.APIUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DcsPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DcsPaymentApplication.class, args);
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner runner(APIUserService apiUserService){
        return args -> {
            APIUser apiUser = apiUserService.findByUsername("sys");
            if(apiUser == null)
                apiUserService.save(new APIUser(null,"System User","sys","master","ROLE_ADMIN"));
        };
    }

}
