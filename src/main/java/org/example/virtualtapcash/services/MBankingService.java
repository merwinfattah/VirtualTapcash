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

    public void deleteUser(Long id) {
        userJpaRepository.deleteById(id);
    }

    public MBankingAccount updateUser(MBankingAccount user) {
        return userJpaRepository.save(user);
    }

}
