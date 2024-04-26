package org.example.virtualtapcash.services;

import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MBankingService {
    @Autowired
    private UserJpaRepository userJpaRepository;

    public MBankingAccount createdUser(MBankingAccount user) {
        return userJpaRepository.save(user);
    }

    public List<MBankingAccount> getAllUser() {
            return userJpaRepository.findAll();
    }

    public Optional<MBankingAccount> getUserById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    public MBankingAccount createUser(MBankingAccount user) {
        return userJpaRepository.save(user);
    }

    public Optional<MBankingAccount> updateUser(Long userId, MBankingAccount user) {
        if (user.getUserId() != userId) {
            throw new IllegalArgumentException("Mismatched user ID in the request path and body.");
        }
        return userJpaRepository.findById(userId).map(existingUser -> {
            // Here, explicitly set the fields that can be updated
            existingUser.setUsername(user.getUsername());
            existingUser.setVirtualTapCashId(user.getVirtualTapCashId());
            existingUser.setCustomerName(user.getCustomerName());
            existingUser.setAccountNumber(user.getAccountNumber());
            existingUser.setBankAccountBalance(user.getBankAccountBalance());

            // After setting the new values, save the updated entity
            return userJpaRepository.save(existingUser);
        });
    }


    public void deleteUser(Long userId) {
        userJpaRepository.deleteById(userId); // This throws EmptyResultDataAccessException if the entity with the given id is not found
    }


}
