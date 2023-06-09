package com.epam.esm.repository_impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of DAO Interface {@link OrderRepository}.
 *
 * @author Danylo Proshyn
 */

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void insertEntity(Order order) {

        em.persist(order);
    }

    @Override
    public List<Order> getEntitiesByUser(Long userId) {

        CriteriaBuilder      cb   = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq   = cb.createQuery(Order.class);
        Root<Order>          root = cq.from(Order.class);

        cq.select(root).where(cb.equal(root.get("user"), userId));

        return em.createQuery(cq).getResultList();
    }
}
