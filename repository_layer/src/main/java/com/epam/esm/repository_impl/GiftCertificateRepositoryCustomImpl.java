package com.epam.esm.repository_impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificate_;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.Tag_;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.GiftCertificateRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of DAO Interface {@link GiftCertificateRepository}.
 *
 * @author Danylo Proshyn
 */

@Repository
@Transactional
public class GiftCertificateRepositoryCustomImpl implements GiftCertificateRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<GiftCertificate> getAll(
            int page, int total, Optional<List<String>> tagNames, Optional<String> namePart,
            Optional<String> descriptionPart, Optional<String> nameOrder, Optional<String> createDateOrder) {

        CriteriaBuilder                cb       = em.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq       = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate>          cqRootGc = cq.from(GiftCertificate.class);

        List<Predicate> predicates = new ArrayList<>();

        Expression<Time> timeDiff =
                cb.function("timediff", Time.class, cb.currentTimestamp(), cqRootGc.get(GiftCertificate_.createdDate));
        predicates.add(cb.greaterThan(cb.prod(cqRootGc.get(GiftCertificate_.duration).as(Integer.class), 24 * 60 * 60),
                cb.function("time_to_sec", Integer.class, timeDiff)));

        if (tagNames.isPresent()) {
            for (String tagName : tagNames.get()) {
                Subquery<Tag>              sq        = cq.subquery(Tag.class);
                Root<GiftCertificate>      sqRootGc  = sq.from(GiftCertificate.class);
                Join<GiftCertificate, Tag> sqJoinTag = sqRootGc.join(GiftCertificate_.tags, JoinType.LEFT);

                predicates.add(cb.exists(sq.where(
                        cb.and(cb.equal(cqRootGc.get(GiftCertificate_.id), sqRootGc.get(GiftCertificate_.id)),
                                cb.equal(sqJoinTag.get(Tag_.name), tagName)))));
            }
        }

        namePart.ifPresent(
                s -> predicates.add(cb.like(cb.lower(cqRootGc.get(GiftCertificate_.name)), '%' + s.toLowerCase() + '%')));
        descriptionPart.ifPresent(
                s -> predicates.add(cb.like(cb.lower(cqRootGc.get(GiftCertificate_.description)), '%' + s.toLowerCase() + '%')));

        List<Order> orderList = new ArrayList<>();
        nameOrder.ifPresent(s -> orderList.add(s.equalsIgnoreCase("desc")
                ? cb.desc(cqRootGc.get(GiftCertificate_.name))
                : cb.asc(cqRootGc.get(GiftCertificate_.name))));
        createDateOrder.ifPresent(s -> orderList.add(s.equalsIgnoreCase("desc")
                ? cb.desc(cqRootGc.get(GiftCertificate_.createdDate))
                : cb.asc(cqRootGc.get(GiftCertificate_.createdDate))));
        orderList.add(cb.asc(cqRootGc.get(GiftCertificate_.id)));

        cq.where(cb.and(predicates.toArray(Predicate[]::new))).orderBy(orderList);

        return em.createQuery(cq).setFirstResult((page - 1) * total).setMaxResults(total).getResultList();
    }
}
