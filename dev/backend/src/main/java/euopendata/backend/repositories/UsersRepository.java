package euopendata.backend.repositories;

import euopendata.backend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

    @Query("SELECT u.email FROM Users u WHERE u.id=?1")
    String getEmailById(Long id);

    @Query("SELECT u.firstName FROM Users u WHERE u.id=?1")
    String getFirstNameById(Long id);

    @Query("SELECT u.lastName FROM Users u WHERE u.id=?1")
    String getLastNameById(Long id);

    @Query("SELECT u.username FROM Users u WHERE u.id=?1")
    String getUsernameById(Long id);

    @Query("SELECT u FROM Users u WHERE u.email=?1")
    Optional<Users> findByEmail(String email);

    Optional<Users> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE Users a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);
}
