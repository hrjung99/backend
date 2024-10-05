package swyp.swyp6_team7.image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select l from Image l where l.article.id = :id")
    List<Image> findByArticleId(@Param("id") Long id);

}
