package com.university.dao.mybatis;

import com.university.entity.Club;
import com.university.mybatis.ClubMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ClubMyBatisDao {
    
    @Inject
    private ClubMapper clubMapper;
    
    public List<Club> findAll() {
        return clubMapper.findAll();
    }
    
    public Club findById(Long id) {
        return clubMapper.findById(id);
    }
    
    @Transactional
    public void save(Club club) {
        clubMapper.insert(club);
    }
}
