package com.university.mybatis;

import com.university.entity.Club;
import org.mybatis.cdi.Mapper;

import java.util.List;

@Mapper
public interface ClubMapper {
    List<Club> findAll();
    Club findById(Long id);
    void insert(Club club);
}
