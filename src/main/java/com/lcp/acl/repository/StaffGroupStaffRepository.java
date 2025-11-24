package com.lcp.acl.repository;

import com.lcp.acl.entity.StaffGroupStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StaffGroupStaffRepository extends JpaRepository<StaffGroupStaff, UUID> {

    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value =
                    "INSERT INTO staff_group_staff (staff_group_id, staff_id, joined_date, added_by) " +
                            "VALUES (:groupId, :userId, :joinedDate, :addedBy) " +
                            "ON CONFLICT (staff_group_id, staff_id) " +
                            "DO UPDATE SET joined_date = :joinedDate, added_by = :addedBy"
    )
    void addUserToGroup(UUID groupId, Long userId, Long addedBy, LocalDateTime joinedDate);

    boolean existsAllByStaffIdInAndStaffGroupId(List<Long> userIds, UUID groupId);

    @Transactional
    void deleteAllByStaffGroupIdAndStaffIdIn(UUID groupId, List<Long> userIds);

    List<StaffGroupStaff> findAllByStaffGroupId(UUID groupId);

    List<StaffGroupStaff> findAllByStaffId(Long staffId);
}
