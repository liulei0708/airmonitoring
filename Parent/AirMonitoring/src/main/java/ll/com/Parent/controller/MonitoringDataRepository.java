package ll.com.Parent.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MonitoringDataRepository extends JpaRepository<MonitoringData, Long>
{
	@Modifying
	@Transactional
	@Query("delete from MonitoringData md where md.newsid = ?1")
	void deleteByNewsId(long id); 
	
	
	List<MonitoringData> getBynewsid(long id);
	
	Boolean existsBynewsid(long id);
}
