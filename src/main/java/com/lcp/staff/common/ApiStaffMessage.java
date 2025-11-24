package com.lcp.staff.common;


import com.lcp.common.ApiMessageBase;
import lombok.Getter;

@Getter
public class ApiStaffMessage extends ApiMessageBase {

    public static ApiStaffMessage
            STAFF_CREATE_SUCCESS = new ApiStaffMessage("staff.create_success"),
            STAFF_UPDATE_SUCCESS = new ApiStaffMessage("staff.update_success"),
            STAFF_DELETE_SUCCESS = new ApiStaffMessage("staff.delete_success"),
            STAFF_NOT_FOUND = new ApiStaffMessage("staff.not_found"),
            STAFF_NOT_FOUND_IN_THIS_BRANCH = new ApiStaffMessage("staff.not_found_in_branch"),
            STAFF_NOT_FOUND_ACTIVE_BRANCH = new ApiStaffMessage("staff.not_found_active_branch"),
            STAFF_EMAIL_USED = new ApiStaffMessage("staff.email.used"),
            STAFF_CITIZEN_ID_USED = new ApiStaffMessage("staff.citizen_id.used"),
            STAFF_TAX_ID_USED = new ApiStaffMessage("staff.tax_id.used"),
            STAFF_WORKING_DATE_INVALID = new ApiStaffMessage("staff.working_date.invalid");

    public static ApiStaffMessage
            JOB_TITLE_CREATE_SUCCESS = new ApiStaffMessage("job_title.create_success"),
            JOB_TITLE_UPDATE_SUCCESS = new ApiStaffMessage("job_title.update_success"),
            JOB_TITLE_DELETE_SUCCESS = new ApiStaffMessage("job_title.delete_success"),
            JOB_TITLE_NOT_FOUND = new ApiStaffMessage("job_title.not_found"),
            JOB_TITLE_CAN_NOT_DELETE_SYSTEM = new ApiStaffMessage("job_title.can_not_delete_system");

    public static ApiStaffMessage
            DEPARTMENT_CREATE_SUCCESS = new ApiStaffMessage("department.create_success"),
            DEPARTMENT_UPDATE_SUCCESS = new ApiStaffMessage("department.update_success"),
            DEPARTMENT_DELETE_SUCCESS = new ApiStaffMessage("department.delete_success"),
            DEPARTMENT_NOT_FOUND = new ApiStaffMessage("department.not_found"),
            DEPARTMENT_CAN_NOT_DELETE_SYSTEM = new ApiStaffMessage("department.can_not_delete_system");

    public static ApiStaffMessage
            BRANCH_CREATE_SUCCESS = new ApiStaffMessage("branch.create_success"),
            BRANCH_UPDATE_SUCCESS = new ApiStaffMessage("branch.update_success"),
            BRANCH_DELETE_SUCCESS = new ApiStaffMessage("branch.delete_success"),
            BRANCH_SET_DEFAULT_SUCCESS = new ApiStaffMessage("branch.set_default_success"),
            BRANCH_VALIDATE_TAX_ID = new ApiStaffMessage("branch.validate_tax_id"),
            BRANCH_VALIDATE_UPDATE_IS_HEAD = new ApiStaffMessage("branch.validate_update_is_head"),
            BRANCH_SET_DEFAULT_INACTIVE = new ApiStaffMessage("branch.set_default_inactive"),
            BRANCH_NOT_FOUND = new ApiStaffMessage("branch.not_found");

    public static ApiStaffMessage

            COMPANY_BANK_ACCOUNT_CREATE_SUCCESS = new ApiStaffMessage("company_bank_account.create_success"),
            COMPANY_BANK_ACCOUNT_UPDATE_SUCCESS = new ApiStaffMessage("company_bank_account.update_success"),
            COMPANY_BANK_ACCOUNT_DELETE_SUCCESS = new ApiStaffMessage("company_bank_account.delete_success"),
            COMPANY_BANK_ACCOUNT_SET_DEFAULT_SUCCESS = new ApiStaffMessage("company_bank_account.set_default_success"),
            COMPANY_BANK_ACCOUNT_SET_DEFAULT_INACTIVE = new ApiStaffMessage("company_bank_account.set_default_inactive"),
            COMPANY_BANK_ACCOUNT_NOT_FOUND = new ApiStaffMessage("company_bank_account.not_found");

    public static ApiStaffMessage

            BRANCH_CONTACT_CREATE_SUCCESS = new ApiStaffMessage("branch_contact.create_success"),
            BRANCH_CONTACT_UPDATE_SUCCESS = new ApiStaffMessage("branch_contact.update_success"),
            BRANCH_CONTACT_SET_DEFAULT_SUCCESS = new ApiStaffMessage("branch_contact.set_default_success"),
            BRANCH_CONTACT_VALIDATE_STATUS = new ApiStaffMessage("branch_contact.validate_status"),
            BRANCH_CONTACT_SET_DEFAULT_INACTIVE = new ApiStaffMessage("branch_contact.set_default_inactive"),
            BRANCH_CONTACT_DELETE_SUCCESS = new ApiStaffMessage("branch_contact.delete_success"),
            BRANCH_CONTACT_NOT_FOUND = new ApiStaffMessage("branch_contact.not_found");

    public static ApiStaffMessage
            STAFF_IN_BRANCH_CREATE_SUCCESS = new ApiStaffMessage("staff_in_branch.create_success"),
            STAFF_IN_BRANCH_UPDATE_SUCCESS = new ApiStaffMessage("staff_in_branch.update_success"),
            STAFF_IN_BRANCH_SET_DEFAULT_SUCCESS = new ApiStaffMessage("staff_in_branch.set_default_success"),
            STAFF_IN_BRANCH_DELETE_SUCCESS = new ApiStaffMessage("staff_in_branch.delete_success"),
            STAFF_IN_BRANCH_WORKING_DATE_INVALID = new ApiStaffMessage("staff_in_branch.working_date.invalid"),
            STAFF_IN_BRANCH_NOT_FOUND = new ApiStaffMessage("staff_in_branch.not_found"),
            STAFF_IN_BRANCH_EXIST = new ApiStaffMessage("staff_in_branch.existed");

    public static ApiStaffMessage
            STAFF_ROLE_CREATE_SUCCESS = new ApiStaffMessage("role.create_success"),
            STAFF_ROLE_DELETE_SUCCESS = new ApiStaffMessage("role.delete_success"),
            ROLE_EXPORT_SUCCESS = new ApiStaffMessage("role.export_success"),
            STAFF_ROLE_UPDATE_SUCCESS = new ApiStaffMessage("role.update_success"),
            STAFF_ROLE_NOT_FOUND = new ApiStaffMessage("role.not_found");

    public static ApiStaffMessage
            COMPANY_PROFILE_CREATE_SUCCESS = new ApiStaffMessage("staff_company_profile.create_success"),
            COMPANY_PROFILE_UPDATE_SUCCESS = new ApiStaffMessage("staff_company_profile.update_success"),
            COMPANY_PROFILE_INVALID_CODE = new ApiStaffMessage("staff_company_profile.invalid_code");

    public static ApiStaffMessage
            PERSON_IN_CHARGE_NOT_FOUND = new ApiStaffMessage("person_in_charge.not_found"),
            PERSON_IN_CHARGE_VALIDATE_RATIO = new ApiStaffMessage("person_in_charge.validate_ratio");

    public ApiStaffMessage(String message) {
        super(message);
    }
}
