package ru.kpfu.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.todo.entity.GlobalPermission;

@Repository
public interface GlobalPermissionRepository extends JpaRepository<GlobalPermission, Long> {
}
