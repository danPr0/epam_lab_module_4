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
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PageImpl<GiftCertificate> getAll(
            int page, int total, Optional<List<String>> tagNames, Optional<String> textFilter,
            Optional<String> nameOrder, Optional<String> createDateOrder) {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<GiftCertificate> certificateQuery =
                createCertificatesQuery(cb, tagNames, textFilter, nameOrder, createDateOrder);
        CriteriaQuery<Long> countQuery = createCountQuery(cb, tagNames, textFilter);

        List<GiftCertificate> certificates =
                em.createQuery(certificateQuery).setFirstResult((page - 1) * total).setMaxResults(total).getResultList();
        Long totalCount = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(certificates, Pageable.ofSize(total), totalCount);
    }

    private CriteriaQuery<GiftCertificate> createCertificatesQuery(
            CriteriaBuilder cb, Optional<List<String>> tagNames, Optional<String> textFilter,
            Optional<String> nameOrder, Optional<String> createDateOrder) {

        CriteriaQuery<GiftCertificate> cq   = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate>          root = cq.from(GiftCertificate.class);

        List<Predicate> predicates = createPredicates(tagNames, textFilter, cb, cq, root);

        List<Order> orderList = new ArrayList<>();
        createDateOrder.ifPresent(s -> orderList.add(s.equalsIgnoreCase("desc")
                                                     ? cb.desc(root.get(GiftCertificate_.createdDate))
                                                     : cb.asc(root.get(GiftCertificate_.createdDate))));
        nameOrder.ifPresent(s -> orderList.add(s.equalsIgnoreCase("desc")
                                               ? cb.desc(root.get(GiftCertificate_.name))
                                               : cb.asc(root.get(GiftCertificate_.name))));
        orderList.add(cb.asc(root.get(GiftCertificate_.id)));

        cq.where(predicates.toArray(Predicate[]::new)).orderBy(orderList);

        return cq;
    }

    private CriteriaQuery<Long> createCountQuery(
            CriteriaBuilder cb, Optional<List<String>> tagNames, Optional<String> textFilter) {

        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<GiftCertificate> root = cq.from(GiftCertificate.class);

        List<Predicate> countPredicates =
                createPredicates(tagNames, textFilter, cb, cq, root);
        cq.select(cb.count(root)).where(countPredicates.toArray(Predicate[]::new));

        return cq;
    }

    private List<Predicate> createPredicates(
            Optional<List<String>> tagNames, Optional<String> textFilter, CriteriaBuilder cb, CriteriaQuery<?> cq,
            Root<GiftCertificate> root) {

        List<Predicate> predicates = new ArrayList<>();

        Expression<Time> timeDiff =
                cb.function("timediff", Time.class, cb.currentTimestamp(), root.get(GiftCertificate_.createdDate));
        predicates.add(cb.greaterThan(cb.prod(root.get(GiftCertificate_.duration).as(Integer.class), 24 * 60 * 60),
                cb.function("time_to_sec", Integer.class, timeDiff)));

        if (tagNames.isPresent()) {
            for (String tagName : tagNames.get()) {
                Subquery<Tag>              sq        = cq.subquery(Tag.class);
                Root<GiftCertificate>      sqRootGc  = sq.from(GiftCertificate.class);
                Join<GiftCertificate, Tag> sqJoinTag = sqRootGc.join(GiftCertificate_.tags, JoinType.LEFT);

                predicates.add(cb.exists(sq.where(
                        cb.and(cb.equal(root.get(GiftCertificate_.id), sqRootGc.get(GiftCertificate_.id)),
                                cb.equal(sqJoinTag.get(Tag_.name), tagName)))));
            }
        }

        textFilter.ifPresent(
                s -> predicates.add(cb.or(
                        cb.like(cb.lower(root.get(GiftCertificate_.name)), '%' + s.toLowerCase() + '%'),
                        cb.like(cb.lower(root.get(GiftCertificate_.description)), '%' + s.toLowerCase() + '%'))));

        return predicates;
    }
}
