package com.betopia.hrm.services.company.calendarAssign;


import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.CalendarAssign;
import com.betopia.hrm.domain.company.request.CalenderAssignRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CalendarAssignService {

    PaginationResponse<CalendarAssign> index(Sort.Direction direction, int page, int perPage);

    List<CalendarAssign> getAllCalendarAssigns();

    CalendarAssign store(CalenderAssignRequest request);

    CalendarAssign show(Long calendarAssignId);

    CalendarAssign update(Long calendarAssignId, CalenderAssignRequest request);

    void destroy(Long calendarAssignId);
}
