package com.lambdaschool.school.repository;

import com.lambdaschool.school.model.Student;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

// CrudRepository
// PagingAndSortingRepository - is the crud repository with paging and sorting
// JpaRepository - crud repository with paging and sorting and sessions - cant use sessions with a rest api
//      ^ don't use
public interface StudentRepository extends PagingAndSortingRepository<Student, Long>
{
    List<Student> findByStudnameContainingIgnoreCase(String name);
}
