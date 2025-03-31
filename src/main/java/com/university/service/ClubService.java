package com.university.service;

import com.university.dao.jpa.ClubJpaDao;
import com.university.dao.mybatis.ClubMyBatisDao;
import com.university.entity.Club;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class ClubService {
    
    @Inject
    private ClubJpaDao clubJpaDao;
    
    @Inject
    private ClubMyBatisDao clubMyBatisDao;
    
    // JPA methods
    public List<Club> getAllClubsJpa() {
        return clubJpaDao.findAll();
    }
    
    public Club getClubByIdJpa(Long id) {
        return clubJpaDao.findById(id);
    }
    
    @Transactional
    public void saveClubJpa(Club club) {
        clubJpaDao.save(club);
    }
    
    // MyBatis methods
    public List<Club> getAllClubsMyBatis() {
        return clubMyBatisDao.findAll();
    }
    
    public Club getClubByIdMyBatis(Long id) {
        return clubMyBatisDao.findById(id);
    }
    
    @Transactional
    public void saveClubMyBatis(Club club) {
        clubMyBatisDao.save(club);
    }
}
