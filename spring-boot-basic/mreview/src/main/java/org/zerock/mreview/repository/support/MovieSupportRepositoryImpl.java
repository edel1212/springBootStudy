package org.zerock.mreview.repository.support;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.QMovie;
import org.zerock.mreview.entity.QMovieImage;
import org.zerock.mreview.entity.QReview;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class MovieSupportRepositoryImpl extends QuerydslRepositorySupport  implements MovieSupportRepository{

    public MovieSupportRepositoryImpl() {
        super(Movie.class);
    }

    @Override
    public Page<Object[]> getListWithQuerydsl(Pageable pageable) {

        // 1. Create QObject
        QMovie movie = QMovie.movie;
        QReview review = QReview.review;
        QMovieImage movieImage = QMovieImage.movieImage;

        // 2 . Create JPQLQuery
        JPQLQuery<Movie> jpqlQUery = from(movie);

        // 3 . Add Left Join
        jpqlQUery.leftJoin(movieImage)
                .on(movieImage.movie.eq(movie)
                        // 👍 SubQuery를 사용하여 inum의 Max()값을 가져옴
                    .and(movieImage.inum.eq(
                        // 💬 SubQuery는 JPAExpressions을 사용해서 가져와야한다!!
                        // 기존에처럼 from(movieImage)를 사용하여 사용할 경우 Error는 없지만
                        // 첫 데이터 한줄의 MoiveImage값은 나오고
                        // 그 이후 데이터는 null로 박혀서 나옴!!
                        JPAExpressions
                                .select(movieImage.inum.max())
                                .from(movieImage)
                                .where(movieImage.movie.eq(movie))
                            )
                        )
                );
        jpqlQUery.leftJoin(review).on(review.movie.eq(movie));

        // 4 . Create JPQLQuery<Tuple>
        JPQLQuery<Tuple> tuple = jpqlQUery.select(movie, movieImage, review.grade.avg(), review.countDistinct());




        // 5 . Sorting
        Sort sort = pageable.getSort();
        sort.stream().forEach(order ->{
            // get Sort Key
            String prop = order.getProperty();
            // get Sort Type
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            // crate PathBuilder
            PathBuilder<Movie> orderByExpression = new PathBuilder<Movie>(Movie.class,"movie");
            // apply Sort for Tuple
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });


        // ☠️ group By를 해주지 않을 경우 단건이 나옴!!! [ 삽질함!! ]
        tuple.groupBy(movie);

        // 6 . Apply Pageable
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        // 7 . Parse List
        List<Tuple> result = tuple.fetch();

        // 8 . get Count - 💬 Reason : Use PageImpl Parameter
        long count = tuple.fetchCount();

        //9 . Return PageImpl : needs Three Parameter ( List , Pageable , Long )
        return new PageImpl( result.stream().map(Tuple::toArray).collect(Collectors.toList())
                            , pageable
                            , count);
    }

    @Override
    public List<Object[]> testGetMovieWithAllQuerydls(Long mno) {

        QMovie movie = QMovie.movie;
        QReview review  = QReview.review;
        QMovieImage movieImage = QMovieImage.movieImage;

        JPQLQuery<Movie> jpqlQuery = from(movie);

        jpqlQuery.where(movie.mno.eq(mno));

        jpqlQuery.leftJoin(movieImage).on(movieImage.movie.eq(movie));
        jpqlQuery.leftJoin(review).on(review.movie.eq(movie));

        //💬 Movie가 아닌 MovieImage로 Group By 해줘야함
        jpqlQuery.groupBy(movieImage);

        List<Tuple> result = jpqlQuery.select(movie, movieImage, review.grade.avg(), review.count()).fetch();

        return result.stream().map(Tuple::toArray).collect(Collectors.toList());
    }
}
