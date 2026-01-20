package com.betopia.hrm.controllers.advice;

import java.util.Map;

public final class ForeignKeyMessages {

    private ForeignKeyMessages() {
        // Prevent instantiation
    }

    public static final Map<String, String> FK_MESSAGES = Map.ofEntries(
            Map.entry("fk_company",
                    "Cannot delete Company — it is still assigned to one or more Business Units."),
            Map.entry("fk_leave_policies_leave_type",
                    "Cannot delete Leave Type — it is still used in Leave Policies."),
            Map.entry("fk_leave_policies_leave_group_assign",
                    "Cannot delete Leave Group Assign — it is still used in Leave Policies."),
            Map.entry("fk_bank",
                    "Cannot delete Bank — it is still used in Bank Branches."),
            Map.entry("fk_location",
                    "Cannot delete Location — it is still used in Bank Branches."),
            Map.entry("fk_country",
                    "Cannot delete Country — it is still used in Banks."),
            Map.entry("fk_leave_requests_employee",
                    "Cannot delete Employee — it is still used in Leave Requests."),
            Map.entry("fk_leave_requests_leave_type",
                    "Cannot delete Leave Type — it is still used in Leave Requests."),
            Map.entry("fk_business_unit",
                    "Cannot delete Workplace Groups — it is still used in Business Units."),
            Map.entry("fk_workplace_group",
                    "Cannot delete Workplaces — it is still used in Workplace Groups."),
            Map.entry("employees_company_id_fkey",
                    "Cannot delete Companies — it is still used in Employees."),
            Map.entry("employees_business_unit_id_fkey",
                    "Cannot delete Business Units — it is still used in Employees."),
            Map.entry("employees_work_place_group_id_fkey",
                    "Cannot delete Workplace Groups — it is still used in Employees."),
            Map.entry("employees_workplace_id_fkey",
                    "Cannot delete Workplaces — it is still used in Employees."),
            Map.entry("employees_department_id_fkey",
                    "Cannot delete Departments — it is still used in Employees."),
            Map.entry("employees_employment_type_id_fkey",
                    "Cannot delete Employee Types — it is still used in Employees."),
            Map.entry("employees_grade_id_fkey",
                    "Cannot delete Grade Types — it is still used in Employees."),
            Map.entry("employees_designation_id_fkey",
                    "Cannot delete Designations — it is still used in Employees."),
            Map.entry("users_employee_serial_id_fkey",
                    "Cannot delete Employees — it is still used in Users."),
            Map.entry("role_has_permission_role_id_fkey",
                    "Cannot delete Roles — it is still used in Role Has Permission."),
            Map.entry("model_has_role_role_id_fkey",
                    "Cannot delete Roles — it is still used in Model Has Role."),
            Map.entry("leave_balance_employee_leave_type_id_fkey",
                    "Cannot delete Leave Types — it is still used in Leave Balance Employee."),
            Map.entry("leave_balance_employee_employee_id_fkey",
                    "Cannot delete Employees — it is still used in Leave Balance Employee.")
    );

    public static String getMessage(String constraintName) {
        return FK_MESSAGES.getOrDefault(constraintName,
                "Cannot complete delete operation — the item is referenced by other data.");
    }
}
