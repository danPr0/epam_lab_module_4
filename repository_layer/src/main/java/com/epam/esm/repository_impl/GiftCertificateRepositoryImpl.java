package com.epam.esm.repository_impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.sqm.TemporalUnit;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.conf.ParamType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

/**
 * Implementation of DAO Interface {@link GiftCertificateRepository}.
 *
 * @author Danylo Proshyn
 */

@Repository
@Transactional
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void insertEntity(GiftCertificate gc) {

        em.persist(gc);
    }

    @Override
    public Optional<GiftCertificate> getEntity(Long id) {

        return Optional.ofNullable(em.find(GiftCertificate.class, id));

    }

    @Override
    public void updateEntity(GiftCertificate gc) {

        em.merge(gc);
    }

    @Override
    public void deleteEntity(Long id) {

        em.detach(em.find(GiftCertificateRepository.class, id));
    }

    @Override
    public List<GiftCertificate> getAll(
            int page, int total, Optional<List<String>> tagNames, Optional<String> namePart,
            Optional<String> descriptionPart, Optional<String> nameOrder, Optional<String> createDateOrder) {

//        CriteriaBuilder                cb   = em.getCriteriaBuilder();
//        CriteriaQuery<GiftCertificate> cq   = cb.createQuery(GiftCertificate.class);
//        Root<GiftCertificate>          root = cq.from(GiftCertificate.class);
//
//        cq.select(root).where(cb.lessThan(cb.currentTimestamp(),
//                cb.function("timestampadd", Timestamp.class, cb.literal(TemporalUnit.DAY), root.get("duration"),
//                        root.get("createDate"))));

        SelectConditionStep<Record> query = select().from(table("gift_certificates"))
                .where("current_date() < timestampadd(day, duration, create_date)");
        if (tagNames.isPresent()) {
            for (String tagName : tagNames.get()) {
                query = query.andExists(
                        select(field("gc_id")).from(table("gift_certificates_tags")).leftJoin(table("tags"))
                                .on(field("tag_id").eq(field("tags.id")))
                                .where(field("gift_certificates.id").eq(field("gc_id")))
                                .and(field("tags.name").eq(tagName)));
            }
        }

        if (namePart.isPresent()) {
            query = query.and(lower(field("name", String.class)).like('%' + namePart.get().toLowerCase() + '%'));
        }
        if (descriptionPart.isPresent()) {
            query = query.and(
                    lower(field("description", String.class)).like('%' + descriptionPart.get().toLowerCase() + '%'));
        }

        SortField<Object> nameSort =
                field("name").sort(nameOrder.map(s -> SortOrder.valueOf(s.toUpperCase())).orElse(SortOrder.DEFAULT));
        SortField<Object> createDateSort = field("create_date").sort(
                createDateOrder.map(s -> SortOrder.valueOf(s.toUpperCase())).orElse(SortOrder.DEFAULT));

        String sql = query.orderBy(nameSort, createDateSort).limit(total).offset((page - 1) * total)
                .getSQL(ParamType.INLINED);

        return em.createNativeQuery(sql, GiftCertificate.class).getResultList();
    }
}
