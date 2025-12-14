package com.ihb.mytalentbackend.service;

import com.ihb.mytalentbackend.domain.Role;
import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.domain.talent.TalentBoard;
import com.ihb.mytalentbackend.repository.UserRepository;
import com.ihb.mytalentbackend.repository.talent.TalentRepository;
import com.ihb.mytalentbackend.repository.talent.TalentRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TalentRepository talentRepository;
    private final TalentRequestRepository talentRequestRepository;


    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        if (user.getCredit() == null) {
            user.setCredit(100);
        }

        return userRepository.save(user);
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }



    @Override
    public void changeRole(String email, Role newRole) {
        userRepository.updateUserRole(email, newRole);
    }


    @Transactional
    @Override
    public void deleteUser(Long id) {
        // 1) 이 유저가 신청자로 남긴 요청들 삭제
        talentRequestRepository.deleteByRequesterId(id);

        // 2) 이 유저가 올린 재능들에 달린 요청들 삭제
        List<TalentBoard> talents = talentRepository.findByUser_Id(id);
        for (TalentBoard t : talents) {
            talentRequestRepository.deleteByTalentId(t.getId());
        }

        // 3) 재능 삭제
        talentRepository.deleteByUserId(id);

        // 4) 유저 삭제
        userRepository.deleteById(id);
    }




    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
