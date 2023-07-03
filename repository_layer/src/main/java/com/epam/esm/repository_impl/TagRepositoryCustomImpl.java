package com.epam.esm.repository_impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.TagRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public class TagRepositoryCustomImpl implements TagRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Tag> complexJPQLQuery() {

        User user = em.createQuery("select o.user from Order o group by o.user order by sum(o.cost) desc limit 1",
                User.class).getSingleResult();

        TypedQuery<Tag> query = em.createQuery(
                "select tag from Order o left join o.giftCertificates gc left join gc.tags tag " +
                        "where o.user.id = :userId group by tag.id order by count(tag.id) desc limit 1", Tag.class);
        query.setParameter("userId", user.getId());

        return Optional.ofNullable(query.getSingleResult());
    }
}
