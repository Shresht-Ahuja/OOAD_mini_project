// UserRepository.java
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

// SongRepository.java
@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
    List<Song> findByGenreIgnoreCase(String genre);
    List<Song> findByTitleContainingIgnoreCase(String keyword);
    List<Song> findByArtist_ArtistId(int artistId);
}

// AlbumRepository / ArtistRepository / PlaylistRepository / ReviewRepository
// → same pattern: extend JpaRepository<Entity, Integer>