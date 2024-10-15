package mate.academy.skillshare.repository.user;

import java.util.Optional;
import mate.academy.skillshare.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles", "favourites.category", "favourites.reviews"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"roles", "favourites.category", "favourites.reviews"})
    Optional<User> findById(Long id);
}
