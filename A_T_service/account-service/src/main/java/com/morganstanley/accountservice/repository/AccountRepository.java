// A_T_Service/account-service/src/main/java/com/morganstanley/accountservice/repository/AccountRepository.java
package com.morganstanley.accountservice.repository;

import com.morganstanley.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
