package ll.com.Parent.controller;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{
	User findByUserNameAndPassWord(String userName,String pwd);
}
