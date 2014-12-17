package cn.kc.platform.account.repository;

import cn.kc.platform.account.entity.Member;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Repository
@Transactional(readOnly = true)
public class AccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private PasswordService passwordService;

    @Transactional
    public Member save(Member member) {
        member.setPassword(passwordService.encryptPassword(member.getPassword()));
        entityManager.persist(member);
        return member;
    }

    public Member findByEmail(String email) {
        try {
            return entityManager.createNamedQuery(Member.FIND_BY_EMAIL, Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }


}
