package com.betopia.hrm.services.hierarchyfilterservice;

import com.betopia.hrm.domain.dto.hierarchyfilter.HierarchyFilterDTO;

public interface HierarchyFilterService {
    HierarchyFilterDTO getHierarchyFilterData(Long companyId,
                                        Long businessUnitId,
                                        Long workplaceGroupId,
                                        Long workplaceId,
                                        Long departmentId);

}
