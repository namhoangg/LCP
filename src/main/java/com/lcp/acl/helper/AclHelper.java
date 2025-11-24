package com.lcp.acl.helper;

import com.lcp.acl.dto.ActionDto;
import com.lcp.acl.dto.ResourceDto;
import com.lcp.acl.entity.*;
import com.lcp.acl.mapper.PermissionMapper;
import com.lcp.acl.mapper.ResourceMapper;
import com.lcp.acl.repository.*;
import com.lcp.staff.dto.SubjectEvaluationDto;
import com.lcp.staff.entity.Staff;
import com.lcp.staff.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AclHelper {
    private final StaffRepository staffRepository;
    private final StaffGroupRepository staffGroupRepository;
    private final StaffGroupStaffRepository staffGroupStaffRepository;
    private final RoleRepository roleRepository;
    private final ActionRepository actionRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionResourceActionRepository permissionResourceActionRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceActionRepository resourceActionRepository;

    public List<SubjectEvaluationDto> getSubjectEvaluations(Long userId) {
        Optional<Staff> staffOptional = staffRepository.findById(userId);

        if (staffOptional.isPresent()) {
            // Find all group user belong to
            Staff staff = staffOptional.get();
            List<StaffGroupStaff> groups = staffGroupStaffRepository.findAllByStaffId(userId);
            List<UUID> groupIds = groups.stream()
                    .map(StaffGroupStaff::getStaffGroup)
                    .map(StaffGroupEntity::getId)
                    .collect(Collectors.toList());

            List<RoleEntity> roles = roleRepository.findAllRoleOfUser(userId, groupIds);
            List<Action> actions = actionRepository.findAll();
            Map<UUID, Action> actionById = actions.stream()
                    .collect(Collectors.toMap(Action::getId, action -> action));
            List<Resource> resources = resourceRepository.findAll();
            Map<UUID, Resource> resourceById = resources.stream()
                    .collect(Collectors.toMap(Resource::getId, resource -> resource));
            List<Permission> permissions = permissionRepository.findAllByRoleIdIn(roles.stream()
                    .map(RoleEntity::getId)
                    .collect(Collectors.toList()));

            List<PermissionResourceAction> permissionResourceActions = permissionResourceActionRepository.findAllByPermissionIdIn(
                    permissions.stream()
                            .map(Permission::getId)
                            .collect(Collectors.toList())
            );

            Map<UUID, List<PermissionResourceAction>> permissionResourceActionByPermissionId = permissionResourceActions.stream()
                    .collect(Collectors.groupingBy(PermissionResourceAction::getPermissionId));

            List<ResourceAction> resourceActions = resourceActionRepository.findAllById(permissionResourceActions.stream()
                    .map(PermissionResourceAction::getResourceActionId)
                    .collect(Collectors.toList())
            );

            Map<Integer, ResourceAction> resourceActionById = resourceActions.stream()
                    .collect(Collectors.toMap(ResourceAction::getId, resourceAction -> resourceAction));

            List<SubjectEvaluationDto> resp = new ArrayList<>();
            for (Permission p : permissions) {
                List<PermissionResourceAction> permissionResourceActionsByPermissionId = permissionResourceActionByPermissionId.get(p.getId());
                if (permissionResourceActionsByPermissionId == null) {
                    continue;
                }
                for (PermissionResourceAction pra : permissionResourceActionByPermissionId.get(p.getId())) {
                    SubjectEvaluationDto subjectEvaluationDto = new SubjectEvaluationDto();
                    subjectEvaluationDto.setScope(p.getScope());
                    ResourceAction ra = resourceActionById.get(pra.getResourceActionId());
                    if (ra != null) {
                        Action action = actionById.get(ra.getActionId());
                        Resource resource = resourceById.get(ra.getResourceId());
                        ActionDto actionDto = PermissionMapper.createActionDto(action);
                        ResourceDto resourceDto = ResourceMapper.createResp(resource);
                        subjectEvaluationDto.setAction(actionDto);
                        subjectEvaluationDto.setResource(resourceDto);
                        resp.add(subjectEvaluationDto);
                    }
                }
            }
            return resp;
        }

        return List.of();
    }
}
