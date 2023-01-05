package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            // 150L의 member는 영속상태가 된다.
            Member member = em.find(Member.class, 150L);
            // 값이 변경되어 Dirty Checking 이 일어난다.
            member.setName("AAAAA");

            // EntityManager 내의 영속성 컨택스트 전체가 지워진다.
            em.close();
            System.out.println("=== AFTER em.close() ===");

            // 150L의 member를 다시 조회해도 영속성 컨택스트가 닫혀서 저장할 곳이 없다.
            Member member2 = em.find(Member.class, 150L);

            System.out.println("=== BEFORE commit ===");
            tx.commit();
        } catch (Exception e) {
            // 비정상 종료시 rollback
            tx.rollback();
        } finally {
            // EntityManager는 쓰레드간 공유해선 안되므로 사용을 다하면 꼭 닫아줘야 한다.
            em.close();
        }
        emf.close();

    }
}
