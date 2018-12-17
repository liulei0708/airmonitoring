package ll.com.Parent.controller;

import java.util.List;
 
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NewsRepository extends PagingAndSortingRepository<News, Long> {

	@Query(value = "select new ll.com.Parent.controller.NewsPO(id,title,date,source) from News order by id desc")
	List<NewsPO> getNewsList();
	
	@Query(value = "select new ll.com.Parent.controller.NewsPO(id,title,date,source) from News")
	List<NewsPO> getNewsList(Pageable pageable);

	News getNewsById(long id);
}
