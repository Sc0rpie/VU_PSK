package com.university.dao.jpa;

import com.university.entity.Club;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class ClubJpaDao {
    
    @PersistenceContext(unitName = "universityPU")
    private EntityManager em;
    
    public List<Club> findAll() {
        return em.createQuery("SELECT c FROM Club c", Club.class).getResultList();
    }
    
    public Club findById(Long id) {
        return em.find(Club.class, id);
    }
    
    @Transactional
    public void save(Club club) {
        if (club.getId() == null) {
            em.persist(club);
        } else {
            em.merge(club);
        }
    }
}
