package project1.project1.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project1.project1.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Admin findByEmailAndPassword(String email, String password);

	List<Admin> findByEmail(String email);
	
}
